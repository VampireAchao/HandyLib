package com.handy.lib.util;

import com.handy.lib.constants.BaseConstants;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.plugin.Plugin;

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
     *
     * @param plugin 插件
     */
    public void enableTable(Plugin plugin) {
        this.enableTable(plugin, this.getStorageMethod());
    }

    /**
     * 初始化连接
     *
     * @param plugin        插件
     * @param storageMethod 连接方式
     */
    public void enableTable(Plugin plugin, String storageMethod) {
        if (BaseConstants.MYSQL.equalsIgnoreCase(storageMethod)) {
            HikariConfig hikariConfig = new HikariConfig();
            String host = BaseConstants.STORAGE_CONFIG.getString("MySQL.Host");
            String database = BaseConstants.STORAGE_CONFIG.getString("MySQL.Database");
            int port = BaseConstants.STORAGE_CONFIG.getInt("MySQL.Port");
            String useSsl = BaseConstants.STORAGE_CONFIG.getString("MySQL.UseSSL");
            String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=" + useSsl + "&useUnicode=true&characterEncoding=UTF-8";
            hikariConfig.setJdbcUrl(jdbcUrl);
            hikariConfig.setPoolName(plugin.getName() + "HikariPool");
            hikariConfig.setUsername(BaseConstants.STORAGE_CONFIG.getString("MySQL.User"));
            hikariConfig.setPassword(BaseConstants.STORAGE_CONFIG.getString("MySQL.Password"));
            // 是否自定义配置，为true时下面两个参数才生效
            hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
            // 连接池大小默认25，官方推荐250-500
            hikariConfig.addDataSourceProperty("prepStmtCacheSize", "10");
            // 单条语句最大长度默认256，官方推荐2048
            hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            ds = new HikariDataSource(hikariConfig);
        } else {
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
     * @param plugin        插件
     * @param storageMethod 连接方式
     * @return conn
     * @throws SQLException 异常
     */
    public Connection getConnection(Plugin plugin, String storageMethod) throws SQLException {
        if (BaseConstants.MYSQL.equalsIgnoreCase(storageMethod)) {
            return ds.getConnection();
        }
        return DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder().getAbsolutePath() + "/" + plugin.getName() + ".db");
    }

    /**
     * 获取连接
     *
     * @param plugin 插件
     * @return conn
     * @throws SQLException 异常
     */
    public Connection getConnection(Plugin plugin) throws SQLException {
        return this.getConnection(plugin, BaseConstants.STORAGE_CONFIG.getString(BaseConstants.STORAGE_METHOD));
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
