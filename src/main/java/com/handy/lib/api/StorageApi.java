package com.handy.lib.api;

import com.handy.lib.InitApi;
import com.handy.lib.constants.BaseConstants;
import com.handy.lib.util.SqlManagerUtil;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

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
        File langFile = new File(InitApi.PLUGIN.getDataFolder(), "storage.yml");
        if (!(langFile.exists())) {
            InitApi.PLUGIN.saveResource("storage.yml", false);
        }
        BaseConstants.STORAGE_CONFIG = YamlConfiguration.loadConfiguration(langFile);
        // 关闭当前数据源
        SqlManagerUtil.getInstance().close();
        // 初始化连接池
        SqlManagerUtil.getInstance().enableTable();
    }

}
