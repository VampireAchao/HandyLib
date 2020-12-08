package com.handy.lib.api;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author hs
 * @date 2020/8/1 10:30
 */
public class LangMsgApi {
    public static FileConfiguration LANG_CONFIG;

    private LangMsgApi() {
    }

    /**
     * 初始化语言文件
     */
    public static void initLangMsg(FileConfiguration langConfig) {
        LANG_CONFIG = langConfig;
    }

}
