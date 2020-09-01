package com.handy.lib.param;

import lombok.Data;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.List;

/**
 * @author hs
 * @Description: {}
 * @date 2020/9/1 16:43
 */
@Data
public class VerifySignParam {
    /**
     * 插件
     */
    private Plugin plugin;
    /**
     * 插件名
     */
    private String pluginName;
    /**
     * 私钥
     */
    private String secretKey;
    /**
     * 签名
     */
    private String sign;

    /**
     * 验证成功消息
     */
    private List<String> verifySignSucceedMsg;

    /**
     * 验证失败消息
     */
    private List<String> verifySignFailureMsg;

    /**
     * 请求错误消息
     */
    private List<String> requestError = Collections.singletonList("§4检测到网络不稳定,一分钟后自动重新验证..");

    /**
     * 重试次数
     */
    private int retryNumber = 6;
}
