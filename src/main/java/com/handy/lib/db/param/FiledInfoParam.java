package com.handy.lib.db.param;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 字段信息
 *
 * @author handy
 * @since 1.4.8
 */
@Data
@Builder
public class FiledInfoParam implements Serializable {

    private static final long serialVersionUID = -674837862935503141L;
    /**
     * 数据库字段名
     */
    private String filedName;

    /**
     * 字段真实名
     */
    private String fieldRealName;

    /**
     * 字段类型
     */
    private String filedType;

    /**
     * 字段注释
     */
    private String filedComment;

    /**
     * 字段是否可为null
     */
    private Boolean filedNotNull;

    /**
     * 字段坐标
     */
    private Integer filedIndex;

    /**
     * 字段长度
     */
    private Integer filedLength;
}
