package com.handy.lib.core;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 集合工具
 *
 * @author handy
 * @since 1.1.8
 */
public class CollUtil {

    /**
     * 集合转,分隔的字符串
     *
     * @param list 集合
     * @return 字符串
     */
    public static <T> String listToStr(List<T> list) {
        return list.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    /**
     * 集合是否为空
     *
     * @param collection 集合
     * @return true 是
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 集合是否不为空
     *
     * @param collection 集合
     * @return true 是
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 判断list是否相等
     *
     * @param list  list
     * @param list1 list1
     * @return true/等于
     */
    public static boolean equals(List<String> list, List<String> list1) {
        if (list == list1) {
            return true;
        }
        if (list == null) {
            return false;
        }
        return list.equals(list1);
    }
}
