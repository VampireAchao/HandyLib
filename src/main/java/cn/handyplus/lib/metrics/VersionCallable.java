package cn.handyplus.lib.metrics;

import java.util.concurrent.Callable;

/**
 * HandyLib版本
 *
 * @author handy
 * @since 1.5.7
 */
public class VersionCallable implements Callable<String> {
    private final String id;

    public VersionCallable(String id) {
        this.id = id;
    }

    @Override
    public String call() throws Exception {
        return this.id;
    }
}
