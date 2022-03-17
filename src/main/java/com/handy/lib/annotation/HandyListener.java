package com.handy.lib.annotation;

import com.handy.lib.constants.VersionCheckEnum;

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
     * 使用该监听器的版本
     *
     * @return 版本
     */
    VersionCheckEnum version() default VersionCheckEnum.V_1_6;

}
