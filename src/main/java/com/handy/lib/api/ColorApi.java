package com.handy.lib.api;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

/**
 * RGB颜色
 *
 * @author handy
 **/
public class ColorApi {
    private ColorApi() {
    }

    public static FileConfiguration colorConfig;

    /**
     * 加载Color文件
     *
     * @param plugin 插件
     */
    public static void enableColor(Plugin plugin) {
        File colorFile = new File(plugin.getDataFolder(), "color.yml");
        if (!(colorFile.exists())) {
            plugin.saveResource("color.yml", false);
        }
        colorConfig = YamlConfiguration.loadConfiguration(colorFile);
    }

}
