package com.handy.lib.util;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.handy.lib.InitApi;
import com.handy.lib.api.MessageApi;
import com.handy.lib.constants.BaseConstants;
import com.handy.lib.constants.VersionCheckEnum;
import com.handy.lib.core.CollUtil;
import com.handy.lib.core.StrUtil;
import com.handy.lib.param.VerifySignParam;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * 调用http工具类
 *
 * @author handy
 */
public class HandyHttpUtil {
    private final static String VERIFY_SIGN = BaseConstants.IP + "/api/public/verifySign";
    private final static String CLOUD_GET_URL = BaseConstants.IP + "/api/public/getItemName";
    private final static String CLOUD_SET_URL = BaseConstants.IP + "/api/public/setItemName";
    private final static String IP_CHINA_URL = BaseConstants.IP + "/api/public/getIp";

    private final static String URL_1_13 = "https://minecraft-admin.oss-cn-hangzhou.aliyuncs.com/zh_cn/1.13.json";
    private final static String URL_1_14 = "https://minecraft-admin.oss-cn-hangzhou.aliyuncs.com/zh_cn/1.14.json";
    private final static String URL_1_15 = "https://minecraft-admin.oss-cn-hangzhou.aliyuncs.com/zh_cn/1.15.json";
    private final static String URL_1_16 = "https://minecraft-admin.oss-cn-hangzhou.aliyuncs.com/zh_cn/1.16.json";
    private final static String URL_1_17 = "https://minecraft-admin.oss-cn-hangzhou.aliyuncs.com/zh_cn/1.17.json";
    private final static String URL_1_18 = "https://minecraft-admin.oss-cn-hangzhou.aliyuncs.com/zh_cn/1.18.json";

    /**
     * 进行验签
     *
     * @param verifySignParam 参数
     */
    public static void verifySign(VerifySignParam verifySignParam) {
        int port = InitApi.PLUGIN.getServer().getPort();
        // 进行校验
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    HashMap<String, String> paramMap = Maps.newHashMapWithExpectedSize(4);
                    paramMap.put("sign", verifySignParam.getSign());
                    paramMap.put("port", port + "");
                    paramMap.put("pluginName", verifySignParam.getPluginName());
                    paramMap.put("secretKey", verifySignParam.getSecretKey());
                    String result = HttpUtil.get(VERIFY_SIGN, paramMap);
                    if (BaseConstants.TRUE.equals(result)) {
                        BaseConstants.SIGN_VERIFY = true;
                        if (CollUtil.isNotEmpty(verifySignParam.getVerifySignSucceedMsg())) {
                            for (String verifySignSucceedMsg : verifySignParam.getVerifySignSucceedMsg()) {
                                MessageApi.sendConsoleMessage(BaseUtil.replaceChatColor(verifySignSucceedMsg));
                            }
                        }
                    } else {
                        BaseConstants.SIGN_VERIFY = false;
                        if (CollUtil.isNotEmpty(verifySignParam.getVerifySignFailureMsg())) {
                            for (String verifySignFailureMsg : verifySignParam.getVerifySignFailureMsg()) {
                                MessageApi.sendConsoleMessage(BaseUtil.replaceChatColor(verifySignFailureMsg));
                            }
                        }
                    }
                    this.cancel();
                } catch (Exception e) {
                    BaseConstants.SIGN_VERIFY = false;
                    if (CollUtil.isNotEmpty(verifySignParam.getRequestError())) {
                        for (String requestError : verifySignParam.getRequestError()) {
                            MessageApi.sendConsoleMessage(BaseUtil.replaceChatColor(requestError));
                        }
                    }
                    if (verifySignParam.getRetryNumber() < 1) {
                        this.cancel();
                    } else {
                        verifySignParam.setRetryNumber(verifySignParam.getRetryNumber() - 1);
                    }
                }
            }
        }.runTaskTimerAsynchronously(InitApi.PLUGIN, 0, 20 * 60);
    }

    /**
     * 重新进行验签(每小时进行一次校验)
     *
     * @param verifySignParam 参数
     */
    public static void anewVerifySign(VerifySignParam verifySignParam) {
        int port = InitApi.PLUGIN.getServer().getPort();
        // 进行校验
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    HashMap<String, String> paramMap = Maps.newHashMapWithExpectedSize(4);
                    paramMap.put("sign", verifySignParam.getSign());
                    paramMap.put("port", port + "");
                    paramMap.put("pluginName", verifySignParam.getPluginName());
                    paramMap.put("secretKey", verifySignParam.getSecretKey());
                    String result = HttpUtil.get(VERIFY_SIGN, paramMap);
                    BaseConstants.SIGN_VERIFY = BaseConstants.TRUE.equals(result);
                } catch (Exception e) {
                    BaseConstants.SIGN_VERIFY = false;
                }
            }
        }.runTaskTimerAsynchronously(InitApi.PLUGIN, 20 * 60 * 60, 20 * 60 * 60);
    }

    /**
     * 版本检测
     *
     * @param player 玩家
     * @param url    路径
     * @param msg    更新提醒 ${version} 版本变量 ${body} 更新内容变量
     */
    public static void checkVersion(Player player, String url, String msg) {
        if (player != null && !player.isOp()) {
            return;
        }

        // 异步处理
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String result = HttpUtil.get(url);
                    if (StrUtil.isNotEmpty(result)) {
                        JsonObject jsonObject = new Gson().fromJson(result, JsonObject.class);
                        // 当前版本
                        String version = InitApi.PLUGIN.getDescription().getVersion();
                        version = version.replace("-permission", "");
                        // 获取到的信息
                        String tagName = jsonObject.get("tag_name").getAsString();
                        String body = jsonObject.get("body").getAsString();
                        // 版本为最新不进行提醒
                        if (version.equals(tagName)) {
                            return;
                        }
                        String message = ChatColor.GREEN + "检测到最新版本:" + tagName + "更新内容:" + body;
                        if (StrUtil.isNotEmpty(msg)) {
                            message = msg.replace("${version}", tagName).replace("${body}", body);
                        }
                        if (player == null) {
                            MessageApi.sendConsoleMessage(message);
                        } else {
                            player.sendMessage(message);
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        }.runTaskAsynchronously(InitApi.PLUGIN);
    }

    /**
     * 云汉化系统
     */
    public static void getZhCn() {
        // 获取版本
        VersionCheckEnum versionCheckEnum = VersionCheckEnum.getEnum();
        switch (versionCheckEnum) {
            case V_1_7:
            case V_1_8:
            case V_1_9:
            case V_1_10:
            case V_1_11:
            case V_1_12:
                break;
            case V_1_13:
                getCloudZhCn(URL_1_13);
                break;
            case V_1_14:
                getCloudZhCn(URL_1_14);
                break;
            case V_1_15:
                getCloudZhCn(URL_1_15);
                break;
            case V_1_16:
                getCloudZhCn(URL_1_16);
                break;
            case V_1_17:
                getCloudZhCn(URL_1_17);
                break;
            case V_1_18:
                getCloudZhCn(URL_1_18);
                break;
            default:
                getCloudZhCn(URL_1_18);
                break;
        }
    }

    /**
     * 下载云汉化文件
     *
     * @param url 下载路径
     */
    private static void getCloudZhCn(String url) {
        final int[] retryNumber = {6};
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    // 下载文件保存到目录
                    HttpUtil.downloadFile(url, InitApi.PLUGIN.getDataFolder(), "zh_cn.json");
                    File zhChFile = new File(InitApi.PLUGIN.getDataFolder(), "zh_cn.json");
                    if (zhChFile.exists()) {
                        BaseUtil.readJsonFileToJsonCacheMap(zhChFile);
                    }
                    this.cancel();
                } catch (Exception ignored) {
                    if (retryNumber[0] == 0) {
                        this.cancel();
                    } else {
                        retryNumber[0]--;
                    }
                }
            }
        }.runTaskTimerAsynchronously(InitApi.PLUGIN, 0, 20 * 60);
    }

    /**
     * 获取云汉化数据
     *
     * @param version 版本
     */
    public static void setCloudItemJsonCacheMap(String version) {
        final int[] retryNumber = {6};
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    HashMap<String, String> paramMap = Maps.newHashMapWithExpectedSize(1);
                    paramMap.put("version", version);
                    String result = HttpUtil.get(CLOUD_GET_URL, paramMap);
                    if (StrUtil.isNotEmpty(result)) {
                        BaseConstants.CLOUD_ITEM_JSON_CACHE_MAP = new Gson().fromJson(result, new TypeToken<Map<String, String>>() {
                        }.getType());
                    }
                    this.cancel();
                } catch (Exception ignored) {
                    if (retryNumber[0] == 0) {
                        this.cancel();
                    } else {
                        retryNumber[0]--;
                    }
                }
            }
        }.runTaskTimerAsynchronously(InitApi.PLUGIN, 0, 20 * 60);
    }

    /**
     * 同步自定义汉化数据
     */
    public static void setItemName() {
        // 异步处理
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Map<String, String> itemJsonCacheMap = BaseConstants.ITEM_JSON_CACHE_MAP;
                    if (itemJsonCacheMap.size() < 1) {
                        return;
                    }
                    VersionCheckEnum versionCheckEnum = VersionCheckEnum.getEnum();
                    itemJsonCacheMap.put("version", versionCheckEnum.getVersion());
                    HttpUtil.post(CLOUD_SET_URL, new Gson().toJson(itemJsonCacheMap));
                } catch (Exception ignored) {
                }
            }
        }.runTaskAsynchronously(InitApi.PLUGIN);
    }

    /**
     * 获取公网ip
     *
     * @return ip
     */
    public static String getIp() {
        try {
            return HttpUtil.get(IP_CHINA_URL);
        } catch (NoSuchAlgorithmException | IOException | KeyManagementException e) {
            e.printStackTrace();
        }
        return "";
    }

}