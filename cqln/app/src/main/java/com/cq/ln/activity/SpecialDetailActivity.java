package com.cq.ln.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cq.ln.R;
import com.cq.ln.adapter.SpecialDetailMusicListAdapter;
import com.cq.ln.appConfig.Appconfig;
import com.cq.ln.constant.ConstantEnum;
import com.cq.ln.utils.Enumer;
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
public class SpecialDetailActivity extends BaseActivity implements IVolleyRequestSuccess, IVolleyRequestfail, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, View
        .OnClickListener, ViewTreeObserver.OnGlobalFocusChangeListener {

    private static final String TAG = "SpecialDetailActivity";

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
    @Bind(R.id.activity_RootView)
    FrameLayout mActivityRootView;
    @Bind(R.id.ImageView_collection)
    ImageView mImageViewCollection;
    @Bind(R.id.txt_collection)
    TextView mTxtCollection;
    @Bind(R.id.effSpecial_Collect_warp)
    RelativeLayout mEffSpecialCollectWarp;
    @Bind(R.id.ImageView_abstruct)
    ImageView mImageViewAbstruct;
    @Bind(R.id.txt_abstruct)
    TextView mTxtAbstruct;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_detail);
        ButterKnife.bind(this);

        traversalView(this);
        createBorderView(this);
        initItemSquareViewList();
        iniAdapter();
//        initPlayBar();

        itemBean = (HiFiMainPageBean.ClassifyBean.ClassifyItemBean) getIntent().getSerializableExtra(Enumer.intent_specailBean);
        albumId = getIntent().getIntExtra("albumId", 0);
        fileType = getIntent().getIntExtra("fileType", 0);
        if (itemBean != null)
            getData(itemBean.href.trim().split("\\?")[1], Appconfig.getKeyNo(this));
        else
            getData("albumId=" + albumId, Appconfig.getKeyNo(this));
        setClickListener();
        this.getRootView(this).getViewTreeObserver().addOnGlobalFocusChangeListener(this);
    }


    private void iniAdapter() {
        musicListAdapter = new SpecialDetailMusicListAdapter(this);
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
        asyncHttpRequest.getSpecailDetail(this, albumIdStr, fileType, keyNo, this, this, "正在加载中...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
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
            Tools.showTip(SpecialDetailActivity.this, bean.getMsg());

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
            Tools.showTip(SpecialDetailActivity.this, bean.getMsg());
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

        ImageTool.loadImage(this, mEffSpecialMain, specailBigPic, 0, R.mipmap.default_small, new ImageTool.LoadSuccess() {
            @Override
            public void onSucceeded(Bitmap bitmap) {
                try {
                    int scaleRatio = 5;
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
                            bitmap.getWidth() / scaleRatio,
                            bitmap.getHeight() / scaleRatio,
                            false);

                    Bitmap blurImage = FastBlurUtil.doBlur(scaledBitmap, 20, false);
                    ivBg.setBackground(new BitmapDrawable(SpecialDetailActivity.this.getResources(), blurImage));
                    AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
                    //动画集
                    AnimationSet set = new AnimationSet(true);
                    set.addAnimation(alphaAnimation);
                    //设置动画时间 (作用到每个动画)
                    set.setDuration(1000);
                    ivBg.startAnimation(set);
                } catch (Exception e) {
                    e.printStackTrace();
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
            ImageTool.loadImage(getApplicationContext(), ItemSquareViewList.get(i).getImageButton(), URLVerifyTools.formatPicListImageUrl(tjList.get(i).getAlbumSmallImg()), 0, R.mipmap
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
        super.onFocusChange(v, hasFocus);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (getCurrentFocus().equals(mEffSpecialCollectWarp))
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
    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
        final SpecialDetailBean.DataBean.TrackListBean bean = (SpecialDetailBean.DataBean.TrackListBean) adapterView.getAdapter().getItem(i);
        if (bean == null)
            return;
        fileType = 0;
        if (TextUtils.equals(bean.getFileType(), "1")) {
            fileType = 1;
        }

        //带列表带跳转
//        Intent intent = new Intent(this, PlayUtilActivity.class);
        Intent intent = new Intent(this, PlayVideoActivity.class);
        intent.putExtra("isSingle", false);
        intent.putExtra("vodId", bean.getVodId());
        intent.putExtra("TrackListBean", bean);
        intent.putExtra("trackList", (Serializable) musicListAdapter.getlist());
        intent.putExtra("currentIndex", i);
        intent.putExtra("specailBigPic", specailBigPic);
        intent.putExtra("fileType", fileType);
        if (specialDetailBean != null && specialDetailBean.getData() != null) {
            intent.putExtra("albumName", specialDetailBean.getData().getAlbumName());//专辑名
            intent.putExtra("artistNames", specialDetailBean.getData().getArtistNames());//艺术家
        }


        intent.putExtra("playIndex", 1);
        intent.putExtra("playListJsonString","[{\"trackId\":13423,\"trackName\":\"骆驼的驼峰是个粮仓吗？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584572\",\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010001_B" +
                ".jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010001_S.jpg\",\"trackDuration\":\"\",\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21," +
                "\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\"," +
                "\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150493\"," +
                "\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}],\"fileType\":\"1\"},{\"trackId\":13424," +
                "\"trackName\":\"苍蝇为什么不会从墙壁上掉下来？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584595\",\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010002_B.jpg\"," +
                "\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010002_S.jpg\",\"trackDuration\":\"\",\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark" +
                ".png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"}," +
                "{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\"," +
                "\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}],\"fileType\":\"1\"},{\"trackId\":13425,\"trackName\":\"为什么鸭子走路像个不倒翁\",\"mp3Url\":\"\",\"flacUrl\":\"\"," +
                "\"vodId\":\"4584607\",\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010003_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010003_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13426,\"trackName\":\"刺猬害怕谁？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584117\",\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010004_B" +
                ".jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010004_S.jpg\",\"trackDuration\":\"\",\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21," +
                "\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\"," +
                "\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150493\"," +
                "\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}],\"fileType\":\"1\"},{\"trackId\":13427," +
                "\"trackName\":\"站着睡觉的马会摔倒吗？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4588586\",\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010005_B.jpg\"," +
                "\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010005_S.jpg\",\"trackDuration\":\"\",\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark" +
                ".png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"}," +
                "{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\"," +
                "\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}],\"fileType\":\"1\"},{\"trackId\":13428,\"trackName\":\"小白兔的眼睛是哭红的吗？\",\"mp3Url\":\"\",\"flacUrl\":\"\"," +
                "\"vodId\":\"4584072\",\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010006_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010006_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13429,\"trackName\":\"蚂蚁为什么特别遵守纪律？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584338\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010007_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010007_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13430,\"trackName\":\"猫咪为什么都要留胡子？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584103\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010008_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010008_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13431,\"trackName\":\"蜜蜂身上有什么暗器？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584611\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010009_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010009_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13432,\"trackName\":\"螃蟹为什么总是横行霸道？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584094\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010010_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010010_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13433,\"trackName\":\"鸡和鸭飞不高，也能算鸟吗？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584379\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010011_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010011_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13434,\"trackName\":\"斑马身上的条纹是谁画上去的？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584590\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010012_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010012_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13435,\"trackName\":\"白鹤睡觉时一只脚站着不累吗？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584374\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010013_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010013_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13436,\"trackName\":\"蜻蜓特别喜欢玩水吗？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584591\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010014_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010014_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13437,\"trackName\":\"蝴蝶的衣裳为什么特别漂亮？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584366\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010015_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010015_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13438,\"trackName\":\"蛇是用什么走路的？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584560\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010016_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010016_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13439,\"trackName\":\"小海马是爸爸生的吗？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584116\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010017_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010017_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13440,\"trackName\":\"没有水鱼会怎么样？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584148\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010018_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010018_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13441,\"trackName\":\"热带鱼身上漂亮的颜色有什么用？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584079\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010019_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010019_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13442,\"trackName\":\"乌贼是怎样脱险的？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584098\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010020_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010020_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13443,\"trackName\":\"萤火虫为什么总爱提着灯笼？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584357\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010021_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010021_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13444,\"trackName\":\"鹦鹉真的会说话吗？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584097\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010022_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010022_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13445,\"trackName\":\"犀牛是牛吗？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584105\",\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010023_B" +
                ".jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010023_S.jpg\",\"trackDuration\":\"\",\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21," +
                "\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\"," +
                "\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150493\"," +
                "\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}],\"fileType\":\"1\"},{\"trackId\":13446," +
                "\"trackName\":\"母鸡特别喜欢炫耀自己的功劳吗？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584068\",\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010024_B.jpg\"," +
                "\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010024_S.jpg\",\"trackDuration\":\"\",\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark" +
                ".png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"}," +
                "{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\"," +
                "\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}],\"fileType\":\"1\"},{\"trackId\":13447,\"trackName\":\"长颈鹿是怎样喝水的？\",\"mp3Url\":\"\",\"flacUrl\":\"\"," +
                "\"vodId\":\"4584088\",\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010025_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010025_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13448,\"trackName\":\"夏天狗把舌头伸出来是为了吓人吗？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584070\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010026_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010026_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13449,\"trackName\":\"老鼠啃东西是因为没吃饱吗？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584111\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010027_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010027_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13450,\"trackName\":\"花丛中飞来飞去的的小蜜蜂在干什么？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584090\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010028_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010028_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13451,\"trackName\":\"蒲公英是怎样生宝宝的\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584073\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010029_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010029_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13452,\"trackName\":\"蘑菇喝饱了水会长得更快吗？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584580\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010030_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010030_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13453,\"trackName\":\"发芽的土豆为什么不能吃？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584583\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010031_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010031_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13454,\"trackName\":\"为什么水仙没有土壤也能生长？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584360\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010032_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010032_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13455,\"trackName\":\"为什么移栽树木时要把枝叶剪得光秃秃的？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584071\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010033_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010033_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13456,\"trackName\":\"下雨以后春笋为什么长得快？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4588583\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010034_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010034_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13457,\"trackName\":\"开花的时间是谁定的？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584089\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010035_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010035_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13458,\"trackName\":\"为什么说竹子不是每年都开花？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584597\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010036_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010036_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13459,\"trackName\":\"香蕉有种子吗？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584381\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010037_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010037_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13460,\"trackName\":\"向日葵为什么喜欢看太阳？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4588576\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010038_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010038_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13461,\"trackName\":\"仙人掌的刺是它的武器吗？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584372\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010039_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010039_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13462,\"trackName\":\"种子是怎样变幼苗的？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584343\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010040_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010040_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13463,\"trackName\":\"含羞草的叶子为什么会合起来？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584603\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010041_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010041_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13464,\"trackName\":\"怎样知道大树的年龄？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584358\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010042_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010042_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13465,\"trackName\":\"睡莲很喜欢睡觉吗？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584107\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010043_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010043_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13466,\"trackName\":\"爬山虎为什么会飞檐走壁？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584610\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010044_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010044_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13467,\"trackName\":\"为什么铁树不喜欢开花？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584608\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010045_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010045_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13468,\"trackName\":\"雪为什么是白色的？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584339\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010046_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010046_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13469,\"trackName\":\"雾是怎样形成的？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584084\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010047_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010047_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13470,\"trackName\":\"为什么沙漠里特别干旱？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584614\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010048_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010048_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13471,\"trackName\":\"星星为什么闪闪发亮？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584096\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010049_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010049_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"},{\"trackId\":13472,\"trackName\":\"雨和雪躲在天上的什么地方？\",\"mp3Url\":\"\",\"flacUrl\":\"\",\"vodId\":\"4584601\"," +
                "\"trackSmallImg\":\"/utvgo_track/small_img/cp00110205010050_B.jpg\",\"trackBigImg\":\"/utvgo_track/utvgo_big/cp00110205010050_S.jpg\",\"trackDuration\":\"\"," +
                "\"cmboIds\":[{\"cmboId\":\"1403\",\"id\":21,\"imgUrl\":\"/product/has_order/ChildrensPark.png\",\"offerId\":\"800520150392\",\"orderBg\":\"/product/order_bg/children.png\"," +
                "\"orderCycle\":\"M\",\"pName\":\"幼教教育29元/月\",\"price\":29.0,\"status\":0,\"type\":\"0\"},{\"cmboId\":\"1498\",\"id\":27,\"imgUrl\":\"/product/has_order/ChildrensPark.png\"," +
                "\"offerId\":\"800520150493\",\"orderBg\":\"/product/order_bg/children.png\",\"orderCycle\":\"Y\",\"pName\":\"幼教教育299元/年\",\"price\":299.0,\"status\":0,\"type\":\"0\"}]," +
                "\"fileType\":\"1\"}]");


        startActivity(intent);
//        if (fileType == 1) { //视频播放
//
//        } else {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    playList(bean, specialDetailBean.getData().getAlbumName(), specialDetailBean.getData().getArtistNames(), i, musicListAdapter.getlist(), specailBigPic, fileType);
//                }
//            }, 500);
//        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
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
                hiFiDialogTools.showAbstructView(this, specialDetailBean.getData().getAlbumIntroduction());
                break;
            case R.id.item_ImageBtn:
                SpecialDetailBean.DataBean.TjListBean bean = (SpecialDetailBean.DataBean.TjListBean) view.findViewById(R.id.item_ImageBtn).getTag();
                if (bean == null)
                    return;
                getData(String.format("albumId=%d", bean.getAlbumId()), Appconfig.getKeyNo(this));
                break;
        }
    }

    private void cancelCollection(int collectionId) {
        asyncHttpRequest.deleteUserCollection(this, collectionId, Appconfig.getKeyNo(getApplicationContext()), this, this);
    }

    /**
     * @param albumId 收藏专辑
     */
    private void collection(int albumId) {
        asyncHttpRequest.collection(this, ConstantEnum.collectionType_Specail_for_interface, albumId, Appconfig.getKeyNo(getApplicationContext()), this, this);
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
