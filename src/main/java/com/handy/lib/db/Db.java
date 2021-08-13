package com.handy.lib.db;

import com.handy.lib.annotation.TableName;

/**
 * 处理器
 *
 * @author handy
 */
public class Db extends Compare {
    /**
     * 测试用
     */
    public static void main(String[] args) {
        System.out.println("字段名：" + DbColumnUtil.getName(User::getLoginName));
        System.out.println("字段名：" + DbColumnUtil.getName(User::getNickName));
    }

    public Db build(Class<?> clazz) {
        TableName tableName = clazz.getAnnotation(TableName.class);
        if (tableName == null) {
            return null;
        }
        DbSql.Builder builder = new DbSql.Builder()
                .setSelect(DbConstant.SELECT)
                .setc(DbConstant.COUNT)
                .set(buildFromSql(tableName.value(), tableName.alias()))
                .where(DbConstant.DEFAULT_WHERE);
        super.builder = builder;
        return this;
    }

    private static String buildFromSql(String tablaCode, String tableAlias) {
        return String.format(DbConstant.FORM, tablaCode, tableAlias);
    }

}