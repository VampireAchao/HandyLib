package com.handy.lib.db.param;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 表信息
 *
 * @author handy
 * @since 1.4.8
 */
@Data
@Builder
public class TableInfoParam implements Serializable {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表注释
     */
    private String tableComment;
}
