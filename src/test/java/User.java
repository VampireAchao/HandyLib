
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

    private static final long serialVersionUID = -5329631778462768336L;

    @TableField(value = "id", comment = "ID")
    private Integer id;

    @TableField(value = "login_name", comment = "登录名", notNull = true)
    private String loginName;

    @TableField(value = "nick_name", comment = "昵称", filedDefault = "默认值")
    private String nickName;

    @TableField(value = "a测试", filedDefault = "1")
    private int a;

    @TableField("a1")
    private Integer a1;

    @TableField("a2")
    private Date a2;
}