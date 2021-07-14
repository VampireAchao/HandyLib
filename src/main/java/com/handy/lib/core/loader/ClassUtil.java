package com.handy.lib.core.loader;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * 类工具类 <br>
 *
 * @author xiaoleilu
 */
public class ClassUtil {
    /**
     * 指定类是否与给定的类名相同
     *
     * @param clazz      类
     * @param className  类名，可以是全类名（包含包名），也可以是简单类名（不包含包名）
     * @param ignoreCase 是否忽略大小写
     * @return 指定类是否与给定的类名相同
     * @since 3.0.7
     */
    protected static boolean equals(Class<?> clazz, String className, boolean ignoreCase) {
        if (null == clazz || ClassStrUtil.isBlank(className)) {
            return false;
        }
        if (ignoreCase) {
            return className.equalsIgnoreCase(clazz.getName()) || className.equalsIgnoreCase(clazz.getSimpleName());
        } else {
            return className.equals(clazz.getName()) || className.equals(clazz.getSimpleName());
        }
    }

    /**
     * 扫描指定包路径下所有包含指定注解的类
     *
     * @param packageName     包路径
     * @param annotationClass 注解类
     * @return 类集合
     * @see ClassScanner#scanPackageByAnnotation(String, Class)
     */
    public static Set<Class<?>> scanPackageByAnnotation(String packageName, final Class<? extends Annotation> annotationClass) {
        return ClassScanner.scanPackageByAnnotation(packageName, annotationClass);
    }

    /**
     * 扫描指定包路径下所有指定类或接口的子类或实现类
     *
     * @param packageName 包路径
     * @param superClass  父类或接口
     * @return 类集合
     * @see ClassScanner#scanPackageBySuper(String, Class)
     */
    public static Set<Class<?>> scanPackageBySuper(String packageName, final Class<?> superClass) {
        return ClassScanner.scanPackageBySuper(packageName, superClass);
    }

    /**
     * @return 获得Java ClassPath路径，不包括 jre
     */
    protected static String[] getJavaClassPaths() {
        return System.getProperty("java.class.path").split(System.getProperty("path.separator"));
    }

}