package com.handy.lib.util;

import com.handy.lib.InitApi;
import com.handy.lib.api.MessageApi;
import com.handy.lib.constants.BaseConstants;
import com.handy.lib.constants.VerifyTypeEnum;
import com.handy.lib.constants.VersionCheckEnum;
import com.handy.lib.core.CollUtil;
import com.handy.lib.core.StrUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 配置管理类
 *
 * @author handy
 * @since 2.8.2
 */
public class HandyConfigUtil {

    /**
     * 加载config
     *
     * @return config
     */
    public static FileConfiguration loadConfig() {
        // 1:没有文件夹就创建
        if (!InitApi.PLUGIN.getDataFolder().exists()) {
            boolean mkdir = InitApi.PLUGIN.getDataFolder().mkdir();
            MessageApi.sendConsoleDebugMessage("创建config：" + mkdir);
        }
        // 2:查询config没有就读取jar中的
        File configFile = new File(InitApi.PLUGIN.getDataFolder(), "config.yml");
        if (!(configFile.exists())) {
            InitApi.PLUGIN.saveDefaultConfig();
        }
        // 读取信息
        InitApi.PLUGIN.reloadConfig();
        // 加载config
        FileConfiguration config = InitApi.PLUGIN.getConfig();
        // 扩展信息赋值
        BaseConstants.DEBUG = config.getBoolean("debug");
        String ip = config.getString("ip");
        if (StrUtil.isNotEmpty(ip)) {
            BaseConstants.IP = ip;
        }
        String verifyType = config.getString("signType");
        if (StrUtil.isNotEmpty(verifyType) && "ip".equals(verifyType)) {
            BaseConstants.VERIFY_TYPE = VerifyTypeEnum.IP;
        }
        return config;
    }

    /**
     * 加载文件
     *
     * @param child 文件路径
     * @return fileConfiguration 文件
     */
    public static FileConfiguration load(String child) {
        File langFile = new File(InitApi.PLUGIN.getDataFolder(), child);
        if (!(langFile.exists())) {
            InitApi.PLUGIN.saveResource(child, false);
        }
        return YamlConfiguration.loadConfiguration(langFile);
    }

    /**
     * 设置节点
     *
     * @param fileConfiguration 文件
     * @param path              yml节点
     * @param value             内容
     */
    public static void setPath(FileConfiguration fileConfiguration, String path, Object value) {
        setPath(fileConfiguration, path, value, null);
    }

    /**
     * 设置节点
     *
     * @param fileConfiguration 文件
     * @param path              yml节点
     * @param value             内容
     * @param comments          注释
     */
    public static void setPath(FileConfiguration fileConfiguration, String path, Object value, List<String> comments) {
        try {
            fileConfiguration.set(path, value);
            if (CollUtil.isNotEmpty(comments) && VersionCheckEnum.getEnum().getVersionId() >= VersionCheckEnum.V_1_18.getVersionId()) {
                fileConfiguration.setComments(path, comments);
            }
            fileConfiguration.save(new File(InitApi.PLUGIN.getDataFolder(), fileConfiguration.getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}