package com.handy.lib.annotation;

import java.lang.annotation.*;

/**
 * 字段
 *
 * @author handy
 * @since 1.4.8
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface TableField {

    /**
     * 字段名
     *
     * @return 字段名
     */
    String value();

    /**
     * mysql 字段注释
     *
     * @return 注释
     */
    String comment() default "";

    /**
     * 长度
     *
     * @return 长度
     */
    int length() default 0;

    /**
     * 是否可为null
     * true可以为null，false不能为null
     * 默认为null
     *
     * @return 是否可为null
     */
    boolean notNull() default false;

    /**
     * 字段默认值
     *
     * @return 字段默认值
     * @since 1.8.6
     */
    String filedDefault() default "";

}