package com.handy.lib.command;

import com.handy.lib.core.CollUtil;
import com.handy.lib.core.StrUtil;
import com.handy.lib.exception.HandyException;
import com.handy.lib.util.BaseUtil;
import lombok.SneakyThrows;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 命令工厂
 *
 * @author handy
 * @since 1.2.0
 */
public class HandyCommandFactory {
    private HandyCommandFactory() {
    }

    private static final HandyCommandFactory INSTANCE = new HandyCommandFactory();

    public static HandyCommandFactory getInstance() {
        return INSTANCE;
    }

    /**
     * 全部实现类
     */
    private static final Map<String, IHandyCommandEvent> HANDY_COMMAND_EVENT_MAP = new HashMap<>();

    /**
     * 全部子命令
     */
    private static Map<String, Map<String, HandySubCommandParam>> SUB_COMMAND_MAP = new HashMap<>();

    /**
     * 初始化实现类
     *
     * @param handyCommandEvents 子命令处理
     */
    public void init(List<IHandyCommandEvent> handyCommandEvents) {
        if (CollUtil.isEmpty(handyCommandEvents)) {
            return;
        }
        for (IHandyCommandEvent handyCommandEvent : handyCommandEvents) {
            HANDY_COMMAND_EVENT_MAP.put(handyCommandEvent.command().toLowerCase(), handyCommandEvent);
        }
    }

    /**
     * 初始化实现类
     *
     * @param subCommandMap 子命令处理
     */
    public void initSubCommand(Map<String, Map<String, HandySubCommandParam>> subCommandMap) {
        SUB_COMMAND_MAP = subCommandMap;
    }

    /**
     * 子命令处理
     *
     * @param command      命令
     * @param sender       执行者
     * @param cmd          命令
     * @param label        别名
     * @param args         入参
     * @param noPermission 没有权限提醒
     * @return true/正常执行，false需要发送提醒
     */
    @SneakyThrows
    public boolean onSubCommand(String command, CommandSender sender, Command cmd, String label, String[] args, String noPermission) {
        if (args.length == 0) {
            return false;
        }
        Map<String, HandySubCommandParam> subCommandParamMap = SUB_COMMAND_MAP.get(command.toLowerCase());
        if (subCommandParamMap == null) {
            return false;
        }
        HandySubCommandParam param = subCommandParamMap.get(args[0].toLowerCase());
        if (param == null) {
            return false;
        }
        if (StrUtil.isNotEmpty(param.getPermission()) && !sender.hasPermission(param.getPermission())) {
            sender.sendMessage(BaseUtil.replaceChatColor(noPermission));
            return true;
        }
        // 捕获自定义异常并抛出提醒
        try {
            param.getMethod().invoke(param.getAClass().newInstance(), sender, cmd, label, args);
        } catch (HandyException exception) {
            sender.sendMessage(BaseUtil.replaceChatColor(exception.getMessage()));
        }
        return true;
    }

    /**
     * 执行命令
     *
     * @param sender       发送人
     * @param cmd          命令
     * @param label        别名
     * @param args         参数
     * @param noPermission 无权限提醒
     * @return true/正常执行，false需要发送提醒
     */
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args, String noPermission) {
        if (args.length == 0) {
            return false;
        }
        IHandyCommandEvent handyInventory = HANDY_COMMAND_EVENT_MAP.get(args[0].toLowerCase());
        if (handyInventory == null) {
            return false;
        }
        if (StrUtil.isNotEmpty(handyInventory.permission()) && !sender.hasPermission(handyInventory.permission())) {
            sender.sendMessage(BaseUtil.replaceChatColor(noPermission));
            return true;
        }
        // 捕获自定义异常并抛出提醒
        try {
            handyInventory.onCommand(sender, cmd, label, args);
        } catch (HandyException exception) {
            sender.sendMessage(BaseUtil.replaceChatColor(exception.getMessage()));
        }
        return true;
    }

}