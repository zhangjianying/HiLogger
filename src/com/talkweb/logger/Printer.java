package com.talkweb.logger;

import ohos.utils.zson.ZSONArray;
import ohos.utils.zson.ZSONObject;

/**
 * 打印对象
 */
public interface Printer {
    void addAdapter(LogAdapter adapter);

    void clearLogAdapters();

    void d(String message, Object... args);

    void d(Object object);

    void i(String message, Object... args);

    void w(String message, Object... args);

    void v(String message, Object... args);

    void e(String message, Object... args);

    void e(Throwable throwable, String message, Object... args);

    void json(String json);

    void json(ZSONObject json);

    void json(ZSONArray json);

    Printer t(String tag);
}
