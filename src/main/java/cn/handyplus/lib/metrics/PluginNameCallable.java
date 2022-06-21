package cn.handyplus.lib.metrics;

import java.util.concurrent.Callable;

/**
 * 插件名称统计
 *
 * @author handy
 * @since 1.6.1
 */
public class PluginNameCallable implements Callable<String> {
    private final String pluginName;

    public PluginNameCallable(String pluginName) {
        this.pluginName = pluginName;
    }

    @Override
    public String call() throws Exception {
        return this.pluginName;
    }

}