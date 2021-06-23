package com.handy.lib.util;

import com.handy.lib.api.StorageApi;
import com.handy.lib.constants.BaseConstants;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.plugin.Plugin;

import java.sql.*;

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
        this.enableTable(plugin, StorageApi.storageConfig.getString(BaseConstants.STORAGE_METHOD));
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
            String host = StorageApi.storageConfig.getString("MySQL.Host");
            String database = StorageApi.storageConfig.getString("MySQL.Database");
            int port = StorageApi.storageConfig.getInt("MySQL.Port");
            String useSsl = StorageApi.storageConfig.getString("MySQL.UseSSL");
            String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=" + useSsl + "&useUnicode=true&characterEncoding=UTF-8";
            hikariConfig.setJdbcUrl(jdbcUrl);
            hikariConfig.setPoolName(plugin.getName() + "HikariPool");
            hikariConfig.setUsername(StorageApi.storageConfig.getString("MySQL.User"));
            hikariConfig.setPassword(StorageApi.storageConfig.getString("MySQL.Password"));
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
        return this.getConnection(plugin, StorageApi.storageConfig.getString(BaseConstants.STORAGE_METHOD));
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

}
