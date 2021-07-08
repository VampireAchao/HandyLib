package inventory;

import com.handy.lib.inventory.click.HandyClickFactory;
import com.handy.lib.inventory.click.IHandyClickEvent;

import java.util.Arrays;
import java.util.List;

/**
 * @author handy
 * @since 1.2.0
 */
public class HandyClickFactoryTest {

    public static void main(String[] args) {
        List<IHandyClickEvent> iHandyClickEvents = Arrays.asList(new ShopClickEvent(), new Shop2ClickEvent(), new OpenClickEvent());
        HandyClickFactory.getInstance().init(iHandyClickEvents);

        HandyClickFactory.getInstance().rawSlotClick("open", 5);

    }
}
