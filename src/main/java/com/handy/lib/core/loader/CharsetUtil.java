package com.handy.lib.core.loader;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

/**
 * 字符集工具类
 *
 * @author xiaoleilu
 */
public class CharsetUtil {

    /**
     * GBK
     */
    protected static final String GBK = "GBK";

    /**
     * UTF-8
     */
    protected static final Charset CHARSET_UTF_8 = StandardCharsets.UTF_8;
    /**
     * GBK
     */
    protected static final Charset CHARSET_GBK;

    static {
        //避免不支持GBK的系统中运行报错 issue#731
        Charset _CHARSET_GBK = null;
        try {
            _CHARSET_GBK = Charset.forName(GBK);
        } catch (UnsupportedCharsetException e) {
            //ignore
        }
        CHARSET_GBK = _CHARSET_GBK;
    }

    /**
     * 转换为Charset对象
     *
     * @param charsetName 字符集，为空则返回默认字符集
     * @return Charset
     * @throws UnsupportedCharsetException 编码不支持
     */
    protected static Charset charset(String charsetName) throws UnsupportedCharsetException {
        return ClassStrUtil.isBlank(charsetName) ? Charset.defaultCharset() : Charset.forName(charsetName);
    }

    /**
     * 系统字符集编码，如果是Windows，则默认为GBK编码，否则取 {@link CharsetUtil#defaultCharsetName()}
     *
     * @return 系统字符集编码
     * @since 3.1.2
     */
    protected static String systemCharsetName() {
        return systemCharset().name();
    }

    /**
     * 系统字符集编码，如果是Windows，则默认为GBK编码，否则取 {@link CharsetUtil#defaultCharsetName()}
     *
     * @return 系统字符集编码
     * @since 3.1.2
     */
    protected static Charset systemCharset() {
        return FileUtil.isWindows() ? CHARSET_GBK : defaultCharset();
    }

    /**
     * 系统默认字符集编码
     *
     * @return 系统字符集编码
     */
    protected static Charset defaultCharset() {
        return Charset.defaultCharset();
    }
}
