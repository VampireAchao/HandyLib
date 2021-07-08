package com.handy.lib.param;

import com.handy.lib.inventory.HandyInventory;
import lombok.Data;
import org.bukkit.entity.Player;

import java.io.Serializable;

/**
 * gui校验参数
 *
 * @author handy
 **/
@Data
public class InventoryCheckParam implements Serializable {

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
