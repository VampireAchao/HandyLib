package com.handy.lib.api;

import com.handy.lib.core.CollUtil;
import com.handy.lib.param.InventoryWriteParam;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author handy
 * @since 1.2.0
 */
public class MaterialApi {
    private MaterialApi() {
    }

    /**
     * 读取配置
     *
     * @param materialConfig 材质文件配置
     * @param pathList       路径
     * @return InventoryWriteParam 配置项
     */
    public static List<InventoryWriteParam> loadInventoryWriteParam(FileConfiguration materialConfig, List<String> pathList) {
        List<InventoryWriteParam> paramList = new ArrayList<>();
        if (materialConfig == null || CollUtil.isEmpty(pathList)) {
            return paramList;
        }
        for (String path : pathList) {
            InventoryWriteParam inventoryWriteParam = materialConfig.getObject(path, InventoryWriteParam.class);
            if (inventoryWriteParam == null) {
                continue;
            }
            paramList.add(inventoryWriteParam);
        }
        return paramList;
    }

}