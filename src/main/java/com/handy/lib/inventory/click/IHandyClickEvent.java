package com.handy.lib.inventory.click;

import com.handy.lib.inventory.HandyInventory;

import java.util.List;

/**
 * 点击事件接口
 *
 * @author handy
 */
public interface IHandyClickEvent {

    /**
     * gui类型
     *
     * @return gui类型
     */
    String guiType();

    /**
     * 生效的格子
     *
     * @return 格子列表
     */
    List<Integer> rawSlotList();

    /**
     * 点击格子进行处理
     *
     * @param handyInventory 入参
     * @param rawSlot        格子
     */
    void rawSlotClick(HandyInventory handyInventory, int rawSlot);
}
