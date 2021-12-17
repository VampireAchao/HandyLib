package com.handy.lib.db.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 索引类型
 *
 * @author handy
 * @since 1.9.6
 */
@Getter
@AllArgsConstructor
public enum IndexEnum {

    /**
     * 无
     */
    NOT("NOT"),
    /**
     * 普通索引
     */
    INDEX("INDEX"),
    /**
     * 唯一索引
     */
    UNIQUE("UNIQUE");

    /**
     * 名称
     */
    public final String name;

}