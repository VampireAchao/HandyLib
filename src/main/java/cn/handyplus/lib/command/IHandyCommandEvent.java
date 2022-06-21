package cn.handyplus.lib.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * 子命令处理接口
 *
 * @author handy
 */
public interface IHandyCommandEvent {

    /**
     * 子命令
     *
     * @return 子命令
     */
    String command();

    /**
     * 权限
     *
     * @return 权限
     */
    String permission();

    /**
     * 执行命令
     *
     * @param sender 发送人
     * @param cmd    命令
     * @param label  别名
     * @param args   参数
     */
    void onCommand(CommandSender sender, Command cmd, String label, String[] args);

}
