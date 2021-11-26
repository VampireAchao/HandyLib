package com.handy.lib.util;

import com.handy.lib.core.CollUtil;
import com.handy.lib.core.StrUtil;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * MythicMob 旧版本工具类
 *
 * @author handy
 * @since 1.8.3
 */
public class MythicMobOldUtil {
    private MythicMobOldUtil() {
    }

    private static final MythicMobOldUtil INSTANCE = new MythicMobOldUtil();

    public static MythicMobOldUtil getInstance() {
        return INSTANCE;
    }

    /**
     * 获取mm map
     *
     * @param pageNum 页数
     * @return key: displayName value: internalName
     */
    public Map<String, String> getMythicMobMap(Integer pageNum) {
        Map<String, String> map = new LinkedHashMap<>();
        List<MythicMob> mythicMobs = this.getMythicMobs(pageNum);
        if (CollUtil.isNotEmpty(mythicMobs)) {
            for (MythicMob mythicMob : mythicMobs) {
                map.put(mythicMob.getDisplayName() != null ? mythicMob.getDisplayName() : mythicMob.getInternalName(), mythicMob.getInternalName());
            }
        }
        return map;
    }

    /**
     * 获取mm怪物
     *
     * @param pageNum 页数
     * @return mm怪物
     */
    private List<MythicMob> getMythicMobs(Integer pageNum) {
        Collection<MythicMob> mythicMobs = MythicMobs.inst().getMobManager().getMobTypes();
        Stream<MythicMob> limit = mythicMobs.stream().skip(pageNum * 45L).limit(45);
        return limit.collect(Collectors.toList());
    }

    /**
     * 获取单个怪物名称
     *
     * @param internalName 内部名称
     * @return 怪物名称
     */
    public String getMythicMobName(String internalName) {
        MythicMob mythicMob = MythicMobs.inst().getMobManager().getMythicMob(internalName);
        if (mythicMob == null) {
            return internalName;
        }
        String mythicMobName = mythicMob.getDisplayName();
        if (StrUtil.isEmpty(mythicMobName)) {
            return internalName;
        }
        return mythicMobName;
    }

    /**
     * 获取mm怪物数量
     *
     * @return 怪物数量
     */
    public Integer getMythicMobsCount() {
        Collection<MythicMob> mythicMobs = MythicMobs.inst().getMobManager().getMobTypes();
        return mythicMobs.size();
    }

}