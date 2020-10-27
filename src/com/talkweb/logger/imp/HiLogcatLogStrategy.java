package com.talkweb.logger.imp;

import com.talkweb.logger.LogStrategy;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import static com.talkweb.logger.HiLogger.*;
import static com.talkweb.logger.Utils.isNull;
import static com.talkweb.logger.Utils.requireNonNull;

public class HiLogcatLogStrategy implements LogStrategy {
    static final HiLogLabel DEFAULT_TAG = new HiLogLabel(HiLog.LOG_APP, 0x201, "NO_TAG");

    @Override
    public void log(int priority, String tag, String message) {
        requireNonNull(message);
        if (isNull(tag)) {
            _Log(priority, DEFAULT_TAG, message);
        } else {
            HiLogLabel TAG = new HiLogLabel(HiLog.LOG_APP, HMOSLogAdapter.getDomain(), tag);
            _Log(priority, TAG, message);
        }
    }

    private void _Log(int priority, HiLogLabel tag, String message) {
        switch (priority) {
            case VERBOSE:
            case DEBUG: //目前debug日志不会输出.暂时将debug默认置为info
            case INFO:
                HiLog.info(tag, message);
                break;
            case WARN:
                HiLog.warn(tag, message);
                break;
            case ERROR:
                HiLog.error(tag, message);
                break;
            case ASSERT:
                HiLog.fatal(tag, message);
                break;
        }

    }
}
