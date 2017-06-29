package com.cq.ln.fragment;

/**
 * Created by fute on 16/11/4.
 */

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cq.ln.R;
import com.cq.ln.adapter.SpecialDetailMusicListAdapter;
import com.cq.ln.appConfig.Appconfig;
import com.cq.ln.constant.ConstantEnum;
import com.cq.ln.utils.FastBlurUtil;
import com.cq.ln.utils.HiFiDialogTools;
import com.cq.ln.utils.ImageTool;
import com.cq.ln.utils.Tools;
import com.cq.ln.utils.XLog;
import com.cq.ln.views.ItemSquare;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cqdatasdk.bean.CollectionSucceedBean;
import cqdatasdk.bean.DeletedCollectionSucceedBean;
import cqdatasdk.bean.HiFiMainPageBean;
import cqdatasdk.bean.SpecialDetailBean;
import cqdatasdk.interfaces.IVolleyRequestSuccess;
import cqdatasdk.interfaces.IVolleyRequestfail;
import cqdatasdk.network.URLVerifyTools;
import cqdatasdk.utils.JsonUtil;

/**
 * 专辑详情页
 */
public class SpecialDetailFragment extends BaseFragment implements IVolleyRequestSuccess, IVolleyRequestfail, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, View
        .OnClickListener, ViewTreeObserver.OnGlobalFocusChangeListener {

    private static final String TAG = "SpecialDetailActivity";
    private static final String AlbumId = "albumId";
    private static final String FileType = "fileType";
    private static final String ClassifyItemBean = "ClassifyItemBean";
    
    @Bind(R.id.effSpecial_Main)
    ImageView mEffSpecialMain;
    @Bind(R.id.txtSpecailName)
    TextView mTxtSpecailName;
    @Bind(R.id.txtSpecailSinger)
    TextView mTxtSpecailSinger;
    @Bind(R.id.image_Page_PreDVD)
    ImageView mImagePagePre;
    @Bind(R.id.ListView_)
    ListView mListView;
    @Bind(R.id.image_Page_NextDVD)
    ImageView mImagePageNext;
    @Bind(R.id.txtPageNavDVD)
    TextView mTxtPageNav;
    @Bind(R.id.effRecommendSpcecial_One)
    ItemSquare mEffRecommendSpcecialOne;
    @Bind(R.id.effRecommendSpcecial_Two)
    ItemSquare mEffRecommendSpcecialTwo;
    @Bind(R.id.effRecommendSpcecial_Three)
    ItemSquare mEffRecommendSpcecialThree;
    @Bind(R.id.ImageView_collection)
    ImageView mImageViewCollection;
    @Bind(R.id.txt_collection)
    TextView mTxtCollection;
    @Bind(R.id.effSpecial_Collect_warp)
    RelativeLayout mEffSpecialCollectWarp;
    @Bind(R.id.effSpecial_abstruct_warp)
    RelativeLayout mEffSpecialAbstructWarp;
    @Bind(R.id.iv_bg)
    ImageView ivBg;

    private List<ItemSquare> ItemSquareViewList;

    private HiFiMainPageBean.ClassifyBean.ClassifyItemBean itemBean;
    private int albumId;

    private SpecialDetailBean specialDetailBean;
    private String specailBigPic;

    private SpecialDetailMusicListAdapter musicListAdapter;
    private float scale = 1.0f;
    private HiFiDialogTools hiFiDialogTools = new HiFiDialogTools();
    private int fileType = 0;

    // TODO: Rename and change types of parameters
    private int nextUpId;
    private String mParam2; 


    public SpecialDetailFragment() {
        // Required empty public constructor
    }

    public static SpecialDetailFragment newInstance(int albumId, int fileType, Serializable bean) {
        SpecialDetailFragment fragment = new SpecialDetailFragment();
        Bundle args = new Bundle();
        args.putInt(AlbumId, albumId);
        args.putInt(FileType, fileType);
        args.putSerializable(ClassifyItemBean, bean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            albumId = getArguments().getInt(AlbumId);
            fileType = getArguments().getInt(FileType);
            itemBean =(HiFiMainPageBean.ClassifyBean.ClassifyItemBean) getArguments().getSerializable(ClassifyItemBean);
        }

    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_special_detail, container, false);
        ButterKnife.bind(this, view);

        traversalView((ViewGroup) view);
        createBorderView(view);
        initItemSquareViewList();
        iniAdapter();

        if (itemBean != null)
            getData(itemBean.href.trim().split("\\?")[1], Appconfig.getKeyNo(getActivity()));
        else
            getData("albumId=" + albumId, Appconfig.getKeyNo(getActivity()));
        setClickListener();
        view.getViewTreeObserver().addOnGlobalFocusChangeListener(this);
        return view;
    }


    private void iniAdapter() {
        musicListAdapter = new SpecialDetailMusicListAdapter(getActivity());
        mListView.setAdapter(musicListAdapter);
        mListView.setOnItemSelectedListener(this);
        mListView.setOnItemClickListener(this);
    }

    private void initItemSquareViewList() {
        ItemSquareViewList = new ArrayList<>();
        ItemSquareViewList.add(mEffRecommendSpcecialOne);
        ItemSquareViewList.add(mEffRecommendSpcecialTwo);
        ItemSquareViewList.add(mEffRecommendSpcecialThree);
    }

    /**
     * @param albumIdStr 参数:albumId=888
     * @param keyNo
     */
    private void getData(String albumIdStr, String keyNo) {//Appconfig.getKeyNo(getApplicationContext())
        asyncHttpRequest.getSpecailDetail(getActivity(), albumIdStr, fileType, keyNo, this, this, "正在加载中...");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        XLog.d(this.getClass().getSimpleName() + "===> onDestroyView");
    }

    /**
     * @param method 直接获得bean模型对象，进行绑定即可
     * @param object
     */
    @Override

    public void onSucceeded(String method, String key, Object object) {
        if ("album.action".equals(method)) {//获得专辑详情数据
            specialDetailBean = (SpecialDetailBean) object;
            if (object != null) {
                XLog.d("onSucceeded object==" + object.toString());
            }
            bindData(specialDetailBean);
            return;
        } else if ("addUserCollection.action".equals(method)) {//添加收藏
            CollectionSucceedBean bean = (CollectionSucceedBean) object;
            if (bean == null)
                return;
            Tools.showTip(getActivity(), bean.getMsg());

            if (JsonUtil.isLogicSucceed(bean.getCode()))
                if (specialDetailBean != null) {
                    specialDetailBean.getData().setCollectionId(bean.getCollectionId());
                    specialDetailBean.getData().setIfCollection(ConstantEnum.isCollection_Yes);
                    refreshUI(specialDetailBean);
                }
            return;

        } else if ("deleteUserCollection.action".equals(method)) {//取消收藏
            DeletedCollectionSucceedBean bean = (DeletedCollectionSucceedBean) object;
            if (bean == null)
                return;
            Tools.showTip(getActivity(), bean.getMsg());
            if (JsonUtil.isLogicSucceed(bean.getCode()))
                if (specialDetailBean != null) {
                    specialDetailBean.getData().setIfCollection(ConstantEnum.isCollection_NO);
                    specialDetailBean.getData().setCollectionId(0);
                    refreshUI(specialDetailBean);
                }
            return;
        }
    }


    private void bindData(SpecialDetailBean specialDetailBean) {
        if (specialDetailBean == null || specialDetailBean.getData() == null)
            return;
        specailBigPic = URLVerifyTools.formatPicListImageUrl(specialDetailBean.getData().getAlbumBigImg());

        ImageTool.loadImage(getActivity(), mEffSpecialMain, specailBigPic, 0, R.mipmap.default_small, new ImageTool.LoadSuccess() {
            @Override
            public void onSucceeded(Bitmap bitmap) {
                if (getActivity()!=null){
                    int scaleRatio = 5;
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
                            bitmap.getWidth() / scaleRatio,
                            bitmap.getHeight() / scaleRatio,
                            false);

                    Bitmap blurImage = FastBlurUtil.doBlur(scaledBitmap, 20, false);
                    ivBg.setBackground(new BitmapDrawable(getActivity().getResources(), blurImage));
                    AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
                    //动画集
                    AnimationSet set = new AnimationSet(true);
                    set.addAnimation(alphaAnimation);
                    //设置动画时间 (作用到每个动画)
                    set.setDuration(1000);
                    ivBg.startAnimation(set);
                }
            }
        }, new ImageTool.Loadfail() {
            @Override
            public void onFailed() {

            }
        });

        //收藏
        refreshUI(specialDetailBean);

        //推荐三个
        bindRecommend(specialDetailBean.getData().getTjList());

        //绑list
        bindList(specialDetailBean.getData().getTrackList());

        //
        bindOtherInfo(0);
        bindTitleInfo(specialDetailBean.getData());

    }

    private void bindTitleInfo(SpecialDetailBean.DataBean data) {
        mTxtSpecailName.setText(data.getAlbumName());
        mTxtSpecailSinger.setText(data.getArtistNames());
    }

    private void refreshUI(SpecialDetailBean specialDetailBean) {
        if (specialDetailBean == null)
            return;
        if (specialDetailBean.getData().getIfCollection() == ConstantEnum.isCollection_Yes) {
            mImageViewCollection.setImageResource(R.mipmap.albums_collect_yes);
            mTxtCollection.setText("已收藏");
        } else {
            mImageViewCollection.setImageResource(R.mipmap.albums_collect_no);
            mTxtCollection.setText("收藏专辑");
        }
    }

    private void setClickListener() {
        mEffSpecialCollectWarp.setOnClickListener(this);
        mEffSpecialAbstructWarp.setOnClickListener(this);
        mEffRecommendSpcecialOne.findViewById(R.id.item_ImageBtn).setOnClickListener(this);
        mEffRecommendSpcecialTwo.findViewById(R.id.item_ImageBtn).setOnClickListener(this);
        mEffRecommendSpcecialThree.findViewById(R.id.item_ImageBtn).setOnClickListener(this);
    }

    private void bindOtherInfo(int index) {
        mTxtPageNav.setText(String.format("%d/%d", index + 1, musicListAdapter.getCount()));
        if (index == 0)
            mImagePagePre.setImageResource(R.mipmap.page_pre_have_nonu);
        else
            mImagePagePre.setImageResource(R.mipmap.page_pre_have_more);
        if (index == musicListAdapter.getCount() - 1)
            mImagePageNext.setImageResource(R.mipmap.page_next_have_nonu);
        else
            mImagePageNext.setImageResource(R.mipmap.page_next_have_more);
    }

    private void bindList(List<SpecialDetailBean.DataBean.TrackListBean> trackList) {
        if (trackList == null)
            return;
        musicListAdapter.clearButMustUsedWithRefresh();
        musicListAdapter.refreshAdapter(trackList);
        mListView.requestFocus();//有数据默认listView 获得焦点
    }


    private void bindRecommend(List<SpecialDetailBean.DataBean.TjListBean> tjList) {
        if (tjList == null)
            return;
        for (int i = 0; i < tjList.size(); i++) {
            ItemSquareViewList.get(i).setText(tjList.get(i).getAlbumName());
            ImageTool.loadImage(getActivity(), ItemSquareViewList.get(i).getImageButton(), URLVerifyTools.formatPicListImageUrl(tjList.get(i).getAlbumSmallImg()), 0, R.mipmap
                    .default_small, null, null);
            ItemSquareViewList.get(i).getImageButton().setTag(tjList.get(i));//绑定tag
        }
    }

    /**
     * @param v        请不要调用父类方法super
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v instanceof ItemSquare)
            v = (View) v.getParent().getParent();
        borderView.setBorderBitmapResId(R.mipmap.listpic_focus_bg);
        switch (v.getId()) {
            case R.id.effSpecial_Collect_warp:
            case R.id.effSpecial_abstruct_warp:
                borderView.setBorderBitmapResId(R.mipmap.button_bg_white_color_border_focus);
                v.setNextFocusRightId(mListView.getId());
                break;
            case R.id.effRecommendSpcecial_One:
            case R.id.effRecommendSpcecial_Two:
            case R.id.effRecommendSpcecial_Three:
                borderView.setBorderBitmapResId(R.mipmap.listpic_focus_bg);
                v.setNextFocusLeftId(mListView.getId());
                break;
        }
        if (hasFocus) {
            v.bringToFront();
            v.getParent().bringChildToFront(v);
            v.requestLayout();
            v.invalidate();
            borderView.setHideView(false);//这一句非常重要
            borderView.setFocusView(v, scale);
        } else {
            borderView.setUnFocusView(v);
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (getActivity().getCurrentFocus().equals(mEffSpecialCollectWarp))
            return;
        setListViewItemFocusState(view, i);
    }

    private void setListViewItemFocusState(View view, int i) {
        borderView.setBorderBitmapResId(R.mipmap.leftnav_focus_current);
        borderView.setHideView(false);//这一句非常重要
        borderView.setFocusView(view, scale);
        bindOtherInfo(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        SpecialDetailBean.DataBean.TrackListBean bean = (SpecialDetailBean.DataBean.TrackListBean) adapterView.getAdapter().getItem(i);
        if (bean == null)
            return;
        int beanFileType = 0;
        if (TextUtils.equals(bean.getFileType(), "1")) {
            beanFileType = 1;
        }
        //带列表带跳转
//        Intent intent = new Intent(getActivity(), MusicPlayActivity.class);
//        intent.putExtra("isSingle", false);
//        intent.putExtra("TrackListBean", bean);
//        intent.putExtra("trackList", (Serializable) musicListAdapter.getlist());
//        intent.putExtra("currentIndex", i);
//        intent.putExtra("specailBigPic", specailBigPic);
//        intent.putExtra("fileType", beanFileType);
//        if (specialDetailBean != null && specialDetailBean.getData() != null) {
//            intent.putExtra("albumName", specialDetailBean.getData().getAlbumName());//专辑名
//            intent.putExtra("artistNames", specialDetailBean.getData().getArtistNames());//艺术家
//        }
//        startActivity(intent);

    }

    @Override
    public void onClick(View view) {
        if (specialDetailBean == null || specialDetailBean.getData() == null)
            return;
        switch (view.getId()) {
            case R.id.effSpecial_Collect_warp://收藏
                if (specialDetailBean.getData().getIfCollection() == ConstantEnum.isCollection_Yes) {
                    cancelCollection(specialDetailBean.getData().getCollectionId());
                } else {
                    collection(specialDetailBean.getData().getAlbumId());
                }
                break;
            case R.id.effSpecial_abstruct_warp://简介
                //FIXME
                hiFiDialogTools.showAbstructView(getActivity(), specialDetailBean.getData().getAlbumIntroduction());
                break;
            case R.id.item_ImageBtn:
                SpecialDetailBean.DataBean.TjListBean bean = (SpecialDetailBean.DataBean.TjListBean) view.findViewById(R.id.item_ImageBtn).getTag();
                if (bean == null)
                    return;
                getData(String.format("albumId=%d", bean.getAlbumId()), Appconfig.getKeyNo(getActivity()));
                break;
        }
    }

    private void cancelCollection(int collectionId) {
        asyncHttpRequest.deleteUserCollection(getActivity(), collectionId, Appconfig.getKeyNo(getActivity()), this, this);
    }

    /**
     * @param albumId 收藏专辑
     */
    private void collection(int albumId) {
        asyncHttpRequest.collection(getActivity(), ConstantEnum.collectionType_Specail_for_interface, albumId, Appconfig.getKeyNo(getActivity()), this, this);
    }

    @Override
    public void onFailed(String method, String key, int errorTipId) {

    }

    @Override
    /**
     * 补充全局ViewTree监听，处理光标错乱的疑难杂症
     */
    public void onGlobalFocusChanged(View oldview, View newview) {
        if (newview == null)
            return;
        //--------------------------------------对listView 的逻辑处理
        if (newview instanceof ListView) {
            int postion = mListView.getSelectedItemPosition();
            Log.d(TAG, "onGlobalFocusChanged postion: " + postion);
            mListView.setItemsCanFocus(true);
            View currentSelectedView = mListView.getChildAt(postion);//每次都重新定位到第一条数据
            if (currentSelectedView != null) {
                mListView.setSelection(postion);
                setListViewItemFocusState(currentSelectedView, postion);
            }
        }
        setNextFocusId(newview);
    }

    /**
     * @param newview 对其他View的处理
     */
    private void setNextFocusId(View newview) {
        if (newview == null)
            return;
        switch (newview.getId()) {
            case R.id.effSpecial_Collect_warp:
            case R.id.effSpecial_abstruct_warp:
                newview.setNextFocusRightId(mListView.getId());
                break;
            case R.id.item_ImageBtn:
                newview.setNextFocusLeftId(mListView.getId());
                break;
            case R.id.ListView_:
                newview.setNextFocusDownId(mListView.getId());
                break;
        }
    }


}
