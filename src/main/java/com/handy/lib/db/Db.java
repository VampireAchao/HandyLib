package com.handy.lib.db;

import com.handy.lib.annotation.TableField;
import com.handy.lib.annotation.TableName;
import com.handy.lib.core.CollUtil;
import com.handy.lib.core.StrUtil;
import com.handy.lib.db.param.FiledInfoParam;
import com.handy.lib.db.param.TableInfoParam;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 处理器
 *
 * @author handy
 * @since 1.4.8
 */
public class Db<T> {

    /**
     * 类
     */
    private final Class<T> clazz;
    /**
     * DbSql
     */
    private DbSql dbSql;

    /**
     * 构造
     *
     * @param clazz 类
     */
    public Db(Class<T> clazz) {
        this.clazz = clazz;
        init();
    }

    /**
     * 初始化使用
     *
     * @param clazz 类
     * @param <T>   T
     * @return T
     */
    public static <T> Db<T> use(Class<T> clazz) {
        return new Db<>(clazz);
    }

    /**
     * 条件构造器
     *
     * @return 条件构造器
     */
    public Compare<T> where() {
        return new Compare<T>(dbSql);
    }

    /**
     * 更新构造器
     *
     * @return 更新构造器
     */
    public UpdateCondition<T> update() {
        return new UpdateCondition<T>(dbSql);
    }

    /**
     * 执行器
     *
     * @return 执行器
     */
    public DbExecution<T> execution() {
        return new DbExecution<T>(dbSql, clazz);
    }

    /**
     * 执行器
     *
     * @param storageMethod 存储方式
     * @return 执行器
     */
    public DbExecution<T> execution(String storageMethod) {
        return new DbExecution<T>(dbSql, clazz, storageMethod);
    }

    /**
     * 构建基础条件
     */
    private void init() {
        TableName tableName = this.clazz.getAnnotation(TableName.class);
        if (tableName == null) {
            throw new RuntimeException("tableName 为空");
        }
        // 表信息
        TableInfoParam tableInfoParam = TableInfoParam.builder().tableName(tableName.value()).tableComment(tableName.comment()).build();
        Field[] fields = this.clazz.getDeclaredFields();
        List<String> fieldList = new ArrayList<>();
        LinkedHashMap<String, FiledInfoParam> filedInfoMap = new LinkedHashMap<>();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            TableField tableField = field.getAnnotation(TableField.class);
            if (tableField != null && StrUtil.isNotEmpty(tableField.value())) {
                fieldList.add(DbConstant.POINT + tableField.value() + DbConstant.POINT);
                FiledInfoParam build = FiledInfoParam.builder()
                        .filedName(tableField.value())
                        .fieldRealName(field.getName())
                        .filedType(field.getGenericType().getTypeName())
                        .filedComment(tableField.comment())
                        .filedNotNull(tableField.notNull())
                        .filedDefault(tableField.filedDefault())
                        .filedIndex(i + 1)
                        .filedLength(tableField.length())
                        .build();
                filedInfoMap.put(tableField.value(), build);
            }
        }
        if (CollUtil.isEmpty(fieldList)) {
            throw new RuntimeException("fieldList 为空");
        }
        this.dbSql = DbSql.builder()
                .tableName(DbConstant.POINT + tableName.value() + DbConstant.POINT)
                .tableInfoParam(tableInfoParam)
                .filed(CollUtil.listToStr(fieldList))
                .filedInfoMap(filedInfoMap)
                .where(DbConstant.DEFAULT_WHERE)
                .updateFiledList(new ArrayList<>())
                .updateFiledMap(new LinkedHashMap<>())
                .build();
    }

}