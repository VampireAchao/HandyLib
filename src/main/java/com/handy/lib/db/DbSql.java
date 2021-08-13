package com.handy.lib.db;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * dbSql处理器
 *
 * @author handy
 * @since 1.4.8
 */
@Data
public class DbSql implements Serializable {

    /**
     * select sql
     */
    private String select;

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


    public String pageCountSql() {
        return assemblySql(this.select, this.count, this.from, this.join, this.where);
    }

    public String pageDataSql() {
        return assemblySql(this.select, this.filed, this.from, this.join, this.where, this.order, this.limit);
    }

    /**
     * 组合sql
     *
     * @param sqlColl sql内容
     * @return sql
     */
    private static String assemblySql(String... sqlColl) {
        StringBuilder sb = new StringBuilder();
        for (String s : sqlColl) {
            if (StringUtils.isBlank(s)) {
                continue;
            }
            sb.append(s);
        }
        return sb.toString();
    }

}