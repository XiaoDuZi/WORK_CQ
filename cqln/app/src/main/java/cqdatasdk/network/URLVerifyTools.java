package cqdatasdk.network;

import android.text.TextUtils;

/**
 * Created by godfather on 2016/3/16.
 * 统一url合法性检验处理类
 */
public class URLVerifyTools {


    /**
     * @param url 校验url的合法性，添加http头,仅仅适用于首页
     * @return
     */
    public static String formatMainImageUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        if (url.toLowerCase().startsWith("http"))
            return url;
        return HostConfig.picMainHost + url;

    }


    /**
     * @param url 二级页面，三级页面的图片地址处理
     * @return
     */
    public static String formatPicListImageUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        if (url.toLowerCase().startsWith("http"))
            return url;
        return HostConfig.picListHost + url;

    }

//    public static String formatArtistImageUrl(String url){
//        if (TextUtils.isEmpty(url)){
//            return "";
//        }
//        if (url.toLowerCase().startsWith("http"))
//            return url;
//        return HostConfig.picListHost +url;
//    }


    /**
     * 校验url的合法性，添加http头,仅仅适用于:活动，签到，充值有礼，订购有礼
     *
     * @param url webView 加载活动，签到，充值有礼，订购有礼，链接的拼接前缀：http://192.168.34.81/HiFi_cq/
     * @return
     */
    public static String formatWebViewUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        if (url.toLowerCase().startsWith("http"))
            return url;
        return HostConfig.webViewUrlPrefix + url;

    }


}
