package com.old.time.utils;

import android.util.Log;

public class DebugLog {
    private static boolean debug = true;

    synchronized public static void on(boolean is) {
        debug = is;
    }

    public static void d(String tag, String msg) {
        if (debug) {
            int strLength = msg.length();
            int start = 0;
            int end = LOG_MAXLENGTH;
            for (int i = 0; i < 1000; i++) {
                //剩下的文本还是大于规定长度则继续重复截取并输出
                if (strLength > end) {
                    Log.d(tag + i, msg.substring(start, end));
                    start = end;
                    end = end + LOG_MAXLENGTH;

                } else {
                    Log.d(tag, msg.substring(start, strLength));

                    break;
                }
            }
        }
    }

    public static void e(String tag, String msg) {
        if (debug) Log.e(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (debug) Log.i(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (debug) Log.v(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (debug) Log.w(tag, msg);
    }

    //规定每段显示的长度
    private static int LOG_MAXLENGTH = 2000;

}
