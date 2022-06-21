package cn.handyplus.lib.db;

import cn.handyplus.lib.api.MessageApi;
import cn.handyplus.lib.constants.BaseConstants;
import cn.handyplus.lib.core.BeanUtil;
import cn.handyplus.lib.core.DateUtil;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.db.enter.Page;
import cn.handyplus.lib.db.enums.FieldTypeEnum;
import cn.handyplus.lib.db.enums.IndexEnum;
import cn.handyplus.lib.db.enums.SqlKeyword;
import cn.handyplus.lib.db.param.FiledInfoParam;
import cn.handyplus.lib.db.param.TableInfoParam;
import cn.handyplus.lib.util.SqlManagerUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.*;
import java.util.stream.Collectors;

/**
 * db执行器
 *
 * @author handy
 * @since 1.4.8
 */
public class DbExecution<T> implements BaseMapper<T> {

    private final Class<T> clazz;
    private final DbSql dbSql;
    private final String storageMethod;
    private final boolean isMysql;

    protected DbExecution(DbSql dbSql, Class<T> clazz) {
        this.dbSql = dbSql;
        this.clazz = clazz;
        this.storageMethod = BaseConstants.STORAGE_CONFIG.getString(BaseConstants.STORAGE_METHOD);
        isMysql = BaseConstants.MYSQL.equalsIgnoreCase(storageMethod);
    }

    protected DbExecution(DbSql dbSql, Class<T> clazz, String storageMethod) {
        this.dbSql = dbSql;
        this.clazz = clazz;
        this.storageMethod = storageMethod;
        isMysql = BaseConstants.MYSQL.equalsIgnoreCase(storageMethod);
    }

    @Override
    public void create() {
        // 新增表
        TableInfoParam tableInfoParam = dbSql.getTableInfoParam();
        String createTable = isMysql ? DbConstant.CREATE_TABLE : DbConstant.SQLITE_CREATE_TABLE;
        String createTableSql = String.format(createTable, tableInfoParam.getTableName());
        MessageApi.sendConsoleDebugMessage("新增表sql: " + createTableSql);
        SqlService.getInstance().executionSql(createTableSql, storageMethod);
        // 新增表注释
        if (isMysql) {
            String tableCommentSql = String.format(DbConstant.TABLE_COMMENT, tableInfoParam.getTableName(), tableInfoParam.getTableComment());
            MessageApi.sendConsoleDebugMessage("新增表注释: " + tableCommentSql);
            SqlService.getInstance().executionSql(tableCommentSql, storageMethod);
        }
        // 现有字段
        LinkedHashMap<String, FiledInfoParam> filedInfoMap = dbSql.getFiledInfoMap();
        String sql;
        if (isMysql) {
            String database = BaseConstants.STORAGE_CONFIG.getString("MySQL.Database");
            sql = String.format(DbConstant.TABLE_INFO, tableInfoParam.getTableName(), database);
        } else {
            sql = String.format(DbConstant.SQLITE_TABLE_INFO, tableInfoParam.getTableName());
        }
        MessageApi.sendConsoleDebugMessage("查询字段sql: " + sql);
        List<String> filedNameList = SqlService.getInstance().getTableInfo(sql, storageMethod);
        // 新增字段
        for (String filedName : filedInfoMap.keySet()) {
            // 新增字段
            FiledInfoParam filedInfoParam = filedInfoMap.get(filedName);
            FieldTypeEnum fieldTypeEnum = FieldTypeEnum.getEnum(filedInfoParam);
            if (!filedNameList.contains(filedName)) {
                String addColumn = isMysql ? DbConstant.ADD_COLUMN : DbConstant.SQLITE_ADD_COLUMN;
                String filedSql;
                if (isMysql) {
                    // mysql处理
                    filedSql = filedInfoParam.getFiledNotNull() ? DbConstant.NOT_NULL : "";
                    if (StrUtil.isNotEmpty(filedInfoParam.getFiledDefault())) {
                        filedSql += String.format(DbConstant.DEFAULT, filedInfoParam.getFiledDefault());
                    }
                } else {
                    // sqlite not null 必须有默认值
                    filedSql = filedInfoParam.getFiledNotNull() ? DbConstant.NOT_NULL : "";
                    if (StrUtil.isNotEmpty(filedInfoParam.getFiledDefault())) {
                        filedSql += String.format(DbConstant.DEFAULT, filedInfoParam.getFiledDefault());
                    } else if (filedInfoParam.getFiledNotNull()) {
                        filedSql += String.format(DbConstant.DEFAULT, "");
                    }
                }

                Integer filedLength = filedInfoParam.getFiledLength() != 0 ? filedInfoParam.getFiledLength() : fieldTypeEnum.getLength();
                String mysqlType = fieldTypeEnum.getMysqlType();
                String filedLengthStr = filedLength.toString();
                if (FieldTypeEnum.DOUBLE.getMysqlType().equals(mysqlType) || FieldTypeEnum.BASIC_DOUBLE.getMysqlType().equals(mysqlType)) {
                    filedLengthStr = filedLength + ", 2";
                }
                String createFieldSql = String.format(addColumn, tableInfoParam.getTableName(), filedInfoParam.getFiledName(), mysqlType, filedLengthStr, filedSql);
                createFieldSql = createFieldSql.replace("(0)", "");
                MessageApi.sendConsoleDebugMessage("新增字段: " + createFieldSql);
                SqlService.getInstance().executionSql(createFieldSql, storageMethod);
            }
            if (isMysql) {
                // 修改字段信息
                String filedSql = filedInfoParam.getFiledNotNull() ? DbConstant.NOT_NULL : "";
                if ("id".equals(filedName)) {
                    filedSql = DbConstant.NOT_NULL + DbConstant.AUTO_INCREMENT;
                }
                if (StrUtil.isNotEmpty(filedInfoParam.getFiledDefault())) {
                    filedSql += String.format(DbConstant.DEFAULT, filedInfoParam.getFiledDefault());
                }
                if (StrUtil.isNotEmpty(filedInfoParam.getFiledComment())) {
                    filedSql += String.format(DbConstant.COMMENT, filedInfoParam.getFiledComment());
                }
                Integer filedLength = filedInfoParam.getFiledLength() != 0 ? filedInfoParam.getFiledLength() : fieldTypeEnum.getLength();
                String mysqlType = fieldTypeEnum.getMysqlType();
                String filedLengthStr = filedLength.toString();
                if (FieldTypeEnum.DOUBLE.getMysqlType().equals(mysqlType) || FieldTypeEnum.BASIC_DOUBLE.getMysqlType().equals(mysqlType)) {
                    filedLengthStr = filedLength + ", 2";
                }
                String fieldCommentSql = String.format(DbConstant.ADD_COLUMN_COMMENT, tableInfoParam.getTableName(), filedInfoParam.getFiledName(), fieldTypeEnum.getMysqlType(), filedLengthStr, filedSql);
                fieldCommentSql = fieldCommentSql.replace("(0)", "");
                MessageApi.sendConsoleDebugMessage("新增字段注释: " + fieldCommentSql);
                SqlService.getInstance().executionSql(fieldCommentSql, storageMethod);
            }
        }
        // 新增索引
        if (isMysql) {
            String showIndexSql = String.format(DbConstant.SHOW_INDEX, tableInfoParam.getTableName());
            MessageApi.sendConsoleDebugMessage("查询表索引sql: " + showIndexSql);
            List<String> mysqlTableIndexList = SqlService.getInstance().getMysqlTableIndex(showIndexSql, storageMethod);
            for (String filedName : filedInfoMap.keySet()) {
                FiledInfoParam filedInfoParam = filedInfoMap.get(filedName);
                if (IndexEnum.NOT.equals(filedInfoParam.getIndexEnum())) {
                    continue;
                }
                if (mysqlTableIndexList.contains(filedName)) {
                    continue;
                }
                String indexName = filedName;
                if (IndexEnum.UNIQUE.equals(filedInfoParam.getIndexEnum())) {
                    indexName = "un_" + indexName;
                } else {
                    indexName = "idx_" + indexName;
                }
                String addIndexSql = String.format(DbConstant.ADD_INDEX, tableInfoParam.getTableName(), indexName, filedName);
                MessageApi.sendConsoleDebugMessage("新增表索引: " + addIndexSql);
                SqlService.getInstance().executionSql(addIndexSql, storageMethod);
            }
        }
    }

    @Override
    public int insert(T obj) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rst = null;
        String sql = "";
        try {
            conn = SqlManagerUtil.getInstance().getConnection(storageMethod);
            sql = dbSql.insertDataSql();
            MessageApi.sendConsoleDebugMessage("insert: " + sql);
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            // 获取字段信息
            Map<String, FiledInfoParam> filedInfoParamMap = dbSql.getFiledInfoMap().values().stream().collect(Collectors.groupingBy(FiledInfoParam::getFieldRealName, Collectors.collectingAndThen(Collectors.toList(), value -> value.get(0))));
            Map<String, Object> paramMap = BeanUtil.beanToMap(obj);
            // 赋值
            for (String key : paramMap.keySet()) {
                FiledInfoParam filedInfoParam = filedInfoParamMap.get(key);
                if (filedInfoParam == null) {
                    continue;
                }
                ps.setObject(filedInfoParam.getFiledIndex(), paramMap.get(key));
            }
            ps.executeUpdate();
            //获取自增id
            int id = 0;
            rst = ps.getGeneratedKeys();
            if (rst.next()) {
                id = rst.getInt(1);
            }
            return id;
        } catch (SQLException e) {
            MessageApi.sendConsoleMessage("&a insert查询出现异常,错误sql:" + sql);
            e.printStackTrace();
        } finally {
            SqlManagerUtil.getInstance().closeSql(conn, ps, rst);
        }
        return 0;
    }

    @Override
    public boolean insertBatch(List<T> objList) {
        for (T t : objList) {
            this.insert(t);
        }
        return true;
    }

    @Override
    public T selectOne() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rst = null;
        String sql = "";
        try {
            conn = SqlManagerUtil.getInstance().getConnection(storageMethod);
            sql = dbSql.selectDataSql();
            MessageApi.sendConsoleDebugMessage("selectOne: " + sql);
            ps = conn.prepareStatement(sql);
            rst = ps.executeQuery();
            if (!rst.isBeforeFirst()) {
                return null;
            }
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            T newInstance = (T) constructor.newInstance();
            LinkedHashMap<String, FiledInfoParam> filedInfoMap = dbSql.getFiledInfoMap();
            while (rst.next()) {
                for (String filedName : filedInfoMap.keySet()) {
                    FiledInfoParam filedInfoParam = filedInfoMap.get(filedName);
                    Object obj = rst.getObject(filedInfoParam.getFiledName());
                    if (obj == null) {
                        continue;
                    }
                    // 特殊字段类型处理
                    obj = specialHandling(filedInfoParam, obj);
                    Field field = clazz.getDeclaredField(filedInfoParam.getFieldRealName());
                    field.setAccessible(true);
                    field.set(newInstance, obj);
                }
            }
            return newInstance;
        } catch (SQLException | NoSuchFieldException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException e) {
            MessageApi.sendConsoleMessage("&a selectOne查询出现异常,错误sql:" + sql);
            e.printStackTrace();
        } finally {
            SqlManagerUtil.getInstance().closeSql(conn, ps, rst);
        }
        return null;
    }

    /**
     * 查询总数
     *
     * @return 总数
     */
    @Override
    public Integer count() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rst = null;
        int count = 0;
        String sql = "";
        try {
            conn = SqlManagerUtil.getInstance().getConnection(storageMethod);
            sql = dbSql.selectCountSql();
            MessageApi.sendConsoleDebugMessage("count: " + sql);
            ps = conn.prepareStatement(sql);
            rst = ps.executeQuery();
            while (rst.next()) {
                count = rst.getInt(1);
            }
        } catch (SQLException e) {
            MessageApi.sendConsoleMessage("&a count查询出现异常,错误sql:" + sql);
            e.printStackTrace();
        } finally {
            SqlManagerUtil.getInstance().closeSql(conn, ps, rst);
        }
        return count;
    }

    /**
     * 查询列表
     *
     * @return 列表
     */
    @Override
    public List<T> list() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rst = null;
        List<T> list = new ArrayList<>();
        String sql = "";
        try {
            conn = SqlManagerUtil.getInstance().getConnection(storageMethod);
            sql = dbSql.selectDataSql();
            MessageApi.sendConsoleDebugMessage("list: " + sql);
            ps = conn.prepareStatement(sql);
            rst = ps.executeQuery();
            if (!rst.isBeforeFirst()) {
                return list;
            }

            LinkedHashMap<String, FiledInfoParam> filedInfoMap = dbSql.getFiledInfoMap();
            while (rst.next()) {
                Constructor<?> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                T newInstance = (T) constructor.newInstance();
                for (String filedName : filedInfoMap.keySet()) {
                    FiledInfoParam filedInfoParam = filedInfoMap.get(filedName);
                    Object obj = rst.getObject(filedInfoParam.getFiledName());
                    if (obj == null) {
                        continue;
                    }
                    // 特殊字段类型处理
                    obj = specialHandling(filedInfoParam, obj);
                    Field field = clazz.getDeclaredField(filedInfoParam.getFieldRealName());
                    field.setAccessible(true);
                    field.set(newInstance, obj);
                }
                list.add(newInstance);
            }
            return list;
        } catch (SQLException | NoSuchFieldException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException e) {
            MessageApi.sendConsoleMessage("&a List查询出现异常,错误sql:" + sql);
            e.printStackTrace();
        } finally {
            SqlManagerUtil.getInstance().closeSql(conn, ps, rst);
        }
        return list;
    }

    /**
     * 特殊字段类型处理
     *
     * @param filedInfoParam 字段信息
     * @param obj            值
     * @return 新值
     */
    private Object specialHandling(FiledInfoParam filedInfoParam, Object obj) {
        FieldTypeEnum fieldTypeEnum = FieldTypeEnum.getEnum(filedInfoParam);
        switch (fieldTypeEnum) {
            // date处理
            case DATE:
                if (!isMysql) {
                    String str = obj.toString();
                    obj = new Date(Long.parseLong(str));
                } else {
                    if (obj instanceof LocalDateTime) {
                        obj = DateUtil.toDate((LocalDateTime) obj);
                    }
                }
                break;
            // 布尔处理
            case BOOLEAN:
            case BASIC_BOOLEAN:
                if (obj instanceof Integer) {
                    Integer bool = (Integer) obj;
                    obj = bool == 1;
                }
                break;
            // Long处理
            case LONG:
            case BASIC_LONG:
                if (obj instanceof Integer) {
                    Integer bool = (Integer) obj;
                    obj = bool.longValue();
                }
                break;
            // FLOAT处理
            case FLOAT:
            case BASIC_FLOAT:
                if (obj instanceof Double) {
                    Double bool = (Double) obj;
                    obj = bool.floatValue();
                }
            default:
                break;
        }
        return obj;
    }

    /**
     * 分页
     *
     * @return 集合
     */
    @Override
    public Page<T> page() {
        Integer count = this.count();
        List<T> list = new ArrayList<>();
        if (count > 0) {
            list = this.list();
        }
        return new Page<T>(count, list);
    }

    /**
     * 删除
     *
     * @return 条数
     */
    @Override
    public int delete() {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "";
        try {
            conn = SqlManagerUtil.getInstance().getConnection(storageMethod);
            sql = dbSql.deleteDataSql();
            MessageApi.sendConsoleDebugMessage("delete: " + sql);
            ps = conn.prepareStatement(sql);
            return ps.executeUpdate();
        } catch (SQLException e) {
            MessageApi.sendConsoleMessage("&a delete出现异常,错误sql:" + sql);
            e.printStackTrace();
        } finally {
            SqlManagerUtil.getInstance().closeSql(conn, ps, null);
        }
        return 0;
    }

    /**
     * 更新
     *
     * @return 条数
     */
    @Override
    public int update() {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "";
        try {
            conn = SqlManagerUtil.getInstance().getConnection(storageMethod);
            sql = dbSql.updateDataSql();
            MessageApi.sendConsoleDebugMessage("update: " + sql);
            ps = conn.prepareStatement(sql);
            // 获取字段信息
            LinkedHashMap<Integer, Object> updateFiledMap = dbSql.getUpdateFiledMap();
            for (Integer index : updateFiledMap.keySet()) {
                ps.setObject(index, updateFiledMap.get(index));
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            MessageApi.sendConsoleMessage("&a update出现异常,错误sql:" + sql);
            e.printStackTrace();
        } finally {
            SqlManagerUtil.getInstance().closeSql(conn, ps, null);
        }
        return 0;
    }

    /**
     * 根据id查询
     *
     * @param id id
     * @return T
     */
    @Override
    public T selectById(Integer id) {
        dbSql.addCondition(true, "id", SqlKeyword.EQ, id);
        return this.selectOne();
    }

    /**
     * 批量id查询
     *
     * @param ids ids
     * @return List
     */
    @Override
    public List<T> selectBatchIds(List<Integer> ids) {
        dbSql.addInCondition(true, "id", SqlKeyword.IN, ids);
        return this.list();
    }

    /**
     * 根据id更新
     *
     * @param id id
     * @return 结果
     */
    @Override
    public int updateById(Integer id) {
        dbSql.addCondition(true, "id", SqlKeyword.EQ, id);
        return this.update();
    }

    /**
     * 根据id删除
     *
     * @param id id
     * @return 结果
     */
    @Override
    public int deleteById(Integer id) {
        dbSql.addCondition(true, "id", SqlKeyword.EQ, id);
        return this.delete();
    }

    /**
     * 根据ids删除
     *
     * @param ids ids
     * @return 结果
     */
    @Override
    public int deleteBatchIds(List<Integer> ids) {
        dbSql.addInCondition(true, "id", SqlKeyword.IN, ids);
        return this.delete();
    }

}