package com.talkweb.logger;

public interface LogAdapter {

    /**
     * 是否允许记录日志  false--不记录日志
     * 可以结合编译等级 做开关
     *
     * @param priority
     * @param tag
     * @return
     */
    boolean isLoggable(int priority, String tag);

    void log(int priority, String tag, String message);
}
