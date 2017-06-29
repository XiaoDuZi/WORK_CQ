package com.cq.ln.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cq.ln.R;
import com.cq.ln.adapter.CollectionRecenPlayAdapter;
import com.cq.ln.adapter.CollectionSingleAdapter;
import com.cq.ln.adapter.CollectionSpecialAdapter;
import com.cq.ln.appConfig.Appconfig;
import com.cq.ln.constant.ConstantEnum;
import com.cq.ln.utils.ActivityUtility;
import com.cq.ln.utils.AnimationUtils;
import com.cq.ln.utils.KeyActionControlUtils;
import com.cq.ln.utils.StrTool;
import com.cq.ln.utils.Tools;
import com.cq.ln.utils.XLog;
import com.cq.ln.views.FocusBorderView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cqdatasdk.bean.CollectionSinglebean;
import cqdatasdk.bean.CollectionSpecailBean;
import cqdatasdk.bean.RecentPlayBean;
import cqdatasdk.bean.SpecialDetailBean;
import cqdatasdk.interfaces.IVolleyRequestSuccess;
import cqdatasdk.interfaces.IVolleyRequestfail;
import cqdatasdk.utils.BeanConvertUtils;


/**
 * 用户中心 -->更换为 [我的收藏]
 */
public class UserCenterActivity extends BaseActivity implements AdapterView.OnItemClickListener, IVolleyRequestSuccess, IVolleyRequestfail {
    private static final String TAG = "UserCenterActivity";


    private final float scale = 1.0f;
    @Bind(R.id.btnRecentPlay)
    Button mBtnRecentPlay;
    @Bind(R.id.btnCollection)
    Button mBtnCollection;
    @Bind(R.id.btnUserInfo)
    Button mBtnUserInfo;
    @Bind(R.id.left_layout)
    FrameLayout mLeftLayout;

    @Bind(R.id.recent_ImageTopNav)
    ImageView mRecentImageTopNav;
    @Bind(R.id.listViewRecent)
    ListView mListViewRecent;
    @Bind(R.id.recent_ImageButtomNav)
    ImageView mRecentImageButtomNav;
    @Bind(R.id.recent_TxtNav)
    TextView mRecentTxtNav;
    @Bind(R.id.recentRootWarp)
    FrameLayout mRecentRootWarp;
    @Bind(R.id.btnSpecailPlay)
    Button mBtnSpecailPlay;
    @Bind(R.id.btnSinglePlay)
    Button mBtnSinglePlay;
    @Bind(R.id.right_layout_other_middle)
    FrameLayout mRightLayoutOtherMiddle;
    @Bind(R.id.collection_ImageTopNav)
    ImageView mCollectionImageTopNav;
    @Bind(R.id.listViewSingle)
    ListView mListViewSingle;
    @Bind(R.id.gridViewSpecail)
    GridView mGridViewSpecail;
    @Bind(R.id.collection_ImageBottomNav)
    ImageView mCollectionImageBottomNav;
    @Bind(R.id.collection_text_tv)
    TextView mCollectionTextTv;
    @Bind(R.id.collectionTxtNav)
    TextView mCollectionTxtNav;
    @Bind(R.id.collection_Bottom_nav)
    LinearLayout mCollectionBottomNav;
    @Bind(R.id.collectionRootWarp)
    FrameLayout mCollectionRootWarp;
    @Bind(R.id.right_layout_other)
    FrameLayout mRightLayoutOther;
    @Bind(R.id.activity_RootView)
    FrameLayout mActivityRootView;


    private FocusBorderView borderView;
    private FrameLayout topLayout;
    /**
     * 记录keep颜色的View
     */
    private View lastFocusView;
    private Context mContext;

    private int showWhich;


    private HashMap<String, Integer> currentLoadedMap = new HashMap<>();//当前已经加载了数据的页面
    private HashMap<String, Integer> totalPageMap = new HashMap<>();//当前有多少页
    private HashMap<String, Integer> totalSize = new HashMap<>();//当前有多少数据

    private String MapKeyRecent = "MapKeyRecent";
    private String MapKeySingle = "MapKeySingle";
    private String MapKeySpecail = "MapKeySpecail";
    //


    /**
     * 最近播放
     */
    private CollectionRecenPlayAdapter collectionRecenPlayAdapter;

    /**
     * 单曲
     */
    private CollectionSingleAdapter collectionSingleAdapter;
    /**
     * 专辑
     */
    private CollectionSpecialAdapter collectionSpecialAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center_layout);

        ButterKnife.bind(this);
        mContext = this;

        //遍历所有view,实现focus监听
        traversalView(this);
        //调用复写的创建BorderView
        createBorderView(this);

        initAdapter();
        mActivityRootView.getViewTreeObserver().addOnGlobalFocusChangeListener(this);

        showWhich = getIntent().getIntExtra("showWhich", ConstantEnum.Main_RecentPlay_);//默认最近播放

        XLog.d(TAG, "showWhich = " + showWhich);
        initWhich(showWhich);

    }

    private void firstGetCollectionData(int collectionType) {
        switch (collectionType) {
            case ConstantEnum.collectionType_single_for_interface:
                getCollectionSingleData(1);

                break;
            case ConstantEnum.collectionType_Specail_for_interface:

                getCollectionSpecailData(1);
                break;
        }
    }


    private void initWhich(int showWhich) {
        final boolean hasFocus = true;
        switch (showWhich) {
            case ConstantEnum.Main_RecentPlay_://最近播放
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBtnRecentPlay.requestFocus();
                        initFocus(mBtnRecentPlay, true);


                    }
                }, 100);
                break;

            case ConstantEnum.Main_Collection_://3收藏
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBtnCollection.requestFocus();
                        initFocus(mBtnCollection, true);
                    }
                }, 100);
                break;
        }
    }


    @Override
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {
        XLog.d(TAG, "oldFocus = " + oldFocus + " newFocus = " + newFocus);


//        if (newFocus != null)
//            newFocus.setBackgroundColor(this.getResources().getColor(R.color.red));
//        if (oldFocus != null)
//            oldFocus.setBackgroundColor(this.getResources().getColor(R.color.gray_a6));

        lastFocusView = oldFocus;
        if (newFocus == null)
            return;

        if (oldFocus != null && oldFocus.equals(mBtnCollection) && newFocus.equals(mGridViewSpecail)) {//非法状态
            mBtnCollection.requestFocus();//强制其获得焦点
            return;
        }
        setKeepingBorder();

        if (newFocus.equals(mListViewSingle)) {//=================单曲
            View currentSelectedView = mListViewSingle.getSelectedView();
            if (currentSelectedView == null) {
                currentSelectedView = mListViewSingle.getChildAt(0);
            }
            if (currentSelectedView != null)
                setListViewFocusView(currentSelectedView);

        } else if (newFocus.equals(mListViewRecent)) {//============最近播放

            View currentSelectedView = mListViewRecent.getSelectedView();//mListViewRecent.getChildAt(1);//奇葩问题,特别处理;

            if (currentSelectedView == null) {
                currentSelectedView = mListViewRecent.getChildAt(0);
            }
            if (currentSelectedView != null)
                setListViewFocusView(currentSelectedView);


        } else if (newFocus.equals(mGridViewSpecail)) {//========专辑
            View currentSelectedView = mGridViewSpecail.getSelectedView();
            if (currentSelectedView == null) {
                currentSelectedView = mGridViewSpecail.getChildAt(0);
            }
            if (currentSelectedView != null)
                setGriViewFocusView(currentSelectedView);
        }
    }


    /**
     * @param currentFocusView 列表模式
     */
    private void setListViewFocusView(View currentFocusView) {
        if (currentFocusView == null)
            return;
        borderView.setBorderBitmapResId(R.mipmap.list_big_focus);
        borderView.setHideView(false);//这一句非常重要
        borderView.setFocusView(currentFocusView, scale);
    }

    /**
     * @param currentFocusView 网格模式
     */
    private void setGriViewFocusView(View currentFocusView) {
        if (currentFocusView == null)
            return;
        borderView.setBorderBitmapResId(R.mipmap.listpic_focus_bg);
        borderView.setHideView(false);//这一句非常重要
        borderView.setFocusView(currentFocusView, scale);
    }


    private void initAdapter() {
        collectionSingleAdapter = new CollectionSingleAdapter(this);
        mListViewSingle.setAdapter(collectionSingleAdapter);

        collectionSpecialAdapter = new CollectionSpecialAdapter(this);
        mGridViewSpecail.setAdapter(collectionSpecialAdapter);

        collectionRecenPlayAdapter = new CollectionRecenPlayAdapter(this);
        mListViewRecent.setAdapter(collectionRecenPlayAdapter);

        //单曲
        mListViewSingle.setOnItemSelectedListener(new myOnItemSelectener(mListViewSingle));
        //专辑
        mGridViewSpecail.setOnItemSelectedListener(new myOnItemSelectener(mGridViewSpecail));
        //最近播放
        mListViewRecent.setOnItemSelectedListener(new myOnItemSelectener(mListViewRecent));

        mListViewSingle.setOnItemClickListener(this);
        mListViewRecent.setOnItemClickListener(this);
        mGridViewSpecail.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Object object = parent.getAdapter().getItem(position);
        if (object == null)
            return;

        XLog.d(TAG, "单击:" + object.toString());

        if (object instanceof RecentPlayBean.DataBean.ListBean) {//最近播放
            RecentPlayBean.DataBean.ListBean bean = (RecentPlayBean.DataBean.ListBean) object;

            SpecialDetailBean.DataBean.TrackListBean trackListBean = BeanConvertUtils.getInstance().Recentbean2Specailbean(bean);

            List<SpecialDetailBean.DataBean.TrackListBean> trackListBeanList =
                    BeanConvertUtils.getInstance().RecentList2SpecailList(collectionRecenPlayAdapter.getlist());

            //带列表带跳转
            goMusicPlayActivity(position, trackListBean, (Serializable) trackListBeanList, bean.getAlbumName(), bean.getArtistName());


            return;
        } else if (object instanceof CollectionSinglebean.DataBean.ListBean.ResultBean) {//单曲

            CollectionSinglebean.DataBean.ListBean.ResultBean bean = (CollectionSinglebean.DataBean.ListBean.ResultBean) object;
            SpecialDetailBean.DataBean.TrackListBean trackListBean = BeanConvertUtils.getInstance().collectionSingle2SpecialBean(bean);

            List<SpecialDetailBean.DataBean.TrackListBean> trackListBeanList =
                    BeanConvertUtils.getInstance().collectionSingleList2SpecailBeanList(collectionSingleAdapter.getlist());

            //带列表带跳转
            goMusicPlayActivity(position, trackListBean, (Serializable) trackListBeanList, bean.getAlbumName(), bean.getArtistName());


            return;
        } else if (object instanceof CollectionSpecailBean.DataBean.ListBean.ResultBean) {//专辑
            CollectionSpecailBean.DataBean.ListBean.ResultBean bean = (CollectionSpecailBean.DataBean.ListBean.ResultBean) object;
            int albumId = StrTool.parsetNum(bean.getAlbumId());
            ActivityUtility.goSpecialDetailActivity(this, albumId);

            return;
        }
    }

    /**
     * //带列表带跳转
     *
     * @param position
     * @param trackListBean
     * @param trackListBeanList
     * @param albumName
     * @param artistName
     */
    private void goMusicPlayActivity(int position, SpecialDetailBean.DataBean.TrackListBean trackListBean, Serializable trackListBeanList, String albumName, String artistName) {
        Intent intent = new Intent(this, PlayUtilActivity.class);
        intent.putExtra("isSingle", false);
        intent.putExtra("TrackListBean", trackListBean);
        intent.putExtra("trackList", trackListBeanList);
        intent.putExtra("currentIndex", position);
        intent.putExtra("albumName", albumName);//专辑名
        intent.putExtra("artistNames", artistName);//艺术家
        startActivity(intent);
    }

    @Override
    public void onSucceeded(String method, String key, Object object) {
        if ("userCollection.action".equals(method)) {
            if (object == null)
                return;
            if (object instanceof CollectionSinglebean) {//=========================单曲
                CollectionSinglebean singlebean = (CollectionSinglebean) object;
                if (singlebean.getData() == null) {
                    Tools.showTip(UserCenterActivity.this, singlebean.getMsg());
                    return;
                }
                int dataPage = singlebean.getData().getPage();
                List<CollectionSinglebean.DataBean.ListBean.ResultBean> temp = singlebean.getData().getList().getResult();
                if (dataPage == 1) {
                    if (temp == null || temp.isEmpty()) {
                        Tools.showTip(UserCenterActivity.this, "暂无数据");
                        return;
                    }
                } else {
                    if (null == temp || temp.isEmpty()) {
//                         Tools.showTip(UserCenterActivity.this, "暂无更多数据");
                        return;
                    }
                    //去重数据
                    int currentpage = currentLoadedMap.get(MapKeySingle);
                    if (dataPage <= currentpage) {//返回的数据所在的page小于等于最新存在的page,需要return，达到去重数据作用
                        XLog.d(TAG, "return currentpage =" + currentpage);
                        return;
                    }
                }
                currentLoadedMap.put(MapKeySingle, singlebean.getData().getPage());//纪录加载了数据的页码
                totalPageMap.put(MapKeySingle, singlebean.getData().getTotalPage());//纪录它有最大多少页
                totalSize.put(MapKeySingle, singlebean.getData().getCount());
                //单曲
                setSingleData2Adapter(temp);
                setCollectionNav(-1, singlebean.getData().getCount(), true);
                return;
            } else if (object instanceof CollectionSpecailBean) {//=========================专辑
                CollectionSpecailBean specailBean = (CollectionSpecailBean) object;
                if (specailBean == null)
                    return;
                if (specailBean.getData() == null) {
                    Tools.showTip(UserCenterActivity.this, specailBean.getMsg());
                    return;
                }
                int dataPage = specailBean.getData().getPage();
                List<CollectionSpecailBean.DataBean.ListBean.ResultBean> temp = specailBean.getData().getList().getResult();
                if (dataPage == 1) {
                    if (temp == null || temp.isEmpty()) {
                        Tools.showTip(UserCenterActivity.this, "暂无数据");
                        return;
                    }
                } else {
                    if (null == temp || temp.isEmpty()) {
//                         Tools.showTip(UserCenterActivity.this, "暂无更多数据");
                        return;
                    }
                    //去重数据
                    int currentpage = currentLoadedMap.get(MapKeySpecail);
                    if (dataPage <= currentpage) {//返回的数据所在的page小于等于最新存在的page,需要return，达到去重数据作用
                        XLog.d(TAG, "return currentpage =" + currentpage);
                        return;
                    }
                }
                currentLoadedMap.put(MapKeySpecail, specailBean.getData().getPage());//纪录加载了数据的页码
                totalPageMap.put(MapKeySpecail, specailBean.getData().getTotalPage());//纪录它有最大多少页
                totalSize.put(MapKeySpecail, specailBean.getData().getCount());
                //专辑
                setSpecailData2Adapter(temp);
                setCollectionNav(-1, specailBean.getData().getCount(), false);
                return;
            }

            return;
        } else if ("userPlayList.action".equals(method)) {//=========================最近播放
            if (object == null)
                return;
            RecentPlayBean mRecentPlayBean = (RecentPlayBean) object;
            if (mRecentPlayBean.getData() == null) {
                Tools.showTip(UserCenterActivity.this, mRecentPlayBean.getMsg());
                return;
            }
            int dataPage = mRecentPlayBean.getData().getPage();
            List<RecentPlayBean.DataBean.ListBean> temp = mRecentPlayBean.getData().getList();
            if (dataPage == 1) {
                if (temp == null || temp.isEmpty()) {
                    Tools.showTip(UserCenterActivity.this, "暂无数据");
                    return;
                }
            } else {
                if (null == temp || temp.isEmpty()) {
//                     Tools.showTip(UserCenterActivity.this, "暂无更多数据");
                    return;
                }
                //去重数据
                int currentpage = currentLoadedMap.get(MapKeyRecent);
                if (dataPage <= currentpage) {//返回的数据所在的page小于等于最新存在的page,需要return，达到去重数据作用
                    XLog.d(TAG, "return currentpage =" + currentpage);
                    return;
                }
            }
            currentLoadedMap.put(MapKeyRecent, mRecentPlayBean.getData().getPage());//纪录加载了数据的页码
            totalPageMap.put(MapKeyRecent, mRecentPlayBean.getData().getTotalPage());//纪录它有最大多少页
            //专辑
            setRecentDataToApdater(temp);

            setRecentPlayNav(-1, mRecentPlayBean.getData().getCount());

            return;
        }
    }


    private void setRecentDataToApdater(List<RecentPlayBean.DataBean.ListBean> list) {
        if (list.isEmpty())
            return;
        collectionRecenPlayAdapter.refreshAdapter(list);

    }


    /**
     * 收藏的专辑
     *
     * @param list
     */
    private void setSpecailData2Adapter(List<CollectionSpecailBean.DataBean.ListBean.ResultBean> list) {
        if (list.isEmpty())
            return;
        collectionSpecialAdapter.refreshAdapter(list);

    }


    /**
     * 收藏的单曲
     *
     * @param list
     */
    private void setSingleData2Adapter(List<CollectionSinglebean.DataBean.ListBean.ResultBean> list) {
        if (list.isEmpty())
            return;
        collectionSingleAdapter.refreshAdapter(list);
    }


    @Override
    public void onFailed(String method, String key, int errorTipId) {

    }


    class myOnItemSelectener implements AdapterView.OnItemSelectedListener {
        View parent;

        public myOnItemSelectener(View parent) {
            this.parent = parent;
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            if (parent.equals(mListViewSingle)) {//单曲
                if (view == null)
                    return;
                if (getCurrentFocus() != null && !getCurrentFocus().equals(mListViewSingle))
                    return;
                setListViewFocusView(view);

                setCollectionNav(position + 1, -1, true);
                return;

            } else if (parent.equals(mGridViewSpecail)) {//专辑
                if (view == null)
                    return;
                if (getCurrentFocus() != null && !getCurrentFocus().equals(mGridViewSpecail))
                    return;
                setGriViewFocusView(view);
                setCollectionNav(position + 1, -1, false);
                return;

            } else if (parent.equals(mListViewRecent)) {//最近播放
                if (view == null)
                    return;
                if (getCurrentFocus() != null && !getCurrentFocus().equals(mListViewRecent))
                    return;


                if (position < mListViewRecent.getFirstVisiblePosition()) {
                    XLog.d(TAG, "position 越界,被纠正!");
                    view = mListViewRecent.getChildAt(0);
                }
                setListViewFocusView(view);
                setRecentPlayNav(position + 1, -1);
                return;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }


    private void getRecentPlayData(int pageno) {
        asyncHttpRequest.getUserRecentPlayBean(this, pageno, Appconfig.getKeyNo(getApplicationContext()), this, this, pageno == 1 ? "" : null);
    }


    /**
     * @param pageno 获取用户收藏单曲
     */
    private void getCollectionSingleData(int pageno) {
        asyncHttpRequest.getUserCollectSingleBean(this, pageno, Appconfig.getKeyNo(getApplicationContext()), this, this, pageno == 1 ? "" : null);
    }

    /**
     * @param pageno 获取用户收藏专辑
     */
    private void getCollectionSpecailData(int pageno) {
        asyncHttpRequest.getUserCollectSpecailBean(this, pageno, Appconfig.getKeyNo(getApplicationContext()), this, this, pageno == 1 ? "" : null);
    }

    /**
     * 最近播放
     *
     * @param cindex
     * @param tsize
     */
    private void setRecentPlayNav(int cindex, int tsize) {
        String orign = mRecentTxtNav.getText().toString();
        String[] origns = {"1", "0"};
        try {
            origns = orign.split("/");
        } catch (Exception e) {
            XLog.d(TAG, "e :" + e);
        }
        if (cindex == -1)
            cindex = Integer.parseInt(origns[0]);
        if (tsize == -1)
            tsize = Integer.parseInt(origns[1]);


        if (cindex > tsize)//TODO 虽是BUG,临时处理!!
            cindex = tsize;

        setRecentTopBottomUINav(cindex, tsize);
        String currentIndex = String.valueOf(cindex);
        String totalSize = String.valueOf(tsize);
        if (currentIndex.equals("-1"))
            currentIndex = origns[0];
        if (totalSize.equals("-1"))
            totalSize = origns[1];

        String result = currentIndex + "/" + totalSize;

        mRecentTxtNav.setText(result);
    }

    /**
     * @param cindex 最近播放上下三角形指示
     * @param tsize
     */
    private void setRecentTopBottomUINav(int cindex, int tsize) {
        if (cindex == 1)
            mRecentImageTopNav.setImageResource(R.mipmap.page_pre_have_nonu);
        else
            mRecentImageTopNav.setImageResource(R.mipmap.page_pre_have_more);

        if (cindex == tsize)
            mRecentImageButtomNav.setImageResource(R.mipmap.page_next_have_nonu);
        else
            mRecentImageButtomNav.setImageResource(R.mipmap.page_next_have_more);

    }

    /**
     * 收藏专辑,收藏单曲,同一个TextView控制
     *
     * @param cindex
     * @param tsize
     * @param isSingle
     */
    private void setCollectionNav(int cindex, int tsize, boolean isSingle) {
        String orign = mCollectionTxtNav.getText().toString();
        String[] origns = {"1", "0"};
        try {
            origns = orign.split("/");
        } catch (Exception e) {
            XLog.d(TAG, "e:" + e);
        }
        if (cindex == -1)
            cindex = Integer.parseInt(origns[0]);
        if (tsize == -1)
            tsize = Integer.parseInt(origns[1]);

        if (cindex > tsize)//TODO 虽是BUG,临时处理!!
            cindex = tsize;
        setCollectionTopBottomNav(cindex, tsize, isSingle);
        String currentIndex = String.valueOf(cindex);
        String totalSize = String.valueOf(tsize);
        if (currentIndex.equals("-1"))
            currentIndex = origns[0];
        if (totalSize.equals("-1"))
            totalSize = origns[1];
        String result = currentIndex + "/" + totalSize;

        mCollectionTxtNav.setText(result);
    }

    private void setCollectionTopBottomNav(int cindex, int tsize, boolean isSingle) {
        if (isSingle) {
            if (cindex == 1)
                mCollectionImageTopNav.setImageResource(R.mipmap.page_pre_have_nonu);
            else
                mCollectionImageTopNav.setImageResource(R.mipmap.page_pre_have_more);

            if (cindex == tsize)
                mCollectionImageBottomNav.setImageResource(R.mipmap.page_next_have_nonu);
            else
                mCollectionImageBottomNav.setImageResource(R.mipmap.page_next_have_more);

        } else {
            if (cindex <= 4)
                mCollectionImageTopNav.setImageResource(R.mipmap.page_pre_have_nonu);
            else
                mCollectionImageTopNav.setImageResource(R.mipmap.page_pre_have_more);

            if (cindex > tsize - 4)
                mCollectionImageBottomNav.setImageResource(R.mipmap.page_next_have_nonu);
            else
                mCollectionImageBottomNav.setImageResource(R.mipmap.page_next_have_more);

        }
    }

    @Override
    public void createBorderView(Activity activity) {
        borderView = new FocusBorderView(mContext);
        topLayout = (FrameLayout) getRootView(activity);
        borderView.setBorderViewHide();
        if (topLayout != null) {
            topLayout.addView(borderView);
        }

    }


    @Override    //复写监听，此处不要调用父类的onFocusChange
    public void onFocusChange(View v, boolean hasFocus) {
        borderView.setBorderBitmapResId(R.mipmap.leftnav_focus_current);


        XLog.d(TAG, "onFocusChange v = " + v.toString() + "  hasFocus =  " + hasFocus);

        if (hasFocus) {
            switch (v.getId()) {
                case R.id.btnRecentPlay://最近播放
                    AnimationUtils.fadein(this, mRecentRootWarp);
                    AnimationUtils.fadeout(this, mCollectionRootWarp);

                    int po = mListViewRecent.getSelectedItemPosition();
                    if (po < 0 || po > collectionRecenPlayAdapter.getCount() - 4)
                        po = 0;
                    mListViewRecent.setSelection(po);//修复BUG,非常重要!!

                    //getData
                    if (collectionRecenPlayAdapter.isEmpty()) {//首次加载
                        getRecentPlayData(1);
                    }

                    break;
                case R.id.btnCollection://收藏
                    AnimationUtils.fadeout(this, mRecentRootWarp);
                    AnimationUtils.fadein(this, mCollectionRootWarp);

                    if (isShowingSingleListView()) {//当前是单曲
                        mBtnCollection.setNextFocusRightId(mBtnSinglePlay.getId());
                        showSingleUI();
                    } else {
                        mBtnCollection.setNextFocusRightId(mBtnSpecailPlay.getId());
                        showSpecailUI();//默认是专辑
                    }

                    if (collectionSpecialAdapter.isEmpty()) {
                        firstGetCollectionData(ConstantEnum.collectionType_Specail_for_interface);
                    }

                    break;
                case R.id.btnSinglePlay://单曲收藏
                    showSingleUI();
                    if (collectionSingleAdapter.isEmpty()) {
                        firstGetCollectionData(ConstantEnum.collectionType_single_for_interface);
                    }

                    if (totalSize.containsKey(MapKeySingle))
                        setCollectionNav(-1, totalSize.get(MapKeySingle), true);

                    break;
                case R.id.btnSpecailPlay://专辑收藏
                    showSpecailUI();
                    if (collectionSpecialAdapter.isEmpty()) {
                        firstGetCollectionData(ConstantEnum.collectionType_Specail_for_interface);
                    }
                    if (totalSize.containsKey(MapKeySpecail))
                        setCollectionNav(-1, totalSize.get(MapKeySpecail), false);
                    break;
            }
        }
        initFocus(v, hasFocus);

    }

    /**
     * @return 是否在显示单曲
     */
    private boolean isShowingSingleListView() {
        return mListViewSingle.getVisibility() == View.VISIBLE;
    }

    private void showSingleUI() {
        AnimationUtils.fadeout(this, mGridViewSpecail);
        AnimationUtils.fadein(this, mListViewSingle);

    }

    private void showSpecailUI() {
        AnimationUtils.fadein(this, mGridViewSpecail);
        AnimationUtils.fadeout(this, mListViewSingle);

    }

    private void initFocus(View v, boolean hasFocus) {
        if (hasFocus) {
            v.bringToFront();
            v.getParent().bringChildToFront(v);
            v.requestLayout();
            v.invalidate();
            borderView.setHideView(false);//这一句非常重要
            borderView.setFocusView(v, scale);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setKeepingBorder();
                }
            }, 200);
        } else {
            borderView.setUnFocusView(v);
            return;
        }
    }


    /**
     * @param event 快速点击控制
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (KeyActionControlUtils.isFastClick(KeyActionControlUtils.MyPadKey.Right))
                        return super.dispatchKeyEvent(event);

                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    if (KeyActionControlUtils.isFastClick(KeyActionControlUtils.MyPadKey.Left))
                        return super.dispatchKeyEvent(event);
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    if (KeyActionControlUtils.isFastClick(KeyActionControlUtils.MyPadKey.Up))
                        return super.dispatchKeyEvent(event);
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    if (KeyActionControlUtils.isFastClick(KeyActionControlUtils.MyPadKey.Down))
                        return super.dispatchKeyEvent(event);

                    //处理分页
                    try {
                        return handlerPaging(event);
                    } catch (Exception e) {
                        XLog.d(TAG, "e:" + e);
                        e.printStackTrace();
                    }

                    break;

                case KeyEvent.KEYCODE_ENTER:
                    if (KeyActionControlUtils.isFastClick(KeyActionControlUtils.MyPadKey.Enter))
                        return super.dispatchKeyEvent(event);
                    break;
            }

        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 分页加载数据
     *
     * @param event
     * @return
     * @throws Exception
     */
    private boolean handlerPaging(KeyEvent event) throws Exception {
        View view = getCurrentFocus();
        if (null == view) {
            return true;
        }

        if (view.equals(mListViewRecent)) {//最近播放

            int loadedPage = currentLoadedMap.get(MapKeyRecent);
            if (loadedPage < totalPageMap.get(MapKeyRecent))
                getRecentPlayData(++loadedPage);

        } else if (view.equals(mListViewSingle)) {//单曲播放
            int loadedPage = currentLoadedMap.get(MapKeySingle);
            if (loadedPage < totalPageMap.get(MapKeySingle))
                getCollectionSingleData(++loadedPage);

        } else if (view.equals(mGridViewSpecail)) {//专辑
            int loadedPage = currentLoadedMap.get(MapKeySpecail);
            if (loadedPage < totalPageMap.get(MapKeySpecail))
                getCollectionSpecailData(++loadedPage);

        }
        return super.dispatchKeyEvent(event);
    }


    private void setKeepingBorder() {
        if (getCurrentFocus() == null)
            return;
        if (getCurrentFocus().equals(mListViewRecent)) {//最近播发的列表
            setbuttonKeepVermilionBorder(mBtnRecentPlay);
            clearbuttonKeepingBorder(mBtnCollection);
            return;

        } else if (getCurrentFocus().equals(mBtnRecentPlay)) {//最近播放
            clearbuttonKeepingBorder(mBtnCollection);
            return;

        } else if (getCurrentFocus().equals(mBtnCollection)) {//收藏
            clearbuttonKeepingBorder(mBtnRecentPlay);
            setRightChildKeepBorder(lastFocusView);
            return;

        } else if (getCurrentFocus().equals(mBtnSpecailPlay)) {//专辑收藏
            clearbuttonKeepingBorder(mBtnRecentPlay, mBtnSinglePlay);
            setbuttonKeepVermilionBorder(mBtnCollection);
            return;

        } else if (getCurrentFocus().equals(mBtnSinglePlay)) {//单曲收藏
            clearbuttonKeepingBorder(mBtnRecentPlay, mBtnSpecailPlay);
            setbuttonKeepVermilionBorder(mBtnCollection);
            return;

        } else if (getCurrentFocus().equals(mGridViewSpecail)) {//专辑收藏网格View
            clearbuttonKeepingBorder(mBtnRecentPlay, mBtnSinglePlay);
            setbuttonKeepVermilionBorder(mBtnCollection, mBtnSpecailPlay);
            return;

        } else if (getCurrentFocus().equals(mListViewSingle)) {//单曲专辑
            clearbuttonKeepingBorder(mBtnRecentPlay, mBtnSpecailPlay);
            setbuttonKeepVermilionBorder(mBtnCollection, mBtnSinglePlay);
            return;

        }

    }

    private void setRightChildKeepBorder(View lastFocusView) {
        if (lastFocusView == null)
            return;

        if (lastFocusView.equals(mBtnSinglePlay)) {
            setbuttonKeepVermilionBorder(mBtnSinglePlay);
        } else if (lastFocusView.equals(mBtnSpecailPlay)) {
            setbuttonKeepVermilionBorder(mBtnSpecailPlay);
        } else if (lastFocusView.equals(mGridViewSpecail)) {//纠正
            setbuttonKeepVermilionBorder(mBtnSpecailPlay);
        } else if (lastFocusView.equals(mListViewSingle)) {//纠正
            setbuttonKeepVermilionBorder(mBtnSinglePlay);
        }
    }


    private void setbuttonKeepVermilionBorder(Button... buttons) {
        if (buttons.length == 0)
            return;
        for (Button button : buttons) {
            if (button == null)
                continue;
            button.setBackgroundDrawable(this.getResources().getDrawable(R.mipmap.leftnav_focus_keep));
        }
    }

    private void clearbuttonKeepingBorder(Button... buttons) {
        if (buttons.length == 0)
            return;
        for (Button button : buttons) {
            if (button == null)
                continue;
            button.setBackgroundDrawable(this.getResources().getDrawable(R.mipmap.nav_empty));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        currentLoadedMap = null;
        totalPageMap = null;

        collectionRecenPlayAdapter = null;
        collectionSingleAdapter = null;
        collectionSpecialAdapter = null;
    }


}
