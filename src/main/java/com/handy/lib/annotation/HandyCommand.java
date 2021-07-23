package com.handy.lib.annotation;

import org.bukkit.permissions.PermissionDefault;

import java.lang.annotation.*;

/**
 * @author handy
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HandyCommand {

    /**
     * 命令
     */
    String name();

    /**
     * 权限
     */
    String permission() default "";

    /**
     * 别名
     */
    String[] aliases() default "";

    /**
     * 描述
     */
    String description() default "";

    /**
     * 没权限的提醒
     */
    String permissionMessage() default "§4你没有权限执行该命令";

    /**
     * 使用说明
     */
    String usage() default "";

    /**
     * 默认权限所有者
     */
    PermissionDefault PERMISSION_DEFAULT() default PermissionDefault.OP;

}