package cn.handyplus.lib.core;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

/**
 * JSON工具类,封装Gson
 *
 * @author handy
 * @since 2.7.5
 */
public class JsonUtil {

    /**
     * 转化json
     *
     * @param obj obj
     * @return json
     */
    public static String toJson(Object obj) {
        return new Gson().toJson(obj);
    }

    /**
     * 转化为类
     *
     * @param json json
     * @param t    类
     * @param <T>  类型
     * @return 类
     */
    public static <T> T toBean(String json, Class<T> t) {
        return new Gson().fromJson(json, t);
    }

    /**
     * 转化为类集合
     *
     * @param json json
     * @param t    类
     * @param <T>  类型
     * @return 类集合
     */
    public static <T> List<T> toList(String json, Class<T> t) {
        return new Gson().fromJson(json, new TypeToken<List<T>>() {
        }.getType());
    }

    /**
     * 转化为Map
     *
     * @param json json
     * @return Map
     */
    public static Map<String, String> toMap(String json) {
        return new Gson().fromJson(json, new TypeToken<Map<String, String>>() {
        }.getType());
    }

}