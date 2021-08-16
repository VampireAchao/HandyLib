package com.handy.lib.db;

import java.io.Serializable;

/**
 * 基础方法
 *
 * @author handy
 */
public interface BaseMapper<T> extends Serializable {
    /**
     * 插入一条记录
     *
     * @return id
     */
    int insert();

    /**
     * 根据 entity 条件，查询一条记录
     *
     * @return T
     */
    T selectOne();
}