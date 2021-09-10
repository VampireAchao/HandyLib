package com.handy.lib.constants;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 常量池
 *
 * @author handy
 */
public abstract class BaseConstants {
    /**
     * 数字正则
     */
    public final static Pattern NUMERIC = Pattern.compile("^-?\\d+(\\.\\d+)?$");

    /**
     * 括号数字正则
     */
    public static final Pattern BRACKET_NUMBER = Pattern.compile("\\[(\\d+)\\]");

    /**
     * rgb变量正则
     */
    public final static Pattern RPG_PATTERN = Pattern.compile("%+[a-zA-Z0-9]+%");

    /**
     * 去除rgb变量正则
     */
    public final static Pattern RPG_DEL_PATTERN = Pattern.compile("[%]");

    /**
     * 强化变量正则
     */
    public final static Pattern INTENSIFY_PATTERN = Pattern.compile("§f\\[\\+§a+[0-9]+§f\\]");

    /**
     * 强化等级变量正则
     */
    public final static Pattern INTENSIFY_LEVEL_PATTERN = Pattern.compile("\\++[0-9]+§f\\]");

    /**
     * 下划线转驼峰正则
     */
    public final static Pattern LINE_PATTERN = Pattern.compile("_(\\w)");

    /**
     * 驼峰转下划线正则
     */
    public final static Pattern HUMP_PATTERN = Pattern.compile("[A-Z]");

    /**
     * 物品汉化map
     */
    public static Map<String, String> jsonCacheMap = new HashMap<>();

    /**
     * 自定义物品汉化map
     */
    public static Map<String, String> itemJsonCacheMap = new HashMap<>();

    /**
     * 云汉化map
     */
    public static Map<String, String> cloudItemJsonCacheMap = new HashMap<>();

    /**
     * 是否通过签名验证
     */
    public static Boolean SIGN_VERIFY = false;

    /**
     * true
     */
    public final static String TRUE = "true";

    /**
     * false
     */
    public final static String FALSE = "false";

    /**
     * 高度
     */
    public final static Double HEIGHT_254 = 254.0D;

    /**
     * 高度
     */
    public final static Integer HEIGHT_255 = 255;

    /**
     * mysql常量
     */
    public final static String MYSQL = "MySQL";

    /**
     * SQLite变量
     */
    public final static String SQLITE = "SQLite";

    /**
     * 存储类型
     */
    public final static String STORAGE_METHOD = "storage-method";

    /**
     * 是否检查更新
     */
    public final static String IS_CHECK_UPDATE = "isCheckUpdate";

    /**
     * 是否检查更新
     */
    public final static String IS_CHECK_UPDATE_TO_OP_MSG = "isCheckUpdateToOpMsg";

    /**
     * 经济插件
     */
    public final static String VAULT = "Vault";

    /**
     * 变量插件
     */
    public final static String PLACEHOLDER_API = "PlaceholderAPI";

    /**
     * 点券插件
     */
    public final static String PLAYER_POINTS = "PlayerPoints";

    /**
     * 怪物插件
     */
    public final static String MYTHIC_MOBS = "MythicMobs";

    /**
     * 公民插件
     */
    public final static String CITIZENS = "Citizens";

    /**
     * gui初始化大小
     */
    public final static int GUI_SIZE_54 = 54;

    /**
     * gui初始化大小
     */
    public final static int GUI_SIZE_27 = 27;

    /**
     * 颜色RPG配置
     */
    public static FileConfiguration COLOR_CONFIG;

    /**
     * 语言文件配置
     */
    public static FileConfiguration LANG_CONFIG;

    /**
     * 存储配置
     */
    public static FileConfiguration STORAGE_CONFIG;

    /**
     * 是否开启debug
     */
    public static boolean DEBUG = false;

}
