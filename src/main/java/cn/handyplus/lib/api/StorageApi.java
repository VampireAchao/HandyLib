package cn.handyplus.lib.api;

import cn.handyplus.lib.constants.BaseConstants;
import cn.handyplus.lib.util.HandyConfigUtil;
import cn.handyplus.lib.util.SqlManagerUtil;

/**
 * sql初始化
 *
 * @author handy
 */
public class StorageApi {
    private StorageApi() {
    }

    /**
     * 加载storage文件,并初始化连接池
     */
    public static void enableSql() {
        enableSql(null);
    }

    /**
     * 加载storage文件,并初始化连接池
     *
     * @param storageMethod 连接方式
     * @since 2.9.2
     */
    public static void enableSql(String storageMethod) {
        BaseConstants.STORAGE_CONFIG = HandyConfigUtil.load("storage.yml");
        // 关闭当前数据源
        SqlManagerUtil.getInstance().close();
        // 初始化连接池
        SqlManagerUtil.getInstance().enableTable(storageMethod);
    }

}