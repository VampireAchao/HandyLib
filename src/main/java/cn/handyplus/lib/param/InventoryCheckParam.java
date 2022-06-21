package cn.handyplus.lib.param;

import cn.handyplus.lib.inventory.HandyInventory;
import lombok.Data;

import java.io.Serializable;

/**
 * gui校验参数
 *
 * @author handy
 **/
@Data
public class InventoryCheckParam implements Serializable {

    private static final long serialVersionUID = 1923067195869748007L;
    /**
     * 校验结果
     */
    private boolean check;

    /**
     * gui
     */
    private HandyInventory handyInventory;
}
