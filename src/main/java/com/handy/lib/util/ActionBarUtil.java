package com.handy.lib.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * ActionBar工具类
 *
 * @author handy
 */
public class ActionBarUtil {

    static Class<?> ChatComponentText;
    static Constructor<?> makeChatComponentText;
    static Class<?> IChatBaseComponent;
    static Class<?> classChatMessageType;

    static Class<?> PacketPlayOutChat;
    static Constructor<?> makePacketPlayOutChat;

    static Class<?> CraftPlayer;
    static Class<?> EntityPlayer;
    static Class<?> PlayerConnection;
    static Class<?> Packet;

    static Method sendPacket;
    static Method makeChatType;
    static Method getHandle;

    public static String version;

    public static void actionBarReflect() {
        version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        if (version.contains("1_17")) {
            return;
        }
        try {
            IChatBaseComponent = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent");
            ChatComponentText = Class.forName("net.minecraft.server." + version + ".ChatComponentText");
            EntityPlayer = Class.forName("net.minecraft.server." + version + ".EntityPlayer");
            CraftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
            PacketPlayOutChat = Class.forName("net.minecraft.server." + version + ".PacketPlayOutChat");
            PlayerConnection = Class.forName("net.minecraft.server." + version + ".PlayerConnection");
            Packet = Class.forName("net.minecraft.server." + version + ".Packet");

            getHandle = CraftPlayer.getMethod("getHandle");
            sendPacket = PlayerConnection.getMethod("sendPacket", Packet);

            makeChatComponentText = ChatComponentText.getConstructor(String.class);

            if (version.contains("1_12") || version.contains("1_13") || version.contains("1_14") || version.contains("1_15") || version.contains("1_16")) {
                classChatMessageType = Class.forName("net.minecraft.server." + version + ".ChatMessageType");
                makeChatType = classChatMessageType.getMethod("a", Byte.TYPE);
                if (version.contains("1_16")) {
                    makePacketPlayOutChat = PacketPlayOutChat.getConstructor(IChatBaseComponent, classChatMessageType, UUID.class);
                } else {
                    makePacketPlayOutChat = PacketPlayOutChat.getConstructor(IChatBaseComponent, classChatMessageType);
                }
            } else {
                makePacketPlayOutChat = PacketPlayOutChat.getConstructor(IChatBaseComponent, byte.class);
            }

        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public static void sendActionBar(Player player, String string) {
        try {
            Object o = makeChatComponentText.newInstance(string);
            Object bar;
            if (version.contains("1_16")) {
                bar = makePacketPlayOutChat.newInstance(o, makeChatType.invoke(null, (byte) 2), player.getUniqueId());
            } else {
                if (version.contains("1_12") || version.contains("1_13") || version.contains("1_14") || version.contains("1_15")) {
                    bar = makePacketPlayOutChat.newInstance(o, makeChatType.invoke(null, (byte) 2));
                } else {
                    bar = makePacketPlayOutChat.newInstance(o, (byte) 2);
                }
            }

            Object entityPlayer = getHandle.invoke(CraftPlayer.cast(player));
            Field con = entityPlayer.getClass().getDeclaredField("playerConnection");
            Object objPlayerConnection = con.get(entityPlayer);
            sendPacket.invoke(objPlayerConnection, bar);

        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
