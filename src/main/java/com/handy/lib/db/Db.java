package com.handy.lib.db;

import com.handy.lib.annotation.TableField;
import com.handy.lib.annotation.TableName;
import com.handy.lib.core.CollUtil;
import com.handy.lib.core.StrUtil;

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
public class Db<T> extends AbstractWrapper<T> {

    /**
     * 构造
     *
     * @param clazz 类
     */
    public Db(Class<?> clazz) {
        super.clazz = clazz;
    }

    /**
     * 构建基础条件
     *
     * @return 查询条件
     */
    public Compare builder() {
        TableName tableName = super.clazz.getAnnotation(TableName.class);
        if (tableName == null) {
            throw new RuntimeException("tableName 为空");
        }
        Field[] fields = super.clazz.getDeclaredFields();
        List<String> fieldList = new ArrayList<>();
        LinkedHashMap<String, Integer> filedIndexMap = new LinkedHashMap<>();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            TableField tableField = field.getAnnotation(TableField.class);
            if (tableField != null && StrUtil.isNotEmpty(tableField.value())) {
                fieldList.add(tableField.value());
                filedIndexMap.put(tableField.value(), i + 1);
            }
        }

        if (CollUtil.isEmpty(fieldList)) {
            throw new RuntimeException("fieldList 为空");
        }
        super.dbSql = DbSql.builder()
                .select(DbConstant.SELECT)
                .insert(DbConstant.INSERT)
                .count(DbConstant.COUNT)
                .filed(CollUtil.listToStr(fieldList))
                .filedIndexMap(filedIndexMap)
                .from(DbConstant.FORM + tableName.value())
                .where(DbConstant.DEFAULT_WHERE)
                .whereData(new LinkedHashMap<>())
                .build();
        return this;
    }

}