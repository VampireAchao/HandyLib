package com.handy.lib.util;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.handy.lib.constants.BaseConstants;
import com.handy.lib.constants.VersionCheckEnum;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hs
 * @Description: {}
 * @date 2020/7/22 11:15
 */
public class HandyHttpUtil {
    private final static String VERIFY_SIGN = "http://mcadmin.ljxmc.top/api/public/verifySign";

    private final static String CLOUD_GET_URL = "http://mcadmin.ljxmc.top/api/public/getItemName";
    private final static String CLOUD_SET_URL = "http://mcadmin.ljxmc.top/api/public/setItemName";

    private final static String URL_1_13 = "https://minecraft-admin.oss-cn-hangzhou.aliyuncs.com/zh_cn/1.13.json";
    private final static String URL_1_14 = "https://minecraft-admin.oss-cn-hangzhou.aliyuncs.com/zh_cn/1.14.json";
    private final static String URL_1_15 = "https://minecraft-admin.oss-cn-hangzhou.aliyuncs.com/zh_cn/1.15.json";
    private final static String URL_1_16 = "https://minecraft-admin.oss-cn-hangzhou.aliyuncs.com/zh_cn/1.16.json";

    /**
     * 验签
     *
     * @param plugin     插件
     * @param pluginName 插件名
     * @param secretKey  私钥
     * @param sign       签名
     */
    public static void verifySign(Plugin plugin, String pluginName, String secretKey, String sign) {
        int port = plugin.getServer().getPort();
        // 进行校验
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    HashMap<String, String> paramMap = Maps.newHashMapWithExpectedSize(4);
                    paramMap.put("sign", sign);
                    paramMap.put("port", port + "");
                    paramMap.put("pluginName", pluginName);
                    paramMap.put("secretKey", secretKey);
                    String result = HttpUtil.get(VERIFY_SIGN, paramMap);
                    if ("true".equals(result)) {
                        plugin.getLogger().info("§a完全版验证成功,感谢您的使用...");
                        BaseConstants.SIGN = true;
                    } else {
                        plugin.getLogger().info("§a检测到使用的公开版,感谢您的使用...");
                        plugin.getLogger().info("§a完整版无任何限制,请联系作者购买QQ:956812056");
                    }
                    this.cancel();
                } catch (Exception e) {
                    plugin.getLogger().info("§4检测到网络不稳定,一分钟后自动重新验证..");
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, 20 * 60);
    }

    /**
     * 版本检测
     *
     * @param plugin 插件
     * @param player 玩家
     * @param url    路径
     */
    public static void checkVersion(Plugin plugin, Player player, String url) {
        if (player != null && !player.isOp()) {
            return;
        }

        // 异步处理
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String result = HttpUtil.get(url);
                    if (StringUtils.isNotBlank(result)) {
                        JsonObject jsonObject = new Gson().fromJson(result, JsonObject.class);
                        // 当前版本
                        String version = plugin.getDescription().getVersion();
                        // 获取到的信息
                        String tagName = jsonObject.get("tag_name").getAsString();
                        String body = jsonObject.get("body").getAsString();
                        // 版本为最新
                        if (version.equals(tagName)) {
                            plugin.getLogger().info("§a当前版本为最新版本");
                            return;
                        }
                        String message = "§检测到最新版本:" + tagName + "更新内容:" + body;
                        if (player == null) {
                            plugin.getLogger().info(message);
                        } else {
                            player.sendMessage(message);
                        }
                    }
                } catch (Exception e) {
                    String message = "§4网络不稳定,本次启动没有进行版本检查";
                    if (player == null) {
                        plugin.getLogger().info(message);
                    } else {
                        player.sendMessage(message);
                    }
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    /**
     * 云汉化系统
     *
     * @param plugin 插件
     * @return
     */
    public static void getZhCn(Plugin plugin) {
        // 获取版本
        String url = URL_1_15;
        VersionCheckEnum versionCheckEnum = VersionCheckEnum.getEnum();
        switch (versionCheckEnum) {
            case V_1_7:
            case V_1_8:
            case V_1_9:
            case V_1_10:
            case V_1_11:
            case V_1_12:
                setCloudItemJsonCacheMap(plugin, versionCheckEnum.getVersion());
                break;
            case V_1_13:
                url = URL_1_13;
                getCloudZhCn(plugin, url);
                break;
            case V_1_14:
                url = URL_1_14;
                getCloudZhCn(plugin, url);
                break;
            case V_1_15:
                url = URL_1_15;
                getCloudZhCn(plugin, url);
                break;
            case V_1_16:
                url = URL_1_16;
                getCloudZhCn(plugin, url);
                break;
            default:
                break;
        }
    }

    /**
     * 下载云汉化文件
     *
     * @param plugin 插件
     * @param url    下载路径
     */
    private static void getCloudZhCn(Plugin plugin, String url) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    // 下载文件保存到目录
                    HttpUtil.downloadFile(url, plugin.getDataFolder(), "zh_cn.json");
                    File zhChFile = new File(plugin.getDataFolder(), "zh_cn.json");
                    if (zhChFile.exists()) {
                        BaseUtil.readJsonFileToJsonCacheMap(zhChFile);
                        plugin.getLogger().info("§a获取云汉化数据成功...");
                    }
                    this.cancel();
                } catch (Exception e) {
                    plugin.getLogger().info("§4检测到网络不稳定,一分钟后自动重新获取云汉化数据..");
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, 20 * 60);
    }

    /**
     * 获取云汉化数据
     *
     * @param plugin  插件
     * @param version 版本
     */
    public static void setCloudItemJsonCacheMap(Plugin plugin, String version) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    HashMap<String, String> paramMap = Maps.newHashMapWithExpectedSize(1);
                    paramMap.put("version", version);
                    String result = HttpUtil.get(CLOUD_GET_URL, paramMap);
                    if (StringUtils.isNotBlank(result)) {
                        BaseConstants.cloudItemJsonCacheMap = new Gson().fromJson(result, Map.class);
                        plugin.getLogger().info("§获取云汉化数据成功...");
                    }
                    this.cancel();
                } catch (Exception e) {
                    plugin.getLogger().info("§4检测到网络不稳定,一分钟后自动重新获取云汉化数据..");
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, 20 * 60);
    }

    /**
     * 同步自定义汉化数据
     */
    public static void setItemName(Plugin plugin) {
        // 异步处理
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Map<String, String> itemJsonCacheMap = BaseConstants.itemJsonCacheMap;
                    if (itemJsonCacheMap.size() < 1) {
                        return;
                    }
                    VersionCheckEnum versionCheckEnum = VersionCheckEnum.getEnum();
                    itemJsonCacheMap.put("version", versionCheckEnum.getVersion());
                    HttpUtil.post(CLOUD_SET_URL, new Gson().toJson(itemJsonCacheMap));
                } catch (Exception e) {
                    plugin.getLogger().info("§4网络不稳定,本次启动没有进行云汉化同步");
                }
            }
        }.runTaskAsynchronously(plugin);
    }

}