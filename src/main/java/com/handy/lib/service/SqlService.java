package com.handy.lib.service;

import com.handy.lib.constants.BaseConstants;
import com.handy.lib.core.StrUtil;
import com.handy.lib.util.SqlManagerUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
     * @param sql sql
     * @since 1.2.3
     */
    public void executionSql(String sql) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = SqlManagerUtil.getInstance().getConnection();
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
     * @param storageMethod 存储方法
     * @param sql           sql
     * @return true/成功
     * @since 1.2.3
     */
    public List<String> getTableInfo(String storageMethod, String sql) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rst = null;
        List<String> filedNameList = new ArrayList<>();
        try {
            if (StrUtil.isNotEmpty(storageMethod)) {
                conn = SqlManagerUtil.getInstance().getConnection(storageMethod);
            } else {
                conn = SqlManagerUtil.getInstance().getConnection();
            }
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
     * @param sql sql
     * @return true/成功
     * @since 1.9.6
     */
    public List<String> getMysqlTableIndex(String sql) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rst = null;
        List<String> columnNameList = new ArrayList<>();
        try {
            conn = SqlManagerUtil.getInstance().getConnection();
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

}