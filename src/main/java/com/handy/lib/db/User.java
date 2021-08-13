package com.handy.lib.db;

import com.handy.lib.annotation.TableField;

import java.io.Serializable;

/**
 * 用户实体类。测试用
 */
public class User implements Serializable {

    private String loginName;

    @TableField("nick")
    private String nickName;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}