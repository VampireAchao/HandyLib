package com.handy.lib.util;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.data.attribute.SXAttributeData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * SxAttribute v2支持
 *
 * @author handy
 * @since 1.1.5
 */
public class SxAttributeV2Util {

    private SxAttributeV2Util() {
    }

    private static class SingletonHolder {
        private static final SxAttributeV2Util INSTANCE = new SxAttributeV2Util();
    }

    public static SxAttributeV2Util getInstance() {
        return SxAttributeV2Util.SingletonHolder.INSTANCE;
    }

    /**
     * 给玩家添加属性
     *
     * @param c             类
     * @param player        玩家
     * @param attributeList 属性
     */
    public void addAttribute(Class<?> c, Player player, List<String> attributeList) {
        ItemStack itemStack = new ItemStack(Material.NAME_TAG);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("PlayerTitle");
        itemMeta.setLore(attributeList);
        itemStack.setItemMeta(itemMeta);
        SXAttributeData sxAttributeData = new SXAttributeData();
        sxAttributeData.add(SXAttribute.getApi().getItemData(null, null, itemStack));
        SXAttribute.getApi().setEntityAPIData(c, player.getUniqueId(), sxAttributeData);
        SXAttribute.getApi().updateStats(player);
    }

    /**
     * 移除玩家属性
     *
     * @param c      类
     * @param player 玩家
     */
    public void removeAttribute(Class<?> c, Player player) {
        SXAttribute.getApi().removeEntityAPIData(c, player.getUniqueId());
    }

}
