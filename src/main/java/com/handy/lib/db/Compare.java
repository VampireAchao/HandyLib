package com.handy.lib.db;

import com.handy.lib.db.enums.SqlKeyword;

import java.io.Serializable;
import java.util.List;

/**
 * 查询条件封装
 *
 * @author handy
 * @since 1.4.8
 */
public class Compare<T> implements Serializable {

    private static final long serialVersionUID = 3795742407169621870L;
    private final DbSql dbSql;

    protected Compare(DbSql dbSql) {
        this.dbSql = dbSql;
    }

    /**
     * 等于 =
     *
     * @param fn  字段
     * @param val 值
     * @param <R> 类型
     * @return this
     */
    public <R> Compare<T> eq(DbFunction<R, ?> fn, Object val) {
        return this.eq(true, fn, val);
    }

    /**
     * 等于 =
     *
     * @param condition 执行条件
     * @param fn        字段
     * @param val       值
     * @param <R>       类型
     * @return this
     */
    public <R> Compare<T> eq(boolean condition, DbFunction<R, ?> fn, Object val) {
        dbSql.addCondition(condition, DbColumnUtil.getFieldName(fn), SqlKeyword.EQ, val);
        return this;
    }

    /**
     * 不等于
     *
     * @param fn  字段
     * @param val 值
     * @param <R> 类型
     * @return this
     */
    public <R> Compare<T> ne(DbFunction<R, ?> fn, Object val) {
        return this.ne(true, fn, val);
    }

    /**
     * 不等于
     *
     * @param condition 执行条件
     * @param fn        字段
     * @param val       值
     * @param <R>       类型
     * @return this
     */
    public <R> Compare<T> ne(boolean condition, DbFunction<R, ?> fn, Object val) {
        dbSql.addCondition(condition, DbColumnUtil.getFieldName(fn), SqlKeyword.NE, val);
        return this;
    }

    /**
     * 大于
     *
     * @param fn  字段
     * @param val 值
     * @param <R> 类型
     * @return this
     */
    public <R> Compare<T> gt(DbFunction<R, ?> fn, Object val) {
        return this.gt(true, fn, val);
    }

    /**
     * 大于
     *
     * @param condition 执行条件
     * @param fn        字段
     * @param val       值
     * @param <R>       类型
     * @return this
     */
    public <R> Compare<T> gt(boolean condition, DbFunction<R, ?> fn, Object val) {
        dbSql.addCondition(condition, DbColumnUtil.getFieldName(fn), SqlKeyword.GT, val);
        return this;
    }

    /**
     * 小于
     *
     * @param fn  字段
     * @param val 值
     * @param <R> 类型
     * @return this
     */
    public <R> Compare<T> lt(DbFunction<R, ?> fn, Object val) {
        return this.lt(true, fn, val);
    }

    /**
     * 小于
     *
     * @param condition 执行条件
     * @param fn        字段
     * @param val       值
     * @param <R>       类型
     * @return this
     */
    public <R> Compare<T> lt(boolean condition, DbFunction<R, ?> fn, Object val) {
        dbSql.addCondition(condition, DbColumnUtil.getFieldName(fn), SqlKeyword.LT, val);
        return this;
    }

    /**
     * 大于等于
     *
     * @param fn  字段
     * @param val 值
     * @param <R> 类型
     * @return this
     */
    public <R> Compare<T> ge(DbFunction<R, ?> fn, Object val) {
        return this.ge(true, fn, val);
    }

    /**
     * 大于等于
     *
     * @param condition 执行条件
     * @param fn        字段
     * @param val       值
     * @param <R>       类型
     * @return this
     */
    public <R> Compare<T> ge(boolean condition, DbFunction<R, ?> fn, Object val) {
        dbSql.addCondition(condition, DbColumnUtil.getFieldName(fn), SqlKeyword.GE, val);
        return this;
    }

    /**
     * 小于等于
     *
     * @param fn  字段
     * @param val 值
     * @param <R> 类型
     * @return this
     */
    public <R> Compare<T> le(DbFunction<R, ?> fn, Object val) {
        return this.le(true, fn, val);
    }

    /**
     * 小于等于
     *
     * @param condition 执行条件
     * @param fn        字段
     * @param val       值
     * @param <R>       类型
     * @return this
     */
    public <R> Compare<T> le(boolean condition, DbFunction<R, ?> fn, Object val) {
        dbSql.addCondition(condition, DbColumnUtil.getFieldName(fn), SqlKeyword.LE, val);
        return this;
    }

    /**
     * LIKE '%值%'
     *
     * @param fn  字段
     * @param val 值
     * @param <R> 类型
     * @return this
     */
    public <R> Compare<T> like(DbFunction<R, ?> fn, Object val) {
        return this.like(true, fn, val);
    }

    /**
     * LIKE '%值%'
     *
     * @param condition 执行条件
     * @param fn        字段
     * @param val       值
     * @param <R>       类型
     * @return this
     */
    public <R> Compare<T> like(boolean condition, DbFunction<R, ?> fn, Object val) {
        dbSql.addCondition(condition, DbColumnUtil.getFieldName(fn), SqlKeyword.LIKE, DbConstant.PERCENT_SIGN + val + DbConstant.PERCENT_SIGN);
        return this;
    }

    /**
     * NOT LIKE '%值%'
     *
     * @param fn  字段
     * @param val 值
     * @param <R> 类型
     * @return this
     */
    public <R> Compare<T> notLike(DbFunction<R, ?> fn, Object val) {
        return this.notLike(true, fn, val);
    }

    /**
     * NOT LIKE '%值%'
     *
     * @param condition 执行条件
     * @param fn        字段
     * @param val       值
     * @param <R>       类型
     * @return this
     */
    public <R> Compare<T> notLike(boolean condition, DbFunction<R, ?> fn, Object val) {
        dbSql.addCondition(condition, DbColumnUtil.getFieldName(fn), SqlKeyword.NOT_LIKE, DbConstant.PERCENT_SIGN + val + DbConstant.PERCENT_SIGN);
        return this;
    }

    /**
     * LIKE '%值'
     *
     * @param fn  字段
     * @param val 值
     * @param <R> 类型
     * @return this
     */
    public <R> Compare<T> likeLeft(DbFunction<R, ?> fn, Object val) {
        return this.likeLeft(true, fn, val);
    }

    /**
     * LIKE '%值'
     *
     * @param condition 执行条件
     * @param fn        字段
     * @param val       值
     * @param <R>       类型
     * @return this
     */
    public <R> Compare<T> likeLeft(boolean condition, DbFunction<R, ?> fn, Object val) {
        dbSql.addCondition(condition, DbColumnUtil.getFieldName(fn), SqlKeyword.LIKE, DbConstant.PERCENT_SIGN + val);
        return this;
    }

    /**
     * LIKE '值%'
     *
     * @param fn  字段
     * @param val 值
     * @param <R> 类型
     * @return this
     */
    public <R> Compare<T> likeRight(DbFunction<R, ?> fn, Object val) {
        return this.likeRight(true, fn, val);
    }

    /**
     * LIKE '值%'
     *
     * @param condition 执行条件
     * @param fn        字段
     * @param val       值
     * @param <R>       类型
     * @return this
     */
    public <R> Compare<T> likeRight(boolean condition, DbFunction<R, ?> fn, Object val) {
        dbSql.addCondition(condition, DbColumnUtil.getFieldName(fn), SqlKeyword.LIKE, val + DbConstant.PERCENT_SIGN);
        return this;
    }

    /**
     * 字段 IN
     *
     * @param fn  字段
     * @param in  值
     * @param <R> 类型
     * @return this
     */
    public <R> Compare<T> in(DbFunction<R, ?> fn, List<Object> in) {
        return this.in(true, fn, in);
    }

    /**
     * 字段 IN
     *
     * @param condition 执行条件
     * @param fn        字段
     * @param in        值
     * @param <R>       类型
     * @return this
     */
    public <R> Compare<T> in(boolean condition, DbFunction<R, ?> fn, List<Object> in) {
        dbSql.addInCondition(condition, DbColumnUtil.getFieldName(fn), SqlKeyword.IN, in);
        return this;
    }

    /**
     * 字段 NOT IN
     *
     * @param fn  字段
     * @param in  值
     * @param <R> 类型
     * @return this
     */
    public <R> Compare<T> notIn(DbFunction<R, ?> fn, List<Object> in) {
        return this.notIn(true, fn, in);
    }

    /**
     * 字段 NOT IN
     *
     * @param condition 执行条件
     * @param fn        字段
     * @param in        值
     * @param <R>       类型
     * @return this
     */
    public <R> Compare<T> notIn(boolean condition, DbFunction<R, ?> fn, List<Object> in) {
        dbSql.addInCondition(condition, DbColumnUtil.getFieldName(fn), SqlKeyword.NOT_IN, in);
        return this;
    }

    /**
     * 排序：ORDER BY 字段 ASC
     *
     * @param fn  字段
     * @param <R> 类型
     * @return this
     */
    public <R> Compare<T> orderByAsc(DbFunction<R, ?> fn) {
        return this.orderByAsc(true, fn);
    }

    /**
     * 排序：ORDER BY 字段 ASC
     *
     * @param condition 执行条件
     * @param fn        字段
     * @param <R>       类型
     * @return this
     */
    public <R> Compare<T> orderByAsc(boolean condition, DbFunction<R, ?> fn) {
        dbSql.addOrderByCondition(condition, DbColumnUtil.getFieldName(fn), SqlKeyword.ASC);
        return this;
    }

    /**
     * 排序：ORDER BY 字段 DESC
     *
     * @param fn  字段
     * @param <R> 类型
     * @return this
     */
    public <R> Compare<T> orderByDesc(DbFunction<R, ?> fn) {
        return this.orderByDesc(true, fn);
    }

    /**
     * 排序：ORDER BY 字段 DESC
     *
     * @param condition 执行条件
     * @param fn        字段
     * @param <R>       类型
     * @return this
     */
    public <R> Compare<T> orderByDesc(boolean condition, DbFunction<R, ?> fn) {
        dbSql.addOrderByCondition(condition, DbColumnUtil.getFieldName(fn), SqlKeyword.DESC);
        return this;
    }

    /**
     * 分组：GROUP BY 字段
     *
     * @param fn  字段
     * @param <R> 类型
     * @return this
     */
    public <R> Compare<T> groupBy(DbFunction<R, ?> fn) {
        return this.groupBy(true, fn);
    }

    /**
     * 分组：GROUP BY 字段
     *
     * @param condition 执行条件
     * @param fn        字段
     * @param <R>       类型
     * @return this
     */
    public <R> Compare<T> groupBy(boolean condition, DbFunction<R, ?> fn) {
        dbSql.addGroupByCondition(condition, DbColumnUtil.getFieldName(fn));
        return this;
    }

    /**
     * limit
     *
     * @param pageNo   当前页
     * @param pageSize 每页显示条数
     * @param <R>      类型
     * @return this
     */
    public <R> Compare<T> limit(int pageNo, int pageSize) {
        return this.limit(true, pageNo, pageSize);
    }

    /**
     * limit
     *
     * @param condition 执行条件
     * @param pageNo    当前页
     * @param pageSize  每页显示条数
     * @param <R>       类型
     * @return this
     */
    public <R> Compare<T> limit(boolean condition, int pageNo, int pageSize) {
        dbSql.addLimitCondition(condition, pageNo, pageSize);
        return this;
    }

}