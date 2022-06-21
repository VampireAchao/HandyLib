package cn.handyplus.lib.annotation;

import java.lang.annotation.*;

/**
 * 子命令
 *
 * @author handy
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HandySubCommand {

    /**
     * 父命令
     *
     * @return 父命令
     */
    String mainCommand();

    /**
     * 子命令
     *
     * @return 子命令
     */
    String subCommand();

    /**
     * 子命令权限
     *
     * @return 子命令权限
     */
    String permission() default "";
}
