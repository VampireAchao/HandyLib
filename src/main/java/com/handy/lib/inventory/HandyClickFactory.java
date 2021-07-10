package com.handy.lib.inventory;

import com.handy.lib.core.CollUtil;
import com.handy.lib.param.InventoryCheckParam;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * 点击进行处理工厂
 *
 * @author handy
 */
public class HandyClickFactory {
    private HandyClickFactory() {
    }

    private static final HandyClickFactory INSTANCE = new HandyClickFactory();

    public static HandyClickFactory getInstance() {
        return INSTANCE;
    }

    /**
     * 全部实现类
     */
    private static List<IHandyClickEvent> HANDY_CLICK_EVENT_LIST = new ArrayList<>();

    private static Plugin PLUGIN;

    /**
     * 初始化实现类
     *
     * @param handyClickEvents 点击处理事件
     * @param plugin           插件
     */
    public void init(List<IHandyClickEvent> handyClickEvents, Plugin plugin) {
        if (CollUtil.isNotEmpty(handyClickEvents)) {
            HANDY_CLICK_EVENT_LIST = handyClickEvents;
            PLUGIN = plugin;
            plugin.getServer().getPluginManager().registerEvents(new HandyInventoryListener(), plugin);
        }
    }

    /**
     * 对应gui校验
     *
     * @param event 事件
     * @return 校验结果
     */
    public InventoryCheckParam inventoryCheck(InventoryClickEvent event) {
        InventoryCheckParam inventoryCheckParam = new InventoryCheckParam();
        inventoryCheckParam.setCheck(false);

        // 判断是否是对应gui
        InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof HandyInventory)) {
            return inventoryCheckParam;
        }
        // 禁止数字键和shift键
        if (event.getClick().isShiftClick() || event.getClick().isKeyboardClick()) {
            // 取消点击效果
            event.setCancelled(true);
            return inventoryCheckParam;
        }
        // 如果操作对象不是玩家则返回
        HumanEntity humanEntity = event.getWhoClicked();
        if (!(humanEntity instanceof Player)) {
            return inventoryCheckParam;
        }
        Player player = (Player) humanEntity;
        // 点击为空返回
        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null || Material.AIR.equals(currentItem.getType())) {
            return inventoryCheckParam;
        }

        HandyInventory handyInventory = (HandyInventory) holder;
        // 判断是否为对应插件
        if (handyInventory.getPlugin() == null || PLUGIN == null || !PLUGIN.getName().equals(handyInventory.getPlugin().getName())) {
            return inventoryCheckParam;
        }

        // 事件是否被取消
        if (event.isCancelled()) {
            return inventoryCheckParam;
        }
        handyInventory.setPlayer(player);
        inventoryCheckParam.setCheck(true);
        inventoryCheckParam.setHandyInventory(handyInventory);
        return inventoryCheckParam;
    }

    /**
     * 进行处理
     *
     * @param handyInventory 入参
     * @param event          事件
     */
    public void rawSlotClick(HandyInventory handyInventory, InventoryClickEvent event) {
        for (IHandyClickEvent handyClickEvent : HANDY_CLICK_EVENT_LIST) {
            if (handyClickEvent.guiType().equals(handyInventory.getGuiType()) && handyClickEvent.rawSlotList().contains(event.getRawSlot())) {
                handyClickEvent.rawSlotClick(handyInventory, event);
                return;
            }
        }
    }

}