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
     * 具体方法
     */
    private Method method;
}
