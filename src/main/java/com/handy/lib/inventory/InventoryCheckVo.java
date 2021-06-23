package com.handy.lib.inventory;

import lombok.Data;
import org.bukkit.entity.Player;

/**
 * gui校验参数
 *
 * @author handy
 **/
@Data
public class InventoryCheckVo {

    /**
     * 校验结果
     */
    private boolean check;

    /**
     * 玩家
     */
    private Player player;

    /**
     * gui
     */
    private HandyInventory handyInventory;
}
