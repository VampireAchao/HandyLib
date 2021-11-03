package com.handy.lib.api;

import com.handy.lib.InitApi;
import com.handy.lib.constants.VersionCheckEnum;
import com.handy.lib.util.BaseUtil;
import com.handy.lib.util.HandyHttpUtil;

import java.io.File;

/**
 * 汉化处理
 *
 * @author handy
 */
public class ItemStackZhCnApi {
    private ItemStackZhCnApi() {
    }

    /**
     * 加载物品汉化文件
     *
     * @since 1.8.2
     */
    public static void initZhCn() {
        initZhCn(true);
    }

    /**
     * 加载物品汉化文件
     *
     * @param isCloudItem 是否云汉化
     */
    public static void initZhCn(boolean isCloudItem) {
        // 优先加载zh_cn.json
        File zhChFile = new File(InitApi.PLUGIN.getDataFolder(), "zh_cn.json");
        if (zhChFile.exists()) {
            BaseUtil.readJsonFileToJsonCacheMap(zhChFile);
        } else {
            // 获取zh_cn.json文件
            HandyHttpUtil.getZhCn();
        }

        // 加载item.json
        File itemFile = new File(InitApi.PLUGIN.getDataFolder(), "item.json");
        if (!itemFile.exists()) {
            InitApi.PLUGIN.saveResource("item.json", false);
        }
        BaseUtil.readJsonFileToItemJsonCacheMap(itemFile);

        // 获取云汉化
        if (isCloudItem) {
            HandyHttpUtil.setCloudItemJsonCacheMap(VersionCheckEnum.getEnum().getVersion());
        }

        // 自动同步自定义汉化
        HandyHttpUtil.setItemName();
    }

}