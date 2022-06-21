package cn.handyplus.lib.api;

import cn.handyplus.lib.constants.BaseConstants;
import cn.handyplus.lib.util.HandyConfigUtil;

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
        BaseConstants.COLOR_CONFIG = HandyConfigUtil.load("color.yml");
    }

}
