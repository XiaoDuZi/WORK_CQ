package com.cq.ln.appConfig;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.cq.ln.constant.ConstantEnum;
import com.cq.ln.utils.Tools;

import special.DiffAppconfig;

/**
 * Created by godfather on 2016/3/21.
 * app 全局变量配置，例如智能卡号
 * <p/>
 * 传递的参数 context 请使用getApplicationContext() 避免出现act的内存泄露
 */
public class Appconfig {
    private static final String KEYNO = "KEYNO";
    private static final String COOKIE = "COOKIE";
    private static final String EPG = "EPG";
    private static final String STBID = "STBID";
    private static final String TRACKID = "TRACKID";

    private static String keyNo = DiffAppconfig.keyNo;
//    private static String keyNo = "9950000002384049";//9950000002477163
    private static String cookie;
    private static String epg_url = "";//从机顶盒获取的
    private static String stbid;//机顶盒系列号
    private static String serviceGroupId;
    private static String trackId;

    private static String balance;
    private static int userIntegral;


    //用户余额
    private static final String BALANCE = "balance";
    //用户积分
    private static final String USERINTEGRAL = "userIntegral";

    //是否订购
    private static final String ISORDER = "isOrder";

    /**
     * @param context
     * @param isOrder
     */
    public static void setOrder(Context context, int isOrder) {
        Tools.setIntegerPreference(context, ISORDER, isOrder);
    }

    /**
     * 判断用户是否订购了hifi音乐套餐 true:已订购
     *
     * @param context
     * @return
     */
    public static boolean isOrderMusicServer(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int isorder = sp.getInt(ISORDER, ConstantEnum.isOrderMusicServer_No);
        return isorder == ConstantEnum.isOrderMusicServer_Yes;
    }


    /**
     * 设置积分
     *
     * @param context
     * @param userIntegral
     */
    public static void setUserIntegral(Context context, int userIntegral) {
        Appconfig.userIntegral = userIntegral;
        Tools.setIntegerPreference(context, USERINTEGRAL, userIntegral);
    }

    /**
     * 获取积分
     *
     * @param context
     * @return
     */
    public static int getUserIntegral(Context context) {
        userIntegral = Tools.getIntegerPreference(context, USERINTEGRAL);
        return userIntegral;
    }


    /**
     * 设置余额
     *
     * @param context
     * @param balance
     */
    public static void setbalance(Context context, String balance) {
        Appconfig.balance = balance;
        if (!TextUtils.isEmpty(balance))
            Tools.setStringPreference(context, BALANCE, balance);
    }

    /**
     * 获取余额
     *
     * @param context
     * @return
     */
    public static String getbalance(Context context) {
        if (TextUtils.isEmpty(balance))
            balance = Tools.getStringPreference(context, BALANCE);
        return balance;
    }


    public static String getStbid(Context context) {
        if (TextUtils.isEmpty(stbid))
            stbid = Tools.getStringPreference(context, STBID);
        return stbid;
    }


    public static String getKeyNo(Context context) {
        if (TextUtils.isEmpty(keyNo))
            keyNo = Tools.getStringPreference(context, KEYNO);
        return keyNo;
    }

    public static String getTrackid(Context context) {
        if (TextUtils.isEmpty(trackId))
            trackId = Tools.getStringPreference(context, TRACKID);
        return trackId;
    }

    public static void setStbid(Context context, String stbid) {
        Appconfig.stbid = stbid;
        if (!TextUtils.isEmpty(stbid))
            Tools.setStringPreference(context, STBID, stbid);
    }


    public static void setKeyNo(Context context, String keyNo) {
        Appconfig.keyNo = keyNo;

        if (!TextUtils.isEmpty(keyNo))
            Tools.setStringPreference(context, KEYNO, keyNo);
    }

    public static void setTrackid(Context context, String trackId) {
        Appconfig.trackId = trackId;

        if (!TextUtils.isEmpty(trackId))
            Tools.setStringPreference(context, TRACKID, trackId);
    }

    public static String getCookie(Context context) {
        if (TextUtils.isEmpty(cookie))
            cookie = Tools.getStringPreference(context, COOKIE);
        return cookie;
    }

    public static void setCookie(Context context, String cookie) {
        Appconfig.cookie = cookie;
        if (!TextUtils.isEmpty(cookie))
            Tools.setStringPreference(context, COOKIE, cookie);
    }

    public static String getServiceGroupId() {
        return serviceGroupId;
    }

    public static void setServiceGroupId(String serviceGroupId) {
        Appconfig.serviceGroupId = serviceGroupId;
    }


    public static String getEpg_url(Context context) {
        if (TextUtils.isEmpty(epg_url))
            epg_url = Tools.getStringPreference(context, EPG);
        return epg_url;
    }

    public static void setEpg_url(Context context, String epg_url) {
        Appconfig.epg_url = epg_url;
        if (!TextUtils.isEmpty(epg_url))
            Tools.setStringPreference(context, EPG, epg_url);

    }


}
