package cn.handyplus.lib.util;

import com.google.common.collect.Maps;
import cn.handyplus.lib.InitApi;
import cn.handyplus.lib.api.MessageApi;
import cn.handyplus.lib.constants.BaseConstants;
import cn.handyplus.lib.constants.VerifyTypeEnum;
import cn.handyplus.lib.constants.VersionCheckEnum;
import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.core.JsonUtil;
import cn.handyplus.lib.core.NetUtil;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.param.VerifySignParam;
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
    private final static String URL_1_19 = "https://minecraft-admin.oss-cn-hangzhou.aliyuncs.com/zh_cn/1.19.json";

    /**
     * 签名验证
     *
     * @param verifySignParam 参数
     * @since 2.7.1
     */
    public static void signVerify(VerifySignParam verifySignParam) {
        final int[] retryNumber = {6};
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    HashMap<String, String> paramMap = new HashMap<>();
                    paramMap.put("sign", verifySignParam.getSign());
                    paramMap.put("port", InitApi.PLUGIN.getServer().getPort() + "");
                    paramMap.put("pluginName", verifySignParam.getPluginName());
                    paramMap.put("secretKey", verifySignParam.getSecretKey());
                    // 判断是否mac验证
                    if (VerifyTypeEnum.MAC.equals(BaseConstants.VERIFY_TYPE)) {
                        paramMap.put("mac", NetUtil.getLocalMacAddress());
                    }
                    String result = HttpUtil.get(VERIFY_SIGN, paramMap);
                    if (BaseConstants.TRUE.equals(result)) {
                        BaseUtil.PERMISSION = true;
                        if (CollUtil.isNotEmpty(verifySignParam.getVerifySignSucceedMsg())) {
                            for (String verifySignSucceedMsg : verifySignParam.getVerifySignSucceedMsg()) {
                                MessageApi.sendConsoleMessage(BaseUtil.replaceChatColor(verifySignSucceedMsg));
                            }
                        }
                    } else {
                        BaseUtil.PERMISSION = false;
                        if (CollUtil.isNotEmpty(verifySignParam.getVerifySignFailureMsg())) {
                            for (String verifySignFailureMsg : verifySignParam.getVerifySignFailureMsg()) {
                                MessageApi.sendConsoleMessage(BaseUtil.replaceChatColor(verifySignFailureMsg));
                            }
                        }
                    }
                    this.cancel();
                } catch (Exception e) {
                    BaseUtil.PERMISSION = false;
                    if (CollUtil.isNotEmpty(verifySignParam.getRequestError())) {
                        for (String requestError : verifySignParam.getRequestError()) {
                            MessageApi.sendConsoleMessage(BaseUtil.replaceChatColor(requestError));
                        }
                    }
                    if (BaseConstants.DEBUG) {
                        e.printStackTrace();
                    }
                    retryNumber[0]--;
                    if (retryNumber[0] < 1) {
                        this.cancel();
                    }
                }
            }
        }.runTaskTimerAsynchronously(InitApi.PLUGIN, 20, 20 * 60);
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
                String oneMsg = ChatColor.GRAY + "_________________/ &e" + InitApi.PLUGIN.getDescription().getName() + ChatColor.GRAY + " \\_________________\n";
                TextComponent message = TextUtil.getInstance().init(oneMsg).build();
                String twoMsg = "&7| &a最新版本: &d" + tagName + " &a当前版本: &d" + version + " &a点击&d&n此处查看&a更新内容 &7|\n";
                message.addExtra(TextUtil.getInstance().init("     " + twoMsg).addClickUrl(InitApi.PLUGIN.getDescription().getWebsite()).build());
                message.addExtra(TextUtil.getInstance().init(ChatColor.GRAY + "-----------------------------------------------").build());
                if (player == null) {
                    MessageApi.sendConsoleMessage(twoMsg);
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
            case V_1_19:
                getCloudZhCn(URL_1_19);
                break;
            default:
                getCloudZhCn(URL_1_19);
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
                    retryNumber[0]--;
                    if (retryNumber[0] < 1) {
                        this.cancel();
                    }
                }
            }
        }.runTaskTimerAsynchronously(InitApi.PLUGIN, 20 * 2, 20 * 60);
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
                        BaseConstants.CLOUD_ITEM_JSON_CACHE_MAP = JsonUtil.toMap(result);
                    }
                    this.cancel();
                } catch (Throwable ignored) {
                    retryNumber[0]--;
                    if (retryNumber[0] < 1) {
                        this.cancel();
                    }
                }
            }
        }.runTaskTimerAsynchronously(InitApi.PLUGIN, 20 * 3, 20 * 60);
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
                    HttpUtil.post(CLOUD_SET_URL, JsonUtil.toJson(itemJsonCacheMap));
                } catch (Exception ignored) {
                }
            }
        }.runTaskLaterAsynchronously(InitApi.PLUGIN, 20 * 60);
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