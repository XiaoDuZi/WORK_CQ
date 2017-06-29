package com.cq.ln.utils;

import android.text.TextUtils;

import special.DiffAppconfig;

/**
 * Created by fute on 16/10/31.
 */

public class AppTools {

    public static boolean isHifiOffice(){
        return TextUtils.equals("hifiOffice", DiffAppconfig.appTag);
    }

    public static boolean isHifiTest(){
        return TextUtils.equals("hifiTest", DiffAppconfig.appTag);
    }

    public static boolean isSsLife(){
        return TextUtils.equals("ssLife", DiffAppconfig.appTag);
    }

}
