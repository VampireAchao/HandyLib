package com.handy.lib.api;

import com.handy.lib.param.InventoryWriteParam;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Inventory配置
 *
 * @author handy
 * @since 1.2.0
 */
public class InventoryApi {
    private InventoryApi() {
    }

    /**
     * 读取配置
     *
     * @param materialConfig 材质文件配置
     * @param pathMap        路径map
     * @return InventoryWriteParam 配置项
     */
    public static List<InventoryWriteParam> loadInventoryWriteParam(FileConfiguration materialConfig, Map<String, Map<String, Long>> pathMap) {
        List<InventoryWriteParam> paramList = new ArrayList<>();
        if (materialConfig == null || pathMap == null || pathMap.isEmpty()) {
            return paramList;
        }
        // 一级目录
        Map<String, Object> values = materialConfig.getValues(false);
        for (String key : values.keySet()) {
            Map<String, Long> subPathMap = pathMap.get(key);
            if (subPathMap == null) {
                continue;
            }
            // 二级目录
            MemorySection memorySection = (MemorySection) values.get(key);
            Map<String, Object> values2 = memorySection.getValues(false);
            for (String values2Key : values2.keySet()) {
                Long indexValue = subPathMap.get(values2Key);
                if (indexValue == null) {
                    continue;
                }
                MemorySection subMemorySection = (MemorySection) values2.get(values2Key);
                boolean isUse = subMemorySection.getBoolean("isUse");
                int index = subMemorySection.getInt("index");
                String material = subMemorySection.getString("material");
                String name = subMemorySection.getString("name");
                List<String> loreList = subMemorySection.getStringList("lore");
                InventoryWriteParam inventoryWriteParam = new InventoryWriteParam(isUse, index, material, name, loreList, indexValue);
                paramList.add(inventoryWriteParam);
            }
        }
        return paramList;
    }

}