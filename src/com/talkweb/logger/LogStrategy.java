package com.talkweb.logger;

/**
 * 定义log格式
 */
public interface LogStrategy {
    void log(int priority, String tag, String message);
}
