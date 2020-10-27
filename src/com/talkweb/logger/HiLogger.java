package com.talkweb.logger;


import com.talkweb.logger.imp.LoggerPrinter;
import ohos.hiviewdfx.HiLog;
import ohos.utils.zson.ZSONArray;
import ohos.utils.zson.ZSONObject;

import static com.talkweb.logger.Utils.requireNonNull;

public class HiLogger {

    /**
     * android 与 ohos 的对应关系
     * <p>
     * 主要兼容 已有android代码
     */
    public static final int VERBOSE = HiLog.LOG_APP;
    public static final int DEBUG = HiLog.DEBUG;
    public static final int INFO = HiLog.INFO;
    public static final int WARN = HiLog.WARN;
    public static final int ERROR = HiLog.ERROR;
    public static final int ASSERT = HiLog.FATAL;


    private static Printer printer = new LoggerPrinter();

    //私有化构造.不允许实例化
    private HiLogger() {
    }

    public static void addLogAdapter(LogAdapter adapter) {
        printer.addAdapter(requireNonNull(adapter));
    }

    public static void clearLogAdapters() {
        printer.clearLogAdapters();
    }

    public static Printer t(String tag) {
        return printer.t(tag);
    }

    public static void d(String message, Object... args) {
        printer.d(message, args);
    }

    public static void d(Object object) {
        printer.d(object);
    }

    public static void i(String message, Object... args) {
        printer.i(message, args);
    }

    public static void v(String message, Object... args) {
        printer.v(message, args);
    }

    public static void w(String message, Object... args) {
        printer.w(message, args);
    }

    public static void e(String message, Object... args) {
        printer.e(null, message, args);
    }

    public static void e(Throwable throwable, String message, Object... args) {
        printer.e(throwable, message, args);
    }

    public static void json(String json) {
        printer.json(json);
    }

    public static void json(ZSONObject json) {
        printer.json(json);
    }


    public static void json(ZSONArray json) {
        printer.json(json);
    }


}
