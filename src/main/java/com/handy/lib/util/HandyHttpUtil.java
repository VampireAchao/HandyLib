package com.handy.lib.util;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.handy.lib.InitApi;
import com.handy.lib.api.MessageApi;
import com.handy.lib.constants.BaseConstants;
import com.handy.lib.constants.VersionCheckEnum;
import com.handy.lib.core.CollUtil;
import com.handy.lib.core.NetUtil;
import com.handy.lib.core.StrUtil;
import com.handy.lib.param.VerifySignParam;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
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
        // 进行校验
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    HashMap<String, String> paramMap = Maps.newHashMapWithExpectedSize(4);
                    paramMap.put("sign", verifySignParam.getSign());
                    paramMap.put("port", InitApi.PLUGIN.getServer().getPort() + "");
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
                    if (BaseConstants.DEBUG) {
                        e.printStackTrace();
                    }
                }
            }
        }.runTaskAsynchronously(InitApi.PLUGIN);
    }

    /**
     * 使用mac地址进行验签
     *
     * @param verifySignParam 参数
     * @since 2.3.8
     */
    public static void macVerifySign(VerifySignParam verifySignParam) {
        // 进行校验
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    HashMap<String, String> paramMap = Maps.newHashMapWithExpectedSize(5);
                    paramMap.put("sign", verifySignParam.getSign());
                    paramMap.put("mac", NetUtil.getLocalMacAddress());
                    paramMap.put("port", InitApi.PLUGIN.getServer().getPort() + "");
                    paramMap.put("pluginName", verifySignParam.getPluginName());
                    paramMap.put("secretKey", verifySignParam.getSecretKey());
                    String result = HttpUtil.get(VERIFY_SIGN, paramMap);
                    if (BaseConstants.TRUE.equals(result)) {
                        BaseConstants.MAC_SIGN_VERIFY = true;
                        if (CollUtil.isNotEmpty(verifySignParam.getVerifySignSucceedMsg())) {
                            for (String verifySignSucceedMsg : verifySignParam.getVerifySignSucceedMsg()) {
                                MessageApi.sendConsoleMessage(BaseUtil.replaceChatColor(verifySignSucceedMsg));
                            }
                        }
                    } else {
                        BaseConstants.MAC_SIGN_VERIFY = false;
                        if (CollUtil.isNotEmpty(verifySignParam.getVerifySignFailureMsg())) {
                            for (String verifySignFailureMsg : verifySignParam.getVerifySignFailureMsg()) {
                                MessageApi.sendConsoleMessage(BaseUtil.replaceChatColor(verifySignFailureMsg));
                            }
                        }
                    }
                    this.cancel();
                } catch (Exception e) {
                    BaseConstants.MAC_SIGN_VERIFY = false;
                    if (CollUtil.isNotEmpty(verifySignParam.getRequestError())) {
                        for (String requestError : verifySignParam.getRequestError()) {
                            MessageApi.sendConsoleMessage(BaseUtil.replaceChatColor(requestError));
                        }
                    }
                    if (BaseConstants.DEBUG) {
                        e.printStackTrace();
                    }
                }
            }
        }.runTaskAsynchronously(InitApi.PLUGIN);
    }

    /**
     * 版本检测
     *
     * @param player 玩家
     * @param url    路径
     */
    public static void checkVersion(Player player, String url) {
        if (player != null && !player.isOp()) {
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(InitApi.PLUGIN, () -> {
            String version = InitApi.PLUGIN.getDescription().getVersion();
            String tagName = BaseUtil.getOfficialVersion(url);
            if (tagName != null && BaseUtil.convertVersion(tagName) > BaseUtil.convertVersion(version)) {
                String oneMsg = ChatColor.GRAY + "_________________/ " + InitApi.PLUGIN.getDescription().getName() + " \\_________________\n";
                TextComponent message = new TextComponent(oneMsg);
                String twoMsg = "&a| &d" + tagName + "&a是最新版本! 当前版本: &d" + version + " &a|";
                TextComponent content = new TextComponent(twoMsg);
                content.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, InitApi.PLUGIN.getDescription().getWebsite()));
                String threeMsg = "ChatColor.GRAY + ---------------------------------------- ";
                TextComponent endMessage = new TextComponent(threeMsg);
                message.addExtra(content);
                message.addExtra(endMessage);
                if (player == null) {
                    MessageApi.sendConsoleMessage(oneMsg);
                    MessageApi.sendConsoleMessage(twoMsg);
                    MessageApi.sendConsoleMessage(threeMsg);
                } else {
                    MessageApi.sendMessage(player, message);
                }
            }
        });
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
                } catch (Throwable ignored) {
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