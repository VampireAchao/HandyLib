package cn.handyplus.lib.api;

import cn.handyplus.lib.constants.BaseConstants;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * 语言文件初始化
 *
 * @author handy
 */
public class LangMsgApi {
    private LangMsgApi() {
    }

    /**
     * 初始化语言文件
     *
     * @param langConfig 语言文件
     */
    public static void initLangMsg(FileConfiguration langConfig) {
        BaseConstants.LANG_CONFIG = langConfig;
    }

}
