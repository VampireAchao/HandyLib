package com.handy.lib.inventory;

import com.handy.lib.InitApi;
import com.handy.lib.core.CollUtil;
import com.handy.lib.param.InventoryCheckParam;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static final Map<String, IHandyClickEvent> HANDY_CLICK_EVENT_MAP = new HashMap<>();

    /**
     * 初始化实现类
     *
     * @param handyClickEvents 点击处理事件
     */
    public void init(List<IHandyClickEvent> handyClickEvents) {
        if (CollUtil.isEmpty(handyClickEvents)) {
            return;
        }
        for (IHandyClickEvent handyClickEvent : handyClickEvents) {
            HANDY_CLICK_EVENT_MAP.put(handyClickEvent.guiType(), handyClickEvent);
        }
        InitApi.PLUGIN.getServer().getPluginManager().registerEvents(new HandyInventoryListener(), InitApi.PLUGIN);
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
        HandyInventory handyInventory = (HandyInventory) holder;
        // 如果操作对象不是玩家则返回
        Player player = HandyInventoryUtil.getPlayer(event);
        if (player == null) {
            return inventoryCheckParam;
        }
        // 禁止数字键和shift键
        if (event.getClick().isShiftClick() || event.getClick().isKeyboardClick()) {
            // 取消点击效果
            event.setCancelled(true);
            return inventoryCheckParam;
        }
        // 点击为空返回
        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null || Material.AIR.equals(currentItem.getType())) {
            // 取消点击效果
            event.setCancelled(true);
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
        IHandyClickEvent handyClickEvent = HANDY_CLICK_EVENT_MAP.get(handyInventory.getGuiType());
        if (handyClickEvent == null) {
            return;
        }
        handyClickEvent.rawSlotClick(handyInventory, event);
    }

}