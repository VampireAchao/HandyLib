package com.handy.lib.core.loader;

/**
 * 字符工具类<br>
 * 部分工具来自于Apache Commons系列
 *
 * @author looly
 * @since 4.0.1
 */
public class CharUtil {
    /**
     * 字符常量：斜杠 {@code '/'}
     */
    protected static final char SLASH = '/';

    /**
     * 字符转为字符串<br>
     * 如果为ASCII字符，使用缓存
     *
     * @param c 字符
     * @return 字符串
     * @see ASCIIStrCache#toString(char)
     */
    protected static String toString(char c) {
        return ASCIIStrCache.toString(c);
    }

    /**
     * 获取给定字符的16进制数值
     *
     * @param b 字符
     * @return 16进制字符
     * @since 5.3.1
     */
    protected static int digit16(int b) {
        return Character.digit(b, 16);
    }

    /**
     * 是否空白符<br>
     * 空白符包括空格、制表符、全角空格和不间断空格<br>
     *
     * @param c 字符
     * @return 是否空白符
     * @see Character#isWhitespace(int)
     * @see Character#isSpaceChar(int)
     * @since 4.0.10
     */
    protected static boolean isBlankChar(char c) {
        return isBlankChar((int) c);
    }

    /**
     * 是否空白符<br>
     * 空白符包括空格、制表符、全角空格和不间断空格<br>
     *
     * @param c 字符
     * @return 是否空白符
     * @see Character#isWhitespace(int)
     * @see Character#isSpaceChar(int)
     * @since 4.0.10
     */
    protected static boolean isBlankChar(int c) {
        return Character.isWhitespace(c)
                || Character.isSpaceChar(c)
                || c == '\ufeff'
                || c == '\u202a';
    }
}
