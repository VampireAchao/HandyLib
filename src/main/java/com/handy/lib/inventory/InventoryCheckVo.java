package com.handy.lib.inventory;

import lombok.Data;
import org.bukkit.entity.Player;

/**
 * @author hs
 * @date 2020-12-19 13:31
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
