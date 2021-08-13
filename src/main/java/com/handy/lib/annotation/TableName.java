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
     * 别名
     *
     * @return 别名
     */
    String alias() default "";
}