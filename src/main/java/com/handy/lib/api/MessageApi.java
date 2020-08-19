package com.handy.lib.api;

import com.handy.lib.constants.VersionCheckEnum;
import com.handy.lib.util.ActionBarUtil;
import com.handy.lib.util.BaseUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author hs
 * @Description: {发送消息}
 * @date 2020/7/23 18:22
 */
public class MessageApi {
    private MessageApi() {
    }

    /**
     * 初始化ActionBar
     */
    public static void initActionBar() {
        ActionBarUtil.actionBarReflect();
    }

    /**
     * 发送消息
     *
     * @param player 玩家
     * @param msg    消息
     */
    public static void sendMessage(Player player, String msg) {
        player.sendMessage(BaseUtil.replaceChatColor(msg));
    }

    /**
     * 发送消息
     *
     * @param sender 玩家
     * @param msg    消息
     */
    public static void sendMessage(CommandSender sender, String msg) {
        sender.sendMessage(BaseUtil.replaceChatColor(msg));
    }

    /**
     * 发送title消息
     *
     * @param player   玩家
     * @param title    标题
     * @param subtitle 副标题
     * @param fadein   淡入时间
     * @param stay     存在时间
     * @param fadeout  淡出时间
     */
    public static void sendTitle(Player player, String title, String subtitle, int fadein, int stay, int fadeout) {
        Integer versionId = VersionCheckEnum.getEnum().getVersionId();
        if (versionId > VersionCheckEnum.V_1_8.getVersionId() && versionId < VersionCheckEnum.V_1_11.getVersionId()) {
            player.sendTitle(BaseUtil.replaceChatColor(title), BaseUtil.replaceChatColor(subtitle));
        }
        if (versionId > VersionCheckEnum.V_1_10.getVersionId()) {
            player.sendTitle(BaseUtil.replaceChatColor(title), BaseUtil.replaceChatColor(subtitle), fadein, stay, fadeout);
        }
    }

    /**
     * 发送title消息
     *
     * @param player   玩家
     * @param title    标题
     * @param subtitle 副标题
     */
    public static void sendTitle(Player player, String title, String subtitle) {
        Integer versionId = VersionCheckEnum.getEnum().getVersionId();
        if (versionId < VersionCheckEnum.V_1_9.getVersionId()) {
            sendMessage(player, BaseUtil.replaceChatColor(title + subtitle));
            return;
        }
        player.sendTitle(BaseUtil.replaceChatColor(title), BaseUtil.replaceChatColor(subtitle));
    }

    /**
     * 发送Actionbar
     *
     * @param player 玩家
     * @param msg    消息
     */
    public static void sendActionbar(Player player, String msg) {
        if (VersionCheckEnum.V_1_7.equals(VersionCheckEnum.getEnum())) {
            return;
        }
        ActionBarUtil.sendActionBar(player, BaseUtil.replaceChatColor(msg));
    }

}
