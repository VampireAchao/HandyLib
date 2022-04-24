package com.handy.lib.api;

import com.handy.lib.constants.BaseConstants;
import com.handy.lib.util.HandyConfigUtil;

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
