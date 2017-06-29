package cqdatasdk.network;


import special.DiffHostConfig;

/**
 * Created by godfather on 2016/3/16.
 */
public class HostConfig {
    public static String path = "CQTVWeb_edu/";
//    public static String path = "utvgoWeb/hifi/";
    public static String groovyPath = "utvgoWeb/tvutvgo/groovy/";

    //重庆api地址  分为测试和正式的
    public static String host = DiffHostConfig.host;

    /**
     * 首页url 配置参数测试的
     * public static String mainUrlParams = "hifiyinle_2798";
     */
    /**
     * 首页url 配置参数正式的
     * public static String mainUrlParams = "hifiyinle_2707";
     */

    //这个参数右build.gradle里面配置，不同打包不同的数值
    public static String mainUrlParams = DiffHostConfig.mainUrlParams;

    //    广州内网域名路径
//    public static String picMainHost = "http://172.17.17.130:8088/news/uploadFile/";
//    重庆域名正式路径
//    public static String picMainHost = "http://192.168.34.81:81/news/uploadFile/";
//    时尚生活测试路径
    public static String picMainHost = DiffHostConfig.imageHost;
    //重庆所有二级三级页面的图片地址
    //广州所有二级三级页面的图片地址
    //public static String picListHost = "";  //暂未
    public static String picListHost = DiffHostConfig.picListHost;

    /**
     * webView 加载活动，签到，充值有礼，订购有礼，链接的拼接前缀：http://192.168.34.81/HiFi_cq/
     * 正式的签到
     */
    public static String webViewUrlPrefix = host + "HiFi_cq/";
    //测试的签到
//    public static String webViewUrlPrefix = "http://192.168.49.147/" + "HiFi_cq/";

    /**
     * 个人中心使用，签到链接：//http://192.168.34.81/HiFi_cq/topic/sign/index.html?menuPos=6
     */
    public static String signInUrl = webViewUrlPrefix + "topic/sign/index.html?menuPos=6";

    /**
     * 个人中心使用，充值链接：http://192.168.34.81/HiFi_cq/android_recharge.html
     */
    public static String rechangeUrl = webViewUrlPrefix + "android_recharge.html";

}
