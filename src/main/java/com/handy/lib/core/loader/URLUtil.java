package com.handy.lib.core.loader;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.jar.JarFile;

/**
 * URL（Uniform Resource Locator）统一资源定位符相关工具类
 *
 * <p>
 * 统一资源定位符，描述了一台特定服务器上某资源的特定位置。
 * </p>
 * URL组成：
 * <pre>
 *   协议://主机名[:端口]/ 路径/[:参数] [?查询]#Fragment
 *   protocol :// hostname[:port] / path / [:parameters][?query]#fragment
 * </pre>
 *
 * @author xiaoleilu
 */
public class URLUtil {
    /**
     * 解码application/x-www-form-urlencoded字符<br>
     * 将%开头的16进制表示的内容解码。
     *
     * @param content URL
     * @param charset 编码
     * @return 解码后的URL
     */
    protected static String decode(String content, String charset) {
        return decode(content, CharsetUtil.charset(charset));
    }

    /**
     * 解码application/x-www-form-urlencoded字符<br>
     * 将%开头的16进制表示的内容解码。
     *
     * @param content 被解码内容
     * @param charset 编码，null表示不解码
     * @return 编码后的字符
     * @since 4.4.1
     */
    protected static String decode(String content, Charset charset) {
        if (null == charset) {
            return content;
        }
        return URLDecoder.decode(content, charset);
    }

    /**
     * 从URL中获取JarFile
     *
     * @param url URL
     * @return JarFile
     * @since 4.1.5
     */
    protected static JarFile getJarFile(URL url) {
        try {
            JarURLConnection urlConnection = (JarURLConnection) url.openConnection();
            return urlConnection.getJarFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}