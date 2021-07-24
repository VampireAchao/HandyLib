package com.handy.lib.core;

import com.handy.lib.InitApi;
import com.handy.lib.api.MessageApi;

import java.math.BigDecimal;

/**
 * 数字计算
 *
 * @author handy
 **/
public class NumberUtil {

    /**
     * double反转成int
     *
     * @param num 小数
     * @return 数字
     * @since 1.3.9
     */
    public static int doubleReverseToInt(double num) {
        if (num == 0) {
            return 0;
        }
        String str = new BigDecimal(num + "").toString();
        MessageApi.sendConsoleDebugMessage(InitApi.PLUGIN, "&a 方法 StrUtil.strReverseToInt 入参: " + str);
        StringBuilder reverse = new StringBuilder(str.replace(".", "")).reverse();
        MessageApi.sendConsoleDebugMessage(InitApi.PLUGIN, "&a 方法 StrUtil.strReverseToInt 出参: " + reverse);
        return Integer.parseInt(reverse.toString());
    }

    /**
     * 余数
     *
     * @param num 数字
     * @return 余数
     * @since 1.3.9
     */
    public static int doubleRemainderToInt(double num) {
        if (num == 0) {
            return 0;
        }
        return (int) (1 / num);
    }

}
