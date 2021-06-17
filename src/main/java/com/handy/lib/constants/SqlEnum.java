package com.handy.lib.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hs
 * @date 2021/6/17 17:21
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
    );
    private final String command;
}
