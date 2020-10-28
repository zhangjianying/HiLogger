package com.talkweb.logger.imp;

import com.talkweb.logger.FormatStrategy;
import com.talkweb.logger.LogStrategy;
import com.talkweb.logger.Utils;
import ohos.app.AbilityContext;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.talkweb.logger.Utils.*;

public class CsvFormatStrategy implements FormatStrategy {
    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final String NEW_LINE_REPLACEMENT = " <br> ";
    private static final String SEPARATOR = ",";

    private Date date;
    private SimpleDateFormat dateFormat;
    private LogStrategy logStrategy;
    private String tag;

    private EventRunner runner;

    private CsvFormatStrategy(Builder builder) {
        requireNonNull(builder);

        date = builder.date;
        dateFormat = builder.dateFormat;
        logStrategy = builder.logStrategy;
        tag = builder.tag;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public void log(int priority, String onceOnlyTag, String message) {
        requireNonNull(message);

        String tag = formatTag(onceOnlyTag);

        date.setTime(System.currentTimeMillis());

        StringBuilder builder = new StringBuilder();

        // machine-readable date/time
        builder.append(Long.toString(date.getTime()));

        // human-readable date/time
        builder.append(SEPARATOR);
        builder.append(dateFormat.format(date));

        // level
        builder.append(SEPARATOR);
        builder.append(logLevel(priority));

        // tag
        builder.append(SEPARATOR);
        builder.append(tag);

        // message
        if (message.contains(NEW_LINE)) {
            message = message.replaceAll(NEW_LINE, NEW_LINE_REPLACEMENT);
        }
        builder.append(SEPARATOR);
        builder.append(message);

        // new line
        builder.append(NEW_LINE);

        logStrategy.log(priority, tag, builder.toString());
    }


    private String formatTag(String tag) {
        if (!Utils.isEmpty(tag) && !Utils.equals(this.tag, tag)) {
            return this.tag + "-" + tag;
        }
        return this.tag;
    }

    public static final class Builder {
        private static final int MAX_BYTES = 500 * 1024; // 500K averages to a 4000 lines per file

        Date date;
        SimpleDateFormat dateFormat;
        LogStrategy logStrategy;
        String tag = "PRETTY_LOGGER";

        private Builder() {
        }

        public Builder date(Date val) {
            date = val;
            return this;
        }


        public Builder dateFormat(SimpleDateFormat val) {
            dateFormat = val;
            return this;
        }


        public Builder logStrategy(LogStrategy val) {
            logStrategy = val;
            return this;
        }


        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }


        public CsvFormatStrategy build(AbilityContext context) {
            if (isNull(date)) {
                date = new Date();
            }
            if (isNull(dateFormat)) {
                dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS", Locale.CHINA);
            }
            if (isNull(logStrategy)) {
                String diskPath = context.getDataDir().getAbsolutePath();
                String folder = diskPath + File.separatorChar + "logger";

                if (isNull(DiskLogAdapter.runner)) {
                    return null;
                }
                DiskLogStrategy.WriteHandler handler = new DiskLogStrategy.WriteHandler(DiskLogAdapter.runner, folder, MAX_BYTES);
                logStrategy = new DiskLogStrategy(handler);
            }
            return new CsvFormatStrategy(this);
        }
    }
}
