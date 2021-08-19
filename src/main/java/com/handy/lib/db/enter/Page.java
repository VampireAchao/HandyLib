package com.handy.lib.db.enter;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author handy
 */
@Data
@AllArgsConstructor
public class Page<T> implements Serializable {

    /**
     * 总数
     */
    private int total;

    /**
     * 查询数据列表
     */
    private List<T> records = Collections.emptyList();

}