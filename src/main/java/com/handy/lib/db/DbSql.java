package com.handy.lib.db;

import com.handy.lib.core.CollUtil;
import com.handy.lib.core.StrUtil;
import com.handy.lib.db.enums.SqlKeyword;
import com.handy.lib.db.param.FiledInfoParam;
import com.handy.lib.db.param.TableInfoParam;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * dbSql处理器
 *
 * @author handy
 * @since 1.4.8
 */
@Builder
public class DbSql implements Serializable {

    private static final long serialVersionUID = 8696707404811115903L;
    /**
     * 表名称
     */
    private String tableName;

    /**
     * 表信息
     */
    @Getter
    private TableInfoParam tableInfoParam;

    /**
     * filed sql
     */
    private String filed;

    /**
     * 字段信息 key: 表字段名;val: 字段信息
     */
    @Getter
    private LinkedHashMap<String, FiledInfoParam> filedInfoMap;

    /**
     * where sql
     */
    private String where;

    /**
     * update Filed sql
     */
    private List<String> updateFiledList;

    /**
     * update Filed map
     */
    @Getter
    private LinkedHashMap<Integer, Object> updateFiledMap;

    /**
     * limit sql
     */
    private String limit;

    /**
     * order sql
     */
    private String order;

    /**
     * group sql
     */
    private String group;


    protected String selectCountSql() {
        return assemblySql(DbConstant.SELECT, DbConstant.COUNT, DbConstant.FORM, this.tableName, this.where);
    }

    protected String selectDataSql() {
        return assemblySql(DbConstant.SELECT, this.filed, DbConstant.FORM, this.tableName, this.where, this.group, this.order, this.limit);
    }

    protected String insertDataSql() {
        List<String> questionMarkList = new ArrayList<>();
        for (int i = 0; i < this.filedInfoMap.size(); i++) {
            questionMarkList.add(DbConstant.QUESTION_MARK);
        }
        return assemblySql(DbConstant.INSERT, this.tableName, DbConstant.LEFT_BRACKET, this.filed, DbConstant.RIGHT_BRACKET, DbConstant.VALUES, DbConstant.LEFT_BRACKET, CollUtil.listToStr(questionMarkList), DbConstant.RIGHT_BRACKET);
    }

    protected String updateDataSql() {
        return assemblySql(DbConstant.UPDATE, this.tableName, DbConstant.SET, CollUtil.listToStr(this.updateFiledList), this.where);
    }

    protected String deleteDataSql() {
        return assemblySql(DbConstant.DELETE, this.tableName, this.where);
    }

    /**
     * 普通查询条件
     *
     * @param condition  是否执行
     * @param fieldName  属性
     * @param sqlKeyword SQL 关键词
     * @param val        条件值
     */
    protected void addCondition(boolean condition, String fieldName, SqlKeyword sqlKeyword, Object val) {
        if (!condition) {
            return;
        }
        this.where += SqlKeyword.AND.getKeyword() + DbConstant.POINT + fieldName + DbConstant.POINT + sqlKeyword.getKeyword() + DbConstant.TRANSFER + val + DbConstant.TRANSFER;
    }

    /**
     * order By 条件
     *
     * @param condition 是否执行
     * @param fieldName 属性
     * @param orderType 类型
     */
    protected void addOrderByCondition(boolean condition, String fieldName, SqlKeyword orderType) {
        if (!condition) {
            return;
        }
        this.order = SqlKeyword.ORDER_BY.getKeyword() + DbConstant.POINT + fieldName + DbConstant.POINT + orderType.getKeyword();
    }

    /**
     * group By 条件
     *
     * @param condition 是否执行
     * @param fieldName 属性
     */
    protected void addGroupByCondition(boolean condition, String fieldName) {
        if (!condition) {
            return;
        }
        this.group = SqlKeyword.GROUP_BY.getKeyword() + DbConstant.POINT + fieldName + DbConstant.POINT;
    }

    /**
     * limit 条件
     *
     * @param condition 是否执行
     * @param pageNo    当前页
     * @param pageSize  每页显示条数
     */
    protected void addLimitCondition(boolean condition, int pageNo, int pageSize) {
        if (!condition) {
            return;
        }
        int ret = (pageNo - 1) * pageSize;
        int offset = Math.max(ret, 0);
        this.limit = SqlKeyword.LIMIT.getKeyword() + pageSize + SqlKeyword.OFFSET.getKeyword() + offset;
    }

    /**
     * in 条件
     *
     * @param condition  是否执行
     * @param fieldName  属性
     * @param sqlKeyword SQL 关键词
     * @param in         条件值
     */
    protected void addInCondition(boolean condition, String fieldName, SqlKeyword sqlKeyword, List<?> in) {
        if (!condition || CollUtil.isEmpty(in)) {
            return;
        }
        List<Object> ins = new ArrayList<>();
        for (Object obj : in) {
            ins.add(DbConstant.TRANSFER + obj + DbConstant.TRANSFER);
        }
        this.where += SqlKeyword.AND.getKeyword() + DbConstant.POINT + fieldName + DbConstant.POINT + sqlKeyword.getKeyword() + DbConstant.LEFT_BRACKET + CollUtil.listToStr(ins) + DbConstant.RIGHT_BRACKET;
    }

    /**
     * update构造
     *
     * @param condition 是否执行
     * @param fieldName 属性
     * @param val       条件值
     */
    protected void updateCondition(boolean condition, String fieldName, Object val) {
        if (!condition) {
            return;
        }
        this.updateFiledList.add(DbConstant.POINT + fieldName + DbConstant.POINT + DbConstant.EQUALS + DbConstant.QUESTION_MARK);
        this.updateFiledMap.put(this.updateFiledList.size(), val);
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