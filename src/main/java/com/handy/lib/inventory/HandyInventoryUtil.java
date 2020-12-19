package com.handy.lib.inventory;

import com.handy.lib.util.BaseUtil;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * handy的gui通用方法
 *
 * @author hs
 * @date 2020/9/1 17:24
 */
public class HandyInventoryUtil {

    /**
     * 刷新背包
     *
     * @param inventory gui
     */
    public static void refreshInventory(Inventory inventory) {
        for (int i = 0; i < 54; i++) {
            inventory.setItem(i, new ItemStack(Material.AIR));
        }
    }

    /**
     * 设置返回按钮
     *
     * @param inventory gui
     * @param index     下标
     */
    public static void setBack(Inventory inventory, Integer index) {
        inventory.setItem(index, BaseUtil.getItemStack(Material.ENDER_PEARL, BaseUtil.getLangMsg("guiBack"), null));
    }

    /**
     * 分页设置
     *
     * @param inventory gui
     * @param pageNum   当前页
     * @param pageCount 总页
     */
    public static void setPage(Inventory inventory, Integer pageNum, Integer pageCount) {
        if (pageCount == 0) {
            pageCount = 1;
        }
        // 上一页
        List<String> previousPage = new ArrayList<>();
        previousPage.add(BaseUtil.getLangMsg("currentPage") + (pageNum + 1));
        previousPage.add(BaseUtil.getLangMsg("totalPages") + pageCount);
        inventory.setItem(48, BaseUtil.getItemStack(Material.PAPER, BaseUtil.getLangMsg("previousPage"), previousPage));
        // 下一页
        List<String> nextPage = new ArrayList<>();
        nextPage.add(BaseUtil.getLangMsg("currentPage") + (pageNum + 1));
        nextPage.add(BaseUtil.getLangMsg("totalPages") + pageCount);
        inventory.setItem(50, BaseUtil.getItemStack(Material.PAPER, BaseUtil.getLangMsg("nextPage"), nextPage));
    }

    /**
     * 对应gui校验
     *
     * @param event   事件
     * @param plugin  插件
     * @param guiType 类型
     * @return 是否对应true:是
     */
    public static InventoryCheckVo inventoryCheck(InventoryClickEvent event, Plugin plugin, String guiType) {
        InventoryCheckVo inventoryCheckVo = new InventoryCheckVo();
        inventoryCheckVo.setCheck(false);
        // 必填校验
        if (event == null || plugin == null || guiType == null) {
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
        // 判断是否是对应gui
        InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof HandyInventory)) {
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
