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
}
