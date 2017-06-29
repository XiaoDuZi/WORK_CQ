package cqdatasdk.network;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.cq.ln.constant.ConstantEnum;
import com.cq.ln.utils.HiFiDialogTools;
import com.cq.ln.utils.LoadingDialogTools;
import com.cq.ln.utils.Tools;
import com.cq.ln.utils.XLog;
import com.google.gson.Gson;

import java.net.URLEncoder;

import cqdatasdk.bean.ArtistAlbumListBean;
import cqdatasdk.bean.ArtistReleaListbean;
import cqdatasdk.bean.CollectionSinglebean;
import cqdatasdk.bean.CollectionSpecailBean;
import cqdatasdk.bean.CollectionSucceedBean;
import cqdatasdk.bean.DeletedCollectionSucceedBean;
import cqdatasdk.bean.DiscRecommendFilterBean;
import cqdatasdk.bean.DiscRecommendListBean;
import cqdatasdk.bean.GetBackGroundMusicBean;
import cqdatasdk.bean.HiFiMainPageBean;
import cqdatasdk.bean.MusicStyleFilterBean;
import cqdatasdk.bean.MusicStyleListBean;
import cqdatasdk.bean.OrderMusicServerBean;
import cqdatasdk.bean.OrderMusicServerBean_;
import cqdatasdk.bean.ParentLockBean;
import cqdatasdk.bean.ParentLockBean_;
import cqdatasdk.bean.RecentPlayBean;
import cqdatasdk.bean.SearchResultBean;
import cqdatasdk.bean.SetBackgroundMusicBean;
import cqdatasdk.bean.SpecialDetailBean;
import cqdatasdk.bean.TopicsBean;
import cqdatasdk.bean.UserAuthResponeBean;
import cqdatasdk.bean.UserBalanceBean;
import cqdatasdk.bean.UserFaceGetSucceedBean;
import cqdatasdk.bean.UserFaceSetSucceedBean;
import cqdatasdk.bean.VipTypeBean;
import cqdatasdk.bean.VipTypeRelateAlbumBean;
import cqdatasdk.bean.syncUserInfoSucceedBean;
import cqdatasdk.interfaces.IVolleyRequestSuccess;
import cqdatasdk.interfaces.IVolleyRequestfail;
import cqdatasdk.utils.JsonUtil;
import special.DiffHostConfig;

/**
 * Created by godfather on 2016/3/16.
 */
public class AsyncHttpRequest {
    private final String TAG = "AsyncHttpRequest";

    public RequestQueue requestQueue;

    public RequestQueue getRequestQueue(Context context) {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(context);
        return requestQueue;
    }

    /**
     * @param url                  快速解析成java bean 对象,通用方法
     * @param className
     * @param context
     * @param volleyRequestSuccess
     * @param iVolleyRequestfail
     * @param <T>
     */
    private <T> void getJavaBean(String url, final Class<T> className, final Context context, final IVolleyRequestSuccess volleyRequestSuccess, final IVolleyRequestfail iVolleyRequestfail) {
        getJavaBean(url, className, context, null, volleyRequestSuccess, iVolleyRequestfail);
    }

    private <T> void getJavaBean(String url, final Class<T> className, final Context context, final IVolleyRequestSuccess volleyRequestSuccess, final IVolleyRequestfail iVolleyRequestfail, String
            loadingStr) {
        getJavaBean(url, className, context, null, volleyRequestSuccess, iVolleyRequestfail, loadingStr);
    }

    private <T> void getJavaBean(String url, final Class<T> className, final Context context, final String key, final IVolleyRequestSuccess volleyRequestSuccess, final IVolleyRequestfail
            iVolleyRequestfail) {
        getJavaBean(url, className, context, key, volleyRequestSuccess, iVolleyRequestfail, null);
    }

    //通用
    private <T> void getJavaBean(String url, final Class<T> className, final Context context, final String key, final IVolleyRequestSuccess volleyRequestSuccess, final IVolleyRequestfail
            iVolleyRequestfail, final String loadingStr) {

        XLog.d("请求连接：" + url);
        final LoadingDialogTools loadingDialogTools = new LoadingDialogTools();//loading需要独立出来
        VolleyRequest volleyRequest = new VolleyRequest(context, getRequestQueue(context), new IVolleyRequestSuccess() {
            @Override
            public void onSucceeded(String method, String key, Object object) {
                if (className == null) {
                    if (!TextUtils.isEmpty(loadingStr)) {
                        loadingDialogTools.Close();
                    }
                    return;
                }
                Gson gson = new Gson();
                object = gson.fromJson(object.toString(), className);
                if (volleyRequestSuccess != null) {
                    try {
                        volleyRequestSuccess.onSucceeded(method, key, object);
                        XLog.d("请求成功返回：" + object.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (!TextUtils.isEmpty(loadingStr)) {
                    loadingDialogTools.Close();
                }
            }
        }, new IVolleyRequestfail() {
            @Override
            public void onFailed(String method, String key, int errorTipid) {
                if (errorTipid != 0) {
                    Tools.showTip(context.getApplicationContext(), errorTipid);
                    XLog.d("请求失败：" + context.getResources().getString(errorTipid));
                }
                if (iVolleyRequestfail != null) {
                    try {
                        iVolleyRequestfail.onFailed(method, key, errorTipid);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (!TextUtils.isEmpty(loadingStr)) {
                    loadingDialogTools.Close();
                }
            }
        });
        if (!TextUtils.isEmpty(loadingStr)) {
            try {
                loadingDialogTools.showloadingDialog(context, loadingStr, null);
            } catch (Exception e) {
                XLog.d(TAG, "e:" + e);
                e.printStackTrace();
            }
        }
        volleyRequest.executeGetRequest(key, addParam(context, url));
    }

    /**
     * @param context              获取首页数据：
     *                             linux版本：http://192.168.34.81/utvgoWeb/hifi/hifiHome/homePage.action?urlCode=hifiyinle_2615
     *                             <p/>
     *                             Android 1.2 正式版本：http://192.168.34.81/utvgoWeb/hifi/hifiHome/homePage.action?urlCode=hifiyinle_2707
     *                             <p/>
     *                             Android 1.3 正式版本：http://192.168.34.81/utvgoWeb/hifi/hifiHome/homePage.action?urlCode=hifiyinle_2798
     * @param volleyRequestSuccess
     * @param iVolleyRequestfail
     * @param loadingString
     */
    public void getMainPageData(final Context context, final IVolleyRequestSuccess volleyRequestSuccess, final IVolleyRequestfail iVolleyRequestfail, String loadingString) {
        final LoadingDialogTools mainLoading = new LoadingDialogTools();//loading需要独立出来
        String url = HostConfig.host + HostConfig.path + "hifiHome/homePage.action?urlCode=" + HostConfig.mainUrlParams;

        VolleyRequest volleyRequest = new VolleyRequest(context, getRequestQueue(context), new IVolleyRequestSuccess() {
            @Override
            public void onSucceeded(String method, String key, Object object) {

                HiFiMainPageBean mHiFiMainPageBean = JsonUtil.getMode(object.toString(), HiFiMainPageBean.class);
                if (volleyRequestSuccess != null)
                    try {
                        volleyRequestSuccess.onSucceeded(method, null, mHiFiMainPageBean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                mainLoading.Close();

            }
        }, new IVolleyRequestfail() {
            @Override
            public void onFailed(String method, String key, int errorTipid) {
                if (errorTipid != 0) {
                    Tools.showTip(context, errorTipid);
                    if (iVolleyRequestfail != null)
                        XLog.d("请求失败：" + context.getResources().getString(errorTipid));
                }
                try {
                    iVolleyRequestfail.onFailed(method, null, errorTipid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mainLoading.Close();
            }
        });
        if (!TextUtils.isEmpty(loadingString)) {
            try {
                mainLoading.showloadingDialog(context, loadingString, null);
            } catch (Exception e) {
                XLog.d(TAG, "e:" + e);
                e.printStackTrace();
            }
        }

        volleyRequest.executeGetRequest(addParam(context, url));
    }

    /**
     * @param context              获取专辑详情，样例：
     *                             http://192.168.34.81/utvgoWeb/hifi/hifiData/album.action?albumId=13&keyNo=123
     * @param albumIdStr           参数:albumId=888
     * @param keyNo
     * @param volleyRequestSuccess
     * @param fail
     */
    public void getSpecailDetail(final Context context, String albumIdStr, int fileType, String keyNo, final IVolleyRequestSuccess volleyRequestSuccess, final IVolleyRequestfail fail, String
            loadingString) {
        String url = HostConfig.host + HostConfig.path + "hifiData/album.action?" + albumIdStr + "&keyNo=" + keyNo;
        if (fileType == 1) {
            url = DiffHostConfig.childHost + HostConfig.path + "hifiData/album.action?" + albumIdStr + "&keyNo=" + keyNo;
        }
        XLog.d("onSucceeded url==" + url);
        getJavaBean(url, SpecialDetailBean.class, context, null, volleyRequestSuccess, fail, loadingString);
    }

    /**
     * @param context              添加播放记录，样例：
     *                             http://192.168.34.81/utvgoWeb/hifi/hifiData/album.action?albumId=13&keyNo=123
     * @param albumIdStr           参数:albumId=888
     * @param keyNo
     * @param volleyRequestSuccess
     * @param fail
     */
    public void addPlayRecord(final Context context, String recordId, String videoId, String videoName, String playTime, String stayTime, String keyNo, final IVolleyRequestSuccess
            volleyRequestSuccess, final IVolleyRequestfail fail, String
            loadingString) {
        String url = HostConfig.host + HostConfig.path + "tvutvgo/playHistory.action?keyNo=" + keyNo + "&recordId=" + recordId + "&videoId=" + videoId + "&contentId=" +
                videoId + "&videoName=" + Uri.encode(videoName) + "&playTime=" + playTime + "&stayTime=" + stayTime;
        getJavaBean(url, null, context, null, volleyRequestSuccess, fail, loadingString);
    }

    /**
     * @param context              获取专辑详情，样例：
     *                             http://192.168.34.81/utvgoWeb/hifi/hifiData/album.action?albumId=13&keyNo=123
     * @param topicsStr            参数:topicsStr=888
     * @param volleyRequestSuccess
     * @param fail
     */
    public void getTopicsDetail(final Context context, String topicsStr, final IVolleyRequestSuccess volleyRequestSuccess, final IVolleyRequestfail fail, String loadingString) {
        String url = DiffHostConfig.host + HostConfig.groovyPath + "zhuanti.groovy?id=" + topicsStr;

//        if (linkType == 15){
//            url = DiffHostConfig.host + HostConfig.groovyPath + "zhuanti.groovy?id=" + topicsStr;
//        }

        XLog.d("onSucceeded url==" + url);
        getJavaBean(url, TopicsBean.class, context, null, volleyRequestSuccess, fail, loadingString);
    }

    /**
     * @param context              添加收藏 collectionType：（0,1）=（专辑、单曲） http://192.168.34.81/utvgoWeb/hifi/hifiUser/addUserCollection.action?collectionType=1&collectionId=123&keyNo=8002003646694252
     * @param collectionType
     * @param collectionId
     * @param keyNo
     * @param volleyRequestSuccess
     * @param fail                 http://192.168.34.81/utvgoWeb/hifi/hifiUser/addUserCollection.action?collectionType=0&collectionId=1213&keyNo=8002003646694252
     */
    public void collection(Context context, int collectionType, int collectionId, String keyNo, final IVolleyRequestSuccess volleyRequestSuccess, final IVolleyRequestfail fail) {
        String url = HostConfig.host + HostConfig.path + "hifiUser/addUserCollection.action?collectionType=" + collectionType + "&collectionId=" + collectionId + "&keyNo=" + keyNo;
        getJavaBean(url, CollectionSucceedBean.class, context, collectionId + "", volleyRequestSuccess, fail);

    }

    /**
     * @param context              取消收藏 样例:http://192.168.34.81/utvgoWeb/hifi/hifiUser/deleteUserCollection.action?keyNo=8002003646694252&collectionId=111
     * @param collectionId
     * @param keyNo
     * @param volleyRequestSuccess
     * @param fail
     */
    public void deleteUserCollection(Context context, int collectionId, String keyNo, final IVolleyRequestSuccess volleyRequestSuccess, final IVolleyRequestfail fail) {
        String url = HostConfig.host + HostConfig.path + "hifiUser/deleteUserCollection.action?collectionId=" + collectionId + "&keyNo=" + keyNo;
        getJavaBean(url, DeletedCollectionSucceedBean.class, context, collectionId + "", volleyRequestSuccess, fail);


    }


    /**
     * @param context              获取唱片推荐刷选分类列表 样例:http://192.168.34.81/utvgoWeb/hifi/hifiHome/columnList.action?urlCode=hifiyinle_2615
     * @param volleyRequestSuccess
     * @param fail
     */
    public void getDiscRecommendFilterList(Context context, final IVolleyRequestSuccess volleyRequestSuccess, final IVolleyRequestfail fail) {
        String url = HostConfig.host + HostConfig.path + "hifiHome/columnList.action?urlCode=hifiyinle_2615";
        getJavaBean(url, DiscRecommendFilterBean.class, context, volleyRequestSuccess, fail);

    }

    //获取儿歌的分类
    public void getChildSongFilterList(Context context, final IVolleyRequestSuccess volleyRequestSuccess, final IVolleyRequestfail fail) {
        String url = DiffHostConfig.childHost + HostConfig.path + "hifiHome/columnList.action?urlCode=" + DiffHostConfig.videoUrlParams;
        getJavaBean(url, DiscRecommendFilterBean.class, context, volleyRequestSuccess, fail, "加载中...");

    }

    /**
     * @param context              获取音乐风格 刷选分类列表 样例：http://192.168.34.81/utvgoWeb/hifi/hifiData/styleList.action
     * @param volleyRequestSuccess
     * @param fail
     */
    public void getMusicStyleFilterList(Context context, final IVolleyRequestSuccess volleyRequestSuccess, final IVolleyRequestfail fail) {
        String url = HostConfig.host + HostConfig.path + "hifiData/styleList.action";
        getJavaBean(url, MusicStyleFilterBean.class, context, volleyRequestSuccess, fail);

    }

    /**
     * @param context              设置背景音乐  样例：http://127.0.0.1:8088/utvgoWeb/hifi/hifiData/collectBackgroundmusic.action
     * @param volleyRequestSuccess
     * @param fail
     */
    public void setBackGroundMusic(Context context, String trackId, String keyNo, final IVolleyRequestSuccess volleyRequestSuccess, final IVolleyRequestfail fail) {
        String url = HostConfig.host + HostConfig.path + "hifiData/collectBackgroundmusic.action?keyNo=" + keyNo + "&trackId=" + trackId;
        getJavaBean(url, SetBackgroundMusicBean.class, context, volleyRequestSuccess, fail);

    }

    /**
     * @param context 搜索页面，输入关键词，搜索内容 样例:http://192.168.34.81/utvgoWeb/hifi/hifiData/search.action?pageno=1&size=5&searchKeyWord=LDH
     * @param keyWord
     * @param pageno  分页
     * @param success
     * @param fail
     */
    public void getSearchResultByKeyWord(Context context, String keyWord, int pageno, IVolleyRequestSuccess success, IVolleyRequestfail fail, String loadingString) {
        String url = HostConfig.host + HostConfig.path + "hifiData/search.action?pageno=" + pageno + "&size=" + ConstantEnum.SearchPageSize + "&searchKeyWord=" + keyWord;
        getJavaBean(url, SearchResultBean.class, context, null, success, fail, loadingString);
    }

    /**
     * @param context 儿歌搜索页面，输入关键词，搜索内容 样例:http://192.168.34.81:82/utvgoWeb/hifi/hifiData/search.action?pageno=1&size=5&fileType=1&searchKeyWord=E
     * @param keyWord
     * @param pageno  分页
     * @param success
     * @param fail
     */
    public void getChildSearchResultByKeyWord(Context context, String keyWord, int pageno, IVolleyRequestSuccess success, IVolleyRequestfail fail, String loadingString) {
        String url = DiffHostConfig.childHost + HostConfig.path + "hifiData/search.action?pageno=" + pageno + "&size=" + ConstantEnum.SearchPageSize + "&searchKeyWord=" + keyWord +
                "&fileType=1";
        getJavaBean(url, SearchResultBean.class, context, null, success, fail, loadingString);
    }

    /**
     * 获得用户收藏单曲
     *
     * @param context http://192.168.34.81/utvgoWeb/hifi/hifiUser/userCollection.action?keyNo=8002003646694252&pageno=1&size=8&collectionType=0
     * @param pageno
     * @param keyNo
     * @param success
     * @param fail
     */
    public void getUserCollectSingleBean(Context context, int pageno, String keyNo, IVolleyRequestSuccess success, IVolleyRequestfail fail, String loadingString) {
        String url = HostConfig.host + HostConfig.path + "hifiUser/userCollection.action?keyNo=" + keyNo + "&pageno=" + pageno + "&size=" + ConstantEnum.UserCenter_PageSize + "&collectionType=" +
                ConstantEnum.collectionType_single_for_interface;
        getJavaBean(url, CollectionSinglebean.class, context, String.valueOf(pageno), success, fail, loadingString);
    }


    /**
     * 获得用户收藏的专辑 http://192.168.34.81/utvgoWeb/hifi/hifiUser/userCollection.action?keyNo=8002003646694252&pageno=1&size=8&collectionType=1
     *
     * @param context
     * @param pageno
     * @param keyNo
     * @param success
     * @param fail
     */
    public void getUserCollectSpecailBean(Context context, int pageno, String keyNo, IVolleyRequestSuccess success, IVolleyRequestfail fail, String loadingString) {
        String url = HostConfig.host + HostConfig.path + "hifiUser/userCollection.action?keyNo=" + keyNo + "&pageno=" + pageno + "&size=" + ConstantEnum.UserCenter_PageSize + "&collectionType=" +
                ConstantEnum.collectionType_Specail_for_interface;
        getJavaBean(url, CollectionSpecailBean.class, context, String.valueOf(pageno), success, fail, loadingString);
    }


    /**
     * @param context 用户最近播放 http://172.16.146.56:8083/utvgoWeb/hifi/hifiUser/userPlayList.action?keyNo=8002003646694252&pageno=1&size=8
     * @param pageno
     * @param keyNo
     * @param success
     * @param fail
     */
    public void getUserRecentPlayBean(Context context, int pageno, String keyNo, IVolleyRequestSuccess success, IVolleyRequestfail fail, String loadingString) {
        String url = HostConfig.host + HostConfig.path + "hifiUser/userPlayList.action?keyNo=" + keyNo + "&pageno=" + pageno + "&size=" + ConstantEnum.UserCenter_PageSize;
        getJavaBean(url, RecentPlayBean.class, context, String.valueOf(pageno), success, fail, loadingString);
    }


    /**
     * * 设置用户头像,样例：http://192.168.34.81/utvgoWeb/hifi/hifiUser/updateUserImg.action?keyNo=9950000000321800&userImgIndex=0
     *
     * @param context
     * @param keyNo
     * @param userImgIndex
     * @param success
     * @param fail
     * @param loadingString
     */
    public void setUserFace(Context context, String keyNo, int userImgIndex, IVolleyRequestSuccess success, IVolleyRequestfail fail, String loadingString) {
        String url = HostConfig.host + HostConfig.path + "hifiUser/updateUserImg.action?keyNo=" + keyNo + "&userImgIndex=" + userImgIndex;
        getJavaBean(url, UserFaceSetSucceedBean.class, context, userImgIndex + "", success, fail, loadingString);
    }

    /**
     * 获取用户头像 ：http://192.168.34.81/utvgoWeb/hifi/hifiUser/getUserImg.action?keyNo=9950000000321800
     *
     * @param context
     * @param keyNo
     * @param success
     * @param fail
     * @param loadingString
     */
    public void getUserFace(Context context, String keyNo, IVolleyRequestSuccess success, IVolleyRequestfail fail, String loadingString) {
        String url = HostConfig.host + HostConfig.path + "hifiUser/getUserImg.action?keyNo=" + keyNo;
        getJavaBean(url, UserFaceGetSucceedBean.class, context, success, fail, loadingString);
    }

    /**
     * 同步BOSS用户信息作用
     * http://192.168.34.81/utvgoWeb/hifi/hifiUser/getUserInfo.action?keyNo=9950000001855937
     *
     * @param context
     * @param keyNo
     * @param success
     * @param fail
     */
    public void syncUserInfo(Context context, String keyNo, IVolleyRequestSuccess success, IVolleyRequestfail fail) {
        String url = HostConfig.host + HostConfig.path + "hifiUser/getUserInfo.action?keyNo=" + keyNo;
        getJavaBean(url, syncUserInfoSucceedBean.class, context, null, success, fail);
    }

    /**
     * @param context       搜索艺术家关联数据列表，样例：http://192.168.34.81/utvgoWeb/hifi/hifiData/artistList.action?pageno=1&size=5&searchKeyWord=A
     * @param searchKeyWord
     * @param pageno
     * @param success
     * @param fail
     */
    public void getArtistReList(Context context, String searchKeyWord, int pageno, IVolleyRequestSuccess success, IVolleyRequestfail fail, String loadingString) {
        StringBuffer sb = new StringBuffer();//用StringBuffer大大减少垃圾产生
        sb.append(HostConfig.host).append(HostConfig.path).append("hifiData/artistList.action?pageno=")
                .append(pageno).append("&size=").append(ConstantEnum.ArtistSearchPageSize).append("&searchKeyWord=")
                .append(searchKeyWord);
        String url = sb.toString();//HostConfig.host + HostConfig.path + "hifiData/artistList.action?pageno=" + pageno + "&size=" + ConstantEnum.ArtistSearchPageSize + "&searchKeyWord=" +
        // searchKeyWord;
        getJavaBean(url, ArtistReleaListbean.class, context, searchKeyWord, success, fail, loadingString);
    }

    /**
     * @param context  搜索唱片推荐关联数据列表，样例：http://192.168.34.81/utvgoWeb/hifi/hifiData/albumByColumn.action?columnId=2620&pageno=1&size=8
     * @param columnId
     * @param pageno
     * @param success
     * @param fail
     */
    public void getDiscRecommendList(Context context, int columnId, int pageno, IVolleyRequestSuccess success, IVolleyRequestfail fail, String loadingString) {
        String url = HostConfig.host + HostConfig.path + "hifiData/albumByColumn.action?columnId=" + columnId + "&pageno=" + pageno + "&size=" + ConstantEnum.DiscRecommendPageSize;
        getJavaBean(url, DiscRecommendListBean.class, context, String.valueOf(columnId), success, fail, loadingString);
    }

    /**
     * @param context  儿歌推荐关联数据列表，样例：http://192.168.34.81/utvgoWeb/hifi/hifiData/albumByColumn.action?columnId=2620&pageno=1&size=8
     * @param columnId
     * @param pageno
     * @param success
     * @param fail
     */
    public void getChildRecommendList(Context context, int columnId, int pageno, IVolleyRequestSuccess success, IVolleyRequestfail fail, String loadingString) {
        String url = DiffHostConfig.childHost + HostConfig.path + "hifiData/albumByColumn.action?columnId=" + columnId + "&pageno=" + pageno + "&size=" + 20;
        getJavaBean(url, DiscRecommendListBean.class, context, String.valueOf(columnId), success, fail, loadingString);
    }

    /**
     * @param context 音乐风格数据,样例:http://192.168.34.81/utvgoWeb/hifi/hifiData/albumList.action?styleId=5&pageno=1&size=5
     * @param styleId
     * @param pageno
     * @param success
     * @param fail
     */
    public void getMusicStyleList(Context context, int styleId, int pageno, IVolleyRequestSuccess success, IVolleyRequestfail fail, String loadingString) {
        String url = HostConfig.host + HostConfig.path + "hifiData/albumList.action?styleId=" + styleId + "&pageno=" + pageno + "&size=" + ConstantEnum.MusicStylePageSize;
        getJavaBean(url, MusicStyleListBean.class, context, String.valueOf(styleId), success, fail, loadingString);
    }

    /**
     * @param context       用户鉴权 ,样例:http://192.168.34.81/utvgoWeb/hifi/hifiData/single.action?singleId=14621&keyNo=9950000002272843
     * @param singleId
     * @param keyNo
     * @param success
     * @param fail
     * @param loadingString
     */
    public void getUserAuth(Context context, String singleId, int fileType, String keyNo, IVolleyRequestSuccess success, IVolleyRequestfail fail, String loadingString) {
        String url = HostConfig.host + HostConfig.path + "hifi/hifiData/single.action?singleId=" + singleId + "&keyNo=" + keyNo;
        getJavaBean(url, UserAuthResponeBean.class, context, null, success, fail, loadingString);
    }

    /**
     * @param context  家长锁 :http://192.168.34.81/utvgoWeb/hifi/hifiUser/optParentLocker.action?keyNo=9950000002272843&dealType=1
     * @param dealType
     * @param keyNo
     * @param success
     * @param fail
     */
    public void getParentLock(Context context, String dealType, String keyNo, IVolleyRequestSuccess success, IVolleyRequestfail fail) {
        String url = HostConfig.host + HostConfig.path + "hifiUser/optParentLocker.action?keyNo=" + keyNo + "&dealType=" + dealType;
        getJavaBean(url, ParentLockBean.class, context, null, success, fail);
    }

    /**
     * 新版家长锁 样例:http://192.168.34.81/utvgoWeb/hifi/hifiUser/userLock.action?keyNo=9950000002272843&dealType=1
     *
     * @param context
     * @param dealType
     * @param keyNo
     * @param success
     * @param fail
     */
    public void getParentLock_(Context context, String dealType, String keyNo, IVolleyRequestSuccess success, IVolleyRequestfail fail) {
        String url = HostConfig.host + HostConfig.path + "hifiUser/userLock.action?keyNo=" + keyNo + "&dealType=" + dealType;
        getJavaBean(url, ParentLockBean_.class, context, null, success, fail, "请稍后...");
    }


    /**
     * http://192.168.34.81/utvgoWeb/hifi/hifiUser/getUserInfo.action?keyNo=9950000002272843
     *
     * @param context
     * @param keyNo
     * @param success
     * @param fail
     */
    public void getUserBalance(Context context, String keyNo, IVolleyRequestSuccess success, IVolleyRequestfail fail, String loadingTip) {
        String url = HostConfig.host + HostConfig.path + "hifiUser/getUserInfo.action?keyNo=" + keyNo;
        getJavaBean(url, UserBalanceBean.class, context, null, success, fail, loadingTip);
    }

    /**
     * 订购开通10元包月HIFI音乐
     *
     * @param context http://192.168.34.81/utvgoWeb/hifi/hifiUser/orderProduct.action?keyNo=9950000002272843
     * @param keyNo
     * @param success
     * @param fail
     */
    public void OrderMusicServer(Context context, String keyNo, IVolleyRequestSuccess success, IVolleyRequestfail fail) {
        String url = HostConfig.host + HostConfig.path + "hifiUser/orderProduct.action?keyNo=" + keyNo;
        getJavaBean(url, OrderMusicServerBean.class, context, null, success, fail);
    }

    /**
     * @param context 新版订购接口 http://192.168.34.81/utvgoWeb/hifi/hifiUser/comboValid.action?keyNo=9950000002272843
     * @param keyNo
     * @param success
     * @param fail
     */
    public void OrderMusicServer_(Context context, String keyNo, IVolleyRequestSuccess success, IVolleyRequestfail fail) {
        String url = HostConfig.host + HostConfig.path + "hifiUser/comboValid.action?keyNo=" + keyNo;
        getJavaBean(url, OrderMusicServerBean_.class, context, null, success, fail);
    }


    /**
     * 新歌抢先听 分类列表 http://192.168.34.81/utvgoWeb/hifi/hifiUser/getVipType.action?keyNo=9950000002384049
     *
     * @param context
     * @param keyNo
     * @param success
     * @param requestfail
     * @param loadstring
     */
    public void getVipType(Context context, String keyNo, IVolleyRequestSuccess success, IVolleyRequestfail requestfail, String loadstring) {
        String url = HostConfig.host + HostConfig.path + "hifiUser/getVipType.action?keyNo=" + keyNo;
        getJavaBean(url, VipTypeBean.class, context, null, success, requestfail, loadstring);
    }

    public void getBgMusic(Context context, String keyNo, IVolleyRequestSuccess success, IVolleyRequestfail requestfail, String loadstring) {
        String url = HostConfig.host + HostConfig.path + "hifiData/getBackgroundMusic.action?keyNo=" + keyNo;
        getJavaBean(url, GetBackGroundMusicBean.class, context, null, success, requestfail, loadstring);
    }


    /**
     * 新歌抢先听 关联专辑 http://192.168.34.81/utvgoWeb/hifi/hifiData/albumList.action?styleId=8&pageno=1&size=8
     *
     * @param context
     * @param styleId
     * @param pageNo
     * @param success
     * @param requestfail
     * @param loadString
     */
    public void getVipTypeRelateAlbum(Context context, int styleId, int pageNo, IVolleyRequestSuccess success, IVolleyRequestfail requestfail, String loadString) {
        String url = HostConfig.host + HostConfig.path + "hifiData/albumList.action?styleId=" + styleId + "&pageno=" + pageNo + "&size=" + ConstantEnum.VipPageSize;
        getJavaBean(url, VipTypeRelateAlbumBean.class, context, styleId + "", success, requestfail, loadString);
    }

    /**
     * 获取艺术家专辑列表
     * http://172.16.146.56:8083/utvgoWeb/hifi/hifiData/albumListByArtis.action?artisId=18&pageno=1&size=8 艺术家获取专辑列表
     *
     * @param context
     * @param url
     * @param volleyRequestSuccess
     * @param iVolleyRequestfail
     */
    public void getArtistSpecialList(final Context context, String url, final IVolleyRequestSuccess volleyRequestSuccess, final IVolleyRequestfail iVolleyRequestfail) {
        //String url = HostConfig.host + HostConfig.path + "hifiData/albumListByArtis.action?artisId=" + artisId + "&pageno=" + pageno + "&size=" + size;
        VolleyRequest volleyRequest = new VolleyRequest(context, getRequestQueue(context), new IVolleyRequestSuccess() {
            @Override
            public void onSucceeded(String method, String key, Object object) {
                ArtistAlbumListBean mArtistAlbumListBean = JsonUtil.getMode(object.toString(), ArtistAlbumListBean.class);
                if (volleyRequestSuccess != null)
                    try {
                        volleyRequestSuccess.onSucceeded(method, null, mArtistAlbumListBean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }, new IVolleyRequestfail() {
            @Override
            public void onFailed(String method, String key, int errorTipid) {
                if (errorTipid != 0) {
                    Tools.showTip(context.getApplicationContext(), errorTipid);
                    XLog.d(TAG, "请求失败：" + context.getResources().getString(errorTipid));
                }
                if (iVolleyRequestfail != null)
                    try {
                        iVolleyRequestfail.onFailed(method, null, errorTipid);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        });
        volleyRequest.executeGetRequest(addParam(context, url));
    }

    //添加接口请求来源类型区分
    private String addParam(Context context, String url) {
        if (url.contains("?")) {
            url = url + "&sourceType=1";
        } else {
            url = url + "?sourceType=1";
        }
        return url + "&version=" + getVersion(context);
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0";
        }
    }

}
