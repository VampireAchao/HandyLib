package com.handy.lib.core;

import lombok.SneakyThrows;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 类加载器
 *
 * @author handy
 * @since 1.2.5
 */
public class ClassUtil {

    private final static String CLASS = ".class";
    private final File FILE;
    private final ClassLoader CLASS_LOADER;

    public ClassUtil(Plugin plugin) {
        this.CLASS_LOADER = plugin.getClass().getClassLoader();
        try {
            FILE = new File(URLDecoder.decode(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), StandardCharsets.UTF_8.toString()));
        } catch (UnsupportedEncodingException ignored) {
            throw new NullPointerException("加载异常...");
        }
    }

    /**
     * 扫描注解下的包
     *
     * @param packageName 包名
     * @param annotation  注解
     * @return 类集合
     */
    @SneakyThrows
    public List<Class<?>> getClassByAnnotation(String packageName, Class<? extends Annotation> annotation) {
        List<Class<?>> classList = new ArrayList<>();
        URL jar = FILE.toURI().toURL();
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{jar}, CLASS_LOADER);
        JarInputStream jarInputStream = new JarInputStream(jar.openStream());
        while (true) {
            JarEntry nextJarEntry = jarInputStream.getNextJarEntry();
            if (nextJarEntry == null) {
                break;
            }
            String name = getName(nextJarEntry);
            if (name == null) {
                continue;
            }
            if (name.startsWith(packageName)) {
                String cname = name.substring(0, name.lastIndexOf(CLASS));
                Class<?> loadClass = urlClassLoader.loadClass(cname);
                // 判断是否有对应注解
                if (loadClass.isAnnotationPresent(annotation)) {
                    classList.add(loadClass);
                }
            }
        }
        return classList;
    }

    /**
     * 扫描注解下的方法
     *
     * @param packageName 包名
     * @param annotation  注解
     * @return 类集合
     */
    @SneakyThrows
    public Map<Class<?>, List<Method>> getMethodByAnnotation(String packageName, Class<? extends Annotation> annotation) {
        Map<Class<?>, List<Method>> map = new HashMap<>();
        URL jar = FILE.toURI().toURL();
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{jar}, CLASS_LOADER);
        JarInputStream jarInputStream = new JarInputStream(jar.openStream());
        while (true) {
            JarEntry nextJarEntry = jarInputStream.getNextJarEntry();
            if (nextJarEntry == null) {
                break;
            }
            String name = getName(nextJarEntry);
            if (name == null) {
                continue;
            }
            if (name.startsWith(packageName)) {
                List<Method> methods = new ArrayList<>();
                String cname = name.substring(0, name.lastIndexOf(CLASS));
                Class<?> loadClass = urlClassLoader.loadClass(cname);
                Method[] declaredMethods = loadClass.getDeclaredMethods();
                List<Method> subCommandMethods = Stream.of(declaredMethods).filter(method -> method.isAnnotationPresent(annotation)).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(subCommandMethods)) {
                    methods.addAll(subCommandMethods);
                }
                map.put(loadClass, methods);
            }
        }
        return map;
    }

    /**
     * 扫描对应包名下接口或者父类的子类
     *
     * @param packageName 包名
     * @param clazz       父类
     * @param <T>         泛型
     * @return 类集合
     */
    @SneakyThrows
    @SuppressWarnings(value = {"unchecked", "rawtypes"})
    public <T> List<Class<T>> getClassByIsAssignableFrom(String packageName, Class<? extends T> clazz) {
        List<Class<T>> classList = new ArrayList<>();
        URL jar = FILE.toURI().toURL();
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{jar}, CLASS_LOADER);
        JarInputStream jarInputStream = new JarInputStream(jar.openStream());
        while (true) {
            JarEntry nextJarEntry = jarInputStream.getNextJarEntry();
            if (nextJarEntry == null) {
                break;
            }
            String name = getName(nextJarEntry);
            if (name == null) {
                continue;
            }
            if (name.startsWith(packageName)) {
                String cname = name.substring(0, name.lastIndexOf(CLASS));
                Class loadClass = urlClassLoader.loadClass(cname);
                if (clazz.isAssignableFrom(loadClass)) {
                    classList.add(loadClass);
                }
            }
        }
        return classList;
    }

    /**
     * 获取名称
     *
     * @param jarEntry jar
     * @return 名称
     */
    private String getName(JarEntry jarEntry) {
        String name = jarEntry.getName();
        if (name.isEmpty()) {
            return null;
        }
        if (!name.endsWith(CLASS)) {
            return null;
        }
        return name.replace("/", ".");
    }

}