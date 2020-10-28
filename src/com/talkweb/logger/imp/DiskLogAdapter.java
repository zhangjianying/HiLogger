package com.talkweb.logger.imp;

import com.talkweb.logger.FormatStrategy;
import com.talkweb.logger.LogAdapter;
import ohos.app.AbilityContext;
import ohos.eventhandler.EventRunner;

import static com.talkweb.logger.Utils.*;

public class DiskLogAdapter implements LogAdapter {
    public static EventRunner runner = null;
    private final FormatStrategy formatStrategy;

    public DiskLogAdapter(AbilityContext context) {
        if (isNull(runner)) {
            runner = EventRunner.create(true);
        } else {
            runner.run();
        }
        formatStrategy = CsvFormatStrategy.newBuilder().build(context);
    }

    public DiskLogAdapter(FormatStrategy formatStrategy) {
        this.formatStrategy = requireNonNull(formatStrategy);
    }

    @Override
    public boolean isLoggable(int priority, String tag) {
        return true;
    }

    @Override
    public void log(int priority, String tag, String message) {
        formatStrategy.log(priority, tag, message);
    }

    @Override
    public void destory() {
        runner.stop();
    }


}
