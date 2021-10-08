package com.handy.lib.command;

import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 子命令缓存
 *
 * @author handy
 */
@Data
public class HandySubCommandParam implements Serializable {
    private static final long serialVersionUID = 1785490670379846671L;
    /**
     * 父命令
     */
    private String command;
    /**
     * 子命令
     */
    private String subCommand;
    /**
     * 子命令权限
     */
    private String permission;
    /**
     * 类
     */
    private Class<?> aClass;
    /**
     * 具体方法
     */
    private Method method;
}
