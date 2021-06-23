package com.handy.lib.service;

import com.handy.lib.api.MessageApi;
import com.handy.lib.constants.SqlEnum;
import com.handy.lib.util.BaseUtil;
import com.handy.lib.util.SqlManagerUtil;
import org.bukkit.plugin.Plugin;

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
     * 查询全部
     *
     * @param plugin        插件
     * @param storageMethod 连接方式
     * @param tableName     表名
     * @return 全部
     */
    public List<Map<String, Object>> findAll(Plugin plugin, String storageMethod, String tableName) {
        // 关闭现有连接
        SqlManagerUtil.getInstance().close();
        // 创建新连接
        SqlManagerUtil.getInstance().enableTable(plugin, storageMethod);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rst = null;
        List<Map<String, Object>> allResult = new ArrayList<>();
        try {
            String selectStr = SqlEnum.SELECT_ALL.getCommand() + tableName;
            conn = SqlManagerUtil.getInstance().getConnection(plugin, storageMethod);
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
                    Object value = rst.getString(colNameList.get(i));
                    map.put(key, value);
                }
                allResult.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlManagerUtil.getInstance().closeSql(conn, ps, rst);
        }
        MessageApi.sendConsoleMessage(plugin, "&a数据表 &e" + tableName + " &a需要转换的数据总条数: &e" + allResult.size());
        return allResult;
    }

    /**
     * 批量新增数据
     *
     * @param plugin        插件
     * @param storageMethod 连接方式
     * @param tableName     表名
     * @param allResult     数据
     */
    public void addDate(Plugin plugin, String storageMethod, String tableName, List<Map<String, Object>> allResult) {
        int successNum = 0;
        int failNum = 0;
        if (BaseUtil.collIsEmpty(allResult)) {
            MessageApi.sendConsoleMessage(plugin, "&a数据表 &e" + tableName + " &a没有需要转换的数据");
            return;
        }
        // 关闭现有连接
        SqlManagerUtil.getInstance().close();
        // 创建新连接
        SqlManagerUtil.getInstance().enableTable(plugin, storageMethod);
        // 获取新增sql
        String sql = this.getSql(allResult.get(0), tableName);
        MessageApi.sendConsoleMessage(plugin, "&a 数据表 &e" + tableName + " &a正在转换，生成转换sql: " + sql);
        for (Map<String, Object> stringObjectMap : allResult) {
            Connection conn = null;
            PreparedStatement ps = null;
            try {
                conn = SqlManagerUtil.getInstance().getConnection(plugin, storageMethod);
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
        MessageApi.sendConsoleMessage(plugin, "&a 数据表 &e" + tableName + " &a转换结束，成功条数: &e" + successNum + " &a失败条数: &e" + failNum);
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
