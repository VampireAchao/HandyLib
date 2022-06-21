package cn.handyplus.lib.db;

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
    public static final String NOT_NULL = "NOT NULL";
    public static final String AUTO_INCREMENT = " AUTO_INCREMENT";
    public static final String DEFAULT = " DEFAULT '%s'";
    public static final String COMMENT = " COMMENT '%s'";

    public static final String SET = " SET ";
    public static final String FORM = " FROM ";
    public static final String VALUES = " VALUES ";

    public static final String QUESTION_MARK = "?";
    public static final String EQUALS = " = ";
    public static final String SUBTRACT = " - ";
    public static final String ADD = " + ";
    public static final String POINT = "`";
    public static final String TRANSFER = "'";
    public static final String LEFT_BRACKET = "(";
    public static final String RIGHT_BRACKET = ")";
    public static final String PERCENT_SIGN = "%";

    public static final String COUNT = "COUNT(*)";
    public static final String DEFAULT_WHERE = " where 1 = 1";

    /**
     * 新增表
     */
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `%s` (`id` INTEGER (11) AUTO_INCREMENT,PRIMARY KEY (`id`)) CHARACTER SET = utf8mb4 ENGINE=INNODB;";

    /**
     * 新增表注释
     */
    public static final String TABLE_COMMENT = "ALTER TABLE `%s` COMMENT '%s';";

    /**
     * 查询表字段信息
     */
    public static final String TABLE_INFO = "SELECT column_name FROM information_schema.COLUMNS WHERE table_name = '%s' AND table_schema = '%s';";

    /**
     * 新增字段
     */
    public static final String ADD_COLUMN = "ALTER TABLE `%s` ADD `%s` %s(%s) %s;";

    /**
     * 修改字段
     */
    public static final String ADD_COLUMN_COMMENT = "ALTER TABLE `%s` MODIFY `%s` %s(%s) %s;";

    /**
     * SQLITE 新增表
     */
    public static final String SQLITE_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `%s` ( `id` INTEGER PRIMARY KEY AUTOINCREMENT);";

    /**
     * SQLITE 表信息
     */
    public static final String SQLITE_TABLE_INFO = "PRAGMA table_info ( %s )";

    /**
     * SQLITE 新增字段
     */
    public static final String SQLITE_ADD_COLUMN = "ALTER TABLE '%s' ADD '%s' %s(%s) %s;";

    /**
     * 查询索引
     *
     * @since 1.9.6
     */
    public static final String SHOW_INDEX = "SHOW INDEX FROM %s;";

    /**
     * 添加索引
     *
     * @since 1.9.6
     */
    public static final String ADD_INDEX = "ALTER TABLE %s ADD INDEX %s (%s);";

    private static final long serialVersionUID = -5121546824758656412L;
}