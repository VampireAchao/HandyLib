package com.handy.lib.db;

import com.handy.lib.annotation.TableField;
import com.handy.lib.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户实体类。测试用
 */
@Data
@TableName(value = "user")
public class User implements Serializable {

    @TableField("login_name")
    private String loginName;

    @TableField("nick_name")
    private String nickName;

    @TableField("a")
    private int a;

    @TableField("a1")
    private Integer a1;
}