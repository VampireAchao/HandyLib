package com.handy.lib.db;

/**
 * 查询条件封装
 *
 * @author handy
 * @since 1.4.8
 */
public abstract class AbstractWrapper<T> extends DbExecution<T> implements Compare {

    /**
     * 等于 =
     *
     * @param condition 执行条件
     * @param fn        字段
     * @param val       值
     * @return this
     */
    @Override
    public <R> Compare eq(boolean condition, DbFunction<R, ?> fn, Object val) {
        dbSql.addCondition(condition, DbColumnUtil.getFieldName(fn), DbConstant.EQ, val);
        return this;
    }

}