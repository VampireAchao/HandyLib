package com.handy.lib.db;

import java.io.Serializable;

/**
 * db 常量
 *
 * @author handy
 */
public class DbConstant implements Serializable {

    public static final String SPACE = " ";

    public static final String SELECT = "SELECT ";
    public static final String SELECT_DISTINCT = "SELECT DISTINCT ";
    public static final String COUNT = " COUNT(1) ";
    public static final String COUNT_DISTINCT = " COUNT ( DISTINCT ";
    public static final String FORM = "  FROM %s %s ";
    public static final String JOIN = "%s %s %s ON %s.%s = %s.%s ";

    public static final String DEFAULT_WHERE = " where 1=1";

    public static final String AND = " and ";
}
