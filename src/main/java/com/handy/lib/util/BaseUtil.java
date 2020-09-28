package com.handy.lib.util;

import com.google.gson.Gson;
import com.handy.lib.api.LangMsgApi;
import com.handy.lib.constants.BaseConstants;
import com.handy.lib.constants.VersionCheckEnum;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * @author hanshuai
 * @Description: {常用方法}
 * @date 2019/6/14 10:15
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
     * 转换小写
     *
     * @param str
     * @return
     */
    public static String toLowerCase(String str) {
        return str != null ? str.toLowerCase() : null;
    }

    /**
     * 颜色代码转换
     *
     * @param str 消息
     * @return
     */
    public static String replaceChatColor(String str) {
        return str.replaceAll("&", "§");
    }

    /**
     * 获取uuid
     *
     * @param playerName
     * @return
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
        Matcher isNum = BaseConstants.NUMERIC.matcher(str);
        if (isNum.matches()) {
            return Integer.valueOf(str);
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
        Matcher isNum = BaseConstants.NUMERIC.matcher(str);
        if (isNum.matches()) {
            return Long.parseLong(str);
        }
        return null;
    }

    /**
     * 获取语言文件中的配置
     *
     * @param langMsg 配置项
     * @return
     */
    public static String getLangMsg(String langMsg) {
        FileConfiguration langConfig = LangMsgApi.LANG_CONFIG;
        if (langConfig == null) {
            return "§4加载语言文件出错,请重新加载!";
        }
        String msg = langConfig.getString(langMsg);
        return msg != null ? replaceChatColor(msg) : "§4语言文件中未找到该配置项:" + langMsg;
    }

    /**
     * 将#替换成空格
     *
     * @param str 字符串
     * @return
     */
    public static String replaceSpace(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        return str.replace("#", " ");
    }

    /**
     * 获取中文名称
     *
     * @param displayName
     * @param type
     * @return
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
     * 字符串转集合
     *
     * @param str
     * @return
     */
    public static List<String> strToStrList(String str) {
        List<String> list = new ArrayList<>();
        if (StringUtils.isBlank(str)) {
            return list;
        }
        return Arrays.asList(str.split(","));
    }

    /**
     * 字符串转集合
     *
     * @param str
     * @return
     */
    public static List<Long> strToLongList(String str) {
        List<Long> list = new ArrayList<>();
        if (StringUtils.isBlank(str)) {
            return list;
        }
        return Arrays.stream(str.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
    }

    /**
     * 集合转,分隔的字符串
     *
     * @param list
     * @return
     */
    public static String listToStr(List list) {
        return StringUtils.join(list.toArray(), ",");
    }

    /**
     * 给物品添加附魔效果
     *
     * @param itemMeta
     */
    public static void setEnchant(ItemMeta itemMeta) {
        // 耐久
        itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        VersionCheckEnum versionCheckEnum = VersionCheckEnum.getEnum();
        if (!VersionCheckEnum.V_1_7.equals(versionCheckEnum)) {
            // 隐藏附魔效果
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
    }

    /**
     * 隐藏附魔效果
     *
     * @param itemMeta
     */
    public static void hideEnchant(ItemMeta itemMeta) {
        VersionCheckEnum versionCheckEnum = VersionCheckEnum.getEnum();
        if (!VersionCheckEnum.V_1_7.equals(versionCheckEnum)) {
            // 隐藏附魔效果
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
    }

    /**
     * 物品生成
     *
     * @param material    材质
     * @param displayName 名称
     * @param loreList    lore
     * @return 自定义物品
     */
    public static ItemStack getItemStack(Material material, String displayName, List<String> loreList) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(displayName);
            if (loreList != null && loreList.size() > 0) {
                itemMeta.setLore(loreList);
            }
            // 附魔效果
            setEnchant(itemMeta);
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    /**
     * 物品生成
     *
     * @param material    材质
     * @param displayName 名称
     * @param loreList    lore
     * @param isEnchant   附魔效果
     * @return 自定义物品
     */
    public static ItemStack getItemStack(Material material, String displayName, List<String> loreList, Boolean isEnchant) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(displayName);
            if (loreList != null && loreList.size() > 0) {
                itemMeta.setLore(loreList);
            }
            if (isEnchant) {
                // 附魔效果
                setEnchant(itemMeta);
            }
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    /**
     * 读取json文件并给map赋值
     *
     * @param file 文件
     */
    public static void readJsonFileToItemJsonCacheMap(File file) {
        String json = readJsonFile(file);
        if (json != null && json.length() > 1) {
            BaseConstants.itemJsonCacheMap = new Gson().fromJson(json, Map.class);
        }
    }

    /**
     * 读取JSON文件并给MAP赋值
     *
     * @PARAM file 文件
     */
    public static void readJsonFileToJsonCacheMap(File file) {
        String json = readJsonFile(file);
        if (json != null && json.length() > 1) {
            BaseConstants.jsonCacheMap = new Gson().fromJson(json, Map.class);
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
            int ch = 0;
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
     * 集合是否为空
     *
     * @param collection
     * @return
     */
    public static boolean colLIsEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 集合是否不为空
     *
     * @param collection
     * @return
     */
    public static boolean colLIsNotEmpty(Collection<?> collection) {
        return !colLIsEmpty(collection);
    }

    /**
     * 物品减少计算
     *
     * @param playerInventory 玩家背包
     * @param itemStack       指定物品
     * @param amount          数量
     * @return 是否成功扣除
     */
    public static Boolean removeItem(PlayerInventory playerInventory, ItemStack itemStack, Integer amount) {
        ItemStack[] contents = playerInventory.getContents();
        int num = 0;
        List<ItemStack> items = new ArrayList<>();
        for (ItemStack item : contents) {
            if (item == null || Material.AIR.equals(item.getType())) {
                continue;
            }

            if (item.isSimilar(itemStack)) {
                num += item.getAmount();
                items.add(item);
            }
            if (num == amount) {
                for (ItemStack itemStack1 : items) {
                    playerInventory.removeItem(itemStack1);
                }
                return true;
            }
            if (num > amount) {
                for (ItemStack itemStack1 : items) {
                    if (amount > itemStack1.getAmount()) {
                        amount = amount - itemStack1.getAmount();
                        playerInventory.removeItem(itemStack1);
                    } else {
                        itemStack1.setAmount(itemStack1.getAmount() - amount);
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 进行物品发送
     *
     * @param playerInventory 玩家背包
     * @param itemStack       物品
     * @param amount          发送数量
     */
    private void addItem(PlayerInventory playerInventory, ItemStack itemStack, int amount) {
        int maxStackSize = itemStack.getMaxStackSize();
        if (amount > maxStackSize) {
            // 如果发送数量大于最大上限
            itemStack.setAmount(maxStackSize);
            playerInventory.addItem(itemStack);
            addItem(playerInventory, itemStack, amount - maxStackSize);
        } else {
            // 小于等于直接发送
            itemStack.setAmount(amount);
            playerInventory.addItem(itemStack);
        }
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     *
     * @param dateTime 时间
     * @return
     */
    public static int getDifferDay(Long dateTime) {
        return (int) ((System.currentTimeMillis() - dateTime) / (1000 * 3600 * 24));
    }

    /**
     * 玩家时间是否为夜晚
     *
     * @param player 玩家
     * @return
     */
    public static boolean playerTimeIsNether(Player player) {
        return World.Environment.NETHER.equals(player.getWorld().getEnvironment());
    }

    /**
     * 玩家时间是否为夜晚
     *
     * @param player 玩家
     * @return
     */
    public static boolean playerTimeIsNotNether(Player player) {
        return !playerTimeIsNether(player);
    }

    /**
     * 世界时间是否为夜晚
     *
     * @param player 玩家
     * @return
     */
    public static boolean worldTimeIsNight(Player player) {
        long time = player.getWorld().getTime() % 24000L;
        return time < 0L || time > 12400L;
    }

    /**
     * 世界时间是否为夜晚
     *
     * @param player 玩家
     * @return
     */
    public static boolean worldTimeIsNotNight(Player player) {
        return !worldTimeIsNight(player);
    }

    /**
     * 判断是否晴天
     *
     * @param player 玩家
     * @return
     */
    public static boolean worldIsStorm(Player player) {
        return player.getWorld().hasStorm();
    }

    /**
     * 判断是否晴天
     *
     * @param player 玩家
     * @return
     */
    public static boolean worldIsNotStorm(Player player) {
        return !worldIsStorm(player);
    }

    /**
     * 玩家头上是否有方块
     *
     * @param player 玩家
     * @return
     */
    public static boolean isUnderRoof(Player player) {
        Block block = player.getLocation().getBlock();
        if (player.getLocation().getY() >= 254.0D) {
            return false;
        }
        while (block.getY() + 1 <= 255) {
            block = block.getRelative(BlockFace.UP);
            if (!Material.AIR.equals(block.getType())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 玩家头上是否有方块
     *
     * @param player 玩家
     * @return
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

}
