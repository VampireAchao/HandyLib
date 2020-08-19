package com.handy.lib.util;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hs
 * @Description: {}
 * @date 2020/3/31 9:58
 */
public final class BukkitObjectSerializerUtil {

    private BukkitObjectSerializerUtil() {
    }

    /**
     * 单对象序列化为字符串
     *
     * @param object 对象
     * @return 字符串
     */
    public static String singleObjectToString(Object object) {
        byte[] raw = null;
        try {
            raw = singleObjectToByteArray(object);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (raw != null) {
            return Base64Coder.encodeLines(raw);
        }

        return null;
    }

    /**
     * 单对象序列化为字节数组
     *
     * @param object 对象
     * @return 字节数组
     * @throws IOException
     */
    public static byte[] singleObjectToByteArray(Object object) throws IOException {
        if (object instanceof ConfigurationSerializable || object instanceof Serializable) {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(buf);

            out.writeObject(object);

            out.close();
            return buf.toByteArray();
        }

        return null;
    }

    /**
     * 集合对象序列化为字符串
     *
     * @param objects 对象
     * @return 字符串
     */
    public static String collectionToString(Collection<Object> objects) {
        byte[] raw = null;
        try {
            raw = collectionToByteArray(objects);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (raw != null) {
            return Base64Coder.encodeLines(raw);
        }
        return null;
    }

    /**
     * 集合对象序列化为字节数组
     *
     * @param objects 对象
     * @return 字节数组
     * @throws IOException
     */
    public static byte[] collectionToByteArray(Collection<Object> objects) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        BukkitObjectOutputStream out = new BukkitObjectOutputStream(buf);
        List<Object> compatible = objects.stream()
                .filter(object -> (object instanceof ConfigurationSerializable || object instanceof Serializable))
                .collect(Collectors.toList());

        out.writeInt(compatible.size());

        for (Object object : compatible) {
            out.writeObject(object);
        }

        out.close();
        return buf.toByteArray();
    }

    /**
     * 字符串反序列化为实例对象
     *
     * @param serialized 字符串
     * @param classOfT   字符串所表示的对象的类
     * @return 实例对象
     */
    public static <T> T singleObjectFromString(String serialized, Class<T> classOfT) {
        try {
            return singleObjectFromByteArray(Base64Coder.decodeLines(serialized), classOfT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字节数组反序列化为实例对象
     *
     * @param serialized 字节数组
     * @param classOfT   字节数组所表示的对象的类
     * @return 实例对象
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static <T> T singleObjectFromByteArray(byte[] serialized, Class<T> classOfT) throws IOException {
        ByteArrayInputStream buf = new ByteArrayInputStream(serialized);
        BukkitObjectInputStream in = new BukkitObjectInputStream(buf);
        T object = null;

        try {
            object = (T) in.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        } finally {
            in.close();
        }

        return object;
    }

    /**
     * 字符串反序列化为实例对象集合
     *
     * @param serialized 字符串
     * @param classOfC   集合类型
     * @param classOfT   字符串所表示的对象的类
     * @return 实例对象集合
     * @throws IOException
     */
    public static <T, C extends Collection<T>> C collectionFromString(String serialized, Class<C> classOfC, Class<T> classOfT) throws IOException {
        return collectionFromByteArray(Base64Coder.decodeLines(serialized), classOfC, classOfT);
    }

    /**
     * 字节数组反序列化为实例对象集合
     *
     * @param serialized 字节数组
     * @param classOfC   集合类型
     * @param classOfT   字节数组所表示的对象的类
     * @return 实例对象集合
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static <T, C extends Collection<T>> C collectionFromByteArray(byte[] serialized, Class<C> classOfC, Class<T> classOfT) throws IOException {
        C objects = null;

        try {
            objects = classOfC.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new IOException("Unable to instantiate collection.", e);
        }

        ByteArrayInputStream buf = new ByteArrayInputStream(serialized);
        BukkitObjectInputStream in = new BukkitObjectInputStream(buf);

        int count = in.readInt();

        try {
            for (int i = 0; i < count; i++) {
                objects.add((T) in.readObject());
            }
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        } finally {
            in.close();
        }
        return objects;
    }
}
