package com.handy.lib;

import com.handy.lib.annotation.HandyListener;
import com.handy.lib.api.CheckVersionApi;
import com.handy.lib.api.MessageApi;
import com.handy.lib.command.HandyCommandEventHandler;
import com.handy.lib.command.HandyCommandFactory;
import com.handy.lib.command.IHandyCommandEvent;
import com.handy.lib.constants.BaseConstants;
import com.handy.lib.inventory.HandyClickEventHandler;
import com.handy.lib.inventory.HandyClickFactory;
import com.handy.lib.inventory.IHandyClickEvent;
import com.handy.lib.util.BaseUtil;
import com.handy.lib.util.HandyHttpUtil;
import com.handy.lib.util.SqlManagerUtil;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
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
     * 加载Color文件
     */
    public InitApi initColor() {
        File colorFile = new File(PLUGIN.getDataFolder(), "color.yml");
        if (!(colorFile.exists())) {
            PLUGIN.saveResource("color.yml", false);
        }
        BaseConstants.COLOR_CONFIG = YamlConfiguration.loadConfiguration(colorFile);
        return this;
    }

    /**
     * 加载物品汉化文件
     */
    public InitApi initZhCn() {
        // 优先加载zh_cn.json
        File zhChFile = new File(PLUGIN.getDataFolder(), "zh_cn.json");
        if (zhChFile.exists()) {
            BaseUtil.readJsonFileToJsonCacheMap(zhChFile);
            return this;
        }
        // 运行云汉化
        HandyHttpUtil.getZhCn(PLUGIN);

        // 加载item.json
        File itemFile = new File(PLUGIN.getDataFolder(), "item.json");
        if (!itemFile.exists()) {
            PLUGIN.saveResource("item.json", false);
        }
        BaseUtil.readJsonFileToItemJsonCacheMap(itemFile);

        // 自动同步自定义汉化
        HandyHttpUtil.setItemName(PLUGIN);
        return this;
    }

    /**
     * 初始化语言文件
     *
     * @param langConfig 语言文件
     */
    public InitApi initLangMsg(FileConfiguration langConfig) {
        BaseConstants.LANG_CONFIG = langConfig;
        return this;
    }

    /**
     * 加载storage文件,并初始化连接池
     */
    public InitApi initSql() {
        File langFile = new File(PLUGIN.getDataFolder(), "storage.yml");
        if (!(langFile.exists())) {
            PLUGIN.saveResource("storage.yml", false);
        }
        BaseConstants.STORAGE_CONFIG = YamlConfiguration.loadConfiguration(langFile);
        // 初始化连接池
        SqlManagerUtil.getInstance().enableTable(PLUGIN);
        return this;
    }

    /**
     * 初始化版本更新提醒
     *
     * @param isVersion 是否提醒
     * @param url       提醒url
     */
    public InitApi checkVersion(boolean isVersion, String url) {
        if (isVersion) {
            CheckVersionApi.checkVersion(PLUGIN, null, url);
        }
        return this;
    }

}