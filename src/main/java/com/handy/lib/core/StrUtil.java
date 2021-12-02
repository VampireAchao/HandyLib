package com.handy.lib.core;

import com.handy.lib.constants.BaseConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * 字符串工具
 *
 * @author handy
 * @since 1.1.8
 */
public class StrUtil {

    /**
     * 是否为空
     *
     * @param str 字符串
     * @return true/是
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * 是否不为空
     *
     * @param str 字符串
     * @return true/是
     */
    public static boolean isNotEmpty(CharSequence str) {
        return !isEmpty(str);
    }

    /**
     * 转换小写
     *
     * @param str 字符串
     * @return 小写字符串
     */
    public static String toLowerCase(String str) {
        return str != null ? str.toLowerCase() : null;
    }

    /**
     * 将#替换成空格
     *
     * @param str 字符串
     * @return 替换后的字符串
     */
    public static String replaceSpace(String str) {
        if (StrUtil.isEmpty(str)) {
            return str;
        }
        return str.replace("#", " ");
    }

    /**
     * 字符串转集合
     *
     * @param str 字符串
     * @return 集合
     */
    public static List<String> strToStrList(String str) {
        List<String> list = new ArrayList<>();
        if (StrUtil.isEmpty(str)) {
            return list;
        }
        return Arrays.stream(str.split(",")).map(String::trim).collect(Collectors.toList());
    }

    /**
     * 字符串转集合
     *
     * @param str 字符串
     * @return 集合
     */
    public static List<Long> strToLongList(String str) {
        List<Long> list = new ArrayList<>();
        if (StrUtil.isEmpty(str)) {
            return list;
        }
        return Arrays.stream(str.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
    }

    /**
     * 字符串转集合
     *
     * @param str 字符串
     * @return 集合
     */
    public static List<Integer> strToIntList(String str) {
        List<Integer> list = new ArrayList<>();
        if (StrUtil.isEmpty(str)) {
            return list;
        }
        return Arrays.stream(str.split(",")).map(s -> Integer.valueOf(s.trim())).collect(Collectors.toList());
    }

    /**
     * 下划线转驼峰
     *
     * @param str 字符
     * @return 结果
     * @since 1.4.8
     */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = BaseConstants.LINE_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 驼峰转下划线
     *
     * @param str 字符
     * @return 结果
     * @since 1.4.8
     */
    public static String humpToLine(String str) {
        Matcher matcher = BaseConstants.HUMP_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}