package com.handy.lib.annotation;

import java.lang.annotation.*;

/**
 * 表注释
 *
 * @author handy
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TableName {
    /**
     * 表名
     *
     * @return 表名
     */
    String value();

    /**
     * 注释
     *
     * @return 注释
     */
    String comment() default "";
}