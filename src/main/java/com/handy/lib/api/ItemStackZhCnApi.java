package com.handy.lib.api;

import com.handy.lib.util.BaseUtil;
import com.handy.lib.util.HandyHttpUtil;
import org.bukkit.plugin.Plugin;

import java.io.File;

/**
 * 云汉化
 *
 * @author hs
 * @date 2020/7/30 11:35
 */
public class ItemStackZhCnApi {
    private ItemStackZhCnApi() {
    }

    /**
     * 加载物品汉化文件
     */
    public static void initZhCn(Plugin plugin) {
        // 优先加载zh_cn.json
        File zhChFile = new File(plugin.getDataFolder(), "zh_cn.json");
        if (zhChFile.exists()) {
            BaseUtil.readJsonFileToJsonCacheMap(zhChFile);
            return;
        }
        // 运行云汉化
        HandyHttpUtil.getZhCn(plugin);

        // 加载item.json
        File itemFile = new File(plugin.getDataFolder(), "item.json");
        if (!itemFile.exists()) {
            plugin.saveResource("item.json", false);
        }
        BaseUtil.readJsonFileToItemJsonCacheMap(itemFile);

        // 自动同步自定义汉化
        HandyHttpUtil.setItemName(plugin);
    }


}
