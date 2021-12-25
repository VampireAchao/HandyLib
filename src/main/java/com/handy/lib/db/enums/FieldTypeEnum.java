package com.handy.lib.db.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字段对应类型
 *
 * @author handy
 * @since 1.4.8
 */
@Getter
@AllArgsConstructor
public enum FieldTypeEnum {
    /**
     * 查询全部数据sql
     */
    STRING("java.lang.String", "VARCHAR", 64),
    INTEGER("java.lang.Integer", "INT", 11),
    LONG("java.lang.Long", "INT", 11),
    BASIC_INT("int", "INT", 11),
    BOOLEAN("java.lang.Boolean", "INT", 11),
    BASIC_BOOLEAN("boolean", "INT", 11),
    DATE("java.util.Date", "DATETIME", 0),
    DOUBLE("java.lang.Double", "DOUBLE", 11),
    BASIC_DOUBLE("double", "DOUBLE", 11),
    BASIC_FLOAT("float", "FLOAT", 11),
    FLOAT("java.lang.Float", "FLOAT", 11),
    ;

    private final String javaType;
    private final String mysqlType;
    private final Integer length;

    /**
     * 获取枚举
     *
     * @param javaType 字段类型
     * @return FieldTypeEnum
     */
    public static FieldTypeEnum getEnum(String javaType) {
        for (FieldTypeEnum fieldTypeEnum : FieldTypeEnum.values()) {
            if (fieldTypeEnum.getJavaType().equals(javaType)) {
                return fieldTypeEnum;
            }
        }
        return STRING;
    }

}