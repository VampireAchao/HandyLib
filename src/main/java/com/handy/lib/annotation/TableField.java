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
     * 注释
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
}
