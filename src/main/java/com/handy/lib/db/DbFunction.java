package com.handy.lib.db;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 使Function获取序列化能力
 *
 * @author handy
 */
@FunctionalInterface
public interface DbFunction<T, R> extends Function<T, R>, Serializable {
}