package com.handy.lib.db;

import java.io.Serializable;

/**
 * 查询条件封装
 *
 * @author handy
 * @since 1.4.8
 */
public interface Compare extends Serializable {

    /**
     * 等于 =
     *
     * @param condition 执行条件
     * @param fn        字段
     * @param val       值
     * @return this
     */
    <R> Compare eq(boolean condition, DbFunction<R, ?> fn, Object val);

}