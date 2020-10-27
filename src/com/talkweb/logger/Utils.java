package com.talkweb.logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.Arrays;

public class Utils {

    /**
     * 如果字符串为null或者length()==0的时候返回true
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * 如果字符串不为null或者length()!=0的时候返回true
     *
     * @param str
     * @return
     */
    public static boolean isNonEmpty(CharSequence str) {
        return str != null || str.length() != 0;
    }

    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        if (nonNull(a) && nonNull(b)) {
            int length = a.length();
            if (length == b.length()) {
                if (a instanceof String && b instanceof String) {
                    return a.equals(b);
                } else {
                    for (int i = 0; i < length; i++) {
                        if (a.charAt(i) != b.charAt(i)) return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 输出错误 stack
     *
     * @param tr
     * @return
     */
    public static String getStackTraceString(Throwable tr) {
        if (isNull(tr)) {
            return "";
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (nonNull(t)) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }


    /**
     * 将对象转为字符串输出
     *
     * @param object
     * @return
     */
    public static String toString(Object object) {
        if (isNull(object)) {
            return "null";
        }
        if (!object.getClass().isArray()) {
            return object.toString();
        }
        if (object instanceof boolean[]) {
            return Arrays.toString((boolean[]) object);
        }
        if (object instanceof byte[]) {
            return Arrays.toString((byte[]) object);
        }
        if (object instanceof char[]) {
            return Arrays.toString((char[]) object);
        }
        if (object instanceof short[]) {
            return Arrays.toString((short[]) object);
        }
        if (object instanceof int[]) {
            return Arrays.toString((int[]) object);
        }
        if (object instanceof long[]) {
            return Arrays.toString((long[]) object);
        }
        if (object instanceof float[]) {
            return Arrays.toString((float[]) object);
        }
        if (object instanceof double[]) {
            return Arrays.toString((double[]) object);
        }
        if (object instanceof Object[]) {
            return Arrays.deepToString((Object[]) object);
        }
        return "Couldn't find a correct type for the object";
    }



    /**
     * 判断对象是否为null
     *
     * @param object 需要判断的对象
     * @return 是否为null
     */
    public static boolean isNull(Object object) {
        return null == object;
    }

    /**
     * 判断对象是否不为null
     *
     * @param object 需要判断的对象
     * @return 是否不为null
     */
    public static boolean nonNull(Object object) {
        return null != object;
    }

    /**
     * 不能为空的对象
     *
     * @param object
     * @param errorMessage
     * @param <T>
     * @return
     */
    public static <T> T requireNonNull(final T object, String errorMessage) {
        if (null == object) {
            throw new NullPointerException(errorMessage);
        }
        return object;
    }

    /**
     * 不能为空的对象
     *
     * @param object
     * @param <T>
     * @return
     */
    public static <T> T requireNonNull(final T object) {
        if (null == object) {
            throw new NullPointerException();
        }
        return object;
    }

}
