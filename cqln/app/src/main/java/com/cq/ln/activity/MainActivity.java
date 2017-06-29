package com.cq.ln.activity;

import android.animation.Animator;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.cq.ln.CqApplication;
import com.cq.ln.R;
import com.cq.ln.adapter.SectionsPagerAdapter;
import com.cq.ln.appConfig.Appconfig;
import com.cq.ln.cache.ACache;
import com.cq.ln.constant.ConstantEnum;
import com.cq.ln.fragment.BaseFragment;
import com.cq.ln.fragment.FirstColumnFragment;
import com.cq.ln.helper.UserFaceViewHelper;
import com.cq.ln.interfaces.MyDialogEnterListener;
import com.cq.ln.interfaces.OnFragmentInteractionListener;
import com.cq.ln.utils.ActivityUtility;
import com.cq.ln.utils.AppTools;
import com.cq.ln.utils.Enumer;
import com.cq.ln.utils.HiFiDialogTools;
import com.cq.ln.utils.Tools;
import com.cq.ln.utils.XLog;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cqdatasdk.bean.GetBackGroundMusicBean;
import cqdatasdk.bean.HiFiMainPageBean;
import cqdatasdk.bean.UserFaceGetSucceedBean;
import cqdatasdk.bean.syncUserInfoSucceedBean;
import cqdatasdk.interfaces.IVolleyRequestSuccess;
import cqdatasdk.interfaces.IVolleyRequestfail;
import cqdatasdk.utils.JsonUtil;
import ipaneltv.toolkit.fragment.PlayActivityInterface;
import ipaneltv.toolkit.fragment.VodPlayFragment;
import ipaneltv.uuids.ChongqingUUIDs;
import special.DiffAppconfig;


/**
 *
 */
public class MainActivity extends BaseActivity implements OnFragmentInteractionListener, View.OnFocusChangeListener, ViewPager.OnPageChangeListener, CompoundButton.OnCheckedChangeListener, Animator
        .AnimatorListener, IVolleyRequestSuccess, IVolleyRequestfail, CqApplication.ApplicationCallBack, PlayActivityInterface.VodPlayBaseInterface.Callback {
    public static VodPlayFragment fragment;
    private static String TAG = "MainActivity";
    @Bind(R.id.txttip)
    TextView mTxttip;
    @Bind(R.id.focusView)
    ImageView mFocusView;
    @Bind(R.id.MainIBtn_Search)
    ImageButton mMainIBtnSearch;
    @Bind(R.id.MainIBtn_User)
    ImageButton mMainIBtnUser;
    @Bind(R.id.RBbtnHandPick)
    RadioButton mRBbtnHandPick;
    @Bind(R.id.radioWarp)
    RadioGroup mRadioWarp;
    @Bind(R.id.mViewPager)
    ViewPager mViewPager;
    @Bind(R.id.MainIBtn_Recent)
    ImageButton mMainIBtnRecent;
    @Bind(R.id.txtRecentPlay)
    TextView mTxtRecentPlay;
    @Bind(R.id.txtSearch)
    TextView mTxtSearch;
    @Bind(R.id.txtUsercenter)
    TextView mTxtUsercenter;
    @Bind(R.id.UserWrap)
    LinearLayout UserWrap;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private int mIndex = Enumer.Fragment_Index_HandPick;
    private RadioButton RadioBtnOne;
    private View currentCompoundButtonView;//当前定位的高亮View
    private int whiteColor, grayColor;
    /**
     * 记录上一个聚焦的RadioButton，为了返回定位
     */
    private RadioButton lastFocusViewForRecord;
    private Map<Integer, Integer> poWidthList = new HashMap<>();
    private boolean isFirstShow;
    private BroadcastReceiver infoReceive;
    private List<View> rightViewList;
    private Context context;
    private HiFiMainPageBean mainPageBean;
    private DisplayImageOptions options;
    private PlayActivityInterface.VodPlayBaseInterface baseInterface;
    //记录当前数据是否是缓存数据
    private boolean isCacheData = true;
    private String vodPlayerUri = "";
    private VodPlayFragment playFragment;
    private HiFiDialogTools hiFiDialogTools = new HiFiDialogTools();
    private int streamType = CqApplication.StreamTypeIpqam;    //数据流类型

    private View.OnClickListener mViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.MainIBtn_Recent://最近播放
                    if (AppTools.isSsLife()) {

                    }else{
                        ActivityUtility.goUserCenter(MainActivity.this, ConstantEnum.Main_RecentPlay_);
                    }
                    break;
                case R.id.MainIBtn_Search://搜索
                    Intent intent = new Intent(context, SearchActivity.class);
                    if (AppTools.isSsLife()) {
                        intent = new Intent(context, ChildTypeActivity.class);
                        intent.putExtra("column", 1);
                        intent.putExtra("ColumnChild", -1);
                        intent.putExtra("columnId", 2947);
                    }
                    startActivity(intent);
                    break;
                case R.id.MainIBtn_User://我的收藏---->个人中心
                    ActivityUtility.goUserInfo(MainActivity.this, "");
                    break;

            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = this;

        //以下两行非常重要，控制光标在自己的预期
        mRBbtnHandPick.requestLayout();
        mRBbtnHandPick.requestFocus();

        TAG = TAG + getResources().getString(R.string.test_tip);

        //计算size
        calulate();


        whiteColor = getResources().getColor(R.color.sslife_title_selector_color);
        grayColor = getResources().getColor(R.color.sslife_title_color);

        initViewPage();//初始化1
        getDataFromNetWork();//loading2

        //按钮监听
        mMainIBtnUser.setOnFocusChangeListener(this);
        mMainIBtnUser.setOnClickListener(mViewOnClickListener);

        mMainIBtnSearch.setOnFocusChangeListener(this);
        mMainIBtnSearch.setOnClickListener(mViewOnClickListener);

        mMainIBtnRecent.setOnFocusChangeListener(this);
        mMainIBtnRecent.setOnClickListener(mViewOnClickListener);

        if (AppTools.isSsLife()){
            UserWrap.setVisibility(View.GONE);
        }

        //其他
        initBroadcast();
        getstbid();
        initRightViewList();


        //延时执行这个，非常重要
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (RadioBtnOne != null) {
                    isFirstShow = true;
                    RadioBtnOne.setChecked(true);
                    RadioBtnOne.requestFocus();
                    RadioBtnOne.setFocusableInTouchMode(true);
                }
                getDataFromCache();//做延时拿取缓存数据，在没有网络状态下也显示首页数据
            }
        }, 200);

    }

    /**
     * 从缓存拿取数据
     */
    private void getDataFromCache() {
        //做缓存
        ACache aCache = ACache.get(this);
        String jsonString = aCache.getAsString(DiffAppconfig.mainBeanJsonKey);
        XLog.d(TAG, "本地缓存数据：" + jsonString);
        HiFiMainPageBean bean = JsonUtil.getMode(jsonString, HiFiMainPageBean.class);
        if (bean != null && bean.data != null) {
            mainPageBean = bean;//无误数据赋值给全局变量
            try {
                isCacheData = true;
                disPatchFirstData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void initViewPage() {
        mViewPager.setEnabled(false);
        mViewPager.setFocusable(false);

        ArrayList<Fragment> fragments = new ArrayList<>();
        Fragment fragment = FirstColumnFragment.newInstance(R.id.RBbtnHandPick, "");
        fragments.add(fragment);
        Fragment fragment2 = FirstColumnFragment.newInstance(R.id.RBtnChildSong, "");
        fragments.add(fragment2);
        Fragment fragment3 = FirstColumnFragment.newInstance(R.id.RBtnHiFi, "");
        fragments.add(fragment3);
        Fragment fragment4 = FirstColumnFragment.newInstance(R.id.RBtnPopular, "");
        fragments.add(fragment4);
        Fragment fragment5 = FirstColumnFragment.newInstance(R.id.RBtnClass, "");
        fragments.add(fragment5);
        Fragment fragment6 = FirstColumnFragment.newInstance(R.id.RBtnJazz, "");
        fragments.add(fragment6);
        Fragment fragment7 = FirstColumnFragment.newInstance(R.id.RBtnNation, "");
        fragments.add(fragment7);
        Fragment fragment8 = FirstColumnFragment.newInstance(R.id.RBtnFree, "");
        fragments.add(fragment8);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(mSectionsPagerAdapter.getCount());
        mViewPager.addOnPageChangeListener(this);
        for (int i = 0; i < mRadioWarp.getChildCount(); i++) {
            RadioButton button = (RadioButton) mRadioWarp.getChildAt(i);
            button.setTag(i);
            button.setOnCheckedChangeListener(this);
            button.setOnFocusChangeListener(this);
            if (i == 0) {
                RadioBtnOne = button;
            }
        }
    }

    private void initRightViewList() {
        rightViewList = new ArrayList<>();
        mTxtRecentPlay.setTag(R.id.TAG_FOR_MAIN_RIGHT_VIEW, mMainIBtnRecent);
        rightViewList.add(mTxtRecentPlay);

        mTxtSearch.setTag(R.id.TAG_FOR_MAIN_RIGHT_VIEW, mMainIBtnSearch);
        rightViewList.add(mTxtSearch);

        mTxtUsercenter.setTag(R.id.TAG_FOR_MAIN_RIGHT_VIEW, mMainIBtnUser);
        rightViewList.add(mTxtUsercenter);
    }

    @Override
    public void onResume() {
        super.onResume();
        //同步BOSS用户信息
        try {
            syncUserInfo();
        } catch (Exception e) {
            XLog.d("e:" + e);
        }
        setUserFace();

        if (!AppTools.isSsLife()) { //时尚生活不获取背景音乐
            getOwnerBgMusic();
        }
    }

    private void setUserFace() {
        int newIndex = Tools.getIntegerPreference(this, ConstantEnum.UserFace_Index, ConstantEnum.UserFace_Index_NO_Setting);
        UserFaceViewHelper.setUserFaceImage(newIndex, mMainIBtnUser);
        if (newIndex == ConstantEnum.UserFace_Index_NO_Setting) {
            asyncHttpRequest.getUserFace(this, Appconfig.getKeyNo(this), this, this, null);
        }
    }

    private void syncUserInfo() {
        String keyNo = Appconfig.getKeyNo(this);
        if (!TextUtils.isEmpty(keyNo)) {
            XLog.d(TAG, "同步BOSS用户信息");
            asyncHttpRequest.syncUserInfo(this, keyNo, this, this);
        }
    }

    private void initBroadcast() {
        infoReceive = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String cookie = intent.getExtras().getString("CookieString").toString();
                String epg = intent.getExtras().getString("EPG");// epg地址
                String serviceGroupId = "" + intent.getExtras().getLong("ServiceGroupId");//分组号
                String smartcard = intent.getExtras().getString("smartcard");

                String str = "cookie :" + cookie + "\n";
                str += "serviceGroupId :" + serviceGroupId + "\n";
                str += "epg :" + epg + "\n";
                str += "smartcard :" + smartcard + "\n";

                Appconfig.setEpg_url(context, epg);

                Appconfig.setServiceGroupId(serviceGroupId);
                Appconfig.setCookie(context, cookie);

                //TODO 酒店测试屏蔽
                Appconfig.setKeyNo(context, smartcard);

                XLog.d(TAG, "已经接收到更新广播 = " + str);

            }
        };
        this.registerReceiver(infoReceive, new IntentFilter(ConstantEnum.broadcast_getSmartCard));
    }

    /**
     * 获得机顶盒系列号
     */
    private void getstbid() {
        Class<?> clazz = null;
        try {
            clazz = Class.forName("android.os.SystemProperties");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Method get = null;
        try {
            get = clazz.getMethod("get", String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            String stb_id = (String) get.invoke(null, "ro.di.stb_id");
            Appconfig.setStbid(this, stb_id);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void getDataFromNetWork() {
        asyncHttpRequest.getMainPageData(this, this, null, "加载中...");
    }

    /**
     * 分发导航栏和第一个栏目的数据
     *
     * @throws Exception
     */
    private void disPatchFirstData() throws Exception {
        if (mainPageBean == null) {
            return;
        }
        if (mainPageBean.data == null) {
            return;
        }

        //绑定栏目标题
        RadioButton button = null;
        HiFiMainPageBean.ClassifyBean bean = null;
        try {//安全设置，防止越界
            for (int i = 0; i < mainPageBean.data.size(); i++) {
                button = (RadioButton) mRadioWarp.getChildAt(i);
                bean = mainPageBean.data.get(i);
                button.setText(bean.name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //第一栏目
        disPacthContentData(0);
        disPacthContentData(Enumer.Fragment_Index_Free);
    }

    /**
     * 分发耗时的数据内容绑定
     */
    private void disPacthContentData(int index) {
        if (mainPageBean == null)
            return;
        if (mainPageBean.data == null)
            return;
        if (index < 0 || index > mSectionsPagerAdapter.getCount() - 1)
            return;
        HiFiMainPageBean.ClassifyBean bean;
        if (options == null)
            options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.clear_image_src) // 默认
                    .showImageForEmptyUri(R.mipmap.clear_image_src) // URI不存在
                    .showImageOnFail(R.mipmap.clear_image_src) // 加载失败
                    .displayer(new RoundedBitmapDisplayer(0)) // 图片圆角
                    .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).imageScaleType(ImageScaleType.NONE_SAFE).bitmapConfig(Bitmap.Config.RGB_565).build();

        BaseFragment baseFragment = ((BaseFragment) mSectionsPagerAdapter.instantiateItem(mViewPager, index));
        try {
            fragmentLoadData(baseFragment, index);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param colnumFragment 判断数据是缓存 还是最新加载的数据,mainPageBean.data 1、缓存数据或者2、最新数据
     * @param index
     */
    private void fragmentLoadData(BaseFragment colnumFragment, int index) throws Exception {
        if (colnumFragment == null)
            return;
        HiFiMainPageBean.ClassifyBean bean;
        if (isCacheData) {
            if (!colnumFragment.hasLoadCacheData) {
                bean = mainPageBean.data.get(index);
                bindFragment(colnumFragment, index, bean);
                colnumFragment.hasLoadCacheData = true;
            }
        } else {
            if (!colnumFragment.hasLoadLastUpdataData) {
                bean = mainPageBean.data.get(index);
                bindFragment(colnumFragment, index, bean);
                colnumFragment.hasLoadLastUpdataData = true;
            }
        }

    }

    /**
     * @param colnumFragment 绑定子栏目数据
     * @param index
     * @param bean
     */
    private void bindFragment(BaseFragment colnumFragment, int index, HiFiMainPageBean.ClassifyBean bean) throws Exception {
        switch (index) {
            case Enumer.Fragment_Index_HandPick:
            case Enumer.Fragment_Index_ChildSong:
            case Enumer.Fragment_Index_HiFi:
            case Enumer.Fragment_Index_Popular:
            case Enumer.Fragment_Index_Class:
            case Enumer.Fragment_Index_Jazz:
            case Enumer.Fragment_Index_Nation:
            case Enumer.Fragment_Index_Free:
                ((FirstColumnFragment) colnumFragment).bindData(bean, options);
                break;
        }
    }

    private void calulate() {
        mRadioWarp.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                for (int i = 0; i < mRadioWarp.getChildCount(); i++) {
                    RadioButton button = (RadioButton) mRadioWarp.getChildAt(i);
                    int width = button.getWidth();
                    poWidthList.put(i, width);
                }
            }
        });

    }

    @Override
    public void onFragmentInteraction(int type, Object object) {

    }

    private void fadeInOut(boolean recent, boolean search, boolean user) {
        if (mTxtRecentPlay == null) {
            return;
        }
        if (recent) {
            mTxtRecentPlay.setVisibility(View.VISIBLE);
            mTxtSearch.setVisibility(View.INVISIBLE);
            mTxtUsercenter.setVisibility(View.INVISIBLE);
        } else if (search) {
            mTxtRecentPlay.setVisibility(View.INVISIBLE);
            mTxtSearch.setVisibility(View.VISIBLE);
            mTxtUsercenter.setVisibility(View.INVISIBLE);
        } else if (user) {
            mTxtRecentPlay.setVisibility(View.INVISIBLE);
            mTxtSearch.setVisibility(View.INVISIBLE);
            mTxtUsercenter.setVisibility(View.VISIBLE);
        } else {
            mTxtRecentPlay.setVisibility(View.INVISIBLE);
            mTxtSearch.setVisibility(View.INVISIBLE);
            mTxtUsercenter.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        super.onFocusChange(view, hasFocus);
        switch (view.getId()) {
            case R.id.MainIBtn_Recent:
            case R.id.MainIBtn_Search:
            case R.id.MainIBtn_User:
                setNextFocusId();
                break;
            default:
                if (view instanceof CompoundButton) {
                    setFocusViewbg(view);
                    if (hasFocus) {
                        ((RadioButton) view).setChecked(true);
                    } else {
                        ((RadioButton) view).setChecked(false);
                    }
                }
                break;
        }
        hideBorderView();
        fadeView(view, hasFocus);
    }

    private void fadeView(View view, boolean hasFocus) {
        if (hasFocus) {
            switch (view.getId()) {
                case R.id.MainIBtn_Recent:
                    fadeInOut(true, false, false);
                    break;
                case R.id.MainIBtn_Search:
                    fadeInOut(false, true, false);
                    break;
                case R.id.MainIBtn_User:
                    fadeInOut(false, false, true);
                    break;
                default:
                    fadeInOut(false, false, false);
                    break;
            }
        } else
            fadeInOut(false, false, false);
    }

    private void setNextFocusId() {
        if (lastFocusViewForRecord != null) {
            mMainIBtnRecent.setNextFocusDownId(lastFocusViewForRecord.getId());
            mMainIBtnSearch.setNextFocusDownId(lastFocusViewForRecord.getId());
            mMainIBtnUser.setNextFocusDownId(lastFocusViewForRecord.getId());
            mMainIBtnUser.setNextFocusLeftId(lastFocusViewForRecord.getId());
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton view, boolean isChecked) {
        int index = Integer.parseInt(view.getTag().toString());
        if (isChecked) {
            mViewPager.setCurrentItem(index);
            try {
                topFocusViewFlyAnim(view);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        resetRadioOtherViewDefaultColor();
    }

    /**
     * @param view 高亮效果设置
     */
    private void setFocusViewbg(View view) {
        ((CompoundButton) view).setTextColor(whiteColor);// 纯白色
        if (view.isFocused()) {
            mFocusView.setImageResource(R.mipmap.nav_check_focus);
        } else
            mFocusView.setImageResource(R.mipmap.nav_check_unfocus);
    }

    private void hideBorderView() {
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            BaseFragment baseFragment = (BaseFragment) mSectionsPagerAdapter.instantiateItem(mViewPager, i);
            baseFragment.setBorderViewHide();
        }
    }

    /**
     * 重置其他view默认颜色
     */
    private void resetRadioOtherViewDefaultColor() {
        if (mRadioWarp == null)
            return;
        for (int i = 0; i < mRadioWarp.getChildCount(); i++) {
            RadioButton button = (RadioButton) mRadioWarp.getChildAt(i);
            if (button == null)
                continue;
            if (currentCompoundButtonView != null) {
                if (button.getId() == currentCompoundButtonView.getId())
                    button.setTextColor(whiteColor);
                else
                    button.setTextColor(grayColor);
            } else {
                button.setTextColor(grayColor);
            }
        }
    }

    /**
     * 顶部动画导航
     *
     * @param view
     */
    private void topFocusViewFlyAnim(View view) throws Exception {
        int position = 0;
        try {
            position = Integer.parseInt(view.getTag().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        poWidthList.put(position, view.getWidth());
//        XLog.d(TAG, "position = " + position + ",view.getWidth() = " + view.getWidth());
        int marginLeftTemp = 0;
        for (int i = 0; i < position; i++) {
            marginLeftTemp += poWidthList.get(i);
        }

        currentCompoundButtonView = view;
        int viewW = view.getWidth();//== 0 ? CommonUtils.getViewMeasurewidth(view) : view.getWidth();
        int viewH = view.getHeight();// == 0 ? CommonUtils.getViewMeasureHeightMethod1(view) : view.getHeight();
        float PivotX = viewW / 2;// view.getPivotX();(getPivotX/Y 在乐视机顶盒获取值为0)
        float pivotY = viewH / 2;// view.getPivotY();
        float topH = mRadioWarp.getY();
        int marginleft = (getResources().getDimensionPixelOffset(R.dimen.dp45));
        float leftW = marginLeftTemp + marginleft;//Tools.dip2px(this, marginleft);//加上xml里面的marginLeft 45dp
        float caluX = leftW + PivotX;
        float caluY = topH + pivotY;
        if (!isFirstShow)
            mFocusView.setVisibility(View.VISIBLE);

        int mFocusViewWidth = mFocusView.getWidth();//== 0 ? CommonUtils.getViewMeasurewidth(mFocusView) : mFocusView.getWidth();
        int mFocusViewHeight = mFocusView.getHeight();//== 0 ? CommonUtils.getViewMeasureHeightMethod1(mFocusView) : mFocusView.getHeight();

        int startX = (int) ((caluX - mFocusViewWidth / 2) + 0.5f);
        int startY = (int) ((caluY - mFocusViewHeight / 2) + 0.5f);

        ViewPropertyAnimator animator = mFocusView.animate();
        animator.setListener(this);
        animator.setDuration(150L);
        animator.x(startX);
        animator.y(startY);
        animator.start();
    }


    /**
     * 分发按键事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    try {
                        return nextFragment();
                    } catch (Exception e) {
                        XLog.d(TAG, "e.getMessage() = " + e.getMessage());
                        return false;
                    }
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    try {
                        return lastFragment();
                    } catch (Exception e) {
                        XLog.d(TAG, "e.getMessage() = " + e.getMessage());
                        return false;
                    }
                case KeyEvent.KEYCODE_MENU:
                    return true;
                case KeyEvent.KEYCODE_DPAD_UP:
                    getLastFocusView();
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    return firstIconFocus(true);
            }
        } else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_MENU) {

            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    //控制光标指向的第一个焦点
    private boolean firstIconFocus(boolean needRadioFocus) {
        int viewPagerIndex = mViewPager.getCurrentItem();
        View cbView = mRadioWarp.getChildAt(viewPagerIndex);
        if (!cbView.isFocused() && needRadioFocus) {
            return false;
        }
        BaseFragment baseFragment = ((BaseFragment) mSectionsPagerAdapter.instantiateItem(mViewPager, viewPagerIndex));
        View contentView = baseFragment.getView();
        View view = contentView.findViewById(R.id.effHandpick_one);
        if (view == null) {
            return false;
        }
        view.requestFocus();
        return true;

    }

    //控制光标指向最后一个焦点
    private boolean lastIconFocus(int viewPagerIndex) {
        BaseFragment baseFragment = ((BaseFragment) mSectionsPagerAdapter.instantiateItem(mViewPager, viewPagerIndex));
        View contentView = baseFragment.getView();
        View view = contentView.findViewById(R.id.effHandpick_Nine);
        if (view == null) {
            return false;
        }
        view.requestFocus();
        return true;

    }

    private void getLastFocusView() {
        View view = this.getCurrentFocus();
        if (view instanceof RadioButton)
            lastFocusViewForRecord = (RadioButton) view;
    }

    /**
     * @return 上一个Fragment
     * @throws Exception
     */
    private boolean lastFragment() throws Exception {
        View currentView = this.getCurrentFocus();
        if (currentView == null)
            return false;
        int id = currentView.getId();
        switch (id) {
            //精选
            case R.id.effHandpick_one:
            case R.id.effHandpick_Five:
                int next = (mIndex - 1) % mSectionsPagerAdapter.getCount();
                if (next > -1) {
                    lastIconFocus(next);
                    mViewPager.setCurrentItem(next, true);
                }
                return true;
            default:
                return false;
        }
    }

    /**
     * @return 跳至下一个Fragment
     * @throws Exception
     */
    private boolean nextFragment() throws Exception {
        View currentView = this.getCurrentFocus();
        if (currentView == null) {
            return false;
        }
        int id = currentView.getId();
        switch (id) {
            //精选
            case R.id.effHandpick_four:
            case R.id.effHandpick_Nine:
                int next = (mIndex + 1) % mSectionsPagerAdapter.getCount();
                XLog.d(TAG, "mIndex = " + mIndex + "，next =" + next);
                if (next < mSectionsPagerAdapter.getCount()) {
                    mViewPager.setCurrentItem(next, true);
                    firstIconFocus(false);
                }
                return true;
            default:
                return false;
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        RadioButton rBtn = ((RadioButton) mRadioWarp.getChildAt(position));
        if (rBtn != null)
            rBtn.setChecked(true);
        mIndex = position;
        disPacthContentData(mIndex);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fragment != null) {
            fragment.stopBackDoor(1);
            XLog.d(TAG, "stopBackDoor(int StreamType)。ipqam点播传1。ip点播传2");
        }
        if (infoReceive != null) {
            this.unregisterReceiver(infoReceive);
        }

        ButterKnife.unbind(this);
        //解决内存泄露
        fixInputMethodManagerLeak(this);
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (isFirstShow)
            mFocusView.setVisibility(View.VISIBLE);
        isFirstShow = false;
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onSucceeded(String method, String key, Object object) throws Exception {
        if ("getUserImg.action".equals(method)) {//获取用户头像
            UserFaceGetSucceedBean bean = (UserFaceGetSucceedBean) object;
            if (bean == null)
                return;
            if (bean.getCode().equals("0")) {
                int faceIndex = bean.getUserImg();
                Tools.setIntegerPreference(this, ConstantEnum.UserFace_Index, faceIndex);
                UserFaceViewHelper.setUserFaceImage(faceIndex, mMainIBtnUser);

                //余额积分
                Appconfig.setbalance(this, bean.getBalance());
                Appconfig.setUserIntegral(this, bean.getUserIntegral());
            }
            return;
        } else if ("getUserInfo.action".equals(method)) {//同步用户信息成功，此处判断用户是否已订购
            //
            if (object == null)
                return;
            syncUserInfoSucceedBean bean = (syncUserInfoSucceedBean) object;
            Appconfig.setOrder(this, bean.getStatus());

        } else if ("homePage.action".equals(method)) {//首页数据--------start
            HiFiMainPageBean bean = (HiFiMainPageBean) object;
            if (bean == null)
                return;
            if (JsonUtil.isLogicSucceed(bean.code)) {//逻辑成功 将数据分发给其他Fragment
                try {
                    if (bean.data == null)
                        return;
                    //做缓存
                    ACache aCache = ACache.get(this);
                    aCache.put(DiffAppconfig.mainBeanJsonKey, JsonUtil.getJsonStr(bean));
                    mainPageBean = bean;//无误数据赋值给全局变量
                    isCacheData = false;
                    disPatchFirstData();
                } catch (Exception e) {
                    XLog.d(TAG, "e = " + e);
                }
            } else {//容错
                hiFiDialogTools.showtips(this, mainPageBean.msg, null);
            }
            //首页数据--------end
            return;
        } else if ("getBackgroundMusic.action".equals(method)) {
            if (object == null)
                return;
            GetBackGroundMusicBean bean = ((GetBackGroundMusicBean) object);
            String code = bean.getCode();
            if ("0".equals(code)) {
                Appconfig.setTrackid(getApplicationContext(), Integer.toString(bean.getTrack().getId()));
                //获取播放链接
                CqApplication.getInstance().setApplicationCallBack(MainActivity.this);
                CqApplication.getInstance().getPlayUrl(MainActivity.this, bean.getTrack().getId(), 0);
            }
        }
    }

    @Override
    public void onFailed(String method, String key, int errorTipId) throws Exception {

    }


    public void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f = null;
        Object obj_get = null;
        for (int i = 0; i < arr.length; i++) {
            String param = arr[i];
            try {
                f = imm.getClass().getDeclaredField(param);
                if (f.isAccessible() == false) {
                    f.setAccessible(true);
                } // author: sodino mail:sodino@qq.com
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        break;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

    }

    private void getOwnerBgMusic() {
        asyncHttpRequest.getBgMusic(this, Appconfig.getKeyNo(getApplicationContext()), this, this, null);
    }

    @Override
    public void onBackPressed() {
        hiFiDialogTools.showLeftRightTip(MainActivity.this, "温馨提示", "确认退出" + getString(R.string.app_name) + "？", "再玩一会", "退出", new MyDialogEnterListener() {
            @Override
            public void onClickEnter(Dialog dialog, Object object) {
                if ((int) object == 1) {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PlayUtilActivity.CodeParentsLock) {
            //验证家长锁成功
            CqApplication.getInstance().getUserBalance();
        }
    }

    @Override
    public void onAppPlayUrl(String playUrl, int streamType) {
        this.vodPlayerUri = playUrl;
        this.streamType = streamType;
        if (baseInterface == null) {
            playFragment = VodPlayFragment.createInstance(ChongqingUUIDs.ID, VodPlayFragment.HUAWEI);
            baseInterface = playFragment.getPlayInterface(MainActivity.this);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(playFragment, null);
            transaction.commitAllowingStateLoss();
        } else {
            baseInterface.start(vodPlayerUri, 3, streamType, 0);
        }
    }

    @Override
    public void onContextReady(String s) {
        if (baseInterface != null) {
            XLog.d(TAG, "onContextReady() s" + s);
            baseInterface.start(vodPlayerUri, 3, streamType, 0);
            baseInterface.setVolume(1);//-1.0~1.0
            baseInterface.setDisplay(0, 0, 10, 10);//播放视频区域 x,y,w,h
        }
    }

    @Override
    public void onVodDuration(long l) {

    }

    @Override
    public void onSeeBackPeriod(long l, long l1) {

    }

    @Override
    public void onPlayStart(boolean b) {

    }

    @Override
    public void onSourceRate(float v) {

    }

    @Override
    public void onSourceSeek(long l) {

    }

    @Override
    public void onSyncMediaTime(long l) {

    }

    @Override
    public void onPlayEnd(float v) {

    }

    @Override
    public void onPlayMsg(String s) {

    }

    @Override
    public void onPlayError(String s) {

    }
}
