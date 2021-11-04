package com.handy.lib.core;

import com.handy.lib.InitApi;
import com.handy.lib.api.MessageApi;
import lombok.SneakyThrows;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * yml工具类
 *
 * @author handy
 * @since 1.8.5
 */
public class YmlUtil<T> {

    /**
     * 文件
     */
    private File file;

    /**
     * 初始化
     *
     * @param fileName 文件名
     */
    public YmlUtil(String fileName) {
        try {
            file = new File(InitApi.PLUGIN.getDataFolder(), fileName + ".yml");
            if (!(file.exists())) {
                boolean newFile = file.createNewFile();
                MessageApi.sendConsoleDebugMessage("创建文件 " + fileName + "结果:" + newFile);
                file = new File(InitApi.PLUGIN.getDataFolder(), fileName + ".yml");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * bean转化为yml
     *
     * @param obj bean
     */
    @SneakyThrows
    public void beanToYml(T obj) {
        Yaml yaml = new Yaml();
        yaml.dump(obj, new FileWriter(file));
    }

    /**
     * yml转化为bean
     *
     * @return bean
     */
    @SneakyThrows
    public T ymlToBean() {
        Yaml yaml = new Yaml();
        return yaml.load(new FileInputStream(file));
    }

}