package com.handy.lib.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handy.lib.constants.BaseConstants;
import com.handy.lib.core.CollUtil;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;

/**
 * 常用方法
 *
 * @author handy
 */
public class BaseUtil {

    /**
     * 判断是否为玩家
     *
     * @param sender 发送者
     * @return true是
     */
    public static Boolean isPlayer(CommandSender sender) {
        return sender instanceof Player;
    }

    /**
     * 判断是否为玩家
     *
     * @param sender 发送者
     * @return true否
     */
    public static Boolean isNotPlayer(CommandSender sender) {
        return !isPlayer(sender);
    }

    /**
     * 颜色代码转换
     *
     * @param str 消息
     * @return 转换后的字符串
     */
    public static String replaceChatColor(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        return replaceRpgChatColor(str.replace("&", "§"));
    }

    /**
     * 颜色代码转换
     *
     * @param stringList 消息
     * @param isRpg      是否rpg消息
     * @return 转换后的字符串集合
     */
    public static List<String> replaceChatColor(List<String> stringList, boolean isRpg) {
        List<String> loreList = new ArrayList<>();
        if (CollUtil.isEmpty(stringList)) {
            return loreList;
        }
        for (String lore : stringList) {
            loreList.add(replaceChatColor(lore, isRpg));
        }
        return loreList;
    }

    /**
     * 颜色代码转换
     *
     * @param str   消息
     * @param isRpg 是否rpg消息
     * @return 转换后的字符串
     */
    public static String replaceChatColor(String str, boolean isRpg) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        String newStr = str.replace("&", "§");
        return isRpg ? replaceRpgChatColor(newStr) : newStr;
    }

    /**
     * rpg颜色转换
     *
     * @param str 字符串
     * @return str
     */
    public static String replaceRpgChatColor(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        if (BaseConstants.COLOR_CONFIG == null) {
            return str;
        }
        Matcher matcher = BaseConstants.RPG_PATTERN.matcher(str);
        List<String> matchStrList = new ArrayList<>();
        while (matcher.find()) {
            matchStrList.add(matcher.group());
        }
        if (CollUtil.isEmpty(matchStrList)) {
            return str;
        }
        for (String value : matchStrList) {
            String rpgStr = BaseConstants.COLOR_CONFIG.getString(stringFilter(value));
            if (StringUtils.isBlank(rpgStr) || rpgStr.length() != 6) {
                continue;
            }
            ChatColor chatColor = ChatColor.of("#" + rpgStr);
            str = str.replace(value, chatColor + "");
        }
        return str;
    }

    /**
     * 获取强化等级转换
     *
     * @param str 字符串
     * @return str
     */
    public static int getIntensifyLevel(String str) {
        if (StringUtils.isBlank(str)) {
            return 0;
        }
        Matcher matcher = BaseConstants.INTENSIFY_PATTERN.matcher(str);
        List<String> matchStrList = new ArrayList<>();
        while (matcher.find()) {
            matchStrList.add(matcher.group());
        }
        if (CollUtil.isEmpty(matchStrList)) {
            return 0;
        }
        String levelStr = matchStrList.get(matchStrList.size() - 1);

        String replace = levelStr.replace("§f[+§a", "").replace("§f]", "");
        return Integer.parseInt(replace);
    }

    /**
     * 去除强化等级
     *
     * @param str 字符串
     * @return str
     */
    public static String delIntensifyLevel(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        Matcher matcher = BaseConstants.INTENSIFY_PATTERN.matcher(str);
        List<String> matchStrList = new ArrayList<>();
        while (matcher.find()) {
            matchStrList.add(matcher.group());
        }
        if (CollUtil.isEmpty(matchStrList)) {
            return str;
        }
        String levelStr = matchStrList.get(matchStrList.size() - 1);
        return str.replace(levelStr, "");
    }

    /**
     * 过滤特殊字符 %
     *
     * @param str 变量
     * @return str
     */
    public static String stringFilter(String str) {
        return BaseConstants.RPG_DEL_PATTERN.matcher(str).replaceAll("").trim();
    }

    /**
     * 获取uuid
     *
     * @param playerName 玩家名
     * @return uuid
     */
    public static UUID getUuid(String playerName) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        return offlinePlayer.getUniqueId();
    }

    /**
     * 利用正则表达式判断字符串是否是数字
     *
     * @param str 字符串
     * @return true是数字
     */
    public static Integer isNumericToInt(String str) {
        try {
            Matcher isNum = BaseConstants.NUMERIC.matcher(str);
            if (isNum.matches()) {
                return Integer.valueOf(str);
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return null;
    }

    /**
     * 利用正则表达式判断字符串是否是数字
     *
     * @param str 字符串
     * @return true是数字
     */
    public static Long isNumericToLong(String str) {
        try {
            Matcher isNum = BaseConstants.NUMERIC.matcher(str);
            if (isNum.matches()) {
                return Long.parseLong(str);
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return null;
    }

    /**
     * 获取语言文件中的配置
     *
     * @param langMsg 配置项
     * @return 语言
     */
    public static String getLangMsg(String langMsg) {
        FileConfiguration langConfig = BaseConstants.LANG_CONFIG;
        if (langConfig == null) {
            return "§4加载语言文件出错,请重新加载!";
        }
        String msg = langConfig.getString(langMsg);
        return msg != null ? replaceChatColor(msg) : "§4语言文件中未找到该配置项:" + langMsg;
    }

    /**
     * 获取中文名称
     *
     * @param displayName 显示名称
     * @param type        类型
     * @return 中文名
     */
    public static String getDisplayName(String displayName, String type) {
        // 如果不为空,返回
        if (StringUtils.isNotBlank(displayName)) {
            return displayName;
        }
        // 如果有汉化信息,转换
        if (BaseConstants.jsonCacheMap.size() > 0) {
            // 物品
            String name = BaseConstants.jsonCacheMap.get("item.minecraft." + type.toLowerCase());
            if (name == null) {
                // 方块
                name = BaseConstants.jsonCacheMap.get("block.minecraft." + type.toLowerCase());
            }
            return name;
        }
        // 如果有自定义汉化信息,转换
        if (BaseConstants.itemJsonCacheMap.size() > 0) {
            // 物品
            String name = BaseConstants.itemJsonCacheMap.get(type);
            if (name == null) {
                return type;
            }
            return name;
        }
        // 如果有云汉化信息,转换
        if (BaseConstants.cloudItemJsonCacheMap.size() > 0) {
            // 物品
            String name = BaseConstants.cloudItemJsonCacheMap.get(type);
            if (name == null) {
                return type;
            }
            return name;
        }
        // 直接返回类型
        return type;
    }

    /**
     * 读取json文件并给map赋值
     *
     * @param file 文件
     */
    public static void readJsonFileToItemJsonCacheMap(File file) {
        String json = readJsonFile(file);
        if (json != null && json.length() > 1) {
            BaseConstants.itemJsonCacheMap = new Gson().fromJson(json, new TypeToken<Map<String, String>>() {
            }.getType());
        }
    }

    /**
     * 读取JSON文件并给MAP赋值
     *
     * @param file 文件
     */
    public static void readJsonFileToJsonCacheMap(File file) {
        String json = readJsonFile(file);
        if (json != null && json.length() > 1) {
            BaseConstants.jsonCacheMap = new Gson().fromJson(json, new TypeToken<Map<String, String>>() {
            }.getType());
        }
    }

    /**
     * 读取json文件
     *
     * @param fileName json文件名
     * @return 返回json字符串
     */
    public static String readJsonFile(File fileName) {
        try {
            FileReader fileReader = new FileReader(fileName);
            Reader reader = new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8);
            int ch;
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     *
     * @param dateTime 时间
     * @return 时间间隔
     */
    public static int getDifferDay(Long dateTime) {
        return (int) ((System.currentTimeMillis() - dateTime) / (1000 * 3600 * 24));
    }

    /**
     * 玩家世界是否为地狱
     *
     * @param player 玩家
     * @return true 是
     */
    public static boolean playerWorldIsNether(Player player) {
        return World.Environment.NETHER.equals(player.getWorld().getEnvironment());
    }

    /**
     * 玩家世界是否bu为地狱
     *
     * @param player 玩家
     * @return true 是
     */
    public static boolean playerWorldIsNotNether(Player player) {
        return !playerWorldIsNether(player);
    }

    /**
     * 世界时间是否为夜晚
     *
     * @param player 玩家
     * @return true 是
     */
    public static boolean worldTimeIsNight(Player player) {
        long time = player.getWorld().getTime() % 24000L;
        return time < 0L || time > 12400L;
    }

    /**
     * 世界时间是否不为夜晚
     *
     * @param player 玩家
     * @return true 是
     */
    public static boolean worldTimeIsNotNight(Player player) {
        return !worldTimeIsNight(player);
    }

    /**
     * 判断是否晴天
     *
     * @param player 玩家
     * @return true 是
     */
    public static boolean worldIsStorm(Player player) {
        return player.getWorld().hasStorm();
    }

    /**
     * 判断是否不为晴天
     *
     * @param player 玩家
     * @return true 是
     */
    public static boolean worldIsNotStorm(Player player) {
        return !worldIsStorm(player);
    }

    /**
     * 玩家头上是否有方块
     *
     * @param player 玩家
     * @return true 有
     */
    public static boolean isUnderRoof(Player player) {
        Block block = player.getLocation().getBlock();
        if (player.getLocation().getY() >= BaseConstants.HEIGHT_254) {
            return false;
        }
        while (block.getY() + 1 <= BaseConstants.HEIGHT_255) {
            block = block.getRelative(BlockFace.UP);
            if (!Material.AIR.equals(block.getType())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 玩家头上是否没有方块
     *
     * @param player 玩家
     * @return true 是
     */
    public static boolean isNotUnderRoof(Player player) {
        return !isUnderRoof(player);
    }

    /**
     * 时间增加
     *
     * @param day 增加天数
     * @return 时间
     */
    public static Date getDate(Integer day) {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        //把日期往后增加一天.整数往后推,负数往前移动
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }

    /**
     * 判断传入的时间是否大于2120-01-01 00:00:00
     *
     * @param date 日期
     * @return true 是
     */
    public static boolean isPerpetual(Date date) {
        return date.getTime() > 4733481600000L;
    }

    /**
     * 获取今日日期
     *
     * @return 今日日期
     */
    public static Date getToday() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse(sdf.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取版本第一位
     *
     * @param plugin 插件
     * @return 版本
     * @since 1.1.5
     */
    public static int getFirstPluginVersion(Plugin plugin) {
        String version = plugin.getDescription().getVersion();
        String[] split = version.split("\\.");
        return Integer.parseInt(split[0]);
    }

    /**
     * 获取[]内的数字
     * 例如：[123]测试123 获取到 123
     *
     * @param str 字符串
     * @return 数字
     * @since 1.1.6
     */
    public static int getSeparatorCustomNameNumber(String str) {
        Matcher matcher = BaseConstants.BRACKET_NUMBER.matcher(str);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return -1;
    }

    /**
     * 根据分隔符获取自定义名称
     * 例如：[123]测试123 获取到 测试123
     *
     * @param str       字符串
     * @param separator 分隔符
     * @return 字符串
     * @since 1.1.6
     */
    public static String getSeparatorCustomName(String str, String separator) {
        return str.substring(str.indexOf(separator) + 1);
    }

}