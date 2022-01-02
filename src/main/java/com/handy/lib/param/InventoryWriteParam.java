package com.handy.lib.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author handy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryWriteParam implements Serializable {

    private static final long serialVersionUID = -3271824563389644704L;
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
     * 额外参数-定位点
     */
    private Long indexValue;

    /**
     * 自定义模型id 1.14+
     */
    private int customModelDataId;

    /**
     * 附魔效果
     */
    private Boolean enchant;

}
