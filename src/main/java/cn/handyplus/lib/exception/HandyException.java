package cn.handyplus.lib.exception;

/**
 * 自定义异常
 *
 * @author handy
 * @since 2.0.4
 */
public class HandyException extends RuntimeException {
    /**
     * 异常消息
     */
    private String message;

    /**
     * 自定义异常构造
     *
     * @param message 消息
     */
    public HandyException(String message) {
        super(message);
    }

}