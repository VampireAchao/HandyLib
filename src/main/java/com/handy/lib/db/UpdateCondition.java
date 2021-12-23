package com.handy.lib.db;

import java.io.Serializable;

/**
 * 更新条件构造器
 *
 * @author handy
 */
public class UpdateCondition<T> implements Serializable {

    private static final long serialVersionUID = 1019927794965371697L;
    private final DbSql dbSql;

    protected UpdateCondition(DbSql dbSql) {
        this.dbSql = dbSql;
    }

    /**
     * 更新条件
     *
     * @param fn  字段
     * @param val 值
     * @param <R> 类型
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
     * @param <R>       类型
     * @return this
     */
    public <R> UpdateCondition<T> set(boolean condition, DbFunction<R, ?> fn, Object val) {
        dbSql.updateCondition(condition, DbColumnUtil.getFieldName(fn), val);
        return this;
    }

    /**
     * 更新条件 减少
     *
     * @param fn                 字段
     * @param calculateFieldName 参与计算的字段
     * @param val                值
     * @param <R>                类型
     * @return this
     * @since 2.1.1
     */
    public <R> UpdateCondition<T> subtract(DbFunction<R, ?> fn, DbFunction<R, ?> calculateFieldName, Object val) {
        return this.subtract(true, fn, calculateFieldName, val);
    }

    /**
     * 更新条件 减少
     *
     * @param condition          执行条件
     * @param fn                 字段
     * @param calculateFieldName 参与计算的字段
     * @param val                值
     * @param <R>                类型
     * @return this
     * @since 2.1.0
     */
    public <R> UpdateCondition<T> subtract(boolean condition, DbFunction<R, ?> fn, DbFunction<R, ?> calculateFieldName, Object val) {
        dbSql.updateCondition(condition, DbColumnUtil.getFieldName(fn), DbColumnUtil.getFieldName(calculateFieldName), DbConstant.SUBTRACT, val);
        return this;
    }

    /**
     * 更新条件 增加
     *
     * @param fn                 字段
     * @param calculateFieldName 参与计算的字段
     * @param val                值
     * @param <R>                类型
     * @return this
     * @since 2.1.1
     */
    public <R> UpdateCondition<T> add(DbFunction<R, ?> fn, DbFunction<R, ?> calculateFieldName, Object val) {
        return this.add(true, fn, calculateFieldName, val);
    }

    /**
     * 更新条件 增加
     *
     * @param condition          执行条件
     * @param fn                 字段
     * @param calculateFieldName 参与计算的字段
     * @param val                值
     * @param <R>                类型
     * @return this
     * @since 2.1.0
     */
    public <R> UpdateCondition<T> add(boolean condition, DbFunction<R, ?> fn, DbFunction<R, ?> calculateFieldName, Object val) {
        dbSql.updateCondition(condition, DbColumnUtil.getFieldName(fn), DbColumnUtil.getFieldName(calculateFieldName), DbConstant.ADD, val);
        return this;
    }

}