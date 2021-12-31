package com.handy.lib;

import com.handy.lib.annotation.HandyCommand;
import com.handy.lib.annotation.HandyListener;
import com.handy.lib.annotation.HandySubCommand;
import com.handy.lib.annotation.TableName;
import com.handy.lib.api.CheckVersionApi;
import com.handy.lib.api.StorageApi;
import com.handy.lib.command.HandyCommandFactory;
import com.handy.lib.command.HandySubCommandParam;
import com.handy.lib.command.IHandyCommandEvent;
import com.handy.lib.core.ClassUtil;
import com.handy.lib.core.CollUtil;
import com.handy.lib.core.StrUtil;
import com.handy.lib.db.Db;
import com.handy.lib.inventory.HandyClickFactory;
import com.handy.lib.inventory.IHandyClickEvent;
import com.handy.lib.metrics.PluginNameCallable;
import com.handy.lib.metrics.VersionCallable;
import com.handy.lib.param.VerifySignParam;
import com.handy.lib.util.HandyHttpUtil;
import lombok.SneakyThrows;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.permissions.DefaultPermissions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 初始化
 *
 * @author handy
 * @since 1.2.0
 */
public class InitApi {
    public static Plugin PLUGIN;
    private static ClassUtil CLASS_UTIL;
    private final static String VERSION = "2.4.2";

    private InitApi() {
    }

    private static class SingletonHolder {
        private static final InitApi INSTANCE = new InitApi();
    }

    public static InitApi getInstance(Plugin plugin) {
        PLUGIN = plugin;
        CLASS_UTIL = new ClassUtil();
        // bStats进行插件使用数据统计
        Metrics metrics = new Metrics(PLUGIN, 12612);
        Metrics.CustomChart versionChart = new Metrics.SimplePie("version", new VersionCallable(VERSION));
        metrics.addCustomChart(versionChart);
        Metrics.CustomChart pluginNameChart = new Metrics.SimplePie("pluginName", new PluginNameCallable(plugin.getName()));
        metrics.addCustomChart(pluginNameChart);
        return InitApi.SingletonHolder.INSTANCE;
    }

    /**
     * 命令注入器
     *
     * @param packageName 扫描的包名
     * @return this
     */
    @SneakyThrows
    public InitApi initCommand(String packageName) {
        // 主命令
        List<Class<?>> commandList = CLASS_UTIL.getClassByAnnotation(packageName, HandyCommand.class);
        if (CollUtil.isEmpty(commandList)) {
            return this;
        }
        for (Class<?> aClass : commandList) {
            HandyCommand handyCommand = aClass.getAnnotation(HandyCommand.class);
            PluginCommand pluginCommand = Bukkit.getPluginCommand(handyCommand.name());
            if (pluginCommand != null) {
                if (aClass.newInstance() instanceof CommandExecutor) {
                    pluginCommand.setExecutor((CommandExecutor) aClass.newInstance());
                }
                if (aClass.newInstance() instanceof TabExecutor) {
                    pluginCommand.setTabCompleter((TabExecutor) aClass.newInstance());
                }
                if (handyCommand.aliases().length > 0) {
                    pluginCommand.setAliases(Arrays.asList(handyCommand.aliases()));
                }
                pluginCommand.setDescription(handyCommand.description());
                pluginCommand.setUsage(handyCommand.usage());
                pluginCommand.setPermissionMessage(handyCommand.permissionMessage());
                if (StrUtil.isNotEmpty(handyCommand.permission())) {
                    pluginCommand.setPermission(handyCommand.permission());
                    DefaultPermissions.registerPermission(handyCommand.permission(), null, handyCommand.PERMISSION_DEFAULT());
                }
            }
        }
        // 子命令
        Map<Class<?>, List<Method>> methodsMap = CLASS_UTIL.getMethodByAnnotation(packageName, HandySubCommand.class);
        if (methodsMap.isEmpty()) {
            return this;
        }
        List<HandySubCommandParam> subCommandParamList = new ArrayList<>();
        for (Class<?> aClass : methodsMap.keySet()) {
            for (Method method : methodsMap.get(aClass)) {
                HandySubCommand handySubCommand = method.getAnnotation(HandySubCommand.class);
                HandySubCommandParam param = new HandySubCommandParam();
                param.setCommand(handySubCommand.mainCommand().toLowerCase().trim());
                param.setSubCommand(handySubCommand.subCommand().toLowerCase().trim());
                param.setPermission(handySubCommand.permission().trim());
                param.setAClass(aClass);
                param.setMethod(method);
                subCommandParamList.add(param);
            }
        }
        Map<String, Map<String, HandySubCommandParam>> subCommandMap = subCommandParamList.stream().collect(Collectors.groupingBy(HandySubCommandParam::getCommand, Collectors.groupingBy(HandySubCommandParam::getSubCommand, Collectors.collectingAndThen(Collectors.toList(), value -> value.get(0)))));
        HandyCommandFactory.getInstance().initSubCommand(subCommandMap);
        return this;
    }

    /**
     * 子命令处理器注入
     *
     * @param packageName 扫描的包名
     * @return this
     */
    @SneakyThrows
    public InitApi initSubCommand(String packageName) {
        List<Class<IHandyCommandEvent>> handyCommandEventList = CLASS_UTIL.getClassByIsAssignableFrom(packageName, IHandyCommandEvent.class);
        if (CollUtil.isEmpty(handyCommandEventList)) {
            return this;
        }
        List<IHandyCommandEvent> handyCommandEvents = new ArrayList<>();
        for (Class<?> aClass : handyCommandEventList) {
            handyCommandEvents.add((IHandyCommandEvent) aClass.newInstance());
        }
        HandyCommandFactory.getInstance().init(handyCommandEvents);
        return this;
    }

    /**
     * 监听器注入
     *
     * @param packageName 扫描的包名
     * @return this
     */
    @SneakyThrows
    public InitApi initListener(String packageName) {
        List<Class<?>> listenerTypesAnnotatedWith = CLASS_UTIL.getClassByAnnotation(packageName, HandyListener.class);
        if (CollUtil.isEmpty(listenerTypesAnnotatedWith)) {
            return this;
        }
        for (Class<?> aClass : listenerTypesAnnotatedWith) {
            PLUGIN.getServer().getPluginManager().registerEvents((Listener) aClass.newInstance(), PLUGIN);
        }
        return this;
    }

    /**
     * 监听器注入
     *
     * @param packageName 扫描的包名
     * @param ignoreList  忽视的类
     * @return this
     */
    @SneakyThrows
    public InitApi initListener(String packageName, List<String> ignoreList) {
        List<Class<?>> listenerTypesAnnotatedWith = CLASS_UTIL.getClassByAnnotation(packageName, HandyListener.class);
        if (CollUtil.isEmpty(listenerTypesAnnotatedWith)) {
            return this;
        }
        for (Class<?> aClass : listenerTypesAnnotatedWith) {
            if (CollUtil.isNotEmpty(ignoreList) && ignoreList.contains(aClass.getName())) {
                continue;
            }
            PLUGIN.getServer().getPluginManager().registerEvents((Listener) aClass.newInstance(), PLUGIN);
        }
        return this;
    }

    /**
     * 背包事件处理器注入
     *
     * @param packageName 扫描的包名
     * @return this
     */
    @SneakyThrows
    public InitApi initClickEvent(String packageName) {
        List<Class<IHandyClickEvent>> handyClickEventList = CLASS_UTIL.getClassByIsAssignableFrom(packageName, IHandyClickEvent.class);
        if (CollUtil.isEmpty(handyClickEventList)) {
            return this;
        }
        List<IHandyClickEvent> handyClickEvents = new ArrayList<>();
        for (Class<?> aClass : handyClickEventList) {
            handyClickEvents.add((IHandyClickEvent) aClass.newInstance());
        }
        HandyClickFactory.getInstance().init(handyClickEvents);
        return this;
    }

    /**
     * 初始化版本更新提醒
     *
     * @param isVersion 是否提醒
     * @param url       提醒url
     * @param msg       更新提醒 ${version} 版本变量 ${body} 更新内容变量
     * @return this
     * @since 1.6.9
     */
    public InitApi checkVersion(boolean isVersion, String url, String msg) {
        if (isVersion) {
            CheckVersionApi.checkVersion(null, url, msg);
        }
        return this;
    }

    /**
     * 初始化版本更新提醒
     *
     * @param isVersion 是否提醒
     * @param url       提醒url
     * @return this
     */
    public InitApi checkVersion(boolean isVersion, String url) {
        if (isVersion) {
            CheckVersionApi.checkVersion(null, url, null);
        }
        return this;
    }

    /**
     * 进行插件使用数据统计
     *
     * @param pluginId 插件id
     * @return this
     */
    public InitApi addMetrics(int pluginId) {
        // bStats进行插件使用数据统计
        new Metrics(PLUGIN, pluginId);
        // cStats进行插件使用数据统计
        new com.iroselle.cstats.bukkit.Metrics(PLUGIN);
        return this;
    }

    /**
     * 进行插件使用数据统计
     *
     * @param pluginId     插件id
     * @param customCharts 自定义图表
     * @return this
     * @since 1.7.9
     */
    public InitApi addMetrics(int pluginId, List<Metrics.CustomChart> customCharts) {
        // bStats进行插件使用数据统计
        Metrics metrics = new Metrics(PLUGIN, pluginId);
        // 添加自定义图表
        if (CollUtil.isNotEmpty(customCharts)) {
            for (Metrics.CustomChart customChart : customCharts) {
                metrics.addCustomChart(customChart);
            }
        }
        // cStats进行插件使用数据统计
        new com.iroselle.cstats.bukkit.Metrics(PLUGIN);
        return this;
    }

    /**
     * 进行验签
     *
     * @param param 入参
     * @return this
     */
    public InitApi verifySign(VerifySignParam param) {
        HandyHttpUtil.verifySign(param);
        HandyHttpUtil.anewVerifySign(param);
        return this;
    }

    /**
     * 进行验签
     *
     * @param param 入参
     * @return this
     * @since 2.3.8
     */
    public InitApi macVerifySign(VerifySignParam param) {
        HandyHttpUtil.macVerifySign(param);
        HandyHttpUtil.macAnewVerifySign(param);
        return this;
    }

    /**
     * 存储初始化
     *
     * @param packageName 扫描的包名
     * @return this
     * @since 1.4.8
     */
    public InitApi enableSql(String packageName) {
        // 初始化链接池
        StorageApi.enableSql();
        // 实体类
        List<Class<?>> tableList = CLASS_UTIL.getClassByAnnotation(packageName, TableName.class);
        for (Class<?> aClass : tableList) {
            Db.use(aClass).execution().create();
        }
        return this;
    }

}