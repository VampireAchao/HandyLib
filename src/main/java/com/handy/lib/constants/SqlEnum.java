package com.handy.lib.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 基础sql
 *
 * @author handy
 * @since 1.0.4
 */
@Getter
@AllArgsConstructor
public enum SqlEnum {
    /**
     * 查询全部数据sql
     */
    SELECT_ALL(
            "SELECT * FROM "
    ),
    ADD_DATA(
            "INSERT INTO "
    ),
    DELETE_ALL(
            "DELETE FROM "
    ),;
    private final String command;
}
