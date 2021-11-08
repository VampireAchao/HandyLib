package com.handy.lib.util;

import com.handy.lib.constants.VersionCheckEnum;
import com.handy.lib.core.CollUtil;
import com.handy.lib.core.StrUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ItemStack工具类
 *
 * @author handy
 * @since 1.1.8
 */
public class ItemStackUtil {

    /**
     * 序列化itemStack为String
     *
     * @param itemStack 物品
     * @return String
     */
    public static String itemStackSerialize(ItemStack itemStack) {
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("item", itemStack);
        return yml.saveToString();
    }

    /**
     * 反序列化String为itemStack
     *
     * @param str 物品str
     * @return ItemStack
     */
    public static ItemStack itemStackDeserialize(String str) {
        YamlConfiguration yml = new YamlConfiguration();
        ItemStack item;
        try {
            yml.loadFromString(str);
            item = yml.getItemStack("item");
        } catch (InvalidConfigurationException ex) {
            item = new ItemStack(Material.AIR, 1);
        }
        return item;
    }

    /**
     * 物品生成
     *
     * @param material    材质
     * @param displayName 名称
     * @return 自定义物品
     */
    public static ItemStack getItemStack(Material material, String displayName) {
        return getItemStack(material, displayName, null);
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
        return getItemStack(material, displayName, loreList, true);
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
            itemMeta.setDisplayName(BaseUtil.replaceRpgChatColor(displayName));
            if (CollUtil.isNotEmpty(loreList)) {
                itemMeta.setLore(BaseUtil.replaceChatColor(loreList, false));
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

    /**
     * 正确获取主手物品
     *
     * @param playerInventory 玩家背包
     * @return ItemStack
     * @since 1.6.0
     */
    public static ItemStack getItemInMainHand(PlayerInventory playerInventory) {
        if (VersionCheckEnum.getEnum().getVersionId() < VersionCheckEnum.V_1_9.getVersionId()) {
            return playerInventory.getItemInHand();
        }
        return playerInventory.getItemInMainHand();
    }

    /**
     * 设置物品的自定义模型数据.
     *
     * @param itemMeta itemMeta
     * @param id       模型id
     * @return itemMeta
     * @since 1.6.4
     */
    public static ItemMeta setCustomModelData(ItemMeta itemMeta, int id) {
        if (itemMeta == null || VersionCheckEnum.getEnum().getVersionId() < VersionCheckEnum.V_1_14.getVersionId() || id == 0) {
            return itemMeta;
        }
        itemMeta.setCustomModelData(id);
        return itemMeta;
    }

    /**
     * 获取必定不为空的ItemMeta
     *
     * @param itemStack ItemStack
     * @return ItemMeta
     * @since 1.7.0
     */
    public static ItemMeta getItemMeta(ItemStack itemStack) {
        return Objects.requireNonNull(itemStack.getItemMeta());
    }

}