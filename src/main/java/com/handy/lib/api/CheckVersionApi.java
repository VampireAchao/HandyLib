package com.handy.lib.api;

import com.handy.lib.util.HandyHttpUtil;
import org.bukkit.entity.Player;

/**
 * 版本检测
 *
 * @author handy
 */
public class CheckVersionApi {
    private CheckVersionApi() {
    }

    /**
     * 版本检测
     *
     * @param player 玩家l
     * @param url    路径
     */
    public static void checkVersion(Player player, String url) {
        HandyHttpUtil.checkVersion(player, url);
    }

}
