package com.handy.lib.param;

import com.handy.lib.core.StrUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author handy
 */
@Data
public class InventoryWriteParam implements Serializable {

    /**
     * 是否使用
     */
    private Boolean isUse;

    /**
     * 坐标
     */
    private Integer index;

    /**
     * 材质
     */
    private String material;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private List<String> loreList;

    /**
     * 构建类
     *
     * @param isUse    是否使用
     * @param index    坐标
     * @param material 材质
     * @param name     名称
     * @param loreList 描述
     */
    public InventoryWriteParam(Boolean isUse, Integer index, String material, String name, List<String> loreList) {
        this.isUse = isUse;
        this.index = index;
        this.material = material;
        this.name = name;
        this.loreList = loreList;
    }

    /**
     * 构建类
     *
     * @param index    坐标
     * @param material 材质
     * @param name     名称
     * @param loreList 描述
     */
    public InventoryWriteParam(Integer index, String material, String name, List<String> loreList) {
        this.isUse = true;
        this.index = index;
        this.material = material;
        this.name = name;
        this.loreList = loreList;
    }

    /**
     * 构建类
     *
     * @param index    坐标
     * @param material 材质
     * @param name     名称
     */
    public InventoryWriteParam(Integer index, String material, String name) {
        this.isUse = true;
        this.index = index;
        this.material = material;
        this.name = name;
    }

    /**
     * 构建类
     *
     * @param isUse    是否使用
     * @param index    坐标
     * @param material 材质
     * @param name     名称
     * @param loreList 描述 , 分割
     */
    public InventoryWriteParam(Boolean isUse, Integer index, String material, String name, String loreListStr) {
        this.isUse = isUse;
        this.index = index;
        this.material = material;
        this.name = name;
        this.loreList = StrUtil.strToStrList(loreListStr);
    }

    /**
     * 构建类
     *
     * @param index    坐标
     * @param material 材质
     * @param name     名称
     * @param loreList 描述 , 分割
     */
    public InventoryWriteParam(Integer index, String material, String name, String loreListStr) {
        this.isUse = true;
        this.index = index;
        this.material = material;
        this.name = name;
        this.loreList = StrUtil.strToStrList(loreListStr);
    }

}
