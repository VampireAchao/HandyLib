package cn.handyplus.lib.util;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

/**
 * TextComponent 的封装
 *
 * @author handy
 * @since 2.7.3
 */
public class TextUtil {

    private TextUtil() {
    }

    private static class SingletonHolder {
        private static final TextUtil INSTANCE = new TextUtil();
    }

    public static TextUtil getInstance() {
        return TextUtil.SingletonHolder.INSTANCE;
    }

    private TextComponent textComponent;

    /**
     * 初始化
     *
     * @param msg 消息内容
     * @return this
     */
    public TextUtil init(String msg) {
        textComponent = new TextComponent(BaseUtil.replaceChatColor(msg));
        return this;
    }

    /**
     * 添加鼠标点击事件
     *
     * @param action 類型
     * @param msg    消息
     * @return this
     * @since 3.0.9
     */
    public TextUtil addClick(ClickEvent.Action action, String msg) {
        textComponent.setClickEvent(new ClickEvent(action, msg));
        return this;
    }

    /**
     * 添加鼠标点击打开url
     *
     * @param url url
     * @return this
     */
    public TextUtil addClickUrl(String url) {
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        return this;
    }

    /**
     * 添加鼠标点击执行名
     *
     * @param command 命令
     * @return this
     */
    public TextUtil addClickCommand(String command) {
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        return this;
    }

    /**
     * 添加鼠标点击 将给定的字符串插入到玩家的文本框中。
     *
     * @param suggestCommand 字符串
     * @return this
     * @since 3.0.9
     */
    public TextUtil addClickSuggestCommand(String suggestCommand) {
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, suggestCommand));
        return this;
    }

    /**
     * 添加鼠标移动上去显示效果
     *
     * @param text 文字
     * @return this
     */
    public TextUtil addHoverText(String text) {
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(BaseUtil.replaceChatColor(text))));
        return this;
    }

    /**
     * 获取 TextComponent
     *
     * @return TextComponent
     */
    public TextComponent build() {
        return this.textComponent;
    }

}