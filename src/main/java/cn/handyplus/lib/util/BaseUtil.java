package cn.handyplus.lib.util;

import cn.handyplus.lib.InitApi;
import cn.handyplus.lib.api.MessageApi;
import cn.handyplus.lib.constants.BaseConstants;
import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.core.DateUtil;
import cn.handyplus.lib.core.JsonUtil;
import cn.handyplus.lib.core.StrUtil;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
        if (StrUtil.isEmpty(str)) {
            return "";
        }
        return replaceRpgChatColor(str.replace("&", "§"));
    }

    /**
     * 颜色代码转换
     *
     * @param stringList 消息
     * @return 转换后的字符串集合
     * @since 2.4.5
     */
    public static List<String> replaceChatColor(List<String> stringList) {
        return replaceChatColor(stringList, true);
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
        if (StrUtil.isEmpty(str)) {
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
        if (StrUtil.isEmpty(str)) {
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
            if (StrUtil.isEmpty(rpgStr) || rpgStr.length() != 6) {
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
        if (StrUtil.isEmpty(str)) {
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
        if (StrUtil.isEmpty(str)) {
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
        return msg != null ? replaceChatColor(msg) : "§4语言文件中未找到该配置项:" + langMsg + "§a,请删除现在语言文件让重新生成";
    }

    /**
     * 获取语言文件中的配置
     *
     * @param langMsg    配置项
     * @param defaultMsg 默认语言
     * @return 语言
     */
    public static String getLangMsg(String langMsg, String defaultMsg) {
        FileConfiguration langConfig = BaseConstants.LANG_CONFIG;
        if (langConfig == null) {
            return "§4加载语言文件出错,请重新加载!";
        }
        String msg = langConfig.getString(langMsg);
        return msg != null ? replaceChatColor(msg) : defaultMsg;
    }

    /**
     * 获取中文名称
     *
     * @param itemStack 物品
     * @return 中文名
     * @since 2.8.9
     */
    public static String getDisplayName(ItemStack itemStack) {
        ItemMeta itemMeta = ItemStackUtil.getItemMeta(itemStack);
        return getDisplayName(itemMeta.getDisplayName(), itemStack.getType().name());
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
        if (StrUtil.isNotEmpty(displayName)) {
            return displayName;
        }
        // 如果有汉化信息,转换
        if (BaseConstants.JSON_CACHE_MAP.size() > 0) {
            // 物品
            String name = BaseConstants.JSON_CACHE_MAP.get("item.minecraft." + type.toLowerCase());
            if (name == null) {
                // 方块
                name = BaseConstants.JSON_CACHE_MAP.get("block.minecraft." + type.toLowerCase());
            }
            if (name != null) {
                return name;
            }
        }
        // 如果有自定义汉化信息,转换
        if (BaseConstants.ITEM_JSON_CACHE_MAP.size() > 0) {
            // 物品
            String name = BaseConstants.ITEM_JSON_CACHE_MAP.get(type);
            if (name != null) {
                return name;
            }
        }
        // 如果有云汉化信息,转换
        if (BaseConstants.CLOUD_ITEM_JSON_CACHE_MAP.size() > 0) {
            // 物品
            String name = BaseConstants.CLOUD_ITEM_JSON_CACHE_MAP.get(type);
            if (name != null) {
                return name;
            }
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
            BaseConstants.ITEM_JSON_CACHE_MAP = JsonUtil.toMap(json);
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
            BaseConstants.JSON_CACHE_MAP = JsonUtil.toMap(json);
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
     * 获取版本第一位
     *
     * @param plugin 插件
     * @return 版本第一位
     * @since 1.1.5
     */
    public static int getFirstPluginVersion(Plugin plugin) {
        String version = plugin.getDescription().getVersion();
        String[] split = version.split("\\.");
        return Integer.parseInt(split[0]);
    }

    /**
     * 获取版本第二位
     *
     * @param plugin 插件
     * @return 版本第二位
     * @since 1.8.4
     */
    public static int getTwoPluginVersion(Plugin plugin) {
        String version = plugin.getDescription().getVersion();
        String[] split = version.split("\\.");
        return Integer.parseInt(split[1]);
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

    /**
     * 根据version字段，进行文件版本处理
     *
     * @param path    文件路径 xxx.yml
     * @param version 版本号
     * @since 1.8.1
     */
    public static void fileVersion(String path, int version) {
        File file = new File(InitApi.PLUGIN.getDataFolder(), path);
        if (!(file.exists())) {
            MessageApi.sendConsoleDebugMessage("文件不存在，无需更新");
            return;
        }
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        int versionId = yamlConfiguration.getInt(BaseConstants.VERSION);
        if (versionId == version) {
            MessageApi.sendConsoleDebugMessage("version 相同，无需更新");
            return;
        }
        // 生成旧文件名
        String[] split = path.split("\\.");
        String fileName = split[0];
        String oleFileName = fileName + "_" + DateUtil.format(new Date(), DateUtil.YYYY_HH) + split[1];
        MessageApi.sendConsoleDebugMessage("旧文件备份, 生成新文件名: " + oleFileName);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)); BufferedWriter out = new BufferedWriter(new FileWriter(oleFileName))) {
            StringBuilder result = new StringBuilder();
            String str;
            // 使用readLine方法，一次读一行
            while ((str = br.readLine()) != null) {
                result.append(System.lineSeparator()).append(str);
            }
            out.write(result.toString());
            InitApi.PLUGIN.saveResource(path, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 转化版本号
     *
     * @param version 版本号
     * @return 版本号数字
     * @since 2.6.8
     */
    public static Integer convertVersion(String version) {
        version = version.replaceAll("[^\\d.]", "");
        if (!version.contains(".")) {
            return Integer.parseInt(version);
        }
        StringBuilder var3 = new StringBuilder();
        String[] var7;
        int var6 = (var7 = version.split("\\.")).length;
        for (int var5 = 0; var5 < var6; ++var5) {
            String var4 = var7[var5];
            String var8 = var4;
            if (var4.length() == 1) {
                var8 = "0" + var4;
            }
            var3.append(var8);
        }
        return Integer.parseInt(var3.toString());
    }

    /**
     * 通过github url获取tag_name号
     *
     * @param url url
     * @return 版本号
     * @since 2.6.8
     */
    public static String getOfficialVersion(String url) {
        try {
            String result = HttpUtil.get(url);
            if (StrUtil.isEmpty(result)) {
                return null;
            }
            JsonObject jsonObject = JsonUtil.toBean(result, JsonObject.class);
            // 获取到的信息
            return jsonObject.get("tag_name").getAsString();
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 权限
     *
     * @since 3.0.1
     */
    protected static boolean PERMISSION = false;

    /**
     * 获取插件权限
     *
     * @return 权限
     * @since 3.0.2
     */
    public static boolean getPermission() {
        return PERMISSION;
    }

    /**
     * 校验是否验证成功
     *
     * @param command 命令
     * @return 是否成功
     * @since 3.0.1
     */
    public static boolean isPerMission(String command) {
        return InitApi.getPermission() && !PERMISSION && !("getIp".equalsIgnoreCase(command) || "reload".equalsIgnoreCase(command));
    }

    /**
     * 加载插件
     *
     * @param pluginName 插件名
     * @since 3.1.1
     */
    private static boolean hook(String pluginName) {
        return Bukkit.getPluginManager().getPlugin(pluginName) != null;
    }

    /**
     * 加载插件
     *
     * @param pluginName     插件名
     * @param succeedMsgNode 成功消息节点
     * @param failureMsgNode 失败消息节点
     * @since 3.1.1
     */
    private static boolean hook(String pluginName, String succeedMsgNode, String failureMsgNode) {
        boolean rst = hook(pluginName);
        MessageApi.sendConsoleMessage(BaseUtil.getLangMsg(rst ? succeedMsgNode : failureMsgNode));
        return rst;
    }

}