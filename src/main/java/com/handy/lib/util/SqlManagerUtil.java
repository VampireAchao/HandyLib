package com.handy.lib.util;

import com.handy.lib.InitApi;
import com.handy.lib.api.MessageApi;
import com.handy.lib.constants.BaseConstants;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.Date;

/**
 * 连接池管理
 *
 * @author handy
 */
public class SqlManagerUtil {
    private SqlManagerUtil() {
    }

    private static final SqlManagerUtil INSTANCE = new SqlManagerUtil();

    public static SqlManagerUtil getInstance() {
        return INSTANCE;
    }

    private HikariDataSource ds;

    /**
     * 初始化连接
     */
    public void enableTable() {
        this.enableTable(this.getStorageMethod());
    }

    /**
     * 初始化连接
     *
     * @param storageMethod 连接方式
     */
    public void enableTable(String storageMethod) {
        try {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setPoolName(InitApi.PLUGIN.getName() + "HikariPool");
            if (BaseConstants.MYSQL.equalsIgnoreCase(storageMethod)) {
                String host = BaseConstants.STORAGE_CONFIG.getString("MySQL.Host");
                String database = BaseConstants.STORAGE_CONFIG.getString("MySQL.Database");
                int port = BaseConstants.STORAGE_CONFIG.getInt("MySQL.Port");
                String useSsl = BaseConstants.STORAGE_CONFIG.getString("MySQL.UseSSL");
                String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=" + useSsl + "&useUnicode=true&characterEncoding=UTF-8";
                hikariConfig.setJdbcUrl(jdbcUrl);
                hikariConfig.setUsername(BaseConstants.STORAGE_CONFIG.getString("MySQL.User"));
                hikariConfig.setPassword(BaseConstants.STORAGE_CONFIG.getString("MySQL.Password"));
            } else {
                String jdbcUrl = "jdbc:sqlite:" + InitApi.PLUGIN.getDataFolder().getAbsolutePath() + "/" + InitApi.PLUGIN.getName() + ".db";
                hikariConfig.setDriverClassName("org.sqlite.JDBC");
                hikariConfig.setJdbcUrl(jdbcUrl);
                hikariConfig.setMinimumIdle(1);
                hikariConfig.setMaximumPoolSize(1);
            }
            ds = new HikariDataSource(hikariConfig);
        } catch (Throwable throwable) {
            if (BaseConstants.DEBUG) {
                throwable.printStackTrace();
            }
            MessageApi.sendConsoleDebugMessage("sqlite 初始化连接异常，使用jdbc方法获取链接");
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取连接
     *
     * @param storageMethod 连接方式
     * @return conn
     * @throws SQLException 异常
     */
    public Connection getConnection(String storageMethod) throws SQLException {
        if (ds == null && BaseConstants.SQLITE.equalsIgnoreCase(storageMethod)) {
            MessageApi.sendConsoleDebugMessage("sqlite HikariDataSource 获取链接异常，使用jdbc方法获取链接");
            return DriverManager.getConnection("jdbc:sqlite:" + InitApi.PLUGIN.getDataFolder().getAbsolutePath() + "/" + InitApi.PLUGIN.getName() + ".db");
        }
        if (this.getStorageMethod().equalsIgnoreCase(storageMethod)) {
            // 如果链接被关闭了，就重新打开
            if (ds.isClosed()) {
                MessageApi.sendConsoleDebugMessage(" HikariDataSource 链接异常关闭，重新打开");
                enableTable(storageMethod);
            }
            return ds.getConnection();
        }
        ds.close();
        enableTable(storageMethod);
        return ds.getConnection();
    }

    /**
     * 获取连接
     *
     * @return conn
     * @throws SQLException 异常
     */
    public Connection getConnection() throws SQLException {
        return this.getConnection(this.getStorageMethod());
    }

    /**
     * 根据当前存储类型获取date
     *
     * @param rst   ResultSet
     * @param index 下标
     * @return Date
     * @throws SQLException SQLException异常
     * @since 1.1.9
     */
    public Date getDate(ResultSet rst, Integer index) throws SQLException {
        if (BaseConstants.SQLITE.equalsIgnoreCase(this.getStorageMethod())) {
            return new Date(Long.parseLong(rst.getString(index)));
        }
        return rst.getDate(index);
    }

    /**
     * 归还数据连接
     *
     * @param ps   PreparedStatement
     * @param conn Connection
     * @param rst  ResultSet
     */
    public void closeSql(Connection conn, PreparedStatement ps, ResultSet rst) {
        try {
            if (rst != null) {
                rst.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭数据源
     */
    public void close() {
        if (ds != null) {
            ds.close();
        }
    }

    /**
     * 判断当前是什么存储类型
     *
     * @return 存储类型
     * @since 1.1.9
     */
    public String getStorageMethod() {
        String storageMethod = BaseConstants.STORAGE_CONFIG.getString(BaseConstants.STORAGE_METHOD);
        if (BaseConstants.MYSQL.equalsIgnoreCase(storageMethod)) {
            return BaseConstants.MYSQL;
        }
        return BaseConstants.SQLITE;
    }

}