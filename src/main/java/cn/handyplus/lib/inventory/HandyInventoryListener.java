package cn.handyplus.lib.inventory;

import cn.handyplus.lib.param.InventoryCheckParam;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * 背包统一监听器
 *
 * @author handy
 */
public class HandyInventoryListener implements Listener {

    /**
     * 点击gui事件
     *
     * @param event 事件
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // 校验
        InventoryCheckParam inventoryCheckParam = HandyClickFactory.getInstance().inventoryCheck(event);
        if (!inventoryCheckParam.isCheck()) {
            return;
        }
        HandyInventory handyInventory = inventoryCheckParam.getHandyInventory();
        // 点击事件是否取消
        event.setCancelled(handyInventory.isToCancel());
        HandyClickFactory.getInstance().rawSlotClick(handyInventory, event);
    }

}