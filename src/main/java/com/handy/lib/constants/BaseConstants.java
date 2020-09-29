package com.handy.lib.constants;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author hs
 * @Description: {一些缓存}
 * @date 2020/7/10 17:59
 */
public abstract class BaseConstants {
    /**
     * 数字正则
     */
    public final static Pattern NUMERIC = Pattern.compile("^-?\\d+(\\.\\d+)?$");

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
    public static Boolean SIGN = false;

    /**
     * true
     */
    public static String TRUE = "true";

    /**
     * 高度
     */
    public static Double HEIGHT_254 = 254.0D;

    /**
     * 高度
     */
    public static Integer HEIGHT_255 = 255;
}
