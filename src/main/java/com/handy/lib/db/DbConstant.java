package com.handy.lib.db;

import java.io.Serializable;

/**
 * db 常量
 *
 * @author handy
 * @since 1.4.8
 */
public class DbConstant implements Serializable {
    public static final String SELECT = "SELECT ";
    public static final String INSERT = "INSERT INTO ";
    public static final String UPDATE = "UPDATE ";
    public static final String DELETE = "DELETE FROM ";

    public static final String SET = " SET ";
    public static final String FORM = " FROM ";
    public static final String VALUES = " VALUES ";

    public static final String QUESTION_MARK = "?";
    public static final String EQUALS = " = ";
    public static final String POINT = "`";
    public static final String TRANSFER = "'";
    public static final String LEFT_BRACKET = "(";
    public static final String RIGHT_BRACKET = ")";
    public static final String PERCENT_SIGN = "%";

    public static final String COUNT = "COUNT(*)";
    public static final String DEFAULT_WHERE = " where 1 = 1";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `%s` (`id` INTEGER (11) AUTO_INCREMENT,PRIMARY KEY (`id`)) ENGINE=INNODB;";
    public static final String TABLE_COMMENT = "ALTER TABLE `%s` COMMENT '%s';";
    public static final String COLUMNS_IF_NOT_EXISTS = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name='%s' AND COLUMN_NAME='%s';";

}