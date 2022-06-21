package cn.handyplus.lib.db;

import cn.handyplus.lib.annotation.TableField;
import cn.handyplus.lib.core.StrUtil;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 获取字段
 *
 * @author handy
 * @since 1.4.8
 */
public class DbColumnUtil {

    /**
     * 从注解上获取字段
     *
     * @param fn  表达式
     * @param <T> 类
     * @return 字段
     */
    public static <T> String getFieldName(DbFunction<T, ?> fn) {
        // 从function取出序列化方法
        Method writeReplaceMethod;
        try {
            writeReplaceMethod = fn.getClass().getDeclaredMethod("writeReplace");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        // 从序列化方法取出序列化的lambda信息
        boolean isAccessible = writeReplaceMethod.isAccessible();
        writeReplaceMethod.setAccessible(true);
        SerializedLambda serializedLambda;
        try {
            serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(fn);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        writeReplaceMethod.setAccessible(isAccessible);

        // 从lambda信息取出method、field、class等
        String fieldName = serializedLambda.getImplMethodName().substring("get".length());
        fieldName = fieldName.replaceFirst(fieldName.charAt(0) + "", (fieldName.charAt(0) + "").toLowerCase());
        Field field;
        try {
            field = Class.forName(serializedLambda.getImplClass().replace("/", ".")).getDeclaredField(fieldName);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        // 从field取出字段名
        TableField tableField = field.getAnnotation(TableField.class);
        if (tableField == null || StrUtil.isEmpty(tableField.value())) {
            throw new RuntimeException("TableField 为空");
        }
        return tableField.value();
    }

}