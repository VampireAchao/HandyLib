package com.handy.lib.util;

import com.handy.lib.annotation.NoAutowired;
import com.handy.lib.annotation.YamlField;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @author lliiooll
 */
public class YamlHelper {

    public static <T> T readYaml(String fileName, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        return readYaml(new File(fileName), clazz);

    }

    public static <T> T readYaml(File file, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        return readYaml(YamlConfiguration.loadConfiguration(file), clazz);

    }

    public static <T> T readYaml(YamlConfiguration yaml, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        return readYaml(yaml, clazz, "");
    }

    public static <T> T readYaml(YamlConfiguration yaml, Class<T> clazz, String path) throws IllegalAccessException, InstantiationException {
        T o = clazz.newInstance();
        Class<?> q = o.getClass();
        for (Field field : q.getDeclaredFields()) {
            field.setAccessible(true);
            Class<?> c = field.getType();
            if (field.getAnnotation(NoAutowired.class) == null) {
                YamlField f = field.getAnnotation(YamlField.class);
                if ((c.isPrimitive() ||
                        c.equals(String.class) ||
                        c.equals(List.class) ||
                        c.equals(Byte.class) ||
                        c.equals(Long.class) ||
                        c.equals(Double.class) ||
                        c.equals(Float.class) ||
                        c.equals(Character.class) ||
                        c.equals(Short.class) ||
                        c.equals(Boolean.class))) {
                    String p = "".equalsIgnoreCase(path) ? (f == null ? field.getName() : f.name()) : path + "." + (f == null ? field.getName() : f.name());
                    field.set(o, yaml.get(p));

                } else {
                    String p = "".equalsIgnoreCase(path) ? (f == null ? field.getName() : f.name()) : path + "." + (f == null ? field.getName() : f.name());
                    field.set(o, readYaml(yaml, c, p));

                }

            }
        }
        return o;
    }

    public static void writeYaml(String fileName, Object object) {
        try {
            writeYaml(new File(fileName), object);
        } catch (IllegalAccessException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeYaml(File file, Object object) throws IllegalAccessException, IOException {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        writeYaml(yaml, object);
        yaml.save(file);

    }

    public static void writeYaml(YamlConfiguration yaml, Object object) throws IllegalAccessException {
        writeYaml(yaml, object, "");
    }

    private static void writeYaml(YamlConfiguration yaml, Object object, String path) throws IllegalAccessException {
        Class<?> clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Class<?> c = field.getType();
            if (field.getAnnotation(NoAutowired.class) == null) {
                YamlField f = field.getAnnotation(YamlField.class);
                if ((c.isPrimitive() ||
                        c.equals(String.class) ||
                        c.equals(List.class) ||
                        c.equals(Byte.class) ||
                        c.equals(Long.class) ||
                        c.equals(Double.class) ||
                        c.equals(Float.class) ||
                        c.equals(Character.class) ||
                        c.equals(Short.class) ||
                        c.equals(Boolean.class))) {
                    String p = "".equalsIgnoreCase(path) ? (f == null ? field.getName() : f.name()) : path + "." + (f == null ? field.getName() : f.name());
                    yaml.set(p, field.get(object));
                } else {
                    String p = "".equalsIgnoreCase(path) ? (f == null ? field.getName() : f.name()) : path + "." + (f == null ? field.getName() : f.name());
                    writeYaml(yaml, field.get(object), p);

                }
            }
        }
    }

}
