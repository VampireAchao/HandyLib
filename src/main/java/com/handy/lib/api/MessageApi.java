package com.handy.lib.api;

import com.handy.lib.constants.BaseConstants;
import com.handy.lib.constants.VersionCheckEnum;
import com.handy.lib.util.BaseUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static org.bukkit.Bukkit.getServer;

/**
 * 发送消息
 *
 * @author handy
 */
public class MessageApi {
    private MessageApi() {
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
     * @param player 玩家
     * @param msg    消息
     */
    public static void sendDebugMessage(Player player, String msg) {
        if (BaseConstants.DEBUG) {
            player.sendMessage(BaseUtil.replaceChatColor(msg));
        }
    }

    /**
     * 发送消息
     *
     * @param player 玩家
     * @param msg    消息
     */
    public static void sendMessage(Player player, TextComponent msg) {
        player.spigot().sendMessage(ChatMessageType.CHAT, msg);
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
     * 发送全服消息
     *
     * @param msg 消息
     */
    public static void sendAllMessage(String msg) {
        Bukkit.broadcastMessage(BaseUtil.replaceChatColor(msg));
    }

    /**
     * 发送Console消息
     *
     * @param plugin 插件
     * @param msg    消息
     */
    public static void sendConsoleMessage(Plugin plugin, String msg) {
        getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[" + plugin.getName() + "] " + BaseUtil.replaceChatColor(msg));
    }

    /**
     * 发送 Console debug消息
     *
     * @param plugin 插件
     * @param msg    消息
     */
    public static void sendConsoleDebugMessage(Plugin plugin, String msg) {
        if (BaseConstants.DEBUG) {
            getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[" + plugin.getName() + "] " + BaseUtil.replaceChatColor(msg));
        }
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
     * 发送全服title消息
     *
     * @param title    标题
     * @param subtitle 副标题
     * @param fadein   淡入时间
     * @param stay     存在时间
     * @param fadeout  淡出时间
     */
    public static void sendAllTitle(String title, String subtitle, int fadein, int stay, int fadeout) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Integer versionId = VersionCheckEnum.getEnum().getVersionId();
            if (versionId > VersionCheckEnum.V_1_8.getVersionId() && versionId < VersionCheckEnum.V_1_11.getVersionId()) {
                player.sendTitle(BaseUtil.replaceChatColor(title), BaseUtil.replaceChatColor(subtitle));
            }
            if (versionId > VersionCheckEnum.V_1_10.getVersionId()) {
                player.sendTitle(BaseUtil.replaceChatColor(title), BaseUtil.replaceChatColor(subtitle), fadein, stay, fadeout);
            }
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
     * 发送全服title消息
     *
     * @param title    标题
     * @param subtitle 副标题
     */
    public static void sendAllTitle(String title, String subtitle) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Integer versionId = VersionCheckEnum.getEnum().getVersionId();
            if (versionId < VersionCheckEnum.V_1_9.getVersionId()) {
                sendMessage(player, BaseUtil.replaceChatColor(title + subtitle));
                return;
            }
            player.sendTitle(BaseUtil.replaceChatColor(title), BaseUtil.replaceChatColor(subtitle));
        }
    }

    /**
     * 发送Actionbar
     *
     * @param player 玩家
     * @param msg    消息
     */
    public static void sendActionbar(Player player, String msg) {
        if (VersionCheckEnum.getEnum().getVersionId() < VersionCheckEnum.V_1_9.getVersionId()) {
            sendMessage(player, msg);
            return;
        }
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(msg));
    }

    /**
     * 发送全服Actionbar
     *
     * @param msg 消息
     */
    public static void sendAllActionbar(String msg) {
        if (VersionCheckEnum.getEnum().getVersionId() < VersionCheckEnum.V_1_9.getVersionId()) {
            sendAllMessage(msg);
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(msg));
        }
    }

}