package com.cq.ln.utils;


import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.cq.ln.BuildConfig;

public class XLog {

    private static final boolean DEBUG = BuildConfig.DEBUG;


    public static void showDebugTip(Context context, String string) {
        if (TextUtils.isEmpty(string))
            return;
        Toast debugLongTip = Toast.makeText(context, string, Toast.LENGTH_SHORT);
        debugLongTip.setText(string);
        debugLongTip.show();

        d(string);
    }


    public static void v(String tag, String msg) {
        if (DEBUG) {
            android.util.Log.v(tag, msg);
        }
    }

    /**
     * @param msg 默认android.util.Log.d("TAG", msg);
     */
    public static void d(String msg) {
        if (DEBUG) {
            android.util.Log.d("TAG", msg);
        }
    }

    public static void ReleaseD(String msg) {
        android.util.Log.d("ReleaseD", msg);
    }


    public static void v(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            android.util.Log.v(tag, msg, tr);
        }
    }

    public static void d(String tag, String msg) {
//        if (DEBUG) {
        android.util.Log.d(tag, msg);
//        }
    }

    public static void d(String tag, String msg, Throwable tr) {

        if (DEBUG) {
            android.util.Log.d(tag, msg, tr);
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            android.util.Log.i(tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            android.util.Log.i(tag, msg, tr);
        }
    }

    public static void w(String tag, String msg) {
        if (DEBUG) {
            android.util.Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            android.util.Log.w(tag, msg, tr);
        }
    }

    public static void w(String tag, Throwable tr) {
        if (DEBUG) {
            android.util.Log.w(tag, tr);
        }
    }

    public static void e(String tag, String msg) {
        if (DEBUG) {
            android.util.Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            android.util.Log.e(tag, msg, tr);
        }
    }
}
