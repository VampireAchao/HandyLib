package com.handy.lib.api;

import com.handy.lib.util.HandyHttpUtil;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * @author hs
 * @Description: {版本检测}
 * @date 2020/7/30 12:34
 */
public class CheckVersionApi {
    private CheckVersionApi() {
    }

    /**
     * 版本检测
     *
     * @param plugin 插件
     * @param player 玩家
     * @param url    路径
     */
    public static void checkVersion(Plugin plugin, Player player, String url) {
        HandyHttpUtil.checkVersion(plugin, player, url);
    }

}
