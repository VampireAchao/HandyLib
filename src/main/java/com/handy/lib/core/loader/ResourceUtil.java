package com.handy.lib.core.loader;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * Resource资源工具类
 *
 * @author Looly
 */
public class ResourceUtil {

    /**
     * 获取指定路径下的资源Iterator<br>
     * 路径格式必须为目录格式,用/分隔，例如:
     *
     * <pre>
     * config/a
     * spring/xml
     * </pre>
     *
     * @param resource 资源路径
     * @return 资源列表
     * @since 4.1.5
     */
    protected static EnumerationIter<URL> getResourceIter(String resource) {
        Enumeration<URL> resources = null;
        try {
            resources = ClassLoaderUtil.getClassLoader().getResources(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new EnumerationIter<>(resources);
    }

}
