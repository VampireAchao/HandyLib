package com.handy.lib.api;

import com.handy.lib.InitApi;
import com.handy.lib.constants.BaseConstants;
import org.bukkit.configuration.file.YamlConfiguration;

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
     */
    public static void enableColor() {
        File colorFile = new File(InitApi.PLUGIN.getDataFolder(), "color.yml");
        if (!(colorFile.exists())) {
            InitApi.PLUGIN.saveResource("color.yml", false);
        }
        BaseConstants.COLOR_CONFIG = YamlConfiguration.loadConfiguration(colorFile);
    }

}
