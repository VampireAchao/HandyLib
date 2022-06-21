package cn.handyplus.lib.db.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * SQL 保留关键字枚举
 *
 * @author handy
 */
@Getter
@AllArgsConstructor
public enum SqlKeyword implements Serializable {
    /**
     * SQL 保留关键字
     */
    AND(" AND "),
    OR(" OR "),
    IN(" IN "),
    NOT_IN(" NOT IN "),
    NOT(" NOT "),
    LIKE(" LIKE "),
    NOT_LIKE(" NOT LIKE "),
    EQ(" = "),
    NE(" <> "),
    GT(" > "),
    LT(" < "),
    GE(" >= "),
    LE(" <= "),
    IS_NULL(" IS NULL "),
    IS_NOT_NULL(" IS NOT NULL "),
    GROUP_BY(" GROUP BY "),
    LIMIT(" LIMIT "),
    OFFSET(" OFFSET "),
    HAVING(" HAVING "),
    ORDER_BY(" ORDER BY "),
    EXISTS(" EXISTS "),
    BETWEEN(" BETWEEN "),
    ASC(" ASC "),
    DESC(" DESC ");

    private final String keyword;

}