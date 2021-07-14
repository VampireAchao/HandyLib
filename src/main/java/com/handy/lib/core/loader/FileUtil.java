package com.handy.lib.core.loader;

import java.io.File;

/**
 * 文件工具类
 *
 * @author looly
 */
public class FileUtil {

    /**
     * 是否为Windows环境
     *
     * @return 是否为Windows环境
     * @since 3.0.9
     */
    protected static boolean isWindows() {
        return '\\' == File.separatorChar;
    }
}
