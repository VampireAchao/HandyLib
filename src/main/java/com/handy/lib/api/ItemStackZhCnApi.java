package com.handy.lib.api;

import com.handy.lib.InitApi;
import com.handy.lib.util.BaseUtil;
import com.handy.lib.util.HandyHttpUtil;

import java.io.File;

/**
 * 云汉化
 *
 * @author handy
 */
public class ItemStackZhCnApi {
    private ItemStackZhCnApi() {
    }

    /**
     * 加载物品汉化文件
     */
    public static void initZhCn() {
        // 优先加载zh_cn.json
        File zhChFile = new File(InitApi.PLUGIN.getDataFolder(), "zh_cn.json");
        if (zhChFile.exists()) {
            BaseUtil.readJsonFileToJsonCacheMap(zhChFile);
            return;
        }
        // 运行云汉化
        HandyHttpUtil.getZhCn();

        // 加载item.json
        File itemFile = new File(InitApi.PLUGIN.getDataFolder(), "item.json");
        if (!itemFile.exists()) {
            InitApi.PLUGIN.saveResource("item.json", false);
        }
        BaseUtil.readJsonFileToItemJsonCacheMap(itemFile);

        // 自动同步自定义汉化
        HandyHttpUtil.setItemName();
    }


}
