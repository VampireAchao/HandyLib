package com.handy.lib;

import com.handy.lib.annotation.HandyListener;
import com.handy.lib.api.CheckVersionApi;
import com.handy.lib.api.MessageApi;
import com.handy.lib.command.HandyCommandEventHandler;
import com.handy.lib.command.HandyCommandFactory;
import com.handy.lib.command.IHandyCommandEvent;
import com.handy.lib.inventory.HandyClickEventHandler;
import com.handy.lib.inventory.HandyClickFactory;
import com.handy.lib.inventory.IHandyClickEvent;
import com.handy.lib.util.ActionBarUtil;
import com.handy.lib.util.MetricsUtil;
import lombok.SneakyThrows;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 初始化
 *
 * @author handy
 * @since 1.2.0
 */
public class InitApi {
    public static Plugin PLUGIN;

    private InitApi() {
    }

    private static class SingletonHolder {
        private static final InitApi INSTANCE = new InitApi();
    }

    public static InitApi getInstance(Plugin plugin) {
        PLUGIN = plugin;
        return InitApi.SingletonHolder.INSTANCE;
    }

    /**
     * 初始化实现类
     *
     * @param packageName 扫描的包名
     * @return this
     */
    @SneakyThrows
    public InitApi init(String packageName) {
        long startTime = System.currentTimeMillis();
        // 初始化工具类
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages(packageName).addScanners(new SubTypesScanner()).addScanners(new FieldAnnotationsScanner()));

        // 子命令处理器注入
        Set<Class<? extends HandyCommandEventHandler>> handyCommandEventList = reflections.getSubTypesOf(HandyCommandEventHandler.class);
        if (handyCommandEventList.size() > 0) {
            List<IHandyCommandEvent> handyCommandEvents = new ArrayList<>();
            for (Class<? extends HandyCommandEventHandler> aClass : handyCommandEventList) {
                handyCommandEvents.add(aClass.newInstance());
            }
            HandyCommandFactory.getInstance().init(handyCommandEvents);
        }

        // 监听器注入
        Set<Class<?>> listenerTypesAnnotatedWith = reflections.getTypesAnnotatedWith(HandyListener.class, true);
        if (listenerTypesAnnotatedWith.size() > 0) {
            for (Class<?> aClass : listenerTypesAnnotatedWith) {
                PLUGIN.getServer().getPluginManager().registerEvents((Listener) aClass.newInstance(), PLUGIN);
            }
        }
        // 背包事件处理器注入
        Set<Class<? extends HandyClickEventHandler>> handyClickEventList = reflections.getSubTypesOf(HandyClickEventHandler.class);
        if (handyClickEventList.size() > 0) {
            List<IHandyClickEvent> handyClickEvents = new ArrayList<>();
            for (Class<? extends HandyClickEventHandler> aClass : handyClickEventList) {
                handyClickEvents.add(aClass.newInstance());
            }
            HandyClickFactory.getInstance().init(handyClickEvents);
        }

        MessageApi.sendConsoleMessage(PLUGIN, "初始化加载耗时：" + (System.currentTimeMillis() - startTime) + "毫秒");
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
            CheckVersionApi.checkVersion(PLUGIN, null, url);
        }
        return this;
    }

    /**
     * 初始化ActionBar
     *
     * @return this
     */
    public InitApi initActionBar() {
        ActionBarUtil.actionBarReflect();
        return this;
    }

    /**
     * 进行插件使用数据统计
     *
     * @param pluginId 插件id
     * @return this
     */
    public InitApi addMetrics(int pluginId) {
        MetricsUtil.addMetrics(PLUGIN, pluginId);
        return this;
    }

}