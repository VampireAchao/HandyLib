package com.handy.lib.db;

import com.handy.lib.annotation.TableField;
import com.handy.lib.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体类。测试用
 *
 * @author handy
 */
@Data
@TableName(value = "user", comment = "备注")
public class User implements Serializable {

    @TableField(value = "id", comment = "ID")
    private Integer id;

    @TableField(value = "login_name", comment = "登录名", notNull = true)
    private String loginName;

    @TableField(value = "nick_name", comment = "昵称")
    private String nickName;

    @TableField("a测试")
    private int a;

    @TableField("a1")
    private Integer a1;

    @TableField("a2")
    private Date a2;
}