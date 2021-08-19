package com.handy.lib.db;

import java.io.Serializable;

/**
 * 更新条件构造器
 *
 * @author handy
 */
public class UpdateCondition<T> implements Serializable {

    private final DbSql dbSql;

    protected UpdateCondition(DbSql dbSql) {
        this.dbSql = dbSql;
    }

    /**
     * 更新条件
     *
     * @param fn  字段
     * @param val 值
     * @return this
     */
    public <R> UpdateCondition<T> set(DbFunction<R, ?> fn, Object val) {
        return this.set(true, fn, val);
    }

    /**
     * 更新条件
     *
     * @param condition 执行条件
     * @param fn        字段
     * @param val       值
     * @return this
     */
    public <R> UpdateCondition<T> set(boolean condition, DbFunction<R, ?> fn, Object val) {
        dbSql.updateCondition(condition, DbColumnUtil.getFieldName(fn), val);
        return this;
    }

}