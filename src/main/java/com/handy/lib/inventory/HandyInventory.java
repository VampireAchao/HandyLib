package com.handy.lib.inventory;

import com.handy.lib.constants.BaseConstants;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * gui仓库模版
 *
 * @author handy
 */
@Data
public class HandyInventory implements InventoryHolder {
    private Inventory inventory;
    private Plugin plugin;
    private Map<Integer, Long> map;
    private String guiType;
    private Integer pageNum;
    private Integer pageCount;
    private String searchType;

    /**
     * 创建gui
     *
     * @param map     参数
     * @param guiType 类型
     * @param title   标题
     * @param size    大小
     * @param plugin  插件
     */
    public HandyInventory(Map<Integer, Long> map, String guiType, String title, int size, Plugin plugin) {
        this.map = map;
        this.guiType = guiType;
        this.inventory = Bukkit.createInventory(this, size, title);
        this.plugin = plugin;
    }

    /**
     * 创建gui
     *
     * @param guiType 类型
     * @param title   标题
     * @param size    大小
     * @param plugin  插件
     */
    public HandyInventory(String guiType, String title, int size, Plugin plugin) {
        this.map = new HashMap<>();
        this.guiType = guiType;
        this.inventory = Bukkit.createInventory(this, size, title);
        this.plugin = plugin;
    }

    /**
     * 创建gui
     *
     * @param guiType 类型
     * @param title   标题
     * @param plugin  插件
     */
    public HandyInventory(String guiType, String title, Plugin plugin) {
        this.map = new HashMap<>();
        this.guiType = guiType;
        this.inventory = Bukkit.createInventory(this, BaseConstants.GUI_SIZE_54, title);
        this.plugin = plugin;
    }

}