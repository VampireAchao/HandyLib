package com.handy.lib;

import com.handy.lib.annotation.HandyListener;
import com.handy.lib.api.CheckVersionApi;
import com.handy.lib.command.HandyCommandFactory;
import com.handy.lib.command.IHandyCommandEvent;
import com.handy.lib.core.ClassUtil;
import com.handy.lib.inventory.HandyClickFactory;
import com.handy.lib.inventory.IHandyClickEvent;
import com.handy.lib.util.ActionBarUtil;
import com.handy.lib.util.MetricsUtil;
import lombok.SneakyThrows;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * 初始化
 *
 * @author handy
 * @since 1.2.0
 */
public class InitApi {
    public static Plugin PLUGIN;
    private static ClassUtil CLASS_UTIL;

    private InitApi() {
    }

    private static class SingletonHolder {
        private static final InitApi INSTANCE = new InitApi();
    }

    public static InitApi getInstance(Plugin plugin) {
        PLUGIN = plugin;
        CLASS_UTIL = new ClassUtil(PLUGIN);
        return InitApi.SingletonHolder.INSTANCE;
    }

    /**
     * 子命令处理器注入
     *
     * @param packageName 扫描的包名
     */
    @SneakyThrows
    public void initCommand(String packageName) {
        List<Class<IHandyCommandEvent>> handyCommandEventList = CLASS_UTIL.forNameIsAssignableFrom(packageName, IHandyCommandEvent.class);
        if (handyCommandEventList.size() > 0) {
            List<IHandyCommandEvent> handyCommandEvents = new ArrayList<>();
            for (Class<?> aClass : handyCommandEventList) {
                handyCommandEvents.add((IHandyCommandEvent) aClass.newInstance());
            }
            HandyCommandFactory.getInstance().init(handyCommandEvents);
        }
    }

    /**
     * 监听器注入
     *
     * @param packageName 扫描的包名
     */
    @SneakyThrows
    public void initListener(String packageName) {
        List<Class<?>> listenerTypesAnnotatedWith = CLASS_UTIL.forNameIsAnnotationPresent(packageName, HandyListener.class);
        if (listenerTypesAnnotatedWith.size() > 0) {
            for (Class<?> aClass : listenerTypesAnnotatedWith) {
                PLUGIN.getServer().getPluginManager().registerEvents((Listener) aClass.newInstance(), PLUGIN);
            }
        }
    }

    /**
     * 背包事件处理器注入
     *
     * @param packageName 扫描的包名
     */
    @SneakyThrows
    public void initClickEvent(String packageName) {
        List<Class<IHandyClickEvent>> handyClickEventList = CLASS_UTIL.forNameIsAssignableFrom(packageName, IHandyClickEvent.class);
        if (handyClickEventList.size() > 0) {
            List<IHandyClickEvent> handyClickEvents = new ArrayList<>();
            for (Class<?> aClass : handyClickEventList) {
                handyClickEvents.add((IHandyClickEvent) aClass.newInstance());
            }
            HandyClickFactory.getInstance().init(handyClickEvents);
        }
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