package com.handy.lib.util;

import com.handy.lib.InitApi;
import com.handy.lib.constants.VersionCheckEnum;
import com.handy.lib.core.CollUtil;
import com.handy.lib.core.StrUtil;
import com.handy.lib.expand.XMaterial;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

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
     * @param material 材质
     * @return 自定义物品
     * @since 2.0.3
     */
    public static ItemStack getItemStack(Material material) {
        return getItemStack(material, null, null);
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
        return getItemStack(material, displayName, loreList, isEnchant, 0);
    }

    /**
     * 物品生成
     *
     * @param material        材质
     * @param displayName     名称
     * @param loreList        lore
     * @param customModelData 自定义模型id
     * @return 自定义物品
     * @since 2.0.3
     */
    public static ItemStack getItemStack(Material material, String displayName, List<String> loreList, int customModelData) {
        return getItemStack(material, displayName, loreList, true, customModelData);
    }

    /**
     * 物品生成
     *
     * @param materialStr     材质名称
     * @param displayName     名称
     * @param loreList        lore
     * @param isEnchant       附魔效果
     * @param customModelData 自定义模型id
     * @return 自定义物品
     * @since 2.3.5
     */
    public static ItemStack getItemStack(String materialStr, String displayName, List<String> loreList, Boolean isEnchant, int customModelData) {
        return getItemStack(getMaterial(materialStr), displayName, loreList, isEnchant, customModelData, true);
    }

    /**
     * 物品生成
     *
     * @param materialStr     材质名称
     * @param displayName     名称
     * @param loreList        lore
     * @param isEnchant       附魔效果
     * @param customModelData 自定义模型id
     * @param replaceMap      lore替换map
     * @return 自定义物品
     * @since 2.3.5
     */
    public static ItemStack getItemStack(String materialStr, String displayName, List<String> loreList, Boolean isEnchant, int customModelData, Map<String, String> replaceMap) {
        return getItemStack(getMaterial(materialStr), displayName, loreList, isEnchant, customModelData, true, replaceMap);
    }

    /**
     * 物品生成
     *
     * @param material        材质
     * @param displayName     名称
     * @param loreList        lore
     * @param isEnchant       附魔效果
     * @param customModelData 自定义模型id
     * @return 自定义物品
     * @since 2.0.3
     */
    public static ItemStack getItemStack(Material material, String displayName, List<String> loreList, Boolean isEnchant, int customModelData) {
        return getItemStack(material, displayName, loreList, isEnchant, customModelData, true);
    }

    /**
     * 物品生成
     *
     * @param material        材质
     * @param displayName     名称
     * @param loreList        lore
     * @param isEnchant       附魔效果
     * @param customModelData 自定义模型id
     * @param hideFlag        隐藏标签
     * @return 自定义物品
     * @since 2.1.7
     */
    public static ItemStack getItemStack(Material material, String displayName, List<String> loreList, Boolean isEnchant, int customModelData, boolean hideFlag) {
        return getItemStack(material, displayName, loreList, isEnchant, customModelData, hideFlag, null);
    }

    /**
     * 物品生成
     *
     * @param material        材质
     * @param displayName     名称
     * @param loreList        lore
     * @param isEnchant       附魔效果
     * @param customModelData 自定义模型id
     * @param hideFlag        隐藏标签
     * @param replaceMap      lore替换map
     * @return 自定义物品
     * @since 2.3.5
     */
    public static ItemStack getItemStack(Material material, String displayName, List<String> loreList, boolean isEnchant, int customModelData, boolean hideFlag, Map<String, String> replaceMap) {
        return getItemStack(material, displayName, loreList, isEnchant, customModelData, hideFlag, replaceMap, isEnchant);
    }

    /**
     * 物品生成
     *
     * @param material        材质
     * @param displayName     名称
     * @param loreList        lore
     * @param isEnchant       附魔效果
     * @param customModelData 自定义模型id
     * @param hideFlag        隐藏标签
     * @param replaceMap      lore替换map
     * @param hideEnchant     隐藏附魔效果
     * @return 自定义物品
     * @since 2.6.6
     */
    public static ItemStack getItemStack(Material material, String displayName, List<String> loreList, boolean isEnchant, int customModelData, boolean hideFlag, Map<String, String> replaceMap, boolean hideEnchant) {
        return getItemStack(material, displayName, loreList, isEnchant, customModelData, hideFlag, replaceMap, hideEnchant, null);
    }

    /**
     * 物品生成
     *
     * @param material        材质
     * @param displayName     名称
     * @param loreList        lore
     * @param isEnchant       附魔效果
     * @param customModelData 自定义模型id
     * @param hideFlag        隐藏标签
     * @param replaceMap      lore替换map
     * @param hideEnchant     隐藏附魔效果
     * @param customData      自定义数据
     * @return 自定义物品
     * @since 2.7.4
     */
    public static ItemStack getItemStack(Material material, String displayName, List<String> loreList, boolean isEnchant, int customModelData, boolean hideFlag, Map<String, String> replaceMap, boolean hideEnchant, String customData) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = getItemMeta(itemStack);
        if (StrUtil.isNotEmpty(displayName)) {
            itemMeta.setDisplayName(BaseUtil.replaceChatColor(displayName));
        }
        if (CollUtil.isNotEmpty(loreList)) {
            itemMeta.setLore(BaseUtil.replaceChatColor(loreReplaceMap(loreList, replaceMap), true));
        }
        // 附魔效果
        if (isEnchant) {
            setEnchant(itemMeta);
        }
        // 隐藏附魔效果
        if (hideEnchant) {
            hideEnchant(itemMeta);
        }
        // 隐藏物品属性
        if (hideFlag) {
            hideAttributes(itemMeta);
        }
        // 设置自定义数据
        if (StrUtil.isNotEmpty(customData)) {
            setPersistentData(itemMeta, customData);
        }
        // 模型效果
        if (customModelData > 0) {
            setCustomModelData(itemMeta, customModelData);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     * 给物品添加附魔效果并隐藏附魔效果
     *
     * @param itemMeta 物品属性
     */
    public static void setEnchant(ItemMeta itemMeta) {
        // 耐久
        itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
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
     * 隐藏Item属性
     *
     * @param itemMeta 物品属性
     */
    public static void hideAttributes(ItemMeta itemMeta) {
        VersionCheckEnum versionCheckEnum = VersionCheckEnum.getEnum();
        if (!VersionCheckEnum.V_1_7.equals(versionCheckEnum)) {
            // 隐藏Item属性
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
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
        ItemStack[] contents;
        if (VersionCheckEnum.getEnum().getVersionId() <= VersionCheckEnum.V_1_8.getVersionId()) {
            contents = playerInventory.getContents();
        } else {
            contents = playerInventory.getStorageContents();
        }
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
    public static void addItem(PlayerInventory playerInventory, ItemStack itemStack, int amount) {
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
        return getMaterial(materialStr, Material.STONE);
    }

    /**
     * 获取材质
     *
     * @param materialStr     材质
     * @param defaultMaterial 未找到的的默认材质
     * @return Material
     */
    public static Material getMaterial(String materialStr, Material defaultMaterial) {
        if (StrUtil.isEmpty(materialStr)) {
            return defaultMaterial;
        }
        materialStr = materialStr.toUpperCase(Locale.ROOT);
        Material material = Material.getMaterial(materialStr);
        if (material != null) {
            return material;
        }
        if (VersionCheckEnum.getEnum().getVersionId() > VersionCheckEnum.V_1_12.getVersionId()) {
            material = Material.getMaterial("LEGACY_" + materialStr, true);
            if (material != null) {
                return material;
            }
        }
        // 小于1.8处理
        if (VersionCheckEnum.getEnum().getVersionId() < VersionCheckEnum.V_1_8.getVersionId()) {
            return defaultMaterial;
        }
        return XMaterial.matchXMaterial(materialStr).orElse(XMaterial.matchXMaterial(defaultMaterial)).parseMaterial();
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
     * @since 1.6.4
     */
    public static void setCustomModelData(ItemMeta itemMeta, int id) {
        if (itemMeta == null || VersionCheckEnum.getEnum().getVersionId() < VersionCheckEnum.V_1_14.getVersionId() || id == 0) {
            return;
        }
        itemMeta.setCustomModelData(id);
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

    /**
     * 进行lore的变量替换
     *
     * @param loreList   原本lore
     * @param replaceMap 替换map
     * @return 新lore
     * @since 2.3.5
     */
    public static List<String> loreReplaceMap(List<String> loreList, Map<String, String> replaceMap) {
        List<String> newLoreList = new ArrayList<>();
        if (replaceMap != null && replaceMap.size() > 0 && CollUtil.isNotEmpty(loreList)) {
            for (String lore : loreList) {
                for (String key : replaceMap.keySet()) {
                    if (lore.contains("${" + key + "}")) {
                        lore = lore.replace("${" + key + "}", replaceMap.get(key));
                    }
                }
                newLoreList.add(lore);
            }
        } else {
            newLoreList.addAll(loreList);
        }
        return newLoreList;
    }

    /**
     * 进行lore的变量批量替换
     *
     * @param loreList   原本loreList
     * @param replaceMap 替换map
     * @param def        默认值
     * @return 新loreList
     * @since 2.3.5
     */
    public static List<String> loreBatchReplaceMap(List<String> loreList, Map<String, List<String>> replaceMap, String def) {
        if (CollUtil.isEmpty(loreList) || replaceMap == null || replaceMap.isEmpty()) {
            return loreList;
        }
        List<String> newLoreList = new ArrayList<>();
        for (String lore : loreList) {
            newLoreList.addAll(loreBatchReplaceMap(lore, replaceMap, def));
        }
        return newLoreList;
    }

    /**
     * 进行lore的变量批量替换
     *
     * @param lore       原本lore
     * @param replaceMap 替换map
     * @param def        默认值
     * @return 新lore
     * @since 2.3.5
     */
    public static List<String> loreBatchReplaceMap(String lore, Map<String, List<String>> replaceMap, String def) {
        List<String> loreList = new ArrayList<>();
        if (StrUtil.isEmpty(lore)) {
            loreList.add(lore);
            return loreList;
        }
        if (replaceMap == null || replaceMap.isEmpty()) {
            loreList.add(lore);
            return loreList;
        }
        for (String key : replaceMap.keySet()) {
            if (lore.contains("${" + key + "}")) {
                List<String> replaceList = replaceMap.get(key);
                if (CollUtil.isEmpty(replaceList)) {
                    loreList.add(lore.replace("${" + key + "}", def));
                } else {
                    for (String replaceStr : replaceList) {
                        loreList.add(lore.replace("${" + key + "}", replaceStr));
                    }
                }
                break;
            }
        }
        if (CollUtil.isEmpty(loreList)) {
            loreList.add(lore);
        }
        return loreList;
    }

    /**
     * 设置数据容器
     *
     * @param itemStack  物品
     * @param customData 自定义数据
     * @since 2.7.4
     */
    public static void setPersistentData(ItemStack itemStack, String customData) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (VersionCheckEnum.getEnum().getVersionId() < VersionCheckEnum.V_1_14.getVersionId() || itemMeta == null) {
            return;
        }
        // 设置数据
        setPersistentData(itemMeta, customData);
        // 重新设置回去
        itemStack.setItemMeta(itemMeta);
    }

    /**
     * 设置数据容器
     *
     * @param itemMeta   物品
     * @param customData 自定义数据
     * @since 2.7.4
     */
    public static void setPersistentData(ItemMeta itemMeta, String customData) {
        // 获取数据容器
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        // 添加自定义标签
        dataContainer.set(new NamespacedKey(InitApi.PLUGIN, "handy_data"), PersistentDataType.STRING, customData);
    }

    /**
     * 获取数据
     *
     * @param itemStack 物品
     * @return 自定义数据
     * @since 2.7.4
     */
    public static String getPersistentData(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (VersionCheckEnum.getEnum().getVersionId() < VersionCheckEnum.V_1_14.getVersionId() || itemMeta == null) {
            return null;
        }
        return itemMeta.getPersistentDataContainer().get(new NamespacedKey(InitApi.PLUGIN, "handy_data"), PersistentDataType.STRING);
    }

}