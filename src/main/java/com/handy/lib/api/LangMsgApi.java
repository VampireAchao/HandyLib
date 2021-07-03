package com.handy.lib.api;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * 语言文件初始化
 *
 * @author handy
 */
public class LangMsgApi {
    public static FileConfiguration LANG_CONFIG;

    private LangMsgApi() {
    }

    /**
     * 初始化语言文件
     *
     * @param langConfig 语言文件
     */
    public static void initLangMsg(FileConfiguration langConfig) {
        LANG_CONFIG = langConfig;
    }

}
