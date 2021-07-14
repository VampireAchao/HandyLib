package com.handy.lib.api;

import com.handy.lib.constants.BaseConstants;
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
        BaseConstants.COLOR_CONFIG = YamlConfiguration.loadConfiguration(colorFile);
    }

}
