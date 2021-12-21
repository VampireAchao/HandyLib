package com.handy.lib.util;

import com.handy.lib.constants.BaseConstants;
import com.handy.lib.core.CollUtil;
import com.handy.lib.core.StrUtil;
import com.handy.lib.exception.HandyException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * 断言
 *
 * @author handy
 * @since 2.0.4
 */
public class AssertUtil {

    /**
     * 不是true报错
     *
     * @param expression 条件
     * @param sender     发送人
     * @param message    消息
     */
    public static void isTrue(boolean expression, CommandSender sender, String message) {
        try {
            if (!expression) {
                throw new HandyException(message);
            }
        } catch (HandyException e) {
            sender.sendMessage(BaseUtil.replaceChatColor(e.getMessage()));
        }
    }

    /**
     * 不是true报错
     *
     * @param expression 条件
     * @param player     发送人
     * @param message    消息
     */
    public static void isTrue(boolean expression, Player sender, String message) {
        try {
            if (!expression) {
                throw new HandyException(message);
            }
        } catch (HandyException e) {
            sender.sendMessage(BaseUtil.replaceChatColor(e.getMessage()));
        }
    }

    /**
     * 是null报错
     *
     * @param object  条件
     * @param sender  发送人
     * @param message 消息
     */
    public static void notNull(Object object, CommandSender sender, String message) {
        try {
            if (object == null) {
                throw new HandyException(message);
            }
        } catch (HandyException e) {
            sender.sendMessage(BaseUtil.replaceChatColor(e.getMessage()));
        }
    }

    /**
     * 是null报错
     *
     * @param object  条件
     * @param sender  发送人
     * @param message 消息
     */
    public static void notNull(Object object, Player sender, String message) {
        try {
            if (object == null) {
                throw new HandyException(message);
            }
        } catch (HandyException e) {
            sender.sendMessage(BaseUtil.replaceChatColor(e.getMessage()));
        }
    }

    /**
     * 是null或者""报错
     *
     * @param str     条件
     * @param sender  发送人
     * @param message 消息
     */
    public static void notEmpty(String str, CommandSender sender, String message) {
        try {
            if (StrUtil.isEmpty(str)) {
                throw new HandyException(message);
            }
        } catch (HandyException e) {
            sender.sendMessage(BaseUtil.replaceChatColor(e.getMessage()));
        }
    }

    /**
     * 是null或者""报错
     *
     * @param str     条件
     * @param sender  发送人
     * @param message 消息
     */
    public static void notEmpty(String str, Player sender, String message) {
        try {
            if (StrUtil.isEmpty(str)) {
                throw new HandyException(message);
            }
        } catch (HandyException e) {
            sender.sendMessage(BaseUtil.replaceChatColor(e.getMessage()));
        }
    }

    /**
     * 是空集合报错
     *
     * @param collection 条件
     * @param sender     发送人
     * @param message    消息
     */
    public static <T> void notNull(Collection<T> collection, CommandSender sender, String message) {
        try {
            if (CollUtil.isEmpty(collection)) {
                throw new HandyException(message);
            }
        } catch (HandyException e) {
            sender.sendMessage(BaseUtil.replaceChatColor(e.getMessage()));
        }
    }

    /**
     * 是空集合报错
     *
     * @param collection 条件
     * @param sender     发送人
     * @param message    消息
     */
    public static <T> void notNull(Collection<T> collection, Player sender, String message) {
        try {
            if (CollUtil.isEmpty(collection)) {
                throw new HandyException(message);
            }
        } catch (HandyException e) {
            sender.sendMessage(BaseUtil.replaceChatColor(e.getMessage()));
        }
    }

    /**
     * 是空map报错
     *
     * @param collection 条件
     * @param sender     发送人
     * @param message    消息
     */
    public static <K, V> void notNull(Map<K, V> map, CommandSender sender, String message) {
        try {
            if (map == null || map.size() < 1) {
                throw new HandyException(message);
            }
        } catch (HandyException e) {
            sender.sendMessage(BaseUtil.replaceChatColor(e.getMessage()));
        }
    }

    /**
     * 是空map报错
     *
     * @param collection 条件
     * @param sender     发送人
     * @param message    消息
     */
    public static <K, V> void notNull(Map<K, V> map, Player sender, String message) {
        try {
            if (map == null || map.size() < 1) {
                throw new HandyException(message);
            }
        } catch (HandyException e) {
            sender.sendMessage(BaseUtil.replaceChatColor(e.getMessage()));
        }
    }

    /**
     * 不是玩家报错
     *
     * @param expression 条件
     * @param sender     发送人
     * @param message    消息
     */
    public static void notPlayer(CommandSender sender, String message) {
        try {
            if (BaseUtil.isNotPlayer(sender)) {
                throw new HandyException(message);
            }
        } catch (HandyException e) {
            sender.sendMessage(BaseUtil.replaceChatColor(e.getMessage()));
        }
    }

    /**
     * 利用正则表达式判断字符串是否是数字
     *
     * @param str     字符串
     * @param sender  发送人
     * @param message 消息
     * @return 数字
     */
    public static Integer isNumericToInt(String str, CommandSender sender, String message) {
        try {
            Matcher isNum = BaseConstants.NUMERIC.matcher(str);
            if (isNum.matches()) {
                return Integer.valueOf(str);
            }
        } catch (Exception e) {
            sender.sendMessage(BaseUtil.replaceChatColor(message));
        }
        return null;
    }

    /**
     * 利用正则表达式判断字符串是否是数字
     *
     * @param str     字符串
     * @param sender  发送人
     * @param message 消息
     * @return 数字
     */
    public static Integer isNumericToInt(String str, Player sender, String message) {
        try {
            Matcher isNum = BaseConstants.NUMERIC.matcher(str);
            if (isNum.matches()) {
                return Integer.valueOf(str);
            }
        } catch (Exception e) {
            sender.sendMessage(BaseUtil.replaceChatColor(message));
        }
        return null;
    }

    /**
     * 利用正则表达式判断字符串是否是数字
     *
     * @param str     字符串
     * @param sender  发送人
     * @param message 消息
     * @return 数字
     */
    public static Long isNumericToLong(String str, CommandSender sender, String message) {
        try {
            Matcher isNum = BaseConstants.NUMERIC.matcher(str);
            if (isNum.matches()) {
                return Long.parseLong(str);
            }
        } catch (Exception e) {
            sender.sendMessage(BaseUtil.replaceChatColor(message));
        }
        return null;
    }

    /**
     * 利用正则表达式判断字符串是否是数字
     *
     * @param str     字符串
     * @param sender  发送人
     * @param message 消息
     * @return 数字
     */
    public static Long isNumericToLong(String str, Player sender, String message) {
        try {
            Matcher isNum = BaseConstants.NUMERIC.matcher(str);
            if (isNum.matches()) {
                return Long.parseLong(str);
            }
        } catch (Exception e) {
            sender.sendMessage(BaseUtil.replaceChatColor(message));
        }
        return null;
    }

}