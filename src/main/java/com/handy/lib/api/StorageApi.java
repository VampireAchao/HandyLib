package com.handy.lib.api;

import com.handy.lib.util.SqlManagerUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

/**
 * sql
 *
 * @author hs
 * @date 2020/7/30 11:40
 */
public class StorageApi {
    private StorageApi() {
    }

    public static FileConfiguration storageConfig;

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
        storageConfig = YamlConfiguration.loadConfiguration(langFile);

        // 初始化连接池
        SqlManagerUtil.getInstance().enableTable(plugin);
    }

}
