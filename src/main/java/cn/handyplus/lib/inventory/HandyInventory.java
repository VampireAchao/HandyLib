package cn.handyplus.lib.inventory;

import cn.handyplus.lib.constants.BaseConstants;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * gui仓库模版
 *
 * @author handy
 */
@Data
public class HandyInventory implements InventoryHolder {
    /**
     * 背包
     */
    private Inventory inventory;
    /**
     * 数据map
     */
    private Map<Integer, Long> map;
    /**
     * 数据map,兼容新版本使用
     *
     * @since 2.3.2
     */
    private Map<Integer, Integer> intMap;
    /**
     * 数据map
     *
     * @since 1.7.0
     */
    private Map<Integer, Object> objMap;
    /**
     * 数据map
     *
     * @since 2.6.5
     */
    private Map<Integer, List<String>> listMap;
    /**
     * 数据map
     *
     * @since 2.7.4
     */
    private Map<Integer, String> strMap;
    /**
     * 额外数据传输对象
     *
     * @since 1.7.0
     */
    private Object obj;
    /**
     * gui传输中的id
     *
     * @since 2.1.8
     */
    private Integer id;
    /**
     * gui类型
     */
    private String guiType;
    /**
     * 当前页
     */
    private Integer pageNum;
    /**
     * 总页数
     */
    private Integer pageCount;
    /**
     * 搜索条件
     */
    private String searchType;
    /**
     * 玩家
     */
    private Player player;

    /**
     * 是否默认取消事件，默认情况下构造赋值为true
     *
     * @since 2.3.6
     */
    private boolean toCancel;

    /**
     * 创建gui
     *
     * @param map     参数
     * @param guiType 类型
     * @param title   标题
     * @param size    大小
     */
    public HandyInventory(Map<Integer, Long> map, String guiType, String title, int size) {
        this.map = map;
        this.intMap = new HashMap<>();
        this.objMap = new HashMap<>();
        this.listMap = new HashMap<>();
        this.strMap = new HashMap<>();
        this.guiType = guiType;
        this.inventory = Bukkit.createInventory(this, size, title);
        this.toCancel = true;
    }

    /**
     * 创建gui
     *
     * @param guiType 类型
     * @param title   标题
     * @param size    大小
     */
    public HandyInventory(String guiType, String title, int size) {
        this.map = new HashMap<>();
        this.intMap = new HashMap<>();
        this.objMap = new HashMap<>();
        this.listMap = new HashMap<>();
        this.strMap = new HashMap<>();
        this.guiType = guiType;
        this.inventory = Bukkit.createInventory(this, size, title);
        this.toCancel = true;
    }

    /**
     * 创建gui
     *
     * @param guiType       类型
     * @param inventoryType 类型
     */
    public HandyInventory(String guiType, InventoryType inventoryType) {
        this.map = new HashMap<>();
        this.intMap = new HashMap<>();
        this.objMap = new HashMap<>();
        this.listMap = new HashMap<>();
        this.strMap = new HashMap<>();
        this.guiType = guiType;
        this.inventory = Bukkit.createInventory(this, inventoryType);
        this.toCancel = true;
    }

    /**
     * 创建gui
     *
     * @param guiType       类型
     * @param title         标题
     * @param inventoryType 类型
     */
    public HandyInventory(String guiType, String title, InventoryType inventoryType) {
        this.map = new HashMap<>();
        this.intMap = new HashMap<>();
        this.objMap = new HashMap<>();
        this.listMap = new HashMap<>();
        this.strMap = new HashMap<>();
        this.guiType = guiType;
        this.inventory = Bukkit.createInventory(this, inventoryType, title);
        this.toCancel = true;
    }

    /**
     * 创建gui
     *
     * @param guiType 类型
     * @param title   标题
     */
    public HandyInventory(String guiType, String title) {
        this.map = new HashMap<>();
        this.intMap = new HashMap<>();
        this.objMap = new HashMap<>();
        this.listMap = new HashMap<>();
        this.strMap = new HashMap<>();
        this.guiType = guiType;
        this.inventory = Bukkit.createInventory(this, BaseConstants.GUI_SIZE_54, title);
        this.toCancel = true;
    }

}