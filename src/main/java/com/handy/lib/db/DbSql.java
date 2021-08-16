package com.handy.lib.db;

import com.handy.lib.core.StrUtil;
import lombok.Builder;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * dbSql处理器
 *
 * @author handy
 * @since 1.4.8
 */
@Builder
public class DbSql implements Serializable {

    /**
     * select sql
     */
    private String select;

    /**
     * insert sql
     */
    private String insert;

    /**
     * count sql
     */
    private String count;

    /**
     * form sql
     */
    private String from;

    /**
     * join sql
     */
    private String join;

    /**
     * where sql
     */
    private String where;

    /**
     * filed sql
     */
    private String filed;

    /**
     * limit sql
     */
    private String limit;

    /**
     * order sql
     */
    private String order;

    /**
     * where data
     */
    private LinkedHashMap<String, Object> whereData;

    /**
     * filed index
     */
    private LinkedHashMap<String, Integer> filedIndexMap;

    public String selectCountSql() {
        return assemblySql(this.select, this.count, this.from, this.join, this.where);
    }

    public String selectDataSql() {
        return assemblySql(this.select, this.filed, this.from, this.join, this.where, this.order, this.limit);
    }

    public String insertDataSql() {
        return assemblySql(this.insert, this.filed, this.from, this.join, this.where, this.order, this.limit);
    }

    /**
     * 普通查询条件
     *
     * @param condition  是否执行
     * @param fieldName  属性
     * @param sqlKeyword SQL 关键词
     * @param val        条件值
     */
    protected void addCondition(boolean condition, String fieldName, String sqlKeyword, Object val) {
        if (!condition) {
            return;
        }
        this.where += DbConstant.AND + DbConstant.POINT + fieldName + DbConstant.POINT + sqlKeyword + DbConstant.QUESTION_MARK;
        whereData.put(fieldName, val);
    }

    /**
     * 获取条件
     *
     * @return 条件
     */
    public LinkedHashMap<String, Object> getWhereData() {
        return this.whereData;
    }

    /**
     * 获取条件
     *
     * @return 条件
     */
    public LinkedHashMap<String, Integer> getFiledIndexMap() {
        return this.filedIndexMap;
    }

    /**
     * 组合sql
     *
     * @param sqlColl sql内容
     * @return sql
     */
    private static String assemblySql(String... sqlColl) {
        StringBuilder sb = new StringBuilder();
        for (String sql : sqlColl) {
            if (StrUtil.isEmpty(sql)) {
                continue;
            }
            sb.append(sql);
        }
        return sb.toString();
    }

}