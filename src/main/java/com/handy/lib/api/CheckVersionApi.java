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
     * @param msg    更新提醒 ${version} 版本变量 ${body} 更新内容变量
     */
    public static void checkVersion(Player player, String url, String msg) {
        HandyHttpUtil.checkVersion(player, url, msg);
    }

}
