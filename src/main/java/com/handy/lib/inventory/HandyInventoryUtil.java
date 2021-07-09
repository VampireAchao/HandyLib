package com.handy.lib.inventory;

import com.handy.lib.constants.BaseConstants;
import com.handy.lib.core.CollUtil;
import com.handy.lib.param.InventoryWriteParam;
import com.handy.lib.util.BaseUtil;
import com.handy.lib.util.ItemStackUtil;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
        inventory.setItem(index, ItemStackUtil.getItemStack(material, BaseUtil.replaceChatColor(name), BaseUtil.replaceChatColor(loreList, true)));
    }

    /**
     * 批量设置指定按钮
     *
     * @param inventory gui
     * @param paramList 入参
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
            setButton(inventory, param.getIndex(), ItemStackUtil.getMaterial(param.getMaterial()), param.getName(), param.getLoreList());
            map.put(param.getIndex(), param.getIndexValue());
        }
    }

}