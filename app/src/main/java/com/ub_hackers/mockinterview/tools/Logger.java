package com.ub_hackers.mockinterview.tools;

import android.util.Log;

public class Logger {

    public static void log(int level, String tag, String msg) {

        tag = "v3_"+tag;

//        if(!BuildConfig.IS_DEVELOP_VERSION) return;

        switch (level){
            case Log.ERROR:
                Log.e(tag, msg);
                break;
            case Log.INFO:
                Log.i(tag, msg);
                break;
            case Log.WARN:
                Log.w(tag, msg);
                break;
            case Log.DEBUG:
            default:
                Log.i(tag, msg);
                break;
        }
    }

    public static void log(String tag, String msg) {
        log(Log.DEBUG, tag, msg);
    }

    public static void error(String tag, String msg) {
        log(Log.ERROR, tag, msg);
    }

    public static void warn(String tag, String msg) {
        log(Log.WARN, tag, msg);
    }

}
