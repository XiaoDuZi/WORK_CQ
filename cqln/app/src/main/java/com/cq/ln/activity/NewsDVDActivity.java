package com.cq.ln.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cq.ln.R;
import com.cq.ln.adapter.SearchLeftFilterAdapter;
import com.cq.ln.adapter.SearchResultGridViewAdapter;
import com.cq.ln.appConfig.Appconfig;
import com.cq.ln.utils.ActivityUtility;
import com.cq.ln.utils.KeyActionControlUtils;
import com.cq.ln.utils.Tools;
import com.cq.ln.utils.XLog;
import com.cq.ln.views.FocusBorderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cqdatasdk.bean.VipTypeBean;
import cqdatasdk.bean.VipTypeRelateAlbumBean;
import cqdatasdk.interfaces.IVolleyRequestSuccess;
import cqdatasdk.interfaces.IVolleyRequestfail;

/**
 * 新碟抢先听
 */
public class NewsDVDActivity extends BaseActivity implements View.OnFocusChangeListener, IVolleyRequestSuccess, IVolleyRequestfail, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, ViewTreeObserver.OnGlobalFocusChangeListener {
    private static final String TAG = "NewsDVDActivity";
    private final float scale = 1.0f;
    @Bind(R.id.btnNewsDVD)
    Button mBtnNewsDVD;
    @Bind(R.id.Left_Warp_DVD)
    FrameLayout mLeftWarpDVD;
    @Bind(R.id.listFilterChildSong)
    ListView mListFilterDVD;
    @Bind(R.id.image_Page_PreDVD)
    ImageView mImagePagePreDVD;
    @Bind(R.id.gridViewResultDVD)
    GridView mGridViewResultDVD;
    @Bind(R.id.image_Page_NextDVD)
    ImageView mImagePageNextDVD;
    @Bind(R.id.txtPageNavDVD)
    TextView mTxtPageNavDVD;

    private FocusBorderView borderView;
    private FrameLayout topLayout;
    /**
     * 记录keep颜色的View
     */
    private View lastFocusViewForRecord;
    private Context mContext;

    private List<VipTypeBean.DataBean> vipTypeDataBeanList;



    private SearchLeftFilterAdapter mFilterAdapter;
    private View mFilterAdapterLastFoucusItem;
    /**
     * 当前第一级定位的View(在线搜索，新碟抢先听，音乐风格，艺术家)
     */
    private View currentTheFirstFilterView;

    private SearchResultGridViewAdapter searchResultGridViewAdapter;
    private List<Button> firstParentButtonList;


    //--------------------------------缓存变量------------------start

    private Map<Integer, List<VipTypeRelateAlbumBean.DataBean.ListBean>> vipRelateAlbumDataMap = new HashMap<>();//新碟抢先听
    private Map<Integer, Integer> vipAlbumTotalPageMap = new HashMap<>();
    private Map<Integer, Integer> vipAlbumLoadedPage = new HashMap<>();//记录加载的页数
    //--------------------------------缓存变量------------------end


    //--------------------------------纠正光标定位作用的变量---------------start
    private boolean LeftListView_FLAG = false;
    private int LeftListView_POSITION = -1;
    private boolean GridViewSelected_FLAG = false;
    private Button correctButton;//需要纠正的按钮
    private boolean IS_PAD_RIGHT_EVENT = false;
    //--------------------------------纠正光标定位作用的变量---------------end
    //---外部控制变量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_dvd);
        ButterKnife.bind(this);
        mContext = this;
        getRootView(this).getViewTreeObserver().addOnGlobalFocusChangeListener(this);

        //遍历所有view,实现focus监听
        traversalView(this);

        //调用复写的创建BorderView
        createBorderView(this);

        setButtonNoKeeping();

        initAdapter();

        initButtonList();



    }



    private void initButtonList() {
        firstParentButtonList = new ArrayList<>();
        firstParentButtonList.add(mBtnNewsDVD);
    }


    private void initAdapter() {
        //初始化左侧的list

        mFilterAdapter = new SearchLeftFilterAdapter(this);
        mListFilterDVD.setAdapter(mFilterAdapter);
        mListFilterDVD.setOnItemSelectedListener(this);
        mListFilterDVD.setOnItemClickListener(this);


        //初始化右边GridView的list
        searchResultGridViewAdapter = new SearchResultGridViewAdapter(this);
        mGridViewResultDVD.setAdapter(searchResultGridViewAdapter);
        mGridViewResultDVD.setOnItemSelectedListener(this);
        mGridViewResultDVD.setOnItemClickListener(this);

    }

    /**
     * 设置没有keep朱红色边框
     */
    private void setButtonNoKeeping() {
        mBtnNewsDVD.setTag(R.id.tag_for_button_is_keeping_color, false);
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

    /**
     * 清除Adapter的item选中状态
     */
    private void clearAdappterItemSelecterState(View view) {
        mListFilterDVD.clearFocus();
        if (view == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.btnNewsDVD:
                if (mFilterAdapterLastFoucusItem != null) {
                    if (mFilterAdapterLastFoucusItem.isSelected()) {
                        mFilterAdapterLastFoucusItem.setSelected(false);
                        XLog.d(TAG, "清除Adapter的item选中状态");
                        mFilterAdapter.notifyDataSetChanged();
                    }
                }
                break;

        }
    }

    /**
     * 清除view的朱红色边框
     */
    private void clearViewKeepVermilionBorder(View view) {
        if (view == null)
            return;
        switch (view.getId()) {
            case R.id.btnNewsDVD:
            case R.id.btnDiscRecommend:
            case R.id.btnMusicStyle:
            case R.id.btnSigner:
                int size = mLeftWarpDVD.getChildCount();
                for (int i = 0; i < size; i++) {
                    View child = mLeftWarpDVD.getChildAt(i);
                    if (child instanceof Button) {
                        Object o = child.getTag(R.id.tag_for_button_is_keeping_color);
                        if ((Boolean) o) {
                            child.setBackgroundDrawable(null);
                            child.setTag(R.id.tag_for_button_is_keeping_color, false);
                        }
                    }
                }
                break;
        }
    }

    /**
     * 设置view 朱红色边框
     */
    private void setParentViewKeepVermilionBorder() {
        if (lastFocusViewForRecord != null) {
            Object o = lastFocusViewForRecord.getTag(R.id.tag_for_button_is_keeping_color);
            if (o != null) {
                lastFocusViewForRecord.setBackgroundDrawable(this.getResources().getDrawable(R.mipmap.leftnav_focus_keep));
                lastFocusViewForRecord.setTag(R.id.tag_for_button_is_keeping_color, true);
            }
        }
    }


    @Override
    public void onSucceeded(String method, String key, Object object) {
        if ("getVipType.action".equals(method)) {//新碟抢先听刷选分类列表
            if (null == object)
                return;
            vipTypeDataBeanList = ((VipTypeBean) object).getData();
            if (vipTypeDataBeanList == null) {//容错处理
                Tools.showTip(NewsDVDActivity.this, ((VipTypeBean) object).getMsg());
                return;
            }
            if (currentTheFirstFilterView.equals(mBtnNewsDVD))
                refreshLeftFilterList(vipTypeDataBeanList);
            return;
        } else if ("albumList.action".equals(method)) {//1、__/__/__/__/__/__/__/__/__/__/__/__/__/__/__/__/__/新碟抢先听分页数据--分页
            if (null == object)
                return;
            VipTypeRelateAlbumBean.DataBean bean = ((VipTypeRelateAlbumBean) object).getData();
            if (null == bean) {//容错处理
                Tools.showTip(NewsDVDActivity.this, ((VipTypeRelateAlbumBean) object).getMsg());
                return;
            }
            List<VipTypeRelateAlbumBean.DataBean.ListBean> discRecommendListBeanList = vipRelateAlbumDataMap.get(Integer.parseInt(key));//searchResultGridViewAdapter.getlist();
            List<VipTypeRelateAlbumBean.DataBean.ListBean> temp = bean.getList();
            if (bean.getPage() == 1) {
                if (null == temp || temp.isEmpty()) {
                    Tools.showTip(NewsDVDActivity.this, "暂无数据");
                    return;
                }
                discRecommendListBeanList = temp;
            } else {
                if (null == temp || temp.isEmpty()) {
//                    Tools.showTip(SearchActivity.this, "暂无更多数据");
                    return;
                }
                int page = vipAlbumLoadedPage.get(Integer.parseInt(key));
                if (bean.getPage() <= page) //返回的数据所在的page小于等于最新存在的page,需要return，达到去重数据作用
                    return;
                discRecommendListBeanList.addAll(temp);
            }
            vipRelateAlbumDataMap.put(Integer.parseInt(key), discRecommendListBeanList);//添加缓存
            vipAlbumTotalPageMap.put(Integer.parseInt(key), bean.getTotalPage());//最大页数
            vipAlbumLoadedPage.put(Integer.parseInt(key), bean.getPage());//当前页数
            refreshRightGridViewList(discRecommendListBeanList);
            setOtherInfo(-1, bean.getTotalPage());
            return;
        }


    }


    /**
     * @param currentPage 新碟抢先听
     *                    栏目数据分页导航
     * @param totalPage
     */
    private void setOtherInfo(int currentPage, int totalPage) {
        if (totalPage == -1) {
            String str = mTxtPageNavDVD.getText().toString().trim().split("/")[1];
            totalPage = Integer.parseInt(str);
        }
        if (currentPage == -1) {
            String str = mTxtPageNavDVD.getText().toString().trim().split("/")[0];
            currentPage = Integer.parseInt(str);
        }

        if (currentPage > totalPage)//TODO 虽是BUG,临时处理!!
            currentPage = totalPage;

        mTxtPageNavDVD.setText(String.format("%d/%d", currentPage, totalPage));

        setGridViewPageNav(currentPage, totalPage);
    }

    private void setGridViewPageNav(int currentPage, int totalPage) {
        if (currentPage == 1)
            mImagePagePreDVD.setImageResource(R.mipmap.page_pre_have_nonu);
        else
            mImagePagePreDVD.setImageResource(R.mipmap.page_pre_have_more);

        if (currentPage == totalPage)
            mImagePageNextDVD.setImageResource(R.mipmap.page_next_have_nonu);
        else
            mImagePageNextDVD.setImageResource(R.mipmap.page_next_have_more);

    }

    /**
     * @param list 刷新左侧的刷新分类列表
     * @param <T>
     */
    private <T> void refreshLeftFilterList(List<T> list) {
        if (list == null || mFilterAdapter == null)
            return;
        mFilterAdapter.clearButMustUsedWithRefresh();
        mFilterAdapter.refreshAdapter(list);

        //马丹。此处一定要延时设置，确保Adapter执行完getView方法，在调用setSelfKeepView()方法，才能确保不会出现错位！！
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //设置keep颜色
                setSelfKeepView();
            }
        }, 200);

        //级联刷新三级数据,也需要延时!!!
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                preRefreshRightData();
            }
        }, 200);
    }

    private void setSelfKeepView() {
        //先清除
        clearListAllListViewItemBg();



        //再设置
        View view = mListFilterDVD.getSelectedView();
        //

        if (view == null)
            view = mListFilterDVD.getChildAt(0);

        if (view != null) {

            int po = mListFilterDVD.getPositionForView(view);
            if (po > mFilterAdapter.getCount() - 1)
                view = mListFilterDVD.getChildAt(mFilterAdapter.getCount() - 1);
            view.setBackgroundDrawable(getResources().getDrawable(R.mipmap.leftnav_focus_keep));
        }
    }


    private void clearListAllListViewItemBg() {
        int count = mListFilterDVD.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = mListFilterDVD.getChildAt(i);
            if (view.getBackground() != null)
                view.setBackgroundResource(0);
        }
    }

    private void preRefreshRightData() {
        View view = mListFilterDVD.getSelectedView();
        if (view == null)
            view = mListFilterDVD.getChildAt(0);
        if (view == null)
            return;
        Object object = view.getTag(R.id.Tag_for_search_LeftFilterList_Databean);
        if (object == null)
            return;

        if (object instanceof VipTypeBean.DataBean) {//新碟抢先听
            getVipTypeRelateAlbumList(((VipTypeBean.DataBean) object).getId(), 1);
        }
    }

    @Override
    public void onFailed(String method, String key, int errorTipId) {

    }


    /**
     * @param v        //复写监听，此处不要调用父类的onFocusChange
     *                 //最左边四个Button、输入框、搜索按钮，的监听事件
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        borderView.setBorderBitmapResId(R.mipmap.leftnav_focus_current);
        final int id = v.getId();
        //----------------------------------1、UI操作,优先-----------
        if (IS_PAD_RIGHT_EVENT)//防止父级光标错乱问题
            return;
        switch (id) {
            case R.id.btnNewsDVD:
                currentTheFirstFilterView = v;
                break;

        }
        if (hasFocus) {
            clearAdappterItemSelecterState(v);
            clearViewKeepVermilionBorder(v);
            v.requestLayout();
            v.invalidate();
            borderView.setHideView(false);//这一句非常重要
            borderView.setFocusView(v, scale);
            lastFocusViewForRecord = v;
        } else {
            borderView.setUnFocusView(v);
            return;//此处要return，避免不必要的重复操作
        }
        //----------------------------------2、数据操作,在UI操作后,做延时，保证动画流畅-----------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadLeftFilterWordList(id);
            }
        }, 400);
    }

    private void loadLeftFilterWordList(int id) {
        switch (id) {
            case R.id.btnNewsDVD://新碟抢先听
                getVipTypeFilterList();
                break;

        }
    }


    @Override
    public void onItemSelected(final AdapterView<?> adapterView, View view, final int position, long id) {

        if (GridViewSelected_FLAG) {
            GridViewSelected_FLAG = false;
            if (correctButton != null) {//纠正错位光标
                correctButton.requestLayout();
                correctButton.requestFocus();
                correctButton.setSelected(true);
            }
            return;
        }
        if (borderView == null)
            return;
        View v = getCurrentFocus();
        if (v != null && v.equals(mListFilterDVD)) {//------------------------------------------左侧ListView
            if (LeftListView_FLAG) {
                LeftListView_FLAG = false;
                if (LeftListView_POSITION != -1)
                    mListFilterDVD.setSelection(LeftListView_POSITION);
                return;
            }

            //1、优先处理动画，保证流畅
            borderView.setBorderBitmapResId(R.mipmap.leftnav_focus_current);
            setLeftFilterListViewItemFocusState(view);//设置listView的item选中光标定位

            //2、处理数据loading
            //此处做个延时，动画耗时300毫秒，保证流畅
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        loadDataByFilter(adapterView, position);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 400);
            return;
        } else if (v != null && v.equals(mGridViewResultDVD)) {//------------------------------------------右侧gridView
            borderView.setBorderBitmapResId(R.mipmap.listpic_focus_bg);
            setRightGridViewItemFocusState(view);
            int currentPage = (int) Math.ceil((position + 1) / 8.0);//每页8条数据
            setOtherInfo(currentPage, -1);
            return;
        }
    }


    /**
     * @param event 非常重要
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            IS_PAD_RIGHT_EVENT = false;
            if (currentTheFirstFilterView == null)
                return false;
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_RIGHT:

                    if (currentTheFirstFilterView.equals(mBtnNewsDVD)) {//新碟抢先听
                        IS_PAD_RIGHT_EVENT = true;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    if (KeyActionControlUtils.isFastClick(KeyActionControlUtils.MyPadKey.Enter))
                        return super.dispatchKeyEvent(event);
                    //处理分页
                    try {
                        return handlerPaging(event);
                    } catch (Exception e) {
                        XLog.d(TAG, "e =" + e);
                    }
            }
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * @param event 处理分页事件
     * @return
     */
    private boolean handlerPaging(KeyEvent event) throws Exception {
        View view = getCurrentFocus();
        if (null == view) {
            return true;
        }
        if (view.equals(mGridViewResultDVD)) {//___/___/___/___/___/___/___/___/___/___/___/___/网格数据分页处理
            View viewData = mListFilterDVD.getSelectedView();
            if (null == viewData) {
                viewData = mListFilterDVD.getChildAt(0);
            }
            Object object = viewData.getTag(R.id.Tag_for_search_LeftFilterList_Databean);
            if (object instanceof VipTypeBean.DataBean) {//新碟抢先听
                VipTypeBean.DataBean bean = (VipTypeBean.DataBean) object;
                int loadedPage = vipAlbumLoadedPage.get(bean.getId());
                if (loadedPage < vipAlbumTotalPageMap.get(bean.getId()))
                    getVipTypeRelateAlbumList(bean.getId(), ++loadedPage);

            }
        }
        return super.dispatchKeyEvent(event);

    }

    private void loadDataByFilter(AdapterView<?> adapterView, int i) throws Exception {
        if (currentTheFirstFilterView.equals(mBtnNewsDVD)) {//新碟
            VipTypeBean.DataBean bean = (VipTypeBean.DataBean) adapterView.getAdapter().getItem(i);
            getVipTypeRelateAlbumList(bean.getId(), 1);
        }
    }


    /**
     * @param oldView 补充监听，处理光标错乱问题,解决首次列表（listView ，GridView）定位不选中的情况,非常重要！！！
     * @param newView 切换了View ，一定优先走这个方法，之后同一个listView 的item focus 不会走这个方法。
     */
    @Override
    public void onGlobalFocusChanged(View oldView, View newView) {
        if (oldView == null || newView == null)
            return;
//        newView.setBackgroundColor(this.getResources().getColor(R.color.red));
//        oldView.setBackgroundColor(this.getResources().getColor(R.color.gray_a6));

        if (oldView.equals(mBtnNewsDVD) && newView.equals(mGridViewResultDVD)) {//新碟抢先听，防止光标飘移
            correctButton = mBtnNewsDVD;
            GridViewSelected_FLAG = true;
            return;
        }


        //设置keep背景
        if (oldView.equals(mListFilterDVD)) {
            View view = mListFilterDVD.getSelectedView();
            if (view != null)
                view.setBackgroundDrawable(this.getResources().getDrawable(R.mipmap.leftnav_focus_keep));//设置keep背景
        }

        if (newView.equals(mListFilterDVD)) {//------------------------------------------左侧的刷选listView
            LeftListView_FLAG = true;
            mListFilterDVD.setNextFocusLeftId(currentTheFirstFilterView.getId());
            View currentSelectedView = mListFilterDVD.getSelectedView();
            if (currentSelectedView == null) {
                int po = mListFilterDVD.getFirstVisiblePosition();
                currentSelectedView = mListFilterDVD.getChildAt(po);
            }
            if (currentSelectedView != null) {
                LeftListView_POSITION = mListFilterDVD.getPositionForView(currentSelectedView);
                borderView.setBorderBitmapResId(R.mipmap.leftnav_focus_current);
                setLeftFilterListViewItemFocusState(currentSelectedView);
            }
            return;
        } else if (newView.equals(mGridViewResultDVD)) {//------------------------------------------右侧网格List
            mGridViewResultDVD.setNextFocusLeftId(mListFilterDVD.getId());
            View currentSelectView = mGridViewResultDVD.getSelectedView();
            if (currentSelectView == null) {
                int po = mGridViewResultDVD.getFirstVisiblePosition();
                currentSelectView = mGridViewResultDVD.getChildAt(po);
            }
            if (currentSelectView != null) {
                borderView.setBorderBitmapResId(R.mipmap.listpic_focus_bg);
                setRightGridViewItemFocusState(currentSelectView);

            }
            return;
        }


    }

    /**
     * @param view 设置左边listView的item Selected
     */
    private void setLeftFilterListViewItemFocusState(View view) {
        mFilterAdapterLastFoucusItem = view;
        borderView.setHideView(false);
        borderView.setFocusView(view, scale);
        lastFocusViewForRecord = currentTheFirstFilterView;

        //设置父级View keep颜色
        setParentViewKeepVermilionBorder();

        //清除自己的所有keep颜色
        clearListAllListViewItemBg();
    }


    /**
     * @param view 设置右边GridView的item Selected
     */
    private void setRightGridViewItemFocusState(View view) {
        borderView.setHideView(false);//这一句非常重要
        borderView.setFocusView(view, scale);//
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }


    /**
     * 获取新碟抢先听刷选的数据 FIXME 要改为抢先听
     */
    private void getVipTypeFilterList() {
        if (vipTypeDataBeanList == null || vipTypeDataBeanList.isEmpty())
            asyncHttpRequest.getVipType(this, Appconfig.getKeyNo(getApplicationContext()), this, this, "加载中...");
        else
            refreshLeftFilterList(vipTypeDataBeanList);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Object object = parent.getAdapter().getItem(position);

        //TODO 处理点击事件跳转
        if (object instanceof VipTypeRelateAlbumBean.DataBean.ListBean) {//新碟抢先听关联的数据
            VipTypeRelateAlbumBean.DataBean.ListBean bean = (VipTypeRelateAlbumBean.DataBean.ListBean) object;
            ActivityUtility.goSpecialDetailActivity(this, bean.getId());

        } else {
            if (object == null) {
                //Tools.showTip(this,"onItemClick object is null ");
            } else {
                //Tools.showTip(this,"onItemClick object=="+object.toString());
            }
        }

    }

    /**
     * @param styleId 获取新碟抢先听数据 FIXME 新碟抢先听
     * @param pageNo
     */
    private void getVipTypeRelateAlbumList(int styleId, int pageNo) {
        if (pageNo == 1) {//拿取一页，先拿缓存
            List<VipTypeRelateAlbumBean.DataBean.ListBean> cacheMap = vipRelateAlbumDataMap.get(styleId);
            if (cacheMap != null && !cacheMap.isEmpty()) {
                refreshRightGridViewList(cacheMap);
                int totalPage = -1;
                totalPage = vipAlbumTotalPageMap.get(styleId);
                setOtherInfo(1, totalPage);
            } else
                asyncHttpRequest.getVipTypeRelateAlbum(this, styleId, pageNo, this, this, pageNo == 1 ? "正在加载中..." : null);
        } else {//拿取多页，不走缓存
            asyncHttpRequest.getVipTypeRelateAlbum(this, styleId, pageNo, this, this, pageNo == 1 ? "正在加载中..." : null);
        }

    }


    private <T> void refreshRightGridViewList(List<T> list) {
        if (list == null || list.isEmpty())
            return;
        searchResultGridViewAdapter.clearButMustUsedWithRefresh();
        searchResultGridViewAdapter.refreshAdapter(list);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);

        XLog.d(TAG, "onDestroy");
        destoryVariable();


    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

        XLog.d(TAG, "onTrimMemory");
        if (level == TRIM_MEMORY_UI_HIDDEN) {

            XLog.d(TAG, "onTrimMemory -->destoryVariable");
            destoryVariable();
        }
    }

    private void destoryVariable() {
        mListFilterDVD = null;
        mGridViewResultDVD = null;

        vipAlbumLoadedPage = null;

        vipAlbumTotalPageMap = null;

        vipRelateAlbumDataMap = null;


        mFilterAdapter = null;
        searchResultGridViewAdapter = null;

        mFilterAdapterLastFoucusItem = null;
        lastFocusViewForRecord = null;

        vipTypeDataBeanList = null;
    }


}
