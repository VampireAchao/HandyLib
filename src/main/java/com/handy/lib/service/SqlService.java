package com.handy.lib.service;

import com.handy.lib.api.MessageApi;
import com.handy.lib.constants.BaseConstants;
import com.handy.lib.constants.SqlEnum;
import com.handy.lib.core.CollUtil;
import com.handy.lib.core.StrUtil;
import com.handy.lib.util.SqlManagerUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.*;

/**
 * sql基础方法
 *
 * @author handy
 * @since 1.0.4
 */
public class SqlService {

    /**
     * 特殊处理的字段
     *
     * @since 1.1.7
     */
    private static List<String> specialFields = new ArrayList<>();

    private SqlService() {
    }

    private static class SingletonHolder {
        private static final SqlService INSTANCE = new SqlService();
    }

    public static SqlService getInstance() {
        return SqlService.SingletonHolder.INSTANCE;
    }

    /**
     * 特殊处理的字段构造注入
     *
     * @param specialFields 特殊字段
     * @return SqlService 对象
     * @since 1.1.7
     */
    public static SqlService getInstance(List<String> specialFields) {
        SqlService.specialFields = specialFields;
        return SqlService.SingletonHolder.INSTANCE;
    }

    /**
     * 查询全部
     *
     * @param storageMethod 连接方式
     * @param tableName     表名
     * @return 全部
     */
    public List<Map<String, Object>> findAll(String storageMethod, String tableName) {
        // 重构连接
        this.refreshStorage(storageMethod);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rst = null;
        List<Map<String, Object>> allResult = new ArrayList<>();
        try {
            String selectStr = SqlEnum.SELECT_ALL.getCommand() + tableName;
            conn = SqlManagerUtil.getInstance().getConnection(storageMethod);
            ps = conn.prepareStatement(selectStr);
            rst = ps.executeQuery();
            // 获得列的结果
            ResultSetMetaData metaData = rst.getMetaData();
            // 获取总的列数
            int columnCount = metaData.getColumnCount();
            List<String> colNameList = new ArrayList<>();
            for (int i = 0; i < columnCount; i++) {
                // 获取第 i列的字段名称
                colNameList.add(metaData.getColumnName(i + 1));
            }
            while (rst.next()) {
                Map<String, Object> map = new HashMap<>();
                for (int i = 0; i < columnCount; i++) {
                    String key = colNameList.get(i);
                    Object value = rst.getObject(colNameList.get(i));
                    map.put(key, value);
                    // 时间字段特殊处理
                    if (value != null && CollUtil.isNotEmpty(SqlService.specialFields) && SqlService.specialFields.contains(key)) {
                        if (BaseConstants.SQLITE.equalsIgnoreCase(storageMethod)) {
                            String str = (String) value;
                            Date date = new Date(Long.parseLong(str));
                            map.put(key, date);
                        } else {
                            LocalDateTime date = (LocalDateTime) value;
                            map.put(key, date.toInstant(ZoneOffset.of("+8")).toEpochMilli());
                        }
                    }
                }
                allResult.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlManagerUtil.getInstance().closeSql(conn, ps, rst);
        }
        MessageApi.sendConsoleMessage("&a数据表 &e" + tableName + " &a需要转换的数据总条数: &e" + allResult.size());
        return allResult;
    }

    /**
     * 批量新增数据
     *
     * @param storageMethod 连接方式
     * @param tableName     表名
     * @param allResult     数据
     */
    public void addDate(String storageMethod, String tableName, List<Map<String, Object>> allResult) {
        int successNum = 0;
        int failNum = 0;
        if (CollUtil.isEmpty(allResult)) {
            return;
        }
        // 重构连接
        this.refreshStorage(storageMethod);
        // 获取新增sql
        String sql = this.getSql(allResult.get(0), tableName);
        for (Map<String, Object> stringObjectMap : allResult) {
            Connection conn = null;
            PreparedStatement ps = null;
            try {
                conn = SqlManagerUtil.getInstance().getConnection(storageMethod);
                ps = conn.prepareStatement(sql);
                // 赋值
                int i = 1;
                for (String key : stringObjectMap.keySet()) {
                    ps.setObject(i, stringObjectMap.get(key));
                    i++;
                }
                int rstCount = ps.executeUpdate();
                if (rstCount > 0) {
                    successNum++;
                } else {
                    failNum++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                SqlManagerUtil.getInstance().closeSql(conn, ps, null);
            }
        }
        MessageApi.sendConsoleMessage("&a 数据表 &e" + tableName + " &a转换结束，成功条数: &e" + successNum + " &a失败条数: &e" + failNum);
    }

    /**
     * 重构连接
     *
     * @param storageMethod 存储方法
     */
    public void refreshStorage(String storageMethod) {
        // 关闭现有连接
        SqlManagerUtil.getInstance().close();
        // 创建新连接
        SqlManagerUtil.getInstance().enableTable(storageMethod);
    }

    /**
     * 创建表
     *
     * @param storageMethod 存储方法
     * @param mysqlSql      mysqlSql
     * @param sqliteSql     sqliteSql
     * @return true/成功
     * @since 1.2.3
     */
    public boolean createTable(String storageMethod, String mysqlSql, String sqliteSql) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            String sql = sqliteSql;
            conn = SqlManagerUtil.getInstance().getConnection(storageMethod);
            if (BaseConstants.MYSQL.equalsIgnoreCase(storageMethod)) {
                sql = mysqlSql;
            }
            ps = conn.prepareStatement(sql);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlManagerUtil.getInstance().closeSql(conn, ps, null);
        }
        return false;
    }

    /**
     * 根据id删除
     *
     * @param sql sql
     * @param id  id
     * @return true/成功
     * @since 1.3.0
     */
    public boolean removeById(String sql, Long id) {
        return this.removeById(null, sql, id);
    }

    /**
     * 根据id删除
     *
     * @param storageMethod 存储方法
     * @param sql           sql
     * @param id            id
     * @return true/成功
     * @since 1.2.3
     */
    public boolean removeById(String storageMethod, String sql, Long id) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            if (StrUtil.isNotEmpty(storageMethod)) {
                conn = SqlManagerUtil.getInstance().getConnection(storageMethod);
            } else {
                conn = SqlManagerUtil.getInstance().getConnection();
            }
            ps = conn.prepareStatement(sql);
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlManagerUtil.getInstance().closeSql(conn, ps, null);
        }
        return false;
    }

    /**
     * 删除全部
     *
     * @param tableName 表名
     * @return true/成功
     * @since 1.3.0
     */
    public boolean removeAll(String tableName) {
        String sql = SqlEnum.DELETE_ALL + "`" + tableName + "`";
        return this.executionSql(sql);
    }

    /**
     * 执行普通sql
     *
     * @param sql sql
     * @return true/成功
     * @since 1.3.0
     */
    public boolean executionSql(String sql) {
        return this.executionSql(null, sql);
    }

    /**
     * 执行普通sql
     *
     * @param storageMethod 存储方法
     * @param sql           sql
     * @return true/成功
     * @since 1.2.3
     */
    public boolean executionSql(String storageMethod, String sql) {
        return this.executionSql(storageMethod, sql, false);
    }

    /**
     * 查询表字段新
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

    /**
     * 执行普通sql
     *
     * @param storageMethod 存储方法
     * @param sql           sql
     * @param ignored       是否忽视异常
     * @return true/成功
     * @since 1.2.3
     */
    public boolean executionSql(String storageMethod, String sql, boolean ignored) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            if (StrUtil.isNotEmpty(storageMethod)) {
                conn = SqlManagerUtil.getInstance().getConnection(storageMethod);
            } else {
                conn = SqlManagerUtil.getInstance().getConnection();
            }
            ps = conn.prepareStatement(sql);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            if (ignored) {
                e.printStackTrace();
            }
        } finally {
            SqlManagerUtil.getInstance().closeSql(conn, ps, null);
        }
        return false;
    }

    /**
     * 查询总数
     *
     * @param sql sql
     * @return 总数
     */
    public Integer count(String sql) {
        return this.count(null, sql);
    }

    /**
     * 查询总数
     *
     * @param storageMethod 存储方法
     * @param sql           sql
     * @return 总数
     */
    public int count(String storageMethod, String sql) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rst = null;
        int count = 0;
        try {
            if (StrUtil.isNotEmpty(storageMethod)) {
                conn = SqlManagerUtil.getInstance().getConnection(storageMethod);
            } else {
                conn = SqlManagerUtil.getInstance().getConnection();
            }
            ps = conn.prepareStatement(sql);
            rst = ps.executeQuery();
            while (rst.next()) {
                count = rst.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlManagerUtil.getInstance().closeSql(conn, ps, rst);
        }
        return count;
    }

    /**
     * 获取新增sql
     *
     * @param stringObjectMap 对象
     * @param tableName       表名
     * @return sql
     */
    private String getSql(Map<String, Object> stringObjectMap, String tableName) {
        List<String> keys = new ArrayList<>(stringObjectMap.keySet());
        StringBuilder addStr = new StringBuilder();
        addStr.append(SqlEnum.ADD_DATA.getCommand()).append("`").append(tableName).append("`");
        addStr.append(" (");
        for (int i = 0; i < keys.size(); i++) {
            addStr.append("`").append(keys.get(i)).append("`");
            if (i < keys.size() - 1) {
                addStr.append(",");
            }
        }
        addStr.append(") ");
        addStr.append("VALUES");
        addStr.append(" (");
        for (int i = 1; i <= stringObjectMap.size(); i++) {
            addStr.append("?");
            if (i < stringObjectMap.size()) {
                addStr.append(",");
            }
        }
        addStr.append(")");
        return addStr.toString();
    }

}
