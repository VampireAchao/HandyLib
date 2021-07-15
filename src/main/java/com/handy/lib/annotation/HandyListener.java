package com.handy.lib.annotation;

import java.lang.annotation.*;

/**
 * 监听器
 *
 * @author handy
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HandyListener {

    /**
     * 是否开启
     *
     * @return true是
     */
    boolean isEnabled() default true;
}
