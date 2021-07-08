package inventory;

import com.handy.lib.inventory.click.IHandyClickEvent;

import java.util.Arrays;
import java.util.List;

/**
 * @author handy
 * @since 1.2.0
 */
public class OpenClickEvent implements IHandyClickEvent {
    @Override
    public String guiType() {
        return "open";
    }

    @Override
    public List<Integer> rawSlotList() {
        return Arrays.asList(4, 5, 6);
    }

    @Override
    public void rawSlotClick() {
        System.out.println("open测试");
    }

}
