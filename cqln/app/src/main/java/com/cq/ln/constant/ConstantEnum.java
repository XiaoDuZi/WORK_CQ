package com.cq.ln.constant;

/**
 * Created by Administrator on 2016/3/17.
 */
public class ConstantEnum {

//    public static final int USER_INFOS = 1;//个人信息
//    public static final int LATELY_PLAY2 = 2;//最近播放
//    public static final int COLLECTION_MUSIC = 3;//收藏

//    public static final int COLLECTIONTYPE_SPECAIL = 4;//专辑
//    public static final int COLLECTIONTYPE_SINGLE = 5;//单曲收藏


//    public static final int LATELY_PLAY2 = 2;//最近播放
//    public static final int COLLECTION_MUSIC = 3;//收藏


    public static final int SEARCH = 4;//搜索


    //首页右上角[最近播放],[搜索]，[我的收藏]

    public static final int Main_RecentPlay_ = 1;
    public static final int Main_Search_ = 2;
    public static final int Main_Collection_ = 3;


    public static final int isCollection_Yes = 0;
    public static final int isCollection_NO = 1;


    public static final String broadcast_getSmartCard = "com.ipanel.join.cq.vodauth.EPG_URL";

    //关键词搜索，一页大小
    public static int SearchPageSize = 5;

    //艺术家搜索分页大小
    public static int ArtistSearchPageSize = 8;
    //唱片推荐分页大小
    public static int DiscRecommendPageSize = 8;
    //音乐风格分页大小
    public static int MusicStylePageSize = 8;
    //个人中心
    public static int UserCenter_PageSize = 8;

    //新歌抢先听 分页
    public static int VipPageSize = 8;


    //    收藏接口专用参数枚举 collectionType：（1,0）=（专辑、单曲）
    public static final int collectionType_Specail_for_interface = 1;//专辑
    public static final int collectionType_single_for_interface = 0;//单曲


    public static final String USER_PREF = "userinfo";

    public static String Method_Get_RTSP = "Method_Get_RTSP";


    //    200已订购，201未订购，202黑名单 203 欠费停用
    public static final String Music_Auth_status_IsOrder = "200";
    public static final String Music_Auth_status_NoOrder = "201";
    public static final String Music_Auth_status_blacklist = "202";
    public static final String Music_Auth_status_disable = "203";


    // "isfree": 0,收费,1:免费

    public static final int Player_Auth_charge = 0;
    public static final int Player_Auth_Free = 1;

    //搜索页面
    public static final int Search_OnLineSearch = 0;//在线搜索
    public static final int Search_DiscRecommend = 1;//唱片推荐
    public static final int Search_MusicStyle = 2;//音乐风格
    public static final int Search_Artist = 3;//艺术家

    //子栏目

    public static final int Search_ColumnChild_None = -1;
    public static final int Search_ColumnChild_Zero = 0;
    public static final int Search_ColumnChild_One = 1;
    public static final int Search_ColumnChild_TWo = 2;
    public static final int Search_ColumnChild_Three = 3;
    public static final int Search_ColumnChild_Four = 4;
    public static final int Search_ColumnChild_Five = 5;


    //--------------搜索页面搜索出来的枚举值------start
    //   2是单曲 1是专辑 0是歌手

    public static final int contentType_Artist = 0;
    public static final int contentType_Special = 1;
    public static final int contentType_Single = 2;

    //--------------搜索页面搜索出来的枚举值------end

    //用户头像持久化存储

    public static int UserFace_Index_NO_Setting = -1;

    public static String UserFace_Index = "UserFace_Index";


    //0 已订购  1 未订购

    public static int isOrderMusicServer_Yes = 0;
    public static int isOrderMusicServer_No = 1;


    public static final float USER_ORDER_BALANCE = 0.0f;//业务限定,用户合法就订购,不需要余额大于10


    //首页数据类型---------------------start
    //专辑  单曲 专题  活动  更多
    // 我的收藏  充值有礼  订购有礼 个人信息 二维码 签到 新碟抢先听 电台

    public static final int linkType_default = -1;//不做处理
    public static final int linkType_album = 0;  //专辑
    public static final int linkType_single = 1; //单曲
    public static final int linkType_special = 2; //专题
    public static final int linkType_act = 3; //活动
    public static final int linkType_more = 4; //更多
    public static final int linkType_myCollect = 5; //收藏
    public static final int linkType_rechange = 6; //充值有礼
    public static final int linkType_order = 7; //订购有礼
    public static final int linkType_userInfo = 8; //个人信息
    public static final int linkType_qrCode = 9; //二维码
    public static final int linkType_signIn = 10; //签到
    public static final int linkType_newsDvd = 11; //新碟抢先听
    public static final int linkType_radio = 12; //电台
    public static final int linkType_childSong = 13; //儿歌类型
    public static final int linkType_childSongContent = 14; //儿歌内容
    public static final int linkType_childSonglinkType_special = 15; //儿歌专题
    //首页数据类型---------------------end
}
