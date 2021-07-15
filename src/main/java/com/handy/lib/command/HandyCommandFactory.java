package com.handy.lib.command;

import com.handy.lib.core.CollUtil;
import com.handy.lib.core.StrUtil;
import com.handy.lib.util.BaseUtil;
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
        String command = args[0];
        IHandyCommandEvent handyInventory = HANDY_COMMAND_EVENT_MAP.get(command.toLowerCase());
        if (handyInventory == null) {
            return false;
        }
        if (StrUtil.isNotEmpty(handyInventory.permission()) && !sender.hasPermission(handyInventory.permission())) {
            sender.sendMessage(BaseUtil.replaceChatColor(noPermission));
            return true;
        }
        handyInventory.onCommand(sender, cmd, label, args);
        return true;
    }

}