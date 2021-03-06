package com.talkweb.logger.imp;

import com.talkweb.logger.LogAdapter;
import com.talkweb.logger.Printer;
import com.talkweb.logger.Utils;
import ohos.utils.zson.ZSONArray;
import ohos.utils.zson.ZSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.talkweb.logger.HiLogger.*;
import static com.talkweb.logger.Utils.*;

public class LoggerPrinter implements Printer {

    private final ThreadLocal<String> localTag = new ThreadLocal<>();
    private final List<LogAdapter> logAdapters = new ArrayList<>();

    private JsonFormatTool jsonFormat = new JsonFormatTool();

    @Override
    public Printer t(String tag) {
        if (tag != null) {
            localTag.set(tag);
        }
        return this;
    }

    @Override
    public void addAdapter(LogAdapter adapter) {
        logAdapters.add(requireNonNull(adapter));
    }

    @Override
    public void clearLogAdapters() {
        //通知释放 独占资源
        for (LogAdapter adp : logAdapters) {
            adp.destory();
        }
        logAdapters.clear();
    }

    @Override
    public void d(String message, Object... args) {
        _log(DEBUG, null, message, args);
    }

    public void d(Object object) {
        _log(DEBUG, null, Utils.toString(object));
    }

    @Override
    public void i(String message, Object... args) {
        _log(INFO, null, message, args);
    }

    @Override
    public void v(String message, Object... args) {
        _log(VERBOSE, null, message, args);
    }

    @Override
    public void w(String message, Object... args) {
        _log(WARN, null, message, args);
    }

    @Override
    public void wtf(String message, Object... args) {
        _log(ASSERT, null, message, args);
    }

    public void e(String message, Object... args) {
        e(null, message, args);
    }

    public void e(Throwable throwable, String message, Object... args) {
        _log(ERROR, throwable, message, args);
    }


    public synchronized void log(int priority,
                                 String tag,
                                 String message,
                                 Throwable throwable) {
        if (nonNull(throwable) && nonNull(message)) {
            message += " : " + getStackTraceString(throwable);
        }
        if (nonNull(throwable) && isNull(message)) {
            message = getStackTraceString(throwable);
        }
        if (Utils.isEmpty(message)) {
            message = "Empty/NULL log message";
        }

        for (LogAdapter adapter : logAdapters) {
            if (adapter.isLoggable(priority, tag)) {
                adapter.log(priority, tag, message);
            }
        }
    }

    private synchronized void _log(int priority,
                                   Throwable throwable,
                                   String msg,
                                   Object... args) {
        requireNonNull(msg);

        String tag = getTag();
        String message = createMessage(msg, args);
        log(priority, tag, message, throwable);
    }

    private String getTag() {
        String tag = localTag.get();
        if (tag != null) {
            localTag.remove();
            return tag;
        }
        return null;
    }

    private String createMessage(String message, Object... args) {
        return isNull(args) || args.length == 0 ? message : String.format(message, args);
    }

    @Override
    public synchronized void json(String json) {
        if (Utils.isEmpty(json)) {
            w("Empty/Null json content");
            return;
        }
        try {
            json = json.trim();
            if (json.startsWith("{")) {
                ZSONObject jsonObject = ZSONObject.stringToZSON(json);
                String message = ZSONObject.toZSONString(jsonObject);
                message = jsonFormat.formatJson(message);
                d(message);
                return;
            }
            if (json.startsWith("[")) {
                ZSONArray jsonArray = ZSONArray.stringToZSONArray(json);
                String message = jsonArray.toString();
                message = jsonFormat.formatJson(message);
                d(message);
                return;
            }
            e("Invalid Json");
        } catch (Exception e) {
            e("Invalid Json");
        }
    }

    @Override
    public synchronized void json(ZSONObject json) {
        if (isNull(json)) {
            w("Empty/Null json content");
            return;
        }
        String message = jsonFormat.formatJson(ZSONObject.toZSONString(json));
        d(message);
    }

    @Override
    public synchronized void json(ZSONArray json) {
        if (isNull(json)) {
            w("Empty/Null json content");
            return;
        }
        String message = jsonFormat.formatJson(json.toString());
        d(message);
    }
}
