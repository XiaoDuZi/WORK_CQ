package com.cq.ln.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
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
import com.cq.ln.adapter.SearchKeyWordResultListAdapter;
import com.cq.ln.adapter.SearchLeftFilterAdapter;
import com.cq.ln.adapter.SearchResultGridViewAdapter;
import com.cq.ln.constant.ConstantEnum;
import com.cq.ln.interfaces.onKeybordListener;
import com.cq.ln.utils.ActivityUtility;
import com.cq.ln.utils.AnimationUtils;
import com.cq.ln.utils.KeyActionControlUtils;
import com.cq.ln.utils.StrTool;
import com.cq.ln.utils.Tools;
import com.cq.ln.utils.XLog;
import com.cq.ln.views.FocusBorderView;
import com.cq.ln.views.T9KeyBordView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cqdatasdk.bean.ArtistEnglishInitialFilterBean;
import cqdatasdk.bean.ArtistReleaListbean;
import cqdatasdk.bean.DiscRecommendFilterBean;
import cqdatasdk.bean.DiscRecommendListBean;
import cqdatasdk.bean.MusicStyleFilterBean;
import cqdatasdk.bean.MusicStyleListBean;
import cqdatasdk.bean.SearchResultBean;
import cqdatasdk.interfaces.IVolleyRequestSuccess;
import cqdatasdk.interfaces.IVolleyRequestfail;

/**
 * 搜索页面
 */
public class SearchActivity extends BaseActivity implements View.OnFocusChangeListener, IVolleyRequestSuccess, IVolleyRequestfail, AdapterView.OnItemSelectedListener, AdapterView
        .OnItemClickListener, ViewTreeObserver.OnGlobalFocusChangeListener, onKeybordListener {
    private static final String TAG = "SearchActivity";
    private final float scale = 1.0f;
    @Bind(R.id.btnNewsDVD)
    Button mBtnOnlineSearch;
    @Bind(R.id.btnDiscRecommend)
    Button mBtnDiscRecommend;
    @Bind(R.id.btnMusicStyle)
    Button mBtnMusicStyle;
    @Bind(R.id.btnSigner)
    Button mBtnSigner;
    @Bind(R.id.btnSearchCommint)
    Button mBtnSearchCommint;
    @Bind(R.id.ListView_Image_Page_Pre)
    ImageView mListViewImagePagePre;
    @Bind(R.id.listResult)
    ListView mListViewSearchResult;
    @Bind(R.id.ListView_Image_Page_Next)
    ImageView mListViewImagePageNext;
    @Bind(R.id.txtSearchPageNav)
    TextView mTxtSearchPageNav;
    @Bind(R.id.Online_RightWarp)
    FrameLayout mOnlineRightWarp;
    @Bind(R.id.listFilterChildSong)
    ListView mListViewFilter;
    @Bind(R.id.image_Page_PreDVD)
    ImageView mImagePagePre;
    @Bind(R.id.gridViewResultDVD)
    GridView mGridViewResult;
    @Bind(R.id.image_Page_NextDVD)
    ImageView mImagePageNext;
    @Bind(R.id.txtPageNavDVD)
    TextView mTxtPageNav;
    @Bind(R.id.Orther_RightWarpDVD)
    FrameLayout mOrtherRightWarp;
    @Bind(R.id.Left_Warp_DVD)
    FrameLayout mLeftWarp;
    @Bind(R.id.customKeyBord)
    T9KeyBordView mCustomKeyBord;
    //    KeyBordView mCustomKeyBord;
    @Bind(R.id.Txt_InputContent)
    TextView mTxtInputContent;
    @Bind(R.id.fl_gridViewResultDVD)
    FrameLayout flGridViewResultDVD;
    @Bind(R.id.iv_bg)
    ImageView iv_bg;

    private FocusBorderView borderView;
    private FrameLayout topLayout;
    /**
     * 记录keep颜色的View
     */
    private View lastFocusViewForRecord;
    private Context mContext;
    private String currentSearchKeyWorld;
    private ArrayList<ArtistEnglishInitialFilterBean> englishInitialBeanList;

    public List<DiscRecommendFilterBean.DataBean> discRecommendFilterBeanList;

    private List<MusicStyleFilterBean.DataBean> musicStyleFilterBeanList;
    private List<SearchResultBean.DataBean.ListBean> searchResultBeanList;

    private SearchLeftFilterAdapter mFilterAdapter;
    private SearchKeyWordResultListAdapter searchKeyWordResultListAdapter;
    private View mFilterAdapterLastFoucusItem;
    /**
     * 当前第一级定位的View(在线搜索，唱片推荐，音乐风格，艺术家)
     */
    public View currentTheFirstFilterView;

    private SearchResultGridViewAdapter searchResultGridViewAdapter;
    private List<Button> firstParentButtonList;


    //当前输入搜索分页
    private int currentSearchPageNo = 1;

    //--------------------------------缓存变量------------------start
    private Map<String, List<ArtistReleaListbean.DataBean.ListBean>> artistDataMap = new HashMap<>();//艺术家
    private Map<String, Integer> artistTotalPageMap = new HashMap<>();//记录最大页数
    private Map<String, Integer> artistLoadedPage = new HashMap<>();//记录加载了数据的页数

    private Map<Integer, List<MusicStyleListBean.DataBean.ListBean>> musicStyleDataMap = new HashMap<>();//音乐风格
    private Map<Integer, Integer> musicStyleTotalPageMap = new HashMap<>();
    private Map<Integer, Integer> musicStyleLoadedPage = new HashMap<>();//记录加载的页数

    public Map<Integer, List<DiscRecommendListBean.DataBean.ListBean>> disRecomendDataMap = new HashMap<>();//唱片推荐
    public Map<Integer, Integer> discRecomendTotalPageMap = new HashMap<>();
    private Map<Integer, Integer> discRecomendLoadedPage = new HashMap<>();//记录加载的页数
    //--------------------------------缓存变量------------------end


    //--------------------------------纠正光标定位作用的变量---------------start
    private boolean LeftListView_FLAG = false;
    private int LeftListView_POSITION = -1;
    private boolean GridViewSelected_FLAG = false;
    private Button correctButton;//需要纠正的按钮
    public boolean IS_PAD_RIGHT_EVENT = false;
    private boolean searchByKeyWordHaveMorePage = true;
    //--------------------------------纠正光标定位作用的变量---------------end
    //---外部控制变量
    private int ColumnChild = ConstantEnum.Search_ColumnChild_None;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
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
        initkeybord();
        // test();
        int column = ConstantEnum.Search_OnLineSearch;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            column = bundle.getInt("column", ConstantEnum.Search_OnLineSearch);
            ColumnChild = bundle.getInt("ColumnChild", ConstantEnum.Search_ColumnChild_None);
        }
        initcolumn(column);
    }

    private void initcolumn(int column) {
        switch (column) {
            case ConstantEnum.Search_OnLineSearch://无需处理逻辑
                iv_bg.setVisibility(View.GONE);
                break;
            case ConstantEnum.Search_DiscRecommend:
                mBtnDiscRecommend.requestFocus();
//                mListViewFilter.getChildAt(0).requestFocus();
                break;
            case ConstantEnum.Search_MusicStyle:
                mBtnMusicStyle.requestFocus();
                break;
            case ConstantEnum.Search_Artist:
                mBtnSigner.requestFocus();
                break;
        }
    }

    private void initkeybord() {
        mCustomKeyBord.setListener(this);
    }

    @Override
    public void OnKeyClick(String key) {
        StringBuffer sb = new StringBuffer(mTxtInputContent.getText().toString());
        switch (key) {
            case "删除":
                String oldStr = sb.toString();
                if (oldStr.length() > 0) {
                    oldStr = oldStr.substring(0, oldStr.length() - 1);
                    sb = new StringBuffer(oldStr);
                }
                break;
            case "清空":
                sb = new StringBuffer("");
                break;
            case "空格":
                sb.append(" ");
                break;
            default:
                sb.append(key);
                break;
        }
        mTxtInputContent.setText(sb.toString());
    }

    private void initButtonList() {
        firstParentButtonList = new ArrayList<>();
        firstParentButtonList.add(mBtnOnlineSearch);
        firstParentButtonList.add(mBtnDiscRecommend);
        firstParentButtonList.add(mBtnMusicStyle);
        firstParentButtonList.add(mBtnSigner);
    }


    private void initAdapter() {
        //初始化左侧的list
        englishInitialBeanList = ArtistEnglishInitialFilterBean.getList();//A~Z数据
        mFilterAdapter = new SearchLeftFilterAdapter(this);
        mListViewFilter.setAdapter(mFilterAdapter);
        mListViewFilter.setOnItemSelectedListener(this);
        mListViewFilter.setOnItemClickListener(this);

        //初始化右边搜索结果的list
        searchKeyWordResultListAdapter = new SearchKeyWordResultListAdapter(this);
        mListViewSearchResult.setAdapter(searchKeyWordResultListAdapter);
        mListViewSearchResult.setOnItemClickListener(this);
        mListViewSearchResult.setOnItemSelectedListener(this);

        //初始化右边GridView的list
        searchResultGridViewAdapter = new SearchResultGridViewAdapter(this);
        mGridViewResult.setAdapter(searchResultGridViewAdapter);
        mGridViewResult.setOnItemSelectedListener(this);
        mGridViewResult.setOnItemClickListener(this);

    }

    /**
     * 设置没有keep朱红色边框
     */
    private void setButtonNoKeeping() {
        mBtnOnlineSearch.setTag(R.id.tag_for_button_is_keeping_color, false);
        mBtnDiscRecommend.setTag(R.id.tag_for_button_is_keeping_color, false);
        mBtnMusicStyle.setTag(R.id.tag_for_button_is_keeping_color, false);
        mBtnSigner.setTag(R.id.tag_for_button_is_keeping_color, false);
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
        mListViewFilter.clearFocus();
        if (view == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.btnNewsDVD:
            case R.id.btnDiscRecommend:
            case R.id.btnMusicStyle:
            case R.id.btnSigner:
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
                int size = mLeftWarp.getChildCount();
                for (int i = 0; i < size; i++) {
                    View child = mLeftWarp.getChildAt(i);
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
        if ("columnList.action".equals(method)) {//唱片推荐刷选分类列表
            if (null == object)
                return;
            discRecommendFilterBeanList = ((DiscRecommendFilterBean) object).getData();
            if (discRecommendFilterBeanList == null) {//容错处理
                Tools.showTip(SearchActivity.this, ((DiscRecommendFilterBean) object).getMsg());
                return;
            }
            if (currentTheFirstFilterView.equals(mBtnDiscRecommend))
                refreshLeftFilterList(discRecommendFilterBeanList);
            return;
        } else if ("styleList.action".equals(method)) {//音乐风格刷选分类列表
            if (null == object)
                return;
            musicStyleFilterBeanList = ((MusicStyleFilterBean) object).getData();
            if (musicStyleFilterBeanList == null) {//容错处理
                Tools.showTip(SearchActivity.this, ((MusicStyleFilterBean) object).getMsg());
                return;
            }
            if (currentTheFirstFilterView.equals(mBtnMusicStyle))
                refreshLeftFilterList(musicStyleFilterBeanList);
            return;
        } else if ("search.action".equals(method)) {//用户输入关键词搜索结果--分页
            if (null == object)
                return;
            SearchResultBean.DataBean bean = ((SearchResultBean) object).getData();
            if (null == bean) {//容错处理
                Tools.showTip(SearchActivity.this, ((SearchResultBean) object).getMsg());
                return;
            }
            List<SearchResultBean.DataBean.ListBean> temp = bean.getList();

            if (currentSearchPageNo == 1) {
                if (null == temp || temp.isEmpty()) {
                    Tools.showTip(SearchActivity.this, "暂无数据");
                    return;
                }
                searchResultBeanList = temp;
            } else {
                if (null == temp || temp.isEmpty()) {
//                    Tools.showTip(SearchActivity.this, "暂无更多数据");
                    return;
                }
                searchResultBeanList.addAll(temp);
            }

            refreshSearchKeyWorldRightListArea();//每次都需要刷新list，确保数据最新！！

            int currenPage = bean.getPage();
            int totalPage = bean.getTotalPage();
            if (currenPage < totalPage)
                searchByKeyWordHaveMorePage = true;
            else
                searchByKeyWordHaveMorePage = false;

            setSearchOtherInfo(-1, bean.getCount());
            return;
        } else if ("albumByColumn.action".equals(method)) {//1、__/__/__/__/__/__/__/__/__/__/__/__/__/__/__/__/__/唱片推荐分页数据--分页
            if (null == object)
                return;
            DiscRecommendListBean.DataBean bean = ((DiscRecommendListBean) object).getData();
            if (null == bean) {//容错处理
                Tools.showTip(SearchActivity.this, ((DiscRecommendListBean) object).getMsg());
                return;
            }
            List<DiscRecommendListBean.DataBean.ListBean> discRecommendListBeanList = disRecomendDataMap.get(Integer.parseInt(key));//searchResultGridViewAdapter.getlist();
            List<DiscRecommendListBean.DataBean.ListBean> temp = bean.getList();
            if (bean.getPage() == 1) {
                if (null == temp || temp.isEmpty()) {
                    Tools.showTip(SearchActivity.this, "暂无数据");
                    return;
                }
                discRecommendListBeanList = temp;
            } else {
                if (null == temp || temp.isEmpty()) {
//                    Tools.showTip(SearchActivity.this, "暂无更多数据");
                    return;
                }
                int page = discRecomendLoadedPage.get(Integer.parseInt(key));
                if (bean.getPage() <= page) //返回的数据所在的page小于等于最新存在的page,需要return，达到去重数据作用
                    return;
                discRecommendListBeanList.addAll(temp);
            }
            disRecomendDataMap.put(Integer.parseInt(key), discRecommendListBeanList);//添加缓存
            discRecomendTotalPageMap.put(Integer.parseInt(key), bean.getTotalPage());//最大页数
            discRecomendLoadedPage.put(Integer.parseInt(key), bean.getPage());//当前页数
            refreshRightGridViewList(discRecommendListBeanList);
            setOtherInfo(-1, bean.getTotalPage());
            return;
        } else if ("albumList.action".equals(method)) {//2、__/__/__/__/__/__/__/__/__/__/__/__/__/__/__/__/__/音乐风格分页数据--分页
            if (null == object)
                return;
            MusicStyleListBean.DataBean bean = ((MusicStyleListBean) object).getData();
            if (null == bean) {//容错处理
                Tools.showTip(SearchActivity.this, ((MusicStyleListBean) object).getMsg());
                return;
            }
            List<MusicStyleListBean.DataBean.ListBean> musicStyleListBeanList = musicStyleDataMap.get(Integer.parseInt(key));//searchResultGridViewAdapter.getlist();
            List<MusicStyleListBean.DataBean.ListBean> temp = bean.getList();
            if (bean.getPage() == 1) {
                if (null == temp || temp.isEmpty()) {
                    Tools.showTip(SearchActivity.this, "暂无数据");
                    return;
                }
                musicStyleListBeanList = temp;
            } else {
                if (null == temp || temp.isEmpty()) {
//                    Tools.showTip(SearchActivity.this, "暂无更多数据");
                    return;
                }
                int page = musicStyleLoadedPage.get(Integer.parseInt(key));//已经设置存在的page
                if (bean.getPage() <= page) //返回的数据所在的page小于等于最新存在的page,需要return，达到去重数据作用
                    return;
                else
                    musicStyleListBeanList.addAll(temp);
            }
            musicStyleDataMap.put(Integer.parseInt(key), musicStyleListBeanList);//添加缓存
            musicStyleTotalPageMap.put(Integer.parseInt(key), bean.getTotalPage());//最大页数
            musicStyleLoadedPage.put(Integer.parseInt(key), bean.getPage());//当前页
            refreshRightGridViewList(musicStyleListBeanList);
            setOtherInfo(-1, bean.getTotalPage());
            return;
        } else if ("artistList.action".equals(method)) {//3、__/__/__/__/__/__/__/__/__/__/__/__/__/__/__/__/__/艺术家搜索--分页
            if (null == object)
                return;
            ArtistReleaListbean.DataBean bean = ((ArtistReleaListbean) object).getData();
            if (null == bean) {//容错处理
                Tools.showTip(SearchActivity.this, ((ArtistReleaListbean) object).getMsg());
                return;
            }

            List<ArtistReleaListbean.DataBean.ListBean> artistReleaListBeanList = artistDataMap.get(key);//searchResultGridViewAdapter.getlist();
            List<ArtistReleaListbean.DataBean.ListBean> temp = bean.getList();
            if (bean.getPage() == 1) {
                if (null == temp || temp.isEmpty()) {
                    Tools.showTip(SearchActivity.this, "暂无数据");
                    return;
                }
                artistReleaListBeanList = temp;
            } else {
                if (null == temp || temp.isEmpty()) {
//                    Tools.showTip(SearchActivity.this, "暂无更多数据");
                    return;
                }
                int page = artistLoadedPage.get(key);
                if (bean.getPage() <= page) //返回的数据所在的page小于等于最新存在的page,需要return，达到去重数据作用
                    return;
                artistReleaListBeanList.addAll(temp);
            }
            artistDataMap.put(key, artistReleaListBeanList);//添加缓存
            artistTotalPageMap.put(key, bean.getTotalPage());//最大页数
            artistLoadedPage.put(key, bean.getPage());//当前页数

            refreshRightGridViewList(artistReleaListBeanList);
            setOtherInfo(-1, bean.getTotalPage());
            return;
        }

    }

    private void refreshSearchKeyWorldRightListArea() {
        searchKeyWordResultListAdapter.clearButMustUsedWithRefresh();
        searchKeyWordResultListAdapter.refreshAdapter(searchResultBeanList);

    }


    /**
     * @param currenIndex 输入关键词搜索的page导航
     * @param totalCount
     */
    private void setSearchOtherInfo(int currenIndex, int totalCount) {
        if (currenIndex == -1) {
            String str = mTxtSearchPageNav.getText().toString().trim().split("/")[0];
            currenIndex = Integer.parseInt(str);
        }
        if (totalCount == -1) {
            String str = mTxtSearchPageNav.getText().toString().trim().split("/")[1];
            totalCount = Integer.parseInt(str);
        }
        mTxtSearchPageNav.setText(String.format("%d/%d", currenIndex, totalCount));
        setSearchPageNav(currenIndex, totalCount);
    }

    /**
     * @param index      输入关键词的导航
     * @param totalCount
     */
    private void setSearchPageNav(int index, int totalCount) {
        if (index == 1)
            mListViewImagePagePre.setImageResource(R.mipmap.page_pre_have_nonu);
        else
            mListViewImagePagePre.setImageResource(R.mipmap.page_pre_have_more);

        if (index == totalCount)
            mListViewImagePageNext.setImageResource(R.mipmap.page_next_have_nonu);
        else
            mListViewImagePageNext.setImageResource(R.mipmap.page_next_have_more);

    }


    /**
     * @param currentPage 唱片推荐，音乐风格，艺术家三个栏目数据分页导航
     * @param totalPage
     */
    public void setOtherInfo(int currentPage, int totalPage) {
        if (totalPage == -1) {
            String str = mTxtPageNav.getText().toString().trim().split("/")[1];
            totalPage = Integer.parseInt(str);
        }
        if (currentPage == -1) {
            String str = mTxtPageNav.getText().toString().trim().split("/")[0];
            currentPage = Integer.parseInt(str);
        }

        if (currentPage > totalPage)//TODO 虽是BUG,临时处理!!
            currentPage = totalPage;

        mTxtPageNav.setText(String.format("%d/%d", currentPage, totalPage));

        setGridViewPageNav(currentPage, totalPage);
    }

    private void setGridViewPageNav(int currentPage, int totalPage) {
        if (currentPage == 1)
            mImagePagePre.setImageResource(R.mipmap.page_pre_have_nonu);
        else
            mImagePagePre.setImageResource(R.mipmap.page_pre_have_more);

        if (currentPage == totalPage)
            mImagePageNext.setImageResource(R.mipmap.page_next_have_nonu);
        else
            mImagePageNext.setImageResource(R.mipmap.page_next_have_more);

    }

    /**
     * @param list 刷新左侧的刷新分类列表
     * @param <T>
     */
    public <T> void refreshLeftFilterList(List<T> list) {
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

        if (ColumnChild != ConstantEnum.Search_ColumnChild_None)
            mListViewFilter.setSelection(ColumnChild);

        //再设置
        View view = mListViewFilter.getSelectedView();
        //

        if (view == null)
            view = mListViewFilter.getChildAt(0);

        if (view != null) {

            int po = mListViewFilter.getPositionForView(view);
            if (po > mFilterAdapter.getCount() - 1)
                view = mListViewFilter.getChildAt(mFilterAdapter.getCount() - 1);
            view.setBackgroundDrawable(getResources().getDrawable(R.mipmap.leftnav_focus_keep));
        }
    }


    private void clearListAllListViewItemBg() {
        int count = mListViewFilter.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = mListViewFilter.getChildAt(i);
            if (view.getBackground() != null)
                view.setBackgroundResource(0);
        }
    }

    private void preRefreshRightData() {
        View view = mListViewFilter.getSelectedView();
        if (view == null)
            view = mListViewFilter.getChildAt(0);
        if (view == null)
            return;
        Object object = view.getTag(R.id.Tag_for_search_LeftFilterList_Databean);
        if (object == null)
            return;

        if (object instanceof ArtistEnglishInitialFilterBean) {//艺术家
            getArtistReList(((ArtistEnglishInitialFilterBean) object).englishInitial, 1);
        } else if (object instanceof DiscRecommendFilterBean.DataBean) {//唱片推荐
            getDiscRecommendList(((DiscRecommendFilterBean.DataBean) object).getId(), 1);
        } else if (object instanceof MusicStyleFilterBean.DataBean) {//音乐风格
            getMusicStyleList(((MusicStyleFilterBean.DataBean) object).getId(), 1);
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
                AnimationUtils.fadein(this, mOnlineRightWarp);
                AnimationUtils.fadeout(this, mOrtherRightWarp);
                currentTheFirstFilterView = v;
                break;
            case R.id.btnDiscRecommend:
            case R.id.btnMusicStyle:
            case R.id.btnSigner:
                AnimationUtils.fadeout(this, mOnlineRightWarp);
                AnimationUtils.fadein(this, mOrtherRightWarp);
                currentTheFirstFilterView = v;
                break;
            case R.id.btnSearchCommint:
                setParentViewKeepVermilionBorder();
                borderView.setBorderBitmapResId(R.mipmap.button_bg_white_color_border_focus);
                break;

            default:
                borderView.setBorderBitmapResId(R.mipmap.t9_button_bg_focus);
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
            case R.id.btnDiscRecommend://唱片推荐
                getDiscRecommendFilterList();
                break;
            case R.id.btnMusicStyle://音乐风格
                getMusicStyleFilterList();
                break;
            case R.id.btnSigner://艺术家
                refreshLeftFilterList(englishInitialBeanList);

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
        if (v != null && v.equals(mListViewFilter)) {//------------------------------------------左侧ListView
            if (LeftListView_FLAG) {
                LeftListView_FLAG = false;
                if (LeftListView_POSITION != -1)
                    mListViewFilter.setSelection(LeftListView_POSITION);
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
        } else if (v != null && v.equals(mListViewSearchResult)) {//---------------------------------右侧关键词搜索出来的list列表
            borderView.setBorderBitmapResId(R.mipmap.list_search_focus_bg);
            setRightkeyWordSearchListViewFocusState(view);
            setSearchOtherInfo(position + 1, -1);
            return;
        } else if (v != null && v.equals(mGridViewResult)) {//------------------------------------------右侧gridView
            borderView.setBorderBitmapResId(R.mipmap.listpic_focus_bg);
            setRightGridViewItemFocusState(view);
            int currentPage = (int) Math.ceil((position + 1) / 8.0);//每页8条数据
            setOtherInfo(currentPage, -1);
            currentGridViewItemPostion = position;
            return;
        }
    }

    private int currentGridViewItemPostion;

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
                    if (currentTheFirstFilterView.equals(mBtnOnlineSearch)) {//在线搜索
                        IS_PAD_RIGHT_EVENT = false;
                    } else if (currentTheFirstFilterView.equals(mBtnDiscRecommend)) {//唱片推荐
                        IS_PAD_RIGHT_EVENT = true;
                    } else if (currentTheFirstFilterView.equals(mBtnMusicStyle)) {//音乐风格
                        IS_PAD_RIGHT_EVENT = true;
                    } else if (currentTheFirstFilterView.equals(mBtnSigner)) {//艺术家
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
                        e.printStackTrace();
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
        if (view.equals(mListViewSearchResult)) {//___/___/___/___/___/___/___/___/___/___/___/___/输入关键词搜索出来的列表分页处理
            if (mListViewSearchResult.getSelectedItemPosition() == searchKeyWordResultListAdapter.getCount() - 1) {
                if (searchByKeyWordHaveMorePage)
                    getSearchResult(++currentSearchPageNo, currentSearchKeyWorld);
                return super.dispatchKeyEvent(event);
            } else {
                return super.dispatchKeyEvent(event);
            }
        } else if (view.equals(mGridViewResult)) {//___/___/___/___/___/___/___/___/___/___/___/___/网格数据分页处理
            View viewData = mListViewFilter.getSelectedView();
            if (null == viewData) {
                viewData = mListViewFilter.getChildAt(0);
            }
            Object object = viewData.getTag(R.id.Tag_for_search_LeftFilterList_Databean);
            if (object instanceof ArtistEnglishInitialFilterBean) {//艺术家
                ArtistEnglishInitialFilterBean bean = (ArtistEnglishInitialFilterBean) object;
                int loadedPage = artistLoadedPage.get(bean.englishInitial);
                if (loadedPage < artistTotalPageMap.get(bean.englishInitial))
                    getArtistReList(bean.englishInitial, ++loadedPage);

            } else if (object instanceof DiscRecommendFilterBean.DataBean) {//唱片推荐
                DiscRecommendFilterBean.DataBean bean = (DiscRecommendFilterBean.DataBean) object;
                int loadedPage = discRecomendLoadedPage.get(bean.getId());
                if (loadedPage < discRecomendTotalPageMap.get(bean.getId()))
                    getDiscRecommendList(bean.getId(), ++loadedPage);

            } else if (object instanceof MusicStyleFilterBean.DataBean) {//音乐风格
                MusicStyleFilterBean.DataBean bean = (MusicStyleFilterBean.DataBean) object;
                int loadedPage = musicStyleLoadedPage.get(bean.getId());
                if (loadedPage < musicStyleTotalPageMap.get(bean.getId()))
                    getMusicStyleList(bean.getId(), ++loadedPage);
            }
        }
        return super.dispatchKeyEvent(event);

    }

    public void loadDataByFilter(AdapterView<?> adapterView, int i) throws Exception {
        if (currentTheFirstFilterView.equals(mBtnSigner)) {//艺术家
            ArtistEnglishInitialFilterBean bean = (ArtistEnglishInitialFilterBean) adapterView.getAdapter().getItem(i);
            getArtistReList(bean.englishInitial, 1);

        } else if (currentTheFirstFilterView.equals(mBtnMusicStyle)) {//音乐风格
            MusicStyleFilterBean.DataBean bean = (MusicStyleFilterBean.DataBean) adapterView.getAdapter().getItem(i);
            getMusicStyleList(bean.getId(), 1);

        } else if (currentTheFirstFilterView.equals(mBtnDiscRecommend)) {//唱片推荐
            DiscRecommendFilterBean.DataBean bean = (DiscRecommendFilterBean.DataBean) adapterView.getAdapter().getItem(i);
            getDiscRecommendList(bean.getId(), 1);
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

        if (oldView.equals(mBtnDiscRecommend) && newView.equals(mGridViewResult)) {//唱片推荐，防止光标飘移
            correctButton = mBtnDiscRecommend;
            GridViewSelected_FLAG = true;
            return;
        }
        if (oldView.equals(mBtnMusicStyle) && newView.equals(mGridViewResult)) {//音乐风格，防止光标飘移
            correctButton = mBtnMusicStyle;
            GridViewSelected_FLAG = true;
            return;
        }
        if (oldView.equals(mBtnSigner) && newView.equals(mGridViewResult)) {//艺术家，防止光标飘移
            correctButton = mBtnSigner;
            GridViewSelected_FLAG = true;
            return;
        }

        //设置keep背景
        if (oldView.equals(mListViewFilter)) {
            View view = mListViewFilter.getSelectedView();
            if (view != null)
                view.setBackgroundDrawable(this.getResources().getDrawable(R.mipmap.leftnav_focus_keep));//设置keep背景
        }

        if (newView.equals(mListViewFilter)) {//------------------------------------------左侧的刷选listView
            LeftListView_FLAG = true;
            mListViewFilter.setNextFocusLeftId(currentTheFirstFilterView.getId());
            View currentSelectedView = mListViewFilter.getSelectedView();
            if (currentSelectedView == null) {
                int po = mListViewFilter.getFirstVisiblePosition();
                currentSelectedView = mListViewFilter.getChildAt(po);
            }
            if (currentSelectedView != null) {
                LeftListView_POSITION = mListViewFilter.getPositionForView(currentSelectedView);
                borderView.setBorderBitmapResId(R.mipmap.leftnav_focus_current);
                setLeftFilterListViewItemFocusState(currentSelectedView);
            }
            return;
        } else if (newView.equals(mGridViewResult)) {//------------------------------------------右侧网格List
            mGridViewResult.setNextFocusLeftId(mListViewFilter.getId());
            View currentSelectView = mGridViewResult.getSelectedView();
            if (currentSelectView == null) {
                int po = mGridViewResult.getFirstVisiblePosition();
                currentSelectView = mGridViewResult.getChildAt(po);
            }
            if (currentSelectView != null) {
                borderView.setBorderBitmapResId(R.mipmap.listpic_focus_bg);
                setRightGridViewItemFocusState(currentSelectView);

            }
            return;
        } else if (newView.equals(mListViewSearchResult)) {//------------------------------右侧关键词搜索出来的listView 列表
            mListViewSearchResult.setNextFocusLeftId(mBtnSearchCommint.getId());
            View currentSelectView = mListViewSearchResult.getSelectedView();
            if (currentSelectView == null) {
                int po = mListViewSearchResult.getFirstVisiblePosition() + 1;
                currentSelectView = mListViewSearchResult.getChildAt(po);
            }
            if (currentSelectView != null) {
                borderView.setBorderBitmapResId(R.mipmap.list_search_focus_bg);
                setRightkeyWordSearchListViewFocusState(currentSelectView);
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

    /**
     * @param view 设置关键词搜索出来的list列表的item selected 状态
     */
    private void setRightkeyWordSearchListViewFocusState(View view) {
        borderView.setHideView(false);//这一句非常重要
        borderView.setFocusView(view, scale);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }


    /**
     * 获取音乐风格刷选的数据
     */
    private void getMusicStyleFilterList() {
        if (musicStyleFilterBeanList == null || musicStyleFilterBeanList.isEmpty())
            asyncHttpRequest.getMusicStyleFilterList(this, this, this);
        else
            refreshLeftFilterList(musicStyleFilterBeanList);
    }

    /**
     * 获取唱片推荐刷选的数据
     */
    public void getDiscRecommendFilterList() {
        if (discRecommendFilterBeanList == null || discRecommendFilterBeanList.isEmpty())
            asyncHttpRequest.getDiscRecommendFilterList(this, this, this);
        else
            refreshLeftFilterList(discRecommendFilterBeanList);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Object object = parent.getAdapter().getItem(position);

        //TODO 处理点击事件跳转
        if (object instanceof SearchResultBean.DataBean.ListBean) {//搜索结果
            SearchResultBean.DataBean.ListBean bean = (SearchResultBean.DataBean.ListBean) object;
            int conentType = StrTool.parsetNum(bean.getContentType());
            //   2是单曲 1是专辑 0是歌手

            if (conentType == ConstantEnum.contentType_Artist) {//歌手 pass-->艺术家专辑列表
                ActivityUtility.goArtistSpecialList(this, StrTool.parsetNum(bean.getContentId()), bean.getName(), bean.getSmallImg());
            } else if (conentType == ConstantEnum.contentType_Special) {//专辑 pass
                ActivityUtility.goSpecialDetailActivity(this, StrTool.parsetNum(bean.getContentId()));
            } else if (conentType == ConstantEnum.contentType_Single) {//单曲 pass测试播放
                XLog.d(TAG, "bean :" + bean.toString());
                int beanFileType = 0;
                if (TextUtils.equals(bean.getFileType(), "1")){
                    beanFileType = 1;
                }
                //单曲播放
                Intent intent = new Intent(this, PlayUtilActivity.class);
                intent.putExtra("isSingle", true);
                intent.putExtra("ablumId", bean.getContentId());//曲目ID
                intent.putExtra("ablumName", bean.getName());//曲目名
                intent.putExtra("fileType", beanFileType);
                startActivity(intent);

            }

        } else if (object instanceof DiscRecommendListBean.DataBean.ListBean) {//唱片推荐关联的数据
            DiscRecommendListBean.DataBean.ListBean bean = (DiscRecommendListBean.DataBean.ListBean) object;
            ActivityUtility.goSpecialDetailActivity(this, bean.getId());

        } else if (object instanceof MusicStyleListBean.DataBean.ListBean) {//音乐风格关联的数据
            MusicStyleListBean.DataBean.ListBean bean = (MusicStyleListBean.DataBean.ListBean) object;
            ActivityUtility.goSpecialDetailActivity(this, bean.getId());

        } else if (object instanceof ArtistReleaListbean.DataBean.ListBean) {//艺术家关联的数据
            ArtistReleaListbean.DataBean.ListBean bean = (ArtistReleaListbean.DataBean.ListBean) object;
            ActivityUtility.goArtistSpecialList(this, bean.getId(), bean.getName(), bean.getSmallImg());
        } else {
            if (object == null) {
                //Tools.showTip(this,"onItemClick object is null ");
            } else {
                //Tools.showTip(this,"onItemClick object=="+object.toString());
            }
        }

    }

    /**
     * @param columnId 获取唱片推荐数据
     */
    public void getDiscRecommendList(int columnId, int pageNo) {
        if (pageNo == 1) {//拿取一页，先拿缓存
            List<DiscRecommendListBean.DataBean.ListBean> cacheMap = disRecomendDataMap.get(columnId);
            if (cacheMap != null && !cacheMap.isEmpty()) {
                refreshRightGridViewList(cacheMap);
                int totalPage = -1;
                totalPage = discRecomendTotalPageMap.get(columnId);
                setOtherInfo(1, totalPage);
            } else
                asyncHttpRequest.getDiscRecommendList(this, columnId, pageNo, this, this, pageNo == 1 ? null : null);
        } else {//拿取多页，不走缓存
            asyncHttpRequest.getDiscRecommendList(this, columnId, pageNo, this, this, pageNo == 1 ? null : null);
        }
    }

    /**
     * @param styleId 获取音乐风格 关联的数据列表
     * @param pageNo
     */
    private void getMusicStyleList(int styleId, int pageNo) {
        if (pageNo == 1) {
            List<MusicStyleListBean.DataBean.ListBean> cacheMap = musicStyleDataMap.get(styleId);
            if (cacheMap != null && !cacheMap.isEmpty()) {
                refreshRightGridViewList(cacheMap);
                int totalPage = -1;
                totalPage = musicStyleTotalPageMap.get(styleId);
                setOtherInfo(1, totalPage);
            } else
                asyncHttpRequest.getMusicStyleList(this, styleId, pageNo, this, this, pageNo == 1 ? null : null);
        } else
            asyncHttpRequest.getMusicStyleList(this, styleId, pageNo, this, this, pageNo == 1 ? null : null);

    }


    /**
     * @param searchKeyWord 获取艺术家关联的数据列表
     */
    private void getArtistReList(String searchKeyWord, int pageNo) {
        if (pageNo == 1) {
            List<ArtistReleaListbean.DataBean.ListBean> cacheMap = artistDataMap.get(searchKeyWord);
            if (cacheMap != null && !cacheMap.isEmpty()) {
                refreshRightGridViewList(cacheMap);
                int totalPage = -1;
                totalPage = artistTotalPageMap.get(searchKeyWord);
                setOtherInfo(1, totalPage);
            } else
                asyncHttpRequest.getArtistReList(this, searchKeyWord, pageNo, this, this, pageNo == 1 ? null : null);
        } else
            asyncHttpRequest.getArtistReList(this, searchKeyWord, pageNo, this, this, pageNo == 1 ? null : null);//pageNo == 1 ? "正在加载中..." : null
    }


    public <T> void refreshRightGridViewList(List<T> list) {
        if (list == null || list.isEmpty())
            return;
        searchResultGridViewAdapter.clearButMustUsedWithRefresh();
        searchResultGridViewAdapter.refreshAdapter(list);
        iv_bg.setVisibility(View.GONE);
    }

    @OnClick({R.id.btnSearchCommint})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.btnSearchCommint:
                currentSearchKeyWorld = mTxtInputContent.getText().toString().trim();
                if (TextUtils.isEmpty(currentSearchKeyWorld)) {
                    Tools.showTip(SearchActivity.this, "请先输入关键词！");
                    return;
                }
                searchByKeyWordHaveMorePage = true;
                currentSearchPageNo = 1;
                getSearchResult(currentSearchPageNo, currentSearchKeyWorld);
                break;

        }
    }

    public void getSearchResult(int pageno, String keyWord) {
        asyncHttpRequest.getSearchResultByKeyWord(this, keyWord, pageno, this, this, pageno == 1 ? "加载中..." : null);
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
        mListViewSearchResult = null;
        mGridViewResult = null;

        artistLoadedPage = null;
        discRecomendLoadedPage = null;
        musicStyleLoadedPage = null;

        artistTotalPageMap = null;
        discRecomendTotalPageMap = null;
        musicStyleTotalPageMap = null;

        artistDataMap = null;
        disRecomendDataMap = null;
        musicStyleDataMap = null;


        mFilterAdapter = null;
        searchKeyWordResultListAdapter = null;
        searchResultGridViewAdapter = null;

        mFilterAdapterLastFoucusItem = null;
        lastFocusViewForRecord = null;

        englishInitialBeanList = null;
        discRecommendFilterBeanList = null;
        musicStyleFilterBeanList = null;
        searchResultBeanList = null;
    }


}
