package com.handy.lib.db;

/**
 * @author handy
 */
public class Compare {
    protected DbSql.Builder builder;

    public <T> String eq(DbFunction<T, ?> fn) {
        builder.where()
        return DbColumnUtil.getName(fn);
    }

}
