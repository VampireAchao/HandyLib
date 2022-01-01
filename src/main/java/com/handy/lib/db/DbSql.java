package com.handy.lib.db;

import com.handy.lib.constants.BaseConstants;
import com.handy.lib.core.CollUtil;
import com.handy.lib.core.StrUtil;
import com.handy.lib.db.enums.SqlKeyword;
import com.handy.lib.db.param.FiledInfoParam;
import com.handy.lib.db.param.TableInfoParam;
import com.handy.lib.util.SqlManagerUtil;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
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
        // 特殊值处理
        val = specialHandling(val);
        this.where += SqlKeyword.AND.getKeyword() + DbConstant.POINT + fieldName + DbConstant.POINT + sqlKeyword.getKeyword() + DbConstant.TRANSFER + val + DbConstant.TRANSFER;
    }

    /**
     * 比较查询条件
     *
     * @param condition        是否执行
     * @param fieldName        属性
     * @param sqlKeyword       SQL 关键词
     * @param compareFieldName 比较字段
     */
    protected void addCondition(boolean condition, String fieldName, SqlKeyword sqlKeyword, String compareFieldName) {
        if (!condition) {
            return;
        }
        this.where += SqlKeyword.AND.getKeyword() + DbConstant.POINT + fieldName + DbConstant.POINT + sqlKeyword.getKeyword() + DbConstant.POINT + compareFieldName + DbConstant.POINT;
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
     * @param fieldName  赋值字段
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
     * @param fieldName 赋值字段
     * @param val       条件值
     */
    protected void updateCondition(boolean condition, String fieldName, Object val) {
        if (!condition) {
            return;
        }
        // 特殊值处理
        val = specialHandling(val);
        this.updateFiledList.add(DbConstant.POINT + fieldName + DbConstant.POINT + DbConstant.EQUALS + DbConstant.QUESTION_MARK);
        this.updateFiledMap.put(this.updateFiledList.size(), val);
    }

    /**
     * 计算类update构造
     *
     * @param condition          是否执行
     * @param fieldName          赋值字段
     * @param calculateFieldName 参与计算字段
     * @param sqlKeyword         计算符
     * @param val                条件值
     * @since 2.1.0
     */
    protected void updateCondition(boolean condition, String fieldName, String calculateFieldName, String sqlKeyword, Object val) {
        if (!condition) {
            return;
        }
        this.updateFiledList.add(DbConstant.POINT + fieldName + DbConstant.POINT + DbConstant.EQUALS + DbConstant.POINT + calculateFieldName + DbConstant.POINT + sqlKeyword + DbConstant.QUESTION_MARK);
        this.updateFiledMap.put(this.updateFiledList.size(), val);
    }

    /**
     * 特殊字段类型处理
     *
     * @param val 值
     * @return 新值
     */
    private Object specialHandling(Object val) {
        if (val == null) {
            return null;
        }
        //布尔处理
        if (val instanceof Boolean) {
            Boolean bool = (Boolean) val;
            val = bool ? 1 : 0;
        }
        // sqlite
        if (BaseConstants.SQLITE.equalsIgnoreCase(SqlManagerUtil.getInstance().getStorageMethod())) {
            // LocalDateTime处理
            if (val instanceof LocalDateTime) {
                LocalDateTime localDateTime = (LocalDateTime) val;
                val = localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
            }
            // LocalDateTime处理
            if (val instanceof Date) {
                Date date = (Date) val;
                val = date.getTime();
            }
        } else {
            // LocalDateTime处理
            if (val instanceof LocalDateTime) {
                LocalDateTime localDateTime = (LocalDateTime) val;
                val = new java.sql.Date(localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
            }
            // LocalDateTime处理
            if (val instanceof Date) {
                Date date = (Date) val;
                val = new java.sql.Date(date.getTime());
            }
        }
        return val;
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