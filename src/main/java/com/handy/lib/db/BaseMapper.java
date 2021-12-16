package com.handy.lib.db;

import com.handy.lib.db.enter.Page;

import java.io.Serializable;
import java.util.List;

/**
 * 基础方法
 *
 * @author handy
 */
public interface BaseMapper<T> extends Serializable {

    /**
     * 建表
     */
    void create();

    /**
     * 插入一条记录
     *
     * @param obj 入参
     * @return id
     */
    int insert(T obj);

    /**
     * 根据 entity 条件，查询一条记录
     *
     * @return T
     */
    T selectOne();

    /**
     * 总数
     *
     * @return 总数
     */
    Integer count();

    /**
     * 查询列表
     *
     * @return 列表
     */
    List<T> list();

    /**
     * 分页
     *
     * @return 集合
     */
    Page<T> page();

    /**
     * 删除
     *
     * @return 条数
     */
    int delete();

    /**
     * 更新
     *
     * @return 条数
     */
    int update();

    /**
     * 根据id查询
     *
     * @param id id
     * @return T
     */
    T selectById(Integer id);

    /**
     * 批量id查询
     *
     * @param ids ids
     * @return List
     */
    List<T> selectBatchIds(List<Integer> ids);

    /**
     * 根据id更新
     *
     * @param id id
     * @return 结果
     */
    int updateById(Integer id);

    /**
     * 根据id删除
     *
     * @param id id
     * @return 结果
     */
    int deleteById(Integer id);

    /**
     * 根据ids删除
     *
     * @param ids ids
     * @return 结果
     */
    int deleteBatchIds(List<Integer> ids);
}