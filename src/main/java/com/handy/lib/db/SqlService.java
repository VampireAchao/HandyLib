package com.handy.lib.db;

import com.handy.lib.constants.BaseConstants;
import com.handy.lib.util.SqlManagerUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * sql基础方法
 *
 * @author handy
 * @since 1.0.4
 */
public class SqlService {

    private SqlService() {
    }

    private static class SingletonHolder {
        private static final SqlService INSTANCE = new SqlService();
    }

    public static SqlService getInstance() {
        return SqlService.SingletonHolder.INSTANCE;
    }

    /**
     * 执行普通sql
     *
     * @param sql           sql
     * @param storageMethod 储存方式
     * @since 2.9.3
     */
    public void executionSql(String sql, String storageMethod) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = SqlManagerUtil.getInstance().getConnection(storageMethod);
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlManagerUtil.getInstance().closeSql(conn, ps, null);
        }
    }

    /**
     * 查询表字段
     *
     * @param sql           sql
     * @param storageMethod 存储方法
     * @return true/成功
     * @since 1.2.3
     */
    public List<String> getTableInfo(String sql, String storageMethod) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rst = null;
        List<String> filedNameList = new ArrayList<>();
        try {
            conn = SqlManagerUtil.getInstance().getConnection(storageMethod);
            ps = conn.prepareStatement(sql);
            rst = ps.executeQuery();
            while (rst.next()) {
                String fieId;
                if (BaseConstants.MYSQL.equals(storageMethod)) {
                    fieId = rst.getString("column_name");
                } else {
                    fieId = rst.getString("name");
                }
                filedNameList.add(fieId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlManagerUtil.getInstance().closeSql(conn, ps, rst);
        }
        return filedNameList;
    }

    /**
     * 查询表索引字段
     *
     * @param sql           sql
     * @param storageMethod 储存方式
     * @return true/成功
     * @since 2.9.3
     */
    public List<String> getMysqlTableIndex(String sql, String storageMethod) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rst = null;
        List<String> columnNameList = new ArrayList<>();
        try {
            conn = SqlManagerUtil.getInstance().getConnection(storageMethod);
            ps = conn.prepareStatement(sql);
            rst = ps.executeQuery();
            while (rst.next()) {
                String columnName = rst.getString("Column_name");
                columnNameList.add(columnName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlManagerUtil.getInstance().closeSql(conn, ps, rst);
        }
        return columnNameList;
    }

    /**
     * 自定义sql查询并返回map
     *
     * @param sql sql
     * @return Map
     * @since 3.0.4
     */
    public Map<String, Object> selectMap(String sql) {
        return this.selectMap(sql, BaseConstants.STORAGE_CONFIG.getString(BaseConstants.STORAGE_METHOD));
    }

    /**
     * 自定义sql查询并返回map
     *
     * @param sql           sql
     * @param storageMethod 存储方法
     * @return Map
     * @since 3.0.3
     */
    public Map<String, Object> selectMap(String sql, String storageMethod) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rst = null;
        Map<String, Object> map = new HashMap<>();
        try {
            conn = SqlManagerUtil.getInstance().getConnection(storageMethod);
            ps = conn.prepareStatement(sql);
            rst = ps.executeQuery();
            ResultSetMetaData rstMetaData = rst.getMetaData();
            for (int i = 1; i <= rstMetaData.getColumnCount(); i++) {
                map.put(rstMetaData.getColumnLabel(i), rst.getObject(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlManagerUtil.getInstance().closeSql(conn, ps, rst);
        }
        return map;
    }

    /**
     * 自定义sql查询并返回List
     *
     * @param sql sql
     * @return List
     * @since 3.0.4
     */
    public List<Map<String, Object>> selectListMap(String sql) {
        return this.selectListMap(sql, BaseConstants.STORAGE_CONFIG.getString(BaseConstants.STORAGE_METHOD));
    }

    /**
     * 自定义sql查询并返回List
     *
     * @param sql           sql
     * @param storageMethod 存储方法
     * @return List
     * @since 3.0.3
     */
    public List<Map<String, Object>> selectListMap(String sql, String storageMethod) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rst = null;
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            conn = SqlManagerUtil.getInstance().getConnection(storageMethod);
            ps = conn.prepareStatement(sql);
            rst = ps.executeQuery();
            ResultSetMetaData rstMetaData = rst.getMetaData();
            while (rst.next()) {
                Map<String, Object> map = new HashMap<>();
                for (int i = 1; i <= rstMetaData.getColumnCount(); i++) {
                    map.put(rstMetaData.getColumnName(i), rst.getObject(i));
                }
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlManagerUtil.getInstance().closeSql(conn, ps, rst);
        }
        return list;
    }

}