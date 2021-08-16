package com.handy.lib.db;

import com.handy.lib.InitApi;
import com.handy.lib.util.SqlManagerUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

/**
 * db执行sql
 *
 * @author handy
 * @since 1.4.8
 */
public class DbExecution<T> implements BaseMapper<T> {

    /**
     * 类
     */
    protected Class<?> clazz;
    /**
     * DbSql
     */
    protected DbSql dbSql;

    @Override
    public int insert() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rst = null;
        try {
            conn = SqlManagerUtil.getInstance().getConnection(InitApi.PLUGIN);
            ps = conn.prepareStatement(dbSql.insertDataSql());
            // 入参
            this.setPs(ps);
            ps.executeUpdate();
            //获取自增id
            int id = 0;
            rst = ps.getGeneratedKeys();
            if (rst.next()) {
                id = rst.getInt(1);
            }
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlManagerUtil.getInstance().closeSql(conn, ps, rst);
        }
        return 0;
    }

    @Override
    public T selectOne() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rst = null;
        try {
            conn = SqlManagerUtil.getInstance().getConnection(InitApi.PLUGIN);
            ps = conn.prepareStatement(dbSql.selectDataSql());
            // 入参
            this.setPs(ps);
            rst = ps.executeQuery();
            LinkedHashMap<Integer, Object> indexObjMap = new LinkedHashMap<>();
            LinkedHashMap<String, Integer> filedIndexMap = dbSql.getFiledIndexMap();
            while (rst.next()) {
                for (int i = 1; i <= filedIndexMap.size(); i++) {
                    indexObjMap.put(i, rst.getObject(i));
                }
            }
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            T newInstance = (T) constructor.newInstance();
            for (String key : filedIndexMap.keySet()) {
                Field field = clazz.getDeclaredField(key);
                field.setAccessible(true);
                field.set(newInstance, indexObjMap.get(filedIndexMap.get(key)));
            }
            return newInstance;
        } catch (SQLException | NoSuchFieldException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            SqlManagerUtil.getInstance().closeSql(conn, ps, rst);
        }
        return null;
    }

    /**
     * 构建条件
     *
     * @param ps ps
     * @throws SQLException 异常
     */
    private void setPs(PreparedStatement ps) throws SQLException {
        LinkedHashMap<String, Object> whereData = dbSql.getWhereData();
        if (!whereData.isEmpty()) {
            int i = 1;
            for (String key : whereData.keySet()) {
                ps.setObject(i, whereData.get(key));
                i++;
            }
        }
    }


}