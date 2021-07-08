package inventory;

import com.handy.lib.inventory.click.IHandyClickEvent;
import com.handy.lib.util.BaseUtil;

import java.util.Arrays;
import java.util.List;

/**
 * @author handy
 * @since 1.2.0
 */
public class ShopClickEvent extends BaseUtil implements IHandyClickEvent {
    @Override
    public String guiType() {
        return "shop";
    }

    @Override
    public List<Integer> rawSlotList() {
        return Arrays.asList(1, 2, 3);
    }

    @Override
    public void rawSlotClick() {
        System.out.println("shop测试");
    }

}
