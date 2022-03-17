package com.handy.lib.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;

/**
 * mc版本
 *
 * @author handy
 */
@Getter
@AllArgsConstructor
public enum VersionCheckEnum {
    /**
     * 版本
     */
    V_1_6("1.6", 6),
    V_1_7("1.7", 7),
    V_1_8("1.8", 8),
    V_1_9("1.9", 9),
    V_1_10("1.10", 10),
    V_1_11("1.11", 11),
    V_1_12("1.12", 12),
    V_1_13("1.13", 13),
    V_1_14("1.14", 14),
    V_1_15("1.15", 15),
    V_1_16("1.16", 16),
    V_1_17("1.17", 17),
    V_1_18("1.18", 18),
    ;

    private final String version;
    private final Integer versionId;

    /**
     * 获取当前版本枚举
     * 未知版本默认会返回最新版
     *
     * @return 当前版本VersionCheckEnum
     */
    public static VersionCheckEnum getEnum() {
        String version = Bukkit.getVersion();
        version = version.substring(version.indexOf(":") + 2);
        for (VersionCheckEnum versionCheckEnum : VersionCheckEnum.values()) {
            if (version.contains(versionCheckEnum.getVersion())) {
                return versionCheckEnum;
            }
        }
        return VersionCheckEnum.V_1_18;
    }

}