package inventory;

import com.handy.lib.inventory.HandyInventory;
import com.handy.lib.inventory.click.IHandyClickEvent;

import java.util.Arrays;
import java.util.List;

/**
 * @author handy
 * @since 1.2.0
 */
public class ShopClickEvent implements IHandyClickEvent {
    @Override
    public String guiType() {
        return "shop";
    }

    @Override
    public List<Integer> rawSlotList() {
        return Arrays.asList(1, 2, 3);
    }

    @Override
    public void rawSlotClick(HandyInventory handyInventory) {
        System.out.println("shop测试");
    }

}
