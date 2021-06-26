package com.handy.lib.util;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 低于4.7的mm工具类
 *
 * @author handy
 * @since 1.1.5
 */
public class MythicMobOldUtil {
    private MythicMobOldUtil() {
    }

    private static class SingletonHolder {
        private static final MythicMobOldUtil INSTANCE = new MythicMobOldUtil();
    }

    public static MythicMobOldUtil getInstance() {
        return MythicMobOldUtil.SingletonHolder.INSTANCE;
    }

    /**
     * 获取mm怪物
     *
     * @param pageNum 页数
     * @return mm怪物
     */
    public List<MythicMob> getMythicMobs(Integer pageNum) {
        Collection<MythicMob> mythicMobs = MythicMobs.inst().getMobManager().getMobTypes();
        Stream<MythicMob> limit = mythicMobs.stream().skip(pageNum * 45L).limit(45);
        return limit.collect(Collectors.toList());
    }

    /**
     * 获取单个怪物信息
     *
     * @param internalName 内部名称
     * @return 怪物信息
     */
    public MythicMob getMythicMob(String internalName) {
        return MythicMobs.inst().getMobManager().getMythicMob(internalName);
    }

    /**
     * 获取单个怪物名称
     *
     * @param internalName 内部名称
     * @return 怪物名称
     */
    public String getMythicMobName(String internalName) {
        MythicMob mythicMob = MythicMobs.inst().getMobManager().getMythicMob(internalName);
        return mythicMob != null ? mythicMob.getDisplayName() : "";
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
