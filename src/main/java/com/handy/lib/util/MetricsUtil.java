package com.handy.lib.util;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.Plugin;

/**
 * 插件使用统计工具类
 *
 * @author handy
 **/
public class MetricsUtil {

    /**
     * 同时添加插件到俩个额统计中
     *
     * @param plugin   插件
     * @param pluginId 插件id
     */
    public static void addMetrics(Plugin plugin, int pluginId) {
        // bStats进行插件使用数据统计
        new Metrics(plugin, pluginId);
        // cStats进行插件使用数据统计
        new CStatsMetrics(plugin);
    }

}
