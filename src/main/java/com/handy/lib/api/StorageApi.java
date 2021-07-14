package com.handy.lib.api;

import com.handy.lib.constants.BaseConstants;
import com.handy.lib.util.SqlManagerUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

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
     *
     * @param plugin 插件
     */
    public static void enableSql(Plugin plugin) {
        File langFile = new File(plugin.getDataFolder(), "storage.yml");
        if (!(langFile.exists())) {
            plugin.saveResource("storage.yml", false);
        }
        BaseConstants.STORAGE_CONFIG = YamlConfiguration.loadConfiguration(langFile);
        // 关闭当前数据源
        SqlManagerUtil.getInstance().close();
        // 初始化连接池
        SqlManagerUtil.getInstance().enableTable(plugin);
    }

}
