package com.talkweb.logger.imp;

import com.talkweb.logger.FormatStrategy;
import com.talkweb.logger.LogAdapter;

import static com.talkweb.logger.Utils.requireNonNull;

public class HMOSLogAdapter implements LogAdapter {
    private final FormatStrategy formatStrategy;
    private static int domain = 201;

    public HMOSLogAdapter() {
        this.formatStrategy = PrettyFormatStrategy.newBuilder().build();
    }

    public HMOSLogAdapter(Integer domain) {
        this.formatStrategy = PrettyFormatStrategy.newBuilder().build();
        this.domain = requireNonNull(domain);
    }

    public HMOSLogAdapter(FormatStrategy formatStrategy) {
        this.formatStrategy = requireNonNull(formatStrategy);
    }

    public HMOSLogAdapter(FormatStrategy formatStrategy, Integer domain) {
        this.formatStrategy = requireNonNull(formatStrategy);
        this.domain = requireNonNull(domain);
    }

    @Override
    public boolean isLoggable(int priority, String tag) {
        return true;
    }

    @Override
    public void log(int priority, String tag, String message) {
        formatStrategy.log(priority, tag, message);
    }


    public static int getDomain() {
        return domain;
    }
}
