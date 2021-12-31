package com.handy.lib.util;

import com.handy.lib.InitApi;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * BossBar封装
 *
 * @author handy
 * @since 2.3.9
 */
public class BossBarUtil {

    /**
     * 创建 BossBar
     *
     * @param title 标题
     * @return BossBar
     * @since 2.3.9
     */
    public static BossBar createBossBar(String title) {
        return createBossBar(title, BarColor.PINK, BarStyle.SOLID, BarFlag.CREATE_FOG);
    }

    /**
     * 创建 BossBar
     *
     * @param title    标题
     * @param barColor 颜色
     * @param barStyle 风格
     * @param barFlag  样式
     * @return BossBar
     * @since 2.3.9
     */
    public static BossBar createBossBar(String title, BarColor barColor, BarStyle barStyle, BarFlag barFlag) {
        return createBossBar(InitApi.PLUGIN.getName(), title, barColor, barStyle, barFlag);
    }

    /**
     * 创建 BossBar
     *
     * @param namespacedKey key
     * @param title         标题
     * @return BossBar
     * @since 2.3.9
     */
    public static BossBar createBossBar(String namespacedKey, String title) {
        return createBossBar(namespacedKey, title, BarColor.PINK, BarStyle.SOLID, BarFlag.CREATE_FOG);
    }

    /**
     * 创建 BossBar
     *
     * @param config        配置文件
     * @param type          节点
     * @param namespacedKey key
     * @param title         标题
     * @return BossBar
     * @since 2.3.9
     */
    public static BossBar createBossBar(FileConfiguration config, String type, String namespacedKey, String title) {
        String color = config.getString(type + ".color", "PINK");
        String style = config.getString(type + ".style", "SOLID");
        String flag = config.getString(type + ".flag", "CREATE_FOG");
        BarColor barColor = BarColor.valueOf(color);
        BarStyle barStyle = BarStyle.valueOf(style);
        BarFlag barFlag = BarFlag.valueOf(flag);
        return createBossBar(namespacedKey, title, barColor, barStyle, barFlag);
    }

    /**
     * 创建 BossBar
     *
     * @param namespacedKey key
     * @param title         标题
     * @param barColor      颜色
     * @param barStyle      风格
     * @param barFlag       样式
     * @return BossBar
     * @since 2.3.9
     */
    public static BossBar createBossBar(String namespacedKey, String title, BarColor barColor, BarStyle barStyle, BarFlag barFlag) {
        return Bukkit.createBossBar(
                new NamespacedKey(InitApi.PLUGIN, namespacedKey),
                title,
                barColor,
                barStyle,
                barFlag
        );
    }

    /**
     * 添加玩家
     *
     * @param namespacedKey key
     * @param player        玩家
     * @since 2.3.9
     */
    public static void addPlayer(String namespacedKey, Player player) {
        addPlayer(namespacedKey, Collections.singletonList(player));
    }

    /**
     * 添加玩家
     *
     * @param namespacedKey key
     * @param playerName    玩家
     * @since 2.3.9
     */
    public static void addPlayer(String namespacedKey, String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null && player.isOnline()) {
            addPlayer(namespacedKey, player);
        }
    }

    /**
     * 添加玩家
     *
     * @param namespacedKey key
     * @param playerUuid    玩家
     * @since 2.3.9
     */
    public static void addPlayer(String namespacedKey, UUID playerUuid) {
        Player player = Bukkit.getPlayer(playerUuid);
        if (player != null && player.isOnline()) {
            addPlayer(namespacedKey, player);
        }
    }

    /**
     * 添加玩家
     *
     * @param namespacedKey  key
     * @param playerUuidList 玩家列表
     * @since 2.3.9
     */
    public static void addPlayerByUuid(String namespacedKey, List<UUID> playerUuidList) {
        for (UUID playerUuid : playerUuidList) {
            addPlayer(namespacedKey, playerUuid);
        }
    }

    /**
     * 添加玩家
     *
     * @param namespacedKey  key
     * @param playerNameList 玩家列表
     * @since 2.3.9
     */
    public static void addPlayerByName(String namespacedKey, List<String> playerNameList) {
        for (String playerName : playerNameList) {
            addPlayer(namespacedKey, playerName);
        }
    }

    /**
     * 添加玩家
     *
     * @param namespacedKey key
     * @param playerList    玩家列表
     * @since 2.3.9
     */
    public static void addPlayer(String namespacedKey, List<Player> playerList) {
        BossBar bossBar = getBossBar(namespacedKey);
        if (bossBar == null) {
            return;
        }
        for (Player player : playerList) {
            bossBar.addPlayer(player);
        }
    }

    /**
     * 移除玩家
     *
     * @param namespacedKey key
     * @param player        玩家
     * @since 2.3.9
     */
    public static void removePlayer(String namespacedKey, Player player) {
        removePlayer(namespacedKey, Collections.singletonList(player));
    }

    /**
     * 移除玩家
     *
     * @param namespacedKey key
     * @param playerName    玩家
     * @since 2.3.9
     */
    public static void removePlayer(String namespacedKey, String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null && player.isOnline()) {
            removePlayer(namespacedKey, player);
        }
    }

    /**
     * 移除玩家
     *
     * @param namespacedKey key
     * @param playerUuid    玩家
     * @since 2.3.9
     */
    public static void removePlayer(String namespacedKey, UUID playerUuid) {
        Player player = Bukkit.getPlayer(playerUuid);
        if (player != null && player.isOnline()) {
            removePlayer(namespacedKey, player);
        }
    }

    /**
     * 移除玩家
     *
     * @param namespacedKey  key
     * @param playerUuidList 玩家列表
     * @since 2.3.9
     */
    public static void removePlayerByUuid(String namespacedKey, List<UUID> playerUuidList) {
        for (UUID playerUuid : playerUuidList) {
            removePlayer(namespacedKey, playerUuid);
        }
    }

    /**
     * 移除玩家
     *
     * @param namespacedKey  key
     * @param playerNameList 玩家列表
     * @since 2.3.9
     */
    public static void removePlayerByName(String namespacedKey, List<String> playerNameList) {
        for (String playerName : playerNameList) {
            removePlayer(namespacedKey, playerName);
        }
    }

    /**
     * 移除玩家
     *
     * @param namespacedKey key
     * @param playerList    玩家列表
     * @since 2.3.9
     */
    public static void removePlayer(String namespacedKey, List<Player> playerList) {
        BossBar bossBar = getBossBar(namespacedKey);
        if (bossBar == null) {
            return;
        }
        for (Player player : playerList) {
            bossBar.removePlayer(player);
        }
    }

    /**
     * 获取对应BossBar
     *
     * @param namespacedKey key
     * @return BossBar
     * @since 2.3.9
     */
    public static BossBar getBossBar(String namespacedKey) {
        return Bukkit.getBossBar(new NamespacedKey(InitApi.PLUGIN, namespacedKey));
    }

    /**
     * 移除 BossBar
     *
     * @param namespacedKey key
     * @return true
     * @since 2.3.9
     */
    public static boolean removeBossBar(String namespacedKey) {
        return Bukkit.removeBossBar(new NamespacedKey(InitApi.PLUGIN, namespacedKey));
    }

}