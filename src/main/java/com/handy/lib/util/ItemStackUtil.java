package com.handy.lib.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handy.lib.constants.VersionCheckEnum;
import com.handy.lib.core.CollUtil;
import com.handy.lib.core.StrUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ItemStack工具类
 *
 * @author handy
 * @since 1.1.8
 */
public class ItemStackUtil {

    /**
     * 序列化itemStack为json
     *
     * @param itemStack 物品
     * @return json
     */
    public static String itemStackSerialize(ItemStack itemStack) {
        return new Gson().toJson(itemStack.serialize());
    }

    /**
     * 序列化itemStack为json
     *
     * @param json 物品json
     * @return json
     */
    public static ItemStack itemStackDeserialize(String json) {
        Map<String, Object> map = new Gson().fromJson(json, new TypeToken<Map<String, Object>>() {
        }.getType());
        return ItemStack.deserialize(map);
    }

    /**
     * 物品生成
     *
     * @param material    材质
     * @param displayName 名称
     * @param loreList    lore
     * @return 自定义物品
     */
    public static ItemStack getItemStack(Material material, String displayName, List<String> loreList) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(displayName);
            if (loreList != null && loreList.size() > 0) {
                itemMeta.setLore(loreList);
            }
            // 附魔效果
            setEnchant(itemMeta);
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    /**
     * 物品生成
     *
     * @param material    材质
     * @param displayName 名称
     * @param loreList    lore
     * @param isEnchant   附魔效果
     * @return 自定义物品
     */
    public static ItemStack getItemStack(Material material, String displayName, List<String> loreList, Boolean isEnchant) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(displayName);
            if (loreList != null && loreList.size() > 0) {
                itemMeta.setLore(loreList);
            }
            if (isEnchant) {
                // 附魔效果
                setEnchant(itemMeta);
            }
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    /**
     * 给物品添加附魔效果
     *
     * @param itemMeta 物品属性
     */
    public static void setEnchant(ItemMeta itemMeta) {
        // 耐久
        itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        VersionCheckEnum versionCheckEnum = VersionCheckEnum.getEnum();
        if (!VersionCheckEnum.V_1_7.equals(versionCheckEnum)) {
            // 隐藏附魔效果
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
    }

    /**
     * 隐藏附魔效果
     *
     * @param itemMeta 物品属性
     */
    public static void hideEnchant(ItemMeta itemMeta) {
        VersionCheckEnum versionCheckEnum = VersionCheckEnum.getEnum();
        if (!VersionCheckEnum.V_1_7.equals(versionCheckEnum)) {
            // 隐藏附魔效果
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
    }

    /**
     * 物品减少计算
     *
     * @param playerInventory 玩家背包
     * @param itemStack       指定物品
     * @param amount          数量
     * @return 是否成功扣除
     */
    public static Boolean removeItem(PlayerInventory playerInventory, ItemStack itemStack, Integer amount) {
        ItemStack[] contents = playerInventory.getContents();
        int num = 0;
        List<ItemStack> items = new ArrayList<>();
        for (ItemStack item : contents) {
            if (item == null || Material.AIR.equals(item.getType())) {
                continue;
            }
            // 1.判断类型
            if (!item.getType().equals(itemStack.getType())) {
                continue;
            }
            if (item.getItemMeta() == null || itemStack.getItemMeta() == null) {
                continue;
            }
            String displayName = item.getItemMeta().getDisplayName();
            if (StrUtil.isEmpty(displayName)) {
                displayName = "";
            }
            String displayName1 = itemStack.getItemMeta().getDisplayName();
            if (StrUtil.isEmpty(displayName1)) {
                displayName1 = "";
            }
            // 2.判断名称
            if (!displayName.equals(displayName1)) {
                continue;
            }
            // 3.判断lore
            if (!CollUtil.equals(item.getItemMeta().getLore(), itemStack.getItemMeta().getLore())) {
                continue;
            }
            num += item.getAmount();
            items.add(item);
            // 如果数量够了就不继续循环了
            if (num >= amount) {
                break;
            }
        }
        if (num == amount) {
            for (ItemStack itemStack1 : items) {
                playerInventory.removeItem(itemStack1);
            }
            return true;
        }
        if (num > amount) {
            for (ItemStack itemStack1 : items) {
                if (amount == 0) {
                    return true;
                }
                if (amount > itemStack1.getAmount()) {
                    amount = amount - itemStack1.getAmount();
                    playerInventory.removeItem(itemStack1);
                } else {
                    itemStack1.setAmount(itemStack1.getAmount() - amount);
                    amount = 0;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 进行物品发送
     *
     * @param playerInventory 玩家背包
     * @param itemStack       物品
     * @param amount          发送数量
     */
    private void addItem(PlayerInventory playerInventory, ItemStack itemStack, int amount) {
        int maxStackSize = itemStack.getMaxStackSize();
        if (amount > maxStackSize) {
            // 如果发送数量大于最大上限
            itemStack.setAmount(maxStackSize);
            playerInventory.addItem(itemStack);
            addItem(playerInventory, itemStack, amount - maxStackSize);
        } else {
            // 小于等于直接发送
            itemStack.setAmount(amount);
            playerInventory.addItem(itemStack);
        }
    }

    /**
     * 获取材质
     *
     * @param materialStr 材质
     * @return Material
     */
    public static Material getMaterial(String materialStr) {
        try {
            if (StringUtils.isNotBlank(materialStr)) {
                return Material.valueOf(materialStr);
            }
        } catch (Exception ignored) {
            Bukkit.getLogger().info("没有找到对应的物品材质: " + materialStr);
        }
        return Material.STONE;
    }

    /**
     * 获取材质
     *
     * @param materialStr 材质
     * @param material    未找到的的默认材质
     * @return Material
     */
    public static Material getMaterial(String materialStr, Material material) {
        try {
            if (StringUtils.isNotBlank(materialStr)) {
                return Material.valueOf(materialStr);
            }
        } catch (Exception ignored) {
            Bukkit.getLogger().info("没有找到对应的物品材质: " + materialStr);
        }
        return material;
    }

}