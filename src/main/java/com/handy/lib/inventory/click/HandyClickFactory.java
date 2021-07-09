package com.handy.lib.inventory.click;

import com.handy.lib.core.CollUtil;
import com.handy.lib.inventory.HandyInventory;

import java.util.ArrayList;
import java.util.List;

/**
 * 点击进行处理工厂
 *
 * @author handy
 */
public class HandyClickFactory {
    private HandyClickFactory() {
    }

    private static final HandyClickFactory INSTANCE = new HandyClickFactory();

    public static HandyClickFactory getInstance() {
        return INSTANCE;
    }

    /**
     * 全部实现类
     */
    private static List<IHandyClickEvent> handyClickEventList = new ArrayList<>();

    /**
     * 初始化实现类
     *
     * @param handyClickEvents 点击处理事件
     */
    public void init(List<IHandyClickEvent> handyClickEvents) {
        if (CollUtil.isNotEmpty(handyClickEvents)) {
            handyClickEventList = handyClickEvents;
        }
    }

    /**
     * 进行处理
     *
     * @param handyInventory 入参
     */
    public void rawSlotClick(HandyInventory handyInventory) {
        IHandyClickEvent handyClickEvent = this.getFactory(handyInventory.getGuiType(), handyInventory.getRawSlot());
        if (handyClickEvent != null) {
            handyClickEvent.rawSlotClick(handyInventory);
        }
    }

    /**
     * 获取实现
     *
     * @param guiType gui类型
     * @param rawSlot 点击的格子
     */
    private IHandyClickEvent getFactory(String guiType, int rawSlot) {
        for (IHandyClickEvent event : handyClickEventList) {
            if (event.guiType().equals(guiType) && event.rawSlotList().contains(rawSlot)) {
                return event;
            }
        }
        return null;
    }

}
