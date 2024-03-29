package cn.handyplus.lib.param;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 完全版校验参数
 *
 * @author handy
 */
@Data
public class VerifySignParam implements Serializable {
    private static final long serialVersionUID = 4515557663680173392L;
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
}