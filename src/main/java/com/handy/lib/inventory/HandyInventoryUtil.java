package com.handy.lib.inventory;

import com.handy.lib.constants.BaseConstants;
import com.handy.lib.core.CollUtil;
import com.handy.lib.param.InventoryCheckParam;
import com.handy.lib.param.InventoryWriteParam;
import com.handy.lib.util.BaseUtil;
import com.handy.lib.util.ItemStackUtil;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

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
    public static void batchSetButton(Inventory inventory, List<InventoryWriteParam> paramList) {
        if (CollUtil.isEmpty(paramList)) {
            return;
        }
        for (InventoryWriteParam param : paramList) {
            if (!param.getIsUse()) {
                continue;
            }
            setButton(inventory, param.getIndex(), ItemStackUtil.getMaterial(param.getMaterial()), param.getName(), param.getLoreList());
        }
    }

    /**
     * 对应gui校验
     *
     * @param event   事件
     * @param plugin  插件
     * @param guiType 类型
     * @return 校验结果
     */
    public static InventoryCheckParam inventoryCheck(InventoryClickEvent event, Plugin plugin, String guiType) {
        InventoryCheckParam inventoryCheckVo = new InventoryCheckParam();
        inventoryCheckVo.setCheck(false);
        // 必填校验
        if (event == null || plugin == null || guiType == null) {
            return inventoryCheckVo;
        }
        // 判断是否是对应gui
        InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof HandyInventory)) {
            return inventoryCheckVo;
        }
        // 禁止数字键和shift键
        if (event.getClick().isShiftClick() || event.getClick().isKeyboardClick()) {
            // 取消点击效果
            event.setCancelled(true);
            return inventoryCheckVo;
        }
        // 如果操作对象不是玩家则返回
        HumanEntity humanEntity = event.getWhoClicked();
        if (!(humanEntity instanceof Player)) {
            return inventoryCheckVo;
        }
        Player player = (Player) humanEntity;
        // 点击为空返回
        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null || Material.AIR.equals(currentItem.getType())) {
            return inventoryCheckVo;
        }

        HandyInventory handyInventory = (HandyInventory) holder;
        // 判断是否为对应插件
        if (handyInventory.getPlugin() == null || !plugin.getName().equals(handyInventory.getPlugin().getName())) {
            return inventoryCheckVo;
        }
        // 判断是否为对应类型
        if (!guiType.equals(handyInventory.getGuiType())) {
            return inventoryCheckVo;
        }
        // 事件是否被取消
        if (event.isCancelled()) {
            return inventoryCheckVo;
        }
        inventoryCheckVo.setCheck(true);
        inventoryCheckVo.setPlayer(player);
        inventoryCheckVo.setHandyInventory(handyInventory);
        return inventoryCheckVo;
    }

}
