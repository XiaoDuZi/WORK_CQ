package special;


/**
 * Created by godfather on 2016/3/16.
 */
public class DiffHostConfig {
    /**
     * 首页url 配置参数测试的
     * public static String mainUrlParams = "hifiyinle_2798";
     */
    /**
     * 首页url 配置参数正式的
     * public static String mainUrlParams = "hifiyinle_2707";
     */
    public static String mainUrlParams = "hifiyinle_2707";

    public static String videoUrlParams = "hifiyinle_2615";

    //Hifi正式的地址
//    public static String host =  "http://192.168.34.145/";
    public static String host = "http://192.168.16.49/";
    //    public static String host =  "http://192.168.34.81/";
    public static String ott_host = host + "ott/";

    //儿歌正式的地址
    public static String childHost = "http://192.168.34.81:82/";

    //后台图片地址
    public static String imageHost = "http://192.168.34.81:81/news/uploadFile/";

    //正式环境图片地址
    public static String picListHost = "http://192.168.34.81:81";


    //首页地址
    public static String homeHost = ott_host + "index.html";

}
