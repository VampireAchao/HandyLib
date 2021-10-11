package com.handy.lib.inventory;

import com.handy.lib.constants.BaseConstants;
import com.handy.lib.core.CollUtil;
import com.handy.lib.param.InventoryWriteParam;
import com.handy.lib.util.BaseUtil;
import com.handy.lib.util.ItemStackUtil;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * handy的gui通用方法
 *
 * @author handy
 */
public class HandyInventoryUtil {

    /**
     * 刷新背包
     *
     * @param inventory gui
     */
    public static void refreshInventory(Inventory inventory) {
        for (int i = 0; i < BaseConstants.GUI_SIZE_54; i++) {
            inventory.setItem(i, new ItemStack(Material.AIR));
        }
    }

    /**
     * 刷新背包
     *
     * @param inventory gui
     * @param size      大小
     * @since 1.2.0
     */
    public static void refreshInventory(Inventory inventory, int size) {
        for (int i = 0; i < size; i++) {
            inventory.setItem(i, new ItemStack(Material.AIR));
        }
    }

    /**
     * 设置指定按钮
     *
     * @param inventory gui
     * @param index     下标
     * @param material  材质
     * @param name      名称
     * @since 1.2.0
     */
    public static void setButton(Inventory inventory, Integer index, Material material, String name) {
        inventory.setItem(index, ItemStackUtil.getItemStack(material, BaseUtil.replaceChatColor(name), null));
    }

    /**
     * 设置指定按钮
     *
     * @param inventory gui
     * @param index     下标
     * @param material  材质
     * @param name      名称
     * @param loreList  loreList
     * @since 1.2.0
     */
    public static void setButton(Inventory inventory, Integer index, Material material, String name, List<String> loreList) {
        setButton(inventory, index, material, name, loreList, 0);
    }

    /**
     * 设置指定按钮
     *
     * @param inventory         gui
     * @param index             下标
     * @param material          材质
     * @param name              名称
     * @param loreList          loreList
     * @param customModelDataId 自定义模型id
     * @since 1.6.5
     */
    public static void setButton(Inventory inventory, Integer index, Material material, String name, List<String> loreList, int customModelDataId) {
        ItemStack itemStack = ItemStackUtil.getItemStack(material, BaseUtil.replaceChatColor(name), BaseUtil.replaceChatColor(loreList, true));
        itemStack.setItemMeta(ItemStackUtil.setCustomModelData(itemStack.getItemMeta(), customModelDataId));
        inventory.setItem(index, itemStack);
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
                int customModelDataId = subMemorySection.getInt("custom-model-data");
                InventoryWriteParam inventoryWriteParam = new InventoryWriteParam(isUse, index, material, name, loreList, indexValue, customModelDataId);
                paramList.add(inventoryWriteParam);
            }
        }
        return paramList;
    }

    /**
     * 批量设置指定按钮
     *
     * @param inventory gui
     * @param paramList 入参
     * @param map       入参
     * @since 1.2.0
     */
    public static void batchSetButton(Inventory inventory, List<InventoryWriteParam> paramList, Map<Integer, Long> map) {
        if (CollUtil.isEmpty(paramList)) {
            return;
        }
        for (InventoryWriteParam param : paramList) {
            if (!param.getIsUse()) {
                continue;
            }
            setButton(inventory, param.getIndex(), ItemStackUtil.getMaterial(param.getMaterial()), param.getName(), param.getLoreList(), param.getCustomModelDataId());
            map.put(param.getIndex(), param.getIndexValue());
        }
    }

    /**
     * 获取玩家
     *
     * @param event 事件
     * @return 玩家
     */
    public static Player getPlayer(InventoryClickEvent event) {
        HumanEntity humanEntity = event.getWhoClicked();
        if (humanEntity instanceof Player) {
            return (Player) humanEntity;
        }
        return null;
    }

    /**
     * 分页设置
     *
     * @param inventory gui
     * @param pageNum   当前页
     * @param pageCount 总页
     * @since 1.5.1
     */
    public static void setPage(Inventory inventory, Integer pageNum, Integer pageCount) {
        setPage(inventory, pageNum, pageCount, Material.PAPER.name(), Material.PAPER.name(), 0, 0);
    }

    /**
     * 分页设置
     *
     * @param inventory                     gui
     * @param pageNum                       当前页
     * @param pageCount                     总页
     * @param previousPageMaterial          上一页材质
     * @param nextPageMaterial              下一页材质
     * @param nextPageCustomModelDataId     上一页模型id
     * @param previousPageCustomModelDataId 下一页模型id
     * @since 1.6.6
     */
    public static void setPage(Inventory inventory, Integer pageNum, Integer pageCount, String previousPageMaterial, String nextPageMaterial, int previousPageCustomModelDataId, int nextPageCustomModelDataId) {
        if (pageCount == 0) {
            pageCount = 1;
        }
        String currentPage = BaseUtil.getLangMsg("currentPage", "&a当前页:");
        String totalPages = BaseUtil.getLangMsg("totalPages", "&a总页数:");
        String previousPageMsg = BaseUtil.getLangMsg("previousPage", "&a点击前往上一页");
        String nextPageMsg = BaseUtil.getLangMsg("nextPage", "&a点击前往下一页");
        // 上一页
        List<String> previousPage = new ArrayList<>();
        previousPage.add(currentPage + (pageNum + 1));
        previousPage.add(totalPages + pageCount);
        ItemStack previousPageItemStack = ItemStackUtil.getItemStack(ItemStackUtil.getMaterial(previousPageMaterial, Material.PAPER), previousPageMsg, previousPage);
        previousPageItemStack.setItemMeta(ItemStackUtil.setCustomModelData(previousPageItemStack.getItemMeta(), previousPageCustomModelDataId));
        inventory.setItem(48, previousPageItemStack);
        // 下一页
        List<String> nextPage = new ArrayList<>();
        nextPage.add(currentPage + (pageNum + 1));
        nextPage.add(totalPages + pageCount);
        ItemStack nextPageItemStack = ItemStackUtil.getItemStack(ItemStackUtil.getMaterial(nextPageMaterial, Material.PAPER), nextPageMsg, nextPage);
        nextPageItemStack.setItemMeta(ItemStackUtil.setCustomModelData(nextPageItemStack.getItemMeta(), nextPageCustomModelDataId));
        inventory.setItem(50, nextPageItemStack);
    }

    /**
     * 分页设置
     *
     * @param fileConfig     配置
     * @param handyInventory gui
     * @param pageCount      总页
     * @since 1.7.0
     */
    public static void setPage(FileConfiguration fileConfig, HandyInventory handyInventory, Integer pageCount) {
        handyInventory.setPageCount(pageCount);
        String nextPageMaterial = fileConfig.getString("nextPage.material");
        String previousPageMaterial = fileConfig.getString("previousPage.material");
        int nextPageCustomModelData = fileConfig.getInt("nextPage.custom-model-data");
        int previousPageCustomModelData = fileConfig.getInt("previousPage.custom-model-data");
        setPage(handyInventory.getInventory(), handyInventory.getPageNum(), pageCount, previousPageMaterial, nextPageMaterial, previousPageCustomModelData, nextPageCustomModelData);
    }

}