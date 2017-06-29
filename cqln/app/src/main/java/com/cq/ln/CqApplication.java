package com.cq.ln;

import android.app.Application;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tencent.bugly.crashreport.CrashReport;
import com.cq.ln.activity.ParentsLockActivity;
import com.cq.ln.activity.PlayUtilActivity;
import com.cq.ln.appConfig.Appconfig;
import com.cq.ln.constant.ConstantEnum;
import com.cq.ln.interfaces.MyDialogEnterListener;
import com.cq.ln.utils.ActivityUtility;
import com.cq.ln.utils.HiFiDialogTools;
import com.cq.ln.utils.Tools;
import com.cq.ln.utils.XLog;

import cqdatasdk.bean.HiFiMainPageBean;
import cqdatasdk.bean.ParentLockBean_;
import cqdatasdk.bean.UserAuthResponeBean;
import cqdatasdk.bean.UserBalanceBean;
import cqdatasdk.interfaces.IVolleyRequestSuccess;
import cqdatasdk.interfaces.IVolleyRequestfail;
import cqdatasdk.network.AsyncHttpRequest;
import cqdatasdk.network.URLVerifyTools;
import cqdatasdk.network.VolleyRequest;


/**
 *
 */
public class CqApplication extends Application implements IVolleyRequestSuccess, IVolleyRequestfail{
    private static final String TAG = "CqApplication";
    public static HiFiMainPageBean.ClassifyBean.ClassifyItemBean mRechangeBean;
    public static DisplayImageOptions options;
    private static CqApplication instance;
    public AsyncHttpRequest asyncHttpRequest = new AsyncHttpRequest();
    public String albumName = "";
    public String artistNames = "";
    private FragmentActivity fragmentActivity;
    private ApplicationCallBack applicationCallBack;
    private int fileType = 0;
    public static final int StreamTypeIpqam = 1;   //NetType=Cable
    public static final int StreamTypeINet = 2;  ///NetType=IP
    private HiFiDialogTools hiFiDialogTools = new HiFiDialogTools();

    public synchronized static CqApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.clear_image_src) // 默认
                .showImageForEmptyUri(R.mipmap.clear_image_src) // URI不存在
                .showImageOnFail(R.mipmap.clear_image_src) // 加载失败
                .displayer(new RoundedBitmapDisplayer(0)) // 图片圆角
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).imageScaleType(ImageScaleType.NONE_SAFE).bitmapConfig(Bitmap.Config.RGB_565).build();

        //设置系统声音为静音，达到禁止focus 等等操作出现声音的情况，
        // TODO 待在重网机顶盒测试会不会影响到其他应用的情况
        // MyAudioManagerUtils.getInstance(this).forbidSystemVoice();
        CrashReport.initCrashReport(getApplicationContext(), "900056642", true);
    }

    public void getPlayUrl(FragmentActivity fragmentActivity, int trackId, int fileType) {
        this.fileType = fileType;
        this.fragmentActivity = fragmentActivity;
        Auth(trackId);
    }

    /**
     * //鉴权
     *
     * @param progId 曲目Id
     */
    private void Auth(int progId) {
        asyncHttpRequest.getUserAuth(fragmentActivity, progId+"", fileType, Appconfig.getKeyNo(getApplicationContext()), this, this, null);
    }

    private void getPlayerRtsp(String progId) {
        final String url = Appconfig.getEpg_url(getApplicationContext()) + "/defaultHD/en/go_authorization.jsp?playType=1&progId=" + progId +
                "&contentType=0&business=1&baseFlag=0&idType=FSN";
        XLog.d(TAG, "获取RTSP连接:" + url);
        VolleyRequest.getRTSPUrl(progId, url, Appconfig.getCookie(getApplicationContext()), new IVolleyRequestSuccess() {
            @Override
            public void onSucceeded(String method, final String vodId, final Object object) {
                fragmentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (null == object) {
                            hiFiDialogTools.showtips(fragmentActivity, "获取播放连接失败,请稍后再试.", null);
                            return;
                        }
                        String vodPlayerUri = "";
                        if (null != object) {
                            vodPlayerUri = object.toString();
                            XLog.d(TAG, "RTSP 播放连接:" + vodPlayerUri);
                        }
                        vodPlayerUri = object.toString();
                        if (applicationCallBack!=null){
                            int streamType;
                            String netType = Uri.parse(vodPlayerUri).getQueryParameter("NetType");
                            if(!TextUtils.isEmpty(netType) && TextUtils.equals(netType.toLowerCase(), "ip")){
                                streamType = StreamTypeINet;
                            }else {
                                streamType = StreamTypeIpqam;
                            }
                            applicationCallBack.onAppPlayUrl(vodPlayerUri, streamType);
                        }
                    }
                });
            }
        });
    }

    /**
     * 查询是否有家长锁
     */
    private void getParentLock() {
        asyncHttpRequest.getParentLock_(fragmentActivity, "1", Appconfig.getKeyNo(getApplicationContext()), this, this);
    }

    /**
     * 开通音乐包月活动
     */
    private void OrderMusicServer() {
        asyncHttpRequest.OrderMusicServer_(fragmentActivity, Appconfig.getKeyNo(getApplicationContext()), this, this);
    }

    /**
     * 查询用户余额
     */
    public void getUserBalance() {
        asyncHttpRequest.getUserBalance(fragmentActivity, Appconfig.getKeyNo(getApplicationContext()), this, this, "请稍后...");
    }

    @Override
    public void onSucceeded(String method, String key, Object object) throws Exception {
        if ("single.action".equals(method)) {//第一个接口,查询订购状态返回
            if (object == null)
                return;
            UserAuthResponeBean bean = (UserAuthResponeBean) object;
            UserAuthResponeBean.DataBean result = bean.getData();
            if (result == null) {
                return;
            }
            switch (result.getIsfree()) {
                case ConstantEnum.Player_Auth_charge://收费
                    chargePlay(result);
                    break;
                case ConstantEnum.Player_Auth_Free://免费,
                    //可播放
                    getPlayerRtsp(result.getVodId());
                default:
                    break;
            }
            return;
        } else if ("userLock.action".equals(method)) {//查询家长锁返回
            if (object == null)
                return;
            final ParentLockBean_ bean = (ParentLockBean_) object;
            if (bean == null)
                return;
            if (bean.getCode().equals("200")) {
                //弹层2 家长锁
                Intent intent = new Intent();
                intent.putExtra("PASSWORD", bean.getPasswd());
                intent.setClass(fragmentActivity, ParentsLockActivity.class);
                fragmentActivity.startActivityForResult(intent, PlayUtilActivity.CodeParentsLock);
                return;
            } else if (bean.getCode().equals("201")) {//没有家长锁
                getUserBalance();
                return;
            } else {
                hiFiDialogTools.showtips(fragmentActivity, bean.getMsg(), null);
            }
            return;
        } else if ("getUserInfo.action".equals(method)) {//查询余额成功返回
            if (object == null)
                return;
            UserBalanceBean bean = (UserBalanceBean) object;
            UserBalanceBean.ResultBean result = bean.getResult();
            if (result == null)
                return;
            if (result.getTotalBalance() > ConstantEnum.USER_ORDER_BALANCE) {//用户金额大于0,开通
                OrderMusicServer();
            } else {
                hiFiDialogTools.showLeftRightTip(fragmentActivity, "温馨提示", getString(R.string.string_96868), "马上充值", "取消", new MyDialogEnterListener() {
                    @Override
                    public void onClickEnter(Dialog dialog, Object object) {
                        if ((int) object == 0) {
                            ActivityUtility.goCustomWebView(fragmentActivity, URLVerifyTools.formatWebViewUrl(CqApplication.mRechangeBean.href));
                        }
                    }
                });
            }
            return;
        }
    }

    @Override
    public void onFailed(String method, String key, int errorTipId) throws Exception {

    }

    /**
     * @param result 付费曲目处理
     */
    private void chargePlay(UserAuthResponeBean.DataBean result) {
        switch (result.getUserCode()) {
            case ConstantEnum.Music_Auth_status_IsOrder://已订购,不处理
                //可播放
                getPlayerRtsp(result.getVodId());
                break;
            case ConstantEnum.Music_Auth_status_NoOrder://未订购
                playThirdSecond(false);
                break;
            case ConstantEnum.Music_Auth_status_blacklist:
                //黑名单,弹层提示
                //FIXME
                hiFiDialogTools.showtips(fragmentActivity, R.string.string_black, null);
                break;
            case ConstantEnum.Music_Auth_status_disable://欠费停用
                playThirdSecond(true);
                break;
        }
    }

    /**
     * 试播30秒
     *
     * @param isDisable 是否停用,放在线程中执行
     */
    private void playThirdSecond(final boolean isDisable) {
        if (isDisable) {//欠费停用
            //FIXME
            Tools.showTip(fragmentActivity, R.string.string_disable);
            return;
        } else {
            //弹层1,未订购
            //FIXME
            hiFiDialogTools.showPlaceOrder(fragmentActivity, new MyDialogEnterListener() {
                @Override
                public void onClickEnter(Dialog dialog, Object object) {
                    if (object == null) {
                        getParentLock();
                    }
                }
            });
        }
    }

    public void setApplicationCallBack(ApplicationCallBack applicationCallBack) {
        this.applicationCallBack = applicationCallBack;
    }

    public interface ApplicationCallBack {
        void onAppPlayUrl(String playUrl, int streamType);
    }
}
