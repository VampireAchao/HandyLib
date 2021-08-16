package com.handy.lib.db;

import java.io.Serializable;

/**
 * db 常量
 *
 * @author handy
 * @since 1.4.8
 */
public class DbConstant implements Serializable {

    public static final String QUESTION_MARK = "?";
    public static final String POINT = "`";
    public static final String AND = " and ";
    public static final String SELECT = "SELECT ";
    public static final String INSERT = "INSERT ";
    public static final String COUNT = "COUNT(1)";
    public static final String FORM = " FROM ";
    public static final String DEFAULT_WHERE = " where 1 = 1";

    public static final String EQ = " = ";
}
