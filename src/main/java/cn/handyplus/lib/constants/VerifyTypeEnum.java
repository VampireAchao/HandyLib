package cn.handyplus.lib.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验签类型
 *
 * @author handy
 * @since 2.7.1
 */
@Getter
@AllArgsConstructor
public enum VerifyTypeEnum {
    /**
     * IP
     */
    IP,
    /**
     * MAC
     */
    MAC
}
