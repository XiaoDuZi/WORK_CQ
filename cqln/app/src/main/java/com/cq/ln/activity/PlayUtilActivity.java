package com.cq.ln.activity;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cq.ln.CqApplication;
import com.cq.ln.R;
import com.cq.ln.appConfig.Appconfig;
import com.cq.ln.constant.ConstantEnum;
import com.cq.ln.interfaces.MyDialogEnterListener;
import com.cq.ln.utils.ActivityUtility;
import com.cq.ln.utils.FastBlurUtil;
import com.cq.ln.utils.HiFiDialogTools;
import com.cq.ln.utils.ImageTool;
import com.cq.ln.utils.StrTool;
import com.cq.ln.utils.Tools;
import com.cq.ln.utils.XLog;
import com.cq.ln.views.FocusBorderView;
import com.cq.ln.views.RoundImageView;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cqdatasdk.bean.CollectionSucceedBean;
import cqdatasdk.bean.DeletedCollectionSucceedBean;
import cqdatasdk.bean.OrderMusicServerBean_;
import cqdatasdk.bean.ParentLockBean_;
import cqdatasdk.bean.SpecialDetailBean;
import cqdatasdk.bean.UserAuthResponeBean;
import cqdatasdk.bean.UserBalanceBean;
import cqdatasdk.interfaces.IVolleyRequestSuccess;
import cqdatasdk.interfaces.IVolleyRequestfail;
import cqdatasdk.network.URLVerifyTools;
import cqdatasdk.network.VolleyRequest;
import ipaneltv.toolkit.fragment.PlayActivityInterface;
import ipaneltv.toolkit.fragment.VodPlayFragment;
import ipaneltv.uuids.ChongqingUUIDs;

/**
 * 播放器页面
 */
public class PlayUtilActivity extends BaseActivity implements IVolleyRequestSuccess, IVolleyRequestfail, PlayActivityInterface.VodPlayBaseInterface.Callback {

    public static final int CodeParentsLock = 1000;
    private static final String TAG = "PlayUtilActivity";
    private static final int PLAY_THRIDTY_TIME = 0;//试播30秒,由于延迟,设置28秒,业务限定为0秒
    private static final String PLAY_PLAY = "0";
    private static final String PLAY_PAUSE = "1";
    private static final int PLAY_READY_NONE = 0;//需要重新开始
    private static final int PLAY_READY_HALF = 1;//准备好一半
    private static final int PLAY_READY_COMPELED = 2;//完全准备好了
    //----------循环模式
    private static final int PLAY_LooperMode_sequence = 0;//顺序播放
    private static final int PLAY_LooperMode_listlooper = 1;//列表循环
    private static final int PLAY_LooperMode_random = 2;//随机播放
    private static final int PLAY_LooperMode_single = 3;//单曲循环

    private static final int StreamTypeIpqam = 1;   //NetType=Cable
    private static final int StreamTypeINet = 2;  ///NetType=IP

    private int streamType = StreamTypeIpqam;    //数据流类型
    @Bind(R.id.player_pre)
    ImageButton mPlayerPre;
    @Bind(R.id.player_pause)
    ImageButton mPlayerPauseOrPlay;
    @Bind(R.id.player_next)
    ImageButton mPlayerNext;
    @Bind(R.id.player_looper)
    ImageButton mPlayerLooper;
    @Bind(R.id.player_collection)
    ImageButton mPlayerCollection;
    @Bind(R.id.player_progress)
    SeekBar mPlayerProgress;
    @Bind(R.id.activity_RootView)
    FrameLayout mActivityRootView;
    @Bind(R.id.txtPlayTime)
    TextView mTxtPlayTime;
    @Bind(R.id.txtDuration)
    TextView mTxtDuration;
    @Bind(R.id.Image_Big)
    RoundImageView mImageBig;
    @Bind(R.id.rl_image)
    RelativeLayout rl_image;
    @Bind(R.id.txtTesturl)
    TextView mTxtTesturl;
    @Bind(R.id.txtTest)
    TextView mTxtTest;
    @Bind(R.id.txtMusicName)
    TextView mTxtMusicName;
    @Bind(R.id.txtSpecail)
    TextView mTxtSpecail;
    @Bind(R.id.txtSinger)
    TextView mTxtSinger;
    @Bind(R.id.player_pre_txt)
    TextView mPlayerPreTxt;
    @Bind(R.id.player_pause_txt)
    TextView mPlayerPauseTxt;
    @Bind(R.id.player_next_txt)
    TextView mPlayerNextTxt;
    @Bind(R.id.player_looper_txt)
    TextView mPlayerLooperTxt;
    @Bind(R.id.player_collection_txt)
    TextView mPlayerCollectionTxt;
    @Bind(R.id.vedio_player_pause)
    ImageButton vedio_player_pause;
    @Bind(R.id.iv_video_bg)
    ImageView iv_video_bg;
    @Bind(R.id.iv_bg)
    ImageView ivBg;
    @Bind(R.id.SetBgMusic)
    RelativeLayout setBgMusicView;

    private Context context;
    private VodPlayFragment fragment;
    private PlayActivityInterface.VodPlayBaseInterface baseInterface;
    private SpecialDetailBean.DataBean.TrackListBean currentBean;
    private UserAuthResponeBean.DataBean authBackCurrentBean;//鉴权返回的当前Bean
    private String albumName = "";
    private String artistNames = "";
    private String vodPlayerUri = "";
    private List<SpecialDetailBean.DataBean.TrackListBean> trackList;
    /**
     * 保存bealist当前播放状态,用于列表模式
     */
    private Map<String, Boolean> beanListPlayStatus = new HashMap<>();
    //播放器初始化控制参数---start
    private int playi = 0;
    private int dura = 0;
    private boolean mRunning = false;
    private int currentPlayTime = 0;
    //播放器初始化控制参数---end
    Handler mUIHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (mPlayerProgress == null || mTxtPlayTime == null)
                return false;
            if (message == null)
                return false;

            dura = getDura();
            int i = message.arg1;
            currentPlayTime = i;
            if (currentPlayTime > dura)
                return false;

            float f = i * 100 / dura;
            mPlayerProgress.setProgress((int) f);
            iv_video_bg.setVisibility(View.GONE);
            String currentTimeStr = Tools.padLeft(i / 60, 2) + ":" + Tools.padLeft(i % 60, 2);
            if (!TextUtils.isEmpty(currentTimeStr))
                mTxtPlayTime.setText(currentTimeStr);

            return false;
        }
    });
    private int playFLag = 0;
    //计时器变量-----end
    //计时器变量-----start
    private HandlerThread handlerThread;
    private Handler mPostRunnableHandler;
    private int currentIndex;//曲目当前在列表的index
    private int currentLoopModel = 0;//当前循环模式
    private boolean isSingle;//是否单曲
    private String ablumId;//曲目ID
    private String ablumName;//曲目名
    /**
     * 是否是自动播放
     */
    private boolean isAutoPlayHideLoading = false;
    private boolean RUN_FLAG;

    /**
     * 计时器作用,由于Timer和TimeTask引起问题而代替使用
     */
    Runnable mBackgroundRunnable = new Runnable() {
        @Override
        public void run() {
            XLog.d(TAG, "run start");
            while (RUN_FLAG) {
                //优化CPU性能------------start
                if (!mRunning)
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                if (!mRunning)
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                //优化CPU性能------------end
                while (mRunning) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (mUIHandler != null) {
                        playi++;
                        Message msg = mUIHandler.obtainMessage();
                        msg.arg1 = playi;
                        mUIHandler.sendMessage(msg);
                    }
                }
            }
        }
    };
    private HiFiDialogTools hiFiDialogTools = new HiFiDialogTools();
    private Animation rotateAnim;
    private int fileType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);
        ButterKnife.bind(this);
        context = this;
        RUN_FLAG = true;
        mPlayerProgress.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

        fileType = getIntent().getIntExtra("fileType", 0);

        traversalView(this);
        createBorderView(this);
        initStatus();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isSingle = bundle.getBoolean("isSingle", false);
            if (isSingle) {//单曲
                ablumId = bundle.getString("ablumId", "");
                ablumName = bundle.getString("ablumName", "");
                int trackId = StrTool.parsetNum(ablumId);
                singleMusicEntryPlayer(trackId, ablumName);

            } else {//列表模式
                currentBean = (SpecialDetailBean.DataBean.TrackListBean) bundle.getSerializable("TrackListBean");
                albumName = bundle.getString("albumName", "");
                artistNames = bundle.getString("artistNames", "");
                currentIndex = bundle.getInt("currentIndex");
                //曲目列表
                trackList = (List<SpecialDetailBean.DataBean.TrackListBean>) bundle.getSerializable("trackList");

                //播放的入口
                listEntryPlayer(currentBean);
                setOtherText();

                String specailBigPic = bundle.getString("specailBigPic", "");
                loadBigpic(specailBigPic);
            }
        }

        //------------------计时器
        new Thread(new Runnable() {
            @Override
            public void run() {
                mRunning = false;
                handlerThread = new HandlerThread("MyHandlerThread");//创建一个HandlerThread
                handlerThread.start();
                mPostRunnableHandler = new Handler(handlerThread.getLooper());//使用HandlerThread的looper对象创建Handler，如果使用默认的构造方法，很有可能阻塞UI线程
                mPostRunnableHandler.post(mBackgroundRunnable);//将线程post到Handler中

            }
        }).start();

        //默认播放暂停位置,此方式不会阻塞主线程！
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FocusBorderView.DURATION_TIME = 0;
                setDefaultFocus();
                FocusBorderView.DURATION_TIME = 300;
            }
        }, 180);

        if (fileType == 1) {
            TextView tv_dur_left = (TextView) findViewById(R.id.tv_dur_left);
            TextView tv_dur_right = (TextView) findViewById(R.id.tv_dur_right);
            mTxtPlayTime = tv_dur_left;
            mTxtDuration = tv_dur_right;
            SeekBar video_player_progress = (SeekBar) findViewById(R.id.video_player_progress);
            video_player_progress.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            mPlayerProgress = video_player_progress;
            RelativeLayout rl_video = (RelativeLayout) findViewById(R.id.rl_video);
            rl_video.setVisibility(View.VISIBLE);
            FrameLayout fl_music = (FrameLayout) findViewById(R.id.fl_music);
            fl_music.setVisibility(View.GONE);
        }

    }

    private void setDefaultFocus() {
        borderView.setBorderBitmapResId(R.mipmap.player_focus_view);
        mPlayerPauseOrPlay.bringToFront();
        mPlayerPauseOrPlay.getParent().bringChildToFront(mPlayerPauseOrPlay);
        mPlayerPauseOrPlay.requestLayout();
        mPlayerPauseOrPlay.requestFocus();
        mPlayerPauseOrPlay.invalidate();
        borderView.setHideView(false);//这一句非常重要
        borderView.setFocusView(mPlayerPauseOrPlay, 1.0f);
    }

    /**
     * @param trackId   单曲播放入口
     * @param ablumName
     */
    public void singleMusicEntryPlayer(int trackId, String ablumName) {
        Auth(trackId);
        resetPlayerParams();
        setUIshowPause();
        mTxtMusicName.setText(ablumName);
    }

    /**
     * 列表模式的播放入口
     *
     * @param bean
     */
    public void listEntryPlayer(SpecialDetailBean.DataBean.TrackListBean bean) {
        if (bean == null)
            return;

        beanListPlayStatus.put(bean.getVodId(), false);//重新loading,再次置为false!
        Auth(bean.getTrackId());//查询订购状态,第一个调用的接口
        resetPlayerParams();
        setUIshowPause();
        try {
            bindDurationAndName(bean.getTrackName(), bean.getTrackDuration());
        } catch (Exception e) {
            XLog.d(TAG, "e:" + e);
            e.printStackTrace();
        }

    }

    private void resetPlayerParams() {
        playi = 0;
        dura = 0;
        mRunning = false;
        currentPlayTime = 0;
        playFLag = PLAY_READY_NONE;
        mPlayerProgress.setProgress(0);
        mTxtPlayTime.setText("00:00");

    }

    /**
     * 初始化按钮状态
     */
    private void initStatus() {
        mPlayerPauseTxt.setText("播放");
        mPlayerPauseOrPlay.setTag(R.id.tag_for_player_PlayOrPause, PLAY_PAUSE);
        setLoopMode(PLAY_LooperMode_sequence);//默认顺序播放
    }

    private void setLoopMode(int model) {
        currentLoopModel = model;
        switch (currentLoopModel) {
            case PLAY_LooperMode_sequence://顺序播放
                mPlayerLooper.setImageResource(R.mipmap.player_order_sequence);
                mPlayerLooperTxt.setText("顺序播放");

                break;
            case PLAY_LooperMode_listlooper://列表循环
                mPlayerLooper.setImageResource(R.mipmap.player_order_listloop);
                mPlayerLooperTxt.setText("列表循环");

                break;
            case PLAY_LooperMode_random://随机播放
                mPlayerLooper.setImageResource(R.mipmap.player_order_randomplay);
                mPlayerLooperTxt.setText("随机播放");

                break;
            case PLAY_LooperMode_single://单曲循环
                mPlayerLooper.setImageResource(R.mipmap.player_order_singleloop);
                mPlayerLooperTxt.setText("单曲循环");

                break;
        }
        mPlayerLooper.setTag(R.id.tag_for_player_LooperMode, currentLoopModel);
    }

    /**
     * @param trackName     曲目名称
     * @param trackDuration 时长
     */
    private void bindDurationAndName(String trackName, String trackDuration) throws Exception {
        if (!TextUtils.isEmpty(trackName))
            mTxtMusicName.setText(trackName);

//        "trackDuration":"0:04:07"
        if (!TextUtils.isEmpty(trackDuration)) {
            String[] strings = {"0", "0", "0"};
            strings = trackDuration.split(":");
            int min = 0;
            int sec = 0;
            if (strings.length == 3) {
                min = Integer.parseInt(strings[1].toString());
                sec = Integer.parseInt(strings[2].toString());
            } else if (strings.length == 2) {
                min = Integer.parseInt(strings[0].toString());
                sec = Integer.parseInt(strings[1].toString());
            }
            String result = Tools.padLeft(min, 2) + ":" + Tools.padLeft(sec, 2);
            mTxtDuration.setText(result);
        }
    }

    private void setOtherText() {
        mTxtSpecail.setText("专辑:" + albumName);
        mTxtSinger.setText("艺术家:" + artistNames);
    }

    /**
     * @param result 单曲模式,补齐其他信息
     */

    private void bindSingleModel(UserAuthResponeBean.DataBean result) {
        if (result == null)
            return;

        mTxtSinger.setText("艺术家:" + result.getArtistNames());

        try {
            bindDurationAndName(null, result.getDuration());
        } catch (Exception e) {
            XLog.d(TAG, "e:" + e);
            e.printStackTrace();
        }
    }

    private void bindCollection(UserAuthResponeBean.DataBean result) {
        if (result == null)
            return;
        authBackCurrentBean = result;

        switch (result.getIfCollection()) {
            case ConstantEnum.isCollection_Yes:
                mPlayerCollection.setImageResource(R.mipmap.player_collect_yes);
                break;

            case ConstantEnum.isCollection_NO:
                mPlayerCollection.setImageResource(R.mipmap.player_collect_no);
                break;
        }
    }

    @Override
    public void onSucceeded(String method, String key, Object object) {
        if ("single.action".equals(method)) {//第一个接口,查询订购状态返回
            if (object == null)
                return;
            UserAuthResponeBean bean = (UserAuthResponeBean) object;
            UserAuthResponeBean.DataBean result = bean.getData();
            if (result == null) {
                hiFiDialogTools.showtips(PlayUtilActivity.this, bean.getMsg(), null);
                return;
            }
            loadBigpic(result.getBigImg());
            bindCollection(result);

            //单曲模式,补齐其他信息
            if (isSingle) {
                bindSingleModel(result);
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
                intent.setClass(PlayUtilActivity.this, ParentsLockActivity.class);
                startActivityForResult(intent, CodeParentsLock);
                return;
            } else if (bean.getCode().equals("201")) {//没有家长锁
                getUserBalance();
                return;
            } else {
                hiFiDialogTools.showtips(PlayUtilActivity.this, bean.getMsg(), null);
            }
            return;

        } else if ("getUserInfo.action".equals(method)) {//查询余额成功返回
            if (object == null)
                return;
            UserBalanceBean bean = (UserBalanceBean) object;
            UserBalanceBean.ResultBean result = bean.getResult();
            if (result == null)
                return;
            //{"status":1,"errors":[],"result":{"custId":"3816815","totalFee":90.54,"totalBalance":0.0,"msg":"没有订购"},"extra":{}}

            if (result.getTotalBalance() > ConstantEnum.USER_ORDER_BALANCE) {//用户金额大于0,开通
                OrderMusicServer();
            } else {
                //弹层3 充值,后期才接入充值
                /**
                 MyDialog.getInstance(this).showPlaceOrderOption(this, result.getTotalBalance(), new onMyDialogEnterListener() {
                @Override public void onClickEnter(Dialog dialog, Object object) {//支付宝充值
                //弹层4,充值金额选择

                //TODO
                }
                });*/

                hiFiDialogTools.showLeftRightTip(PlayUtilActivity.this, "温馨提示", getString(R.string.string_96868), "马上充值", "取消", new MyDialogEnterListener() {
                    @Override
                    public void onClickEnter(Dialog dialog, Object object) {
                        if ((int) object == 0) {
                            ActivityUtility.goCustomWebView(PlayUtilActivity.this, URLVerifyTools.formatWebViewUrl(CqApplication.mRechangeBean.href));
                        }
                    }
                });
            }

            return;
        } else if ("comboValid.action".equals(method)) {//订购包月套餐成功!!

            if (object == null)
                return;
            OrderMusicServerBean_ bean = (OrderMusicServerBean_) object;

            if (bean.getCode() == 200) {//订购成功!
                //TODO 弹层提示!
                hiFiDialogTools.showtips(this, R.string.string_order_succeed, new MyDialogEnterListener() {
                    @Override
                    public void onClickEnter(Dialog dialog, Object object) {
                        //刷新当前播放,调用单曲循环刷新方法即可!
                        isAutoPlayHideLoading = false;
                        singleLooperModelRefresh();
                    }
                });

            } else {//订购失败!

                if (bean == null) {
                    Tools.showTip(PlayUtilActivity.this, "订购失败.");
                    return;
                }
                Tools.showTip(PlayUtilActivity.this, bean.getMsg());
            }

            return;
        } else if ("addUserCollection.action".equals(method)) {//添加收藏单曲成功
            CollectionSucceedBean bean = (CollectionSucceedBean) object;
            if (bean == null)
                return;
            Tools.showTip(PlayUtilActivity.this, bean.getMsg());
            //TODO
            if (authBackCurrentBean == null)
                return;
            if (String.valueOf(authBackCurrentBean.getId()).equals(key)) {//对应的添加收藏
                authBackCurrentBean.setCollectionId(bean.getCollectionId());
                authBackCurrentBean.setIfCollection(ConstantEnum.isCollection_Yes);
                mPlayerCollection.setImageResource(R.mipmap.player_collect_yes);
            }

            return;

        } else if ("deleteUserCollection.action".equals(method)) {//取消收藏单曲
            DeletedCollectionSucceedBean bean = (DeletedCollectionSucceedBean) object;
            if (bean == null)
                return;
            Tools.showTip(PlayUtilActivity.this, bean.getMsg());
            //TODO
            if (authBackCurrentBean == null)
                return;
            if (String.valueOf(authBackCurrentBean.getCollectionId()).equals(key)) {//对应的取消收藏
                authBackCurrentBean.setCollectionId(0);
                mPlayerCollection.setImageResource(R.mipmap.player_collect_no);
                authBackCurrentBean.setIfCollection(ConstantEnum.isCollection_NO);
            }

            return;
        }

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
                //可播放0秒
//                getPlayerRtsp(result.getVodId());
                //计时
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        playThirdSecond(false);
                    }
                }).start();
                break;
            case ConstantEnum.Music_Auth_status_blacklist:
                //黑名单,弹层提示
                //TODO
//                MyDialog.getInstance(this)
                //FIXME
                hiFiDialogTools.showtips(this, R.string.string_black, null);
                break;
            case ConstantEnum.Music_Auth_status_disable://欠费停用
                //可播放0秒
//                getPlayerRtsp(result.getVodId());
                //计时
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        playThirdSecond(true);

                    }
                }).start();
                break;
        }
    }

    /**
     * 试播30秒
     *
     * @param isDisable 是否停用,放在线程中执行
     */
    private void playThirdSecond(final boolean isDisable) {
        while (currentPlayTime < PLAY_THRIDTY_TIME) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (baseInterface != null)
                    baseInterface.stop();
                playFLag = PLAY_READY_NONE;//标识重置
                if (!isSingle)
                    beanListPlayStatus.put(currentBean.getVodId(), false);
                stopTiming();
                XLog.d(TAG, "停止播放.");
                if (isDisable) {//欠费停用
//                    MyDialog.getInstance(context)
                    //FIXME
                    hiFiDialogTools.showtips(context, R.string.string_disable, null);
                    return;
                } else {
                    //弹层1,未订购
//                    MyDialog.getInstance(context)
                    //FIXME
                    hiFiDialogTools.showPlaceOrder(context, new MyDialogEnterListener() {
                        @Override
                        public void onClickEnter(Dialog dialog, Object object) {
                            if (object == null) {
                                getParentLock();
                            }
                        }
                    });
                }
            }
        });

    }

    /**
     * 开通音乐包月活动
     */
    private void OrderMusicServer() {
        asyncHttpRequest.OrderMusicServer_(this, Appconfig.getKeyNo(getApplicationContext()), this, this);
    }

    /**
     * 查询用户余额
     */
    private void getUserBalance() {
        asyncHttpRequest.getUserBalance(this, Appconfig.getKeyNo(getApplicationContext()), this, this, "请稍后...");
    }

    /**
     * 查询是否有家长锁
     */
    private void getParentLock() {
        asyncHttpRequest.getParentLock_(this, "1", Appconfig.getKeyNo(getApplicationContext()), this, this);
    }

    private void loadBigpic(String bigImg) {
        if (TextUtils.isEmpty(bigImg))
            return;

        ImageTool.loadImage(this, mImageBig, URLVerifyTools.formatPicListImageUrl(bigImg), 0, R.mipmap.default_small, new ImageTool.LoadSuccess() {
            @Override
            public void onSucceeded(Bitmap bitmap) {

                int scaleRatio = 5;
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
                        bitmap.getWidth() / scaleRatio,
                        bitmap.getHeight() / scaleRatio,
                        false);

                Bitmap blurImage = FastBlurUtil.doBlur(scaledBitmap, 20, false);
                ivBg.setBackground(new BitmapDrawable(PlayUtilActivity.this.getResources(), blurImage));
                AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
                //动画集
                AnimationSet set = new AnimationSet(true);
                set.addAnimation(alphaAnimation);
                //设置动画时间 (作用到每个动画)
                set.setDuration(1000);
                ivBg.startAnimation(set);
            }
        }, new ImageTool.Loadfail() {
            @Override
            public void onFailed() {

            }
        });

    }

    /**
     * //鉴权
     *
     * @param progId 曲目Id
     */
    private void Auth(int progId) {
        asyncHttpRequest.getUserAuth(this, progId+"", fileType, Appconfig.getKeyNo(getApplicationContext()), this, this, isAutoPlayHideLoading ? null : "请稍后...");
    }

    private void getPlayerRtsp(String progId) {
        final String url = Appconfig.getEpg_url(getApplicationContext()) + "/defaultHD/en/go_authorization.jsp?playType=1&progId=" + progId + "&contentType=0&business=1&baseFlag=0&idType=FSN";

        XLog.d(TAG, "获取RTSP连接:" + url);
        VolleyRequest.getRTSPUrl(progId, url, Appconfig.getCookie(getApplicationContext()), new IVolleyRequestSuccess() {
            @Override
            public void onSucceeded(String method, final String vodId, final Object object) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (null == object) {
                            hiFiDialogTools.showtips(PlayUtilActivity.this, "获取播放连接失败,请稍后再试.", null);
                            stopTiming();
                            return;
                        }
                        vodPlayerUri = object.toString();

                        String netType = Uri.parse(vodPlayerUri).getQueryParameter("NetType");
                        if(!TextUtils.isEmpty(netType) && TextUtils.equals(netType.toLowerCase(), "ip")){
                            streamType = StreamTypeINet;
                        }else {
                            streamType = StreamTypeIpqam;
                        }
//                        hiFiDialogTools.showtips(context, vodPlayerUri + "   netType  " + netType, null);
                        if (!isSingle) {//列表模式容错处理
                            if (currentBean == null)
                                return;
                            //判断返回的RTSP是否跟当前曲目匹配
                            if (!vodId.equals(currentBean.getVodId())) {
                                XLog.d(TAG, "不符合 RTSP->vodId:" + vodId + ",当前曲目vodid=" + currentBean.getVodId());
                                return;
                            }
                            XLog.d(TAG, "符合 RTSP->vodId:" + vodId + ",当前曲目vodid=" + currentBean.getVodId());

                            if (beanListPlayStatus.containsKey(vodId) && beanListPlayStatus.get(vodId)) {
                                XLog.d(TAG, "当前已经在播放.");
                                return;
                            }
                            resetBealistPlayStatusWidthout(vodId);
                        }

                        try {
                            initPlayerViews(vodId);
                        } catch (Exception e) {
                            XLog.d(TAG, "e:" + e);
                            e.printStackTrace();
                        }

                        XLog.d(TAG, "RTSP:" + vodPlayerUri);
                    }
                });
            }
        });
    }

    /**
     * @param vodId 重置除指定vodid以外的
     */
    private void resetBealistPlayStatusWidthout(String vodId) {
        for (Map.Entry<String, Boolean> entry : beanListPlayStatus.entrySet()) {
            if (!entry.getKey().equals(vodId)) {
                beanListPlayStatus.put(entry.getKey(), false);
            }
            XLog.d(TAG, "Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
    }

    private void initPlayerViews(String vodId) throws Exception {
        beanListPlayStatus.put(vodId, true);//此处设置非常重要
        XLog.d(TAG, "initPlayerViews()");
        if (fragment == null)
            fragment = VodPlayFragment.createInstance(ChongqingUUIDs.ID, VodPlayFragment.HUAWEI);
        if (baseInterface == null) {
            baseInterface = fragment.getPlayInterface(this);
//            fragment.setLoosenState(false);//暂不设置支持后台播放
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(fragment, null);
            transaction.commitAllowingStateLoss();
        } else {
            baseInterface.start(vodPlayerUri, 3, streamType, 0);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        borderView.setBorderBitmapResId(R.mipmap.player_focus_view);
        super.onFocusChange(v, hasFocus);

        if (hasFocus) {
            switch (v.getId()) {
                case R.id.player_pre:
                    viewControll(true, false, false, false, false);
                    break;
                case R.id.player_pause:
                    viewControll(false, true, false, false, false);
                    break;

                case R.id.player_next:
                    viewControll(false, false, true, false, false);
                    break;

                case R.id.player_looper:
                    viewControll(false, false, false, true, false);
                    break;

                case R.id.player_collection:
                    viewControll(false, false, false, false, true);
                    break;

            }
        }
    }

    private void viewControll(boolean pre, boolean pause, boolean next, boolean looper, boolean collection) {
        hintAll();
        if (pre) {
            mPlayerPreTxt.setVisibility(View.VISIBLE);
            return;
        } else if (pause) {
            mPlayerPauseTxt.setVisibility(View.VISIBLE);
            return;
        } else if (next) {
            mPlayerNextTxt.setVisibility(View.VISIBLE);
            return;
        } else if (looper) {
            mPlayerLooperTxt.setVisibility(View.VISIBLE);
            return;
        } else if (collection) {
            mPlayerCollectionTxt.setVisibility(View.VISIBLE);
            return;
        }
    }

    private void hintAll() {
        mPlayerPreTxt.setVisibility(View.INVISIBLE);
        mPlayerPauseTxt.setVisibility(View.INVISIBLE);
        mPlayerNextTxt.setVisibility(View.INVISIBLE);
        mPlayerLooperTxt.setVisibility(View.INVISIBLE);
        mPlayerCollectionTxt.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        RUN_FLAG = false;
        super.onDestroy();
        destoryTimingThread();
        ButterKnife.unbind(this);

        XLog.d(TAG, "onDestroy");
        destoryVariable();

    }

    private void destoryVariable() {
        baseInterface = null;
        fragment = null;
        trackList = null;
        beanListPlayStatus = null;
        currentBean = null;
        authBackCurrentBean = null;
    }

    @Override
    public void onTrimMemory(int level) {
        XLog.d(TAG, "onTrimMemory");
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            XLog.d(TAG, "onTrimMemory -->destoryVariable");
            destoryVariable();
        }
    }

    @Override
    public void onFailed(String method, String key, int errorTipId) {

    }

    @Override
    public void onContextReady(String s) {
        if (baseInterface != null) {
            XLog.d(TAG, "onContextReady() s" + s);
            baseInterface.start(vodPlayerUri, 3, streamType, 0);
            baseInterface.setVolume(1);//-1.0~1.0
            if (fileType == 1) {
                DisplayMetrics dm = new DisplayMetrics();
                //取得窗口属性
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                //窗口的宽度
                int screenWidth = dm.widthPixels;
                int screenHeight = dm.heightPixels;
                baseInterface.setDisplay(0, 0, screenWidth, screenHeight);//播放视频区域 x,y,w,h
            } else {
                baseInterface.setDisplay(0, 0, 10, 10);//播放视频区域 x,y,w,h
            }
        }

    }

    @Override
    public void onVodDuration(long l) {
        XLog.d(TAG, "onVodDuration():" + l);
        mTxtDuration.setText(Tools.formatTime(l));
        playFLag = PLAY_READY_COMPELED;
        if (!isSingle)
            beanListPlayStatus.put(currentBean.getVodId(), true);
        startTiming();
    }

    @Override
    public void onSeeBackPeriod(long l, long l1) {
        XLog.d(TAG, "onSeeBackPeriod():l=" + l + "  l1:" + l1);
        mPlayerProgress.setProgress((int) l);
        mPlayerProgress.setSecondaryProgress((int) l1);
        iv_video_bg.setVisibility(View.GONE);
    }

    /**
     * @param b 并非能真正播放出来
     */
    @Override
    public void onPlayStart(boolean b) {
        XLog.d(TAG, "onPlayStart():" + b);
        if (b) {
            // TODO onVodDuration 才真正开始播放
            playFLag = PLAY_READY_HALF;
        } else {
            hiFiDialogTools.showtips(PlayUtilActivity.this, "播放失败,请重试!", null);
            playFLag = PLAY_READY_NONE;
            //列表模式设置未在播放状态

        }
        if (!isSingle)
            beanListPlayStatus.put(currentBean.getVodId(), false);//并非能真正播放出来,所以置为false

        setUIshowPlay();

    }

    /**
     * 开始计时
     */
    private void startTiming() {
        mRunning = true;
        XLog.d(TAG, "开始计时");
        setUIshowPause();
        startRotateIcon();
    }

    private void stopTiming() {
        mRunning = false;
        XLog.d(TAG, "停止计时");
        setUIshowPlay();
        stopRotateIcon();
    }

    /**
     * 播放歌曲时，旋转歌曲图标
     */
    private void startRotateIcon() {
        if (rotateAnim == null) {
            rotateAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_center);
            LinearInterpolator lin = new LinearInterpolator();
            rotateAnim.setInterpolator(lin);
        }
        rl_image.clearAnimation();
        rl_image.startAnimation(rotateAnim);
    }

    /**
     * 停止歌曲图标旋转动画
     */
    private void stopRotateIcon() {
        rl_image.clearAnimation();
    }

    private void destoryTimingThread() {
        mRunning = false;
        //销毁线程
        mPostRunnableHandler.removeCallbacks(mBackgroundRunnable);

        handlerThread.quit();
        mBackgroundRunnable = null;
        mUIHandler = null;

    }

    private int getDura() {
        if (dura > 0)
            return dura;

        String duration = mTxtDuration.getText().toString();
        String[] du = duration.split(":");
        int dMin = Integer.parseInt(du[0]);
        int dSec = Integer.parseInt(du[1]);
        dura = dMin * 60 + dSec;
        return dura;
    }

    @Override
    public void onSourceRate(float v) {
        XLog.d(TAG, "onSourceRate():" + v);
    }

    @Override
    public void onSourceSeek(long l) {
        XLog.d(TAG, "onSourceSeek():" + l);
    }

    @Override
    public void onSyncMediaTime(long l) {
        XLog.d(TAG, "onSyncMediaTime() l:" + l);
    }

    @Override
    public void onPlayEnd(float v) {
        XLog.d(TAG, "onPlayEnd() v:" + v);
        playFLag = PLAY_READY_NONE;//标识重置
        stopTiming();


        //列表模式设置未在播放状态
        if (!isSingle)
            beanListPlayStatus.put(currentBean.getVodId(), false);

        autoPlayNext();
    }

    private void autoPlayNext() {
        isAutoPlayHideLoading = true;

        switch (currentLoopModel) {
            case PLAY_LooperMode_sequence://顺序播放
                nextPlay(false);
                break;
            case PLAY_LooperMode_listlooper://列表循环
                if (isSingle) {
                    singleModeLoop();
                } else {
                    nextPlay(true);
                }
                break;
            case PLAY_LooperMode_random://随机播放
                if (isSingle) {
                    singleModeLoop();
                } else {
                    randomPlay();
                }
                break;
            case PLAY_LooperMode_single://单曲循环
                singleLooperModelRefresh();
                break;
        }

    }

    /**
     * 单曲循环刷新模式
     */
    private void singleLooperModelRefresh() {
        if (isSingle) {//单曲目模式
            singleModeLoop();
        } else {//带列表模式
            if (baseInterface != null)
                baseInterface.stop();
            //播放的入口
            listEntryPlayer(currentBean);
        }
    }

    @Override
    public void onPlayMsg(String s) {
        XLog.d(TAG, "onPlayMsg():" + s);
        hiFiDialogTools.showtips(PlayUtilActivity.this, s, null);
    }

    @Override
    public void onPlayError(String s) {
        playFLag = PLAY_READY_NONE;//标识重置
        if (!isSingle)
            beanListPlayStatus.put(currentBean.getVodId(), false);
        XLog.d(TAG, "onPlayError():" + s);
        hiFiDialogTools.showtips(PlayUtilActivity.this, s, null);
        stopTiming();
    }

    @OnClick({R.id.player_pre, R.id.player_pause, R.id.player_next, R.id.player_looper, R.id.player_collection, R.id.SetBgMusic})
    public void onViewClick(View view) {

        switch (view.getId()) {
            case R.id.player_pre:

                if (Tools.isFastClick(1000))//防止快速点击
                    return;
                isAutoPlayHideLoading = false;
                prePlay(false);
                break;

            case R.id.player_pause:
                setPauseOrPlay();
                break;
            case R.id.player_next:

                if (Tools.isFastClick(1000))//防止快速点击
                    return;
                isAutoPlayHideLoading = false;
                nextPlay(false);

                break;
            case R.id.player_looper:

                if (Tools.isFastClick(100))//防止快速点击
                    return;

                currentLoopModel += 1;
                if (currentLoopModel > PLAY_LooperMode_single)
                    currentLoopModel = PLAY_LooperMode_sequence;
                setLoopMode(currentLoopModel);
                break;

            case R.id.player_collection:

                if (Tools.isFastClick(400))//防止快速点击
                    return;

                if (authBackCurrentBean == null)
                    return;

                if (isSingle) {
                    optionCollection();
                } else {//列表模式
                    if (authBackCurrentBean.getVodId().equals(currentBean.getVodId())) //当前鉴权回来的bean和当前播放的bean一致
                        optionCollection();
                }


                break;

            case R.id.SetBgMusic:
                if (Tools.isFastClick(400))//防止快速点击
                    return;
                Intent intent = new Intent();
                intent.putExtra("TRACKID", Integer.toString(currentBean.getTrackId()));
                intent.setClass(PlayUtilActivity.this, SetBackGroundMusicActivity.class);
                startActivity(intent);
                break;
        }
    }

    //设置播放或者暂停
    private void setPauseOrPlay() {
        if (Tools.isFastClick(300))//防止快速点击
            return;
        String tag = mPlayerPauseOrPlay.getTag(R.id.tag_for_player_PlayOrPause).toString();
        switch (tag) {
            case PLAY_PLAY://播放中
                setPlayPause();
                vedio_player_pause.setVisibility(View.VISIBLE);
                break;
            case PLAY_PAUSE://暂停中
                setPlayPlay();
                vedio_player_pause.setVisibility(View.GONE);
                break;
        }
    }

    private void optionCollection() {
        switch (authBackCurrentBean.getIfCollection()) {
            case ConstantEnum.isCollection_NO:
                collection(authBackCurrentBean.getId());
                break;
            case ConstantEnum.isCollection_Yes:
                cancelCollection(authBackCurrentBean.getCollectionId());
                break;
        }
    }

    private void prePlay(boolean isLooper) {
        if (trackList == null || trackList.isEmpty()) {
            Tools.showTip(PlayUtilActivity.this, "没有上一首.");
            return;
        }
        currentIndex -= 1;
        if (isLooper) {
            if (currentIndex < 0) {//最开始一首
                currentIndex = trackList.size() - 1;
            }
        } else {
            if (currentIndex < 0) {//最开始一首
                currentIndex = 0;
                Tools.showTip(PlayUtilActivity.this, "当前是第一首.");
                return;
            }
        }
        //即将进入播放下一首
        start2NextListPlay();

    }

    private void randomPlay() {
        if (trackList != null || !trackList.isEmpty()) {
            int randomNumber = -1;
            while (randomNumber < 0 || randomNumber > trackList.size()) {
                randomNumber = (int) (Math.random() * trackList.size());//得到[0,trackList.size()-1]的随机数
            }
            currentIndex = randomNumber;
            XLog.d(TAG, "随机播放 currentIndex = " + currentIndex);
            //即将进入播放下一首
            start2NextListPlay();
        }

    }

    private void nextPlay(boolean isLooper) {
        if (trackList == null || trackList.isEmpty()) {
            hiFiDialogTools.showtips(PlayUtilActivity.this, "没有下一首.", null);
            return;
        }

        currentIndex += 1;
        if (isLooper) {
            if (currentIndex > trackList.size() - 1) {//最后一首
                currentIndex = 0;
            }

        } else {
            if (currentIndex > trackList.size() - 1) {//最后一首
                currentIndex = trackList.size() - 1;
                hiFiDialogTools.showtips(PlayUtilActivity.this, "当前是最后一首.", null);
                return;
            }
        }

        //即将进入播放下一首
        start2NextListPlay();

    }

    /**
     * 即将进入播放下一首
     */
    private void start2NextListPlay() {
        SpecialDetailBean.DataBean.TrackListBean lastbean = currentBean;
        currentBean = trackList.get(currentIndex);
        if (lastbean.getVodId().equals(currentBean.getVodId()))//容错处理
            return;
        if (baseInterface != null)
            baseInterface.stop();
        //播放的入口
        listEntryPlayer(currentBean);
    }

    /**
     * 设置播放
     */
    private void setPlayPlay() {
        if (baseInterface != null) {
            if (isSingle) {
                optionResume();
            } else {
                if (beanListPlayStatus.containsKey(currentBean.getVodId()) && !beanListPlayStatus.get(currentBean.getVodId())) {//当前未在播放中
                    beanListPlayStatus.put(currentBean.getVodId(), true);
                    optionResume();
                }

            }

        } else {
            //解决BUG baseInterface==null 时候的BUG
            singleLooperModelRefresh();
        }

    }

    /**
     * 最后操作
     */
    private void optionResume() {
        XLog.d(TAG, "标识 playFLag = " + playFLag);
        if (playFLag == PLAY_READY_NONE) {//重新loading激活播放,才有效
            if (isSingle) {
                XLog.d(TAG, "单曲模式");
                singleModeLoop();
            } else {
                //播放的入口

                XLog.d(TAG, "列表模式,重新loading 当前曲目的RTSP:" + currentBean.getTrackName());
                listEntryPlayer(currentBean);
            }

        } else if (playFLag == PLAY_READY_HALF) {//没有准备完成,再次触发!!playFLag == 1时候,此模式有效
            baseInterface.start(vodPlayerUri, 3, streamType, 0);
            setUIshowPause();
        } else {
            XLog.d(TAG, "baseInterface.resume(); 激活播放");
            baseInterface.resume();
            startTiming();
            setUIshowPause();
        }
    }

    /**
     * 单曲模式的下一首播放,播发完成了必须要重新加载RTSP才能继续播放!
     */
    private void singleModeLoop() {
        if (baseInterface != null)
            baseInterface.stop();
        int trackId = StrTool.parsetNum(ablumId);
        singleMusicEntryPlayer(trackId, ablumName);
    }

    /**
     * 设置暂停
     */
    private void setPlayPause() {
        if (baseInterface != null) {
            if (isSingle) {
                optionPause();
            } else {
                if (beanListPlayStatus.containsKey(currentBean.getVodId()) && beanListPlayStatus.get(currentBean.getVodId())) {//当前在播放中
                    beanListPlayStatus.put(currentBean.getVodId(), false);
                    optionPause();
                }
            }
        }
    }

    /**
     * 最后操作
     */
    private void optionPause() {
        baseInterface.pause();
        stopTiming();
    }


    private void setUIshowPause() {
        mPlayerPauseTxt.setText("暂停");
        mPlayerPauseOrPlay.setImageResource(R.mipmap.player_pause);
        mPlayerPauseOrPlay.setTag(R.id.tag_for_player_PlayOrPause, PLAY_PLAY);
    }


    private void setUIshowPlay() {
        mPlayerPauseTxt.setText("播放");
        mPlayerPauseOrPlay.setImageResource(R.mipmap.player_play);
        mPlayerPauseOrPlay.setTag(R.id.tag_for_player_PlayOrPause, PLAY_PAUSE);
    }


    /**
     * @param albumId 收藏单曲
     */
    private void collection(int albumId) {
        asyncHttpRequest.collection(this, ConstantEnum.collectionType_single_for_interface, albumId, Appconfig.getKeyNo(getApplicationContext()), this, this);
    }


    /**
     * @param collectionId 取消收藏单曲
     */
    private void cancelCollection(int collectionId) {
        asyncHttpRequest.deleteUserCollection(this, collectionId, Appconfig.getKeyNo(getApplicationContext()), this, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CodeParentsLock) {
            //验证家长锁成功
            getUserBalance();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            if (fileType == 1) {
                setPauseOrPlay();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
