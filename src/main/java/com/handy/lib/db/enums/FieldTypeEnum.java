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
    STRING("java.lang.String", "VARCHAR", "text", 64, "ALTER TABLE `%s` ADD `%s` %s(%d);", "ALTER TABLE `%s` MODIFY `%s` %s(%d) COMMENT '%s';"),
    INTEGER("java.lang.Integer", "INT", "INTEGER", 11, "ALTER TABLE `%s` ADD `%s` %s(%d);", "ALTER TABLE `%s` MODIFY `%s` %s(%d) COMMENT '%s';"),
    BASIC_INT("int", "INT", "INTEGER", 11, "ALTER TABLE `%s` ADD `%s` %s(%d);", "ALTER TABLE `%s` MODIFY `%s` %s(%d) COMMENT '%s';"),
    BOOLEAN("java.lang.Boolean", "INT", "INTEGER", 11, "ALTER TABLE `%s` ADD `%s` %s(%d);", "ALTER TABLE `%s` MODIFY `%s` %s(%d) COMMENT '%s';"),
    BASIC_BOOLEAN("boolean", "INT", "INTEGER", 11, "ALTER TABLE `%s` ADD `%s` %s(%d);", "ALTER TABLE `%s` MODIFY `%s` %s(%d) COMMENT '%s';"),
    DATE("java.util.Date", "DATETIME", "INTEGER", 0, "ALTER TABLE `%s` ADD `%s` %s(%d);", "ALTER TABLE `%s` MODIFY `%s` %s(%d) COMMENT '%s';"),
    ;

    private final String javaType;
    private final String mysqlType;
    private final String sqliteType;
    private final Integer length;
    private final String addSql;
    private final String commentSql;

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