package com.cq.ln.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cq.ln.R;
import com.cq.ln.adapter.ArtistSpecialListAdapter;
import com.cq.ln.bean.RequestParameterBean;
import com.cq.ln.utils.ActivityUtility;
import com.cq.ln.utils.ImageTool;
import com.cq.ln.utils.StrTool;
import com.cq.ln.utils.XLog;
import com.cq.ln.views.FocusBorderView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cqdatasdk.bean.ArtistAlbumListBean;
import cqdatasdk.interfaces.IVolleyRequestSuccess;
import cqdatasdk.network.HostConfig;
import cqdatasdk.network.URLVerifyTools;

/**
 * Created by Administrator on 2016/3/31.
 * 艺术家专辑列表页
 */
public class ArtistSpecialListActivity extends BaseActivity {

    @Bind(R.id.ListView_Image_Page_Pre)
    ImageView mListViewImagePagePre;
    @Bind(R.id.gridViewResultDVD)
    GridView mGridViewResult;
    @Bind(R.id.image_Page_NextDVD)
    ImageView mImagePageNext;
    @Bind(R.id.text_tv)
    TextView mTextTv;
    @Bind(R.id.txtSearchPageNav)
    TextView mTxtSearchPageNav;
    @Bind(R.id.activity_RootView)
    FrameLayout mActivityRootView;
    @Bind(R.id.artist_icon_img)
    ImageView mArtistIconImg;
    @Bind(R.id.artist_name_text)
    TextView mArtistNameText;


    private int artistId;
    private String artistName;
    private String artistIconUrl;

    private RequestParameterBean mRequestParameterNearPlay;
    private ArtistSpecialListAdapter mArtistSpecialListAdapter;
    private FocusBorderView borderView;
    private FrameLayout topLayout;
    private ViewTreeObserver mViewTreeObserver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_special_list_layout);
        ButterKnife.bind(this);

        //遍历所有view,实现focus监听
        traversalView(this);
        //调用复写的创建BorderView
        createBorderView(this);

        artistId = getIntent().getIntExtra("artistId", 0);
        artistName = getIntent().getStringExtra("artistName");
        artistIconUrl = getIntent().getStringExtra("artistIconUrl");
        setArtistInfo();

        initListener();
        initObject();

        getData(artistId, mRequestParameterNearPlay.pageNo, 10);

        //  test();

    }

    private void setArtistInfo() {
        if (!TextUtils.isEmpty(artistName)) {
            mArtistNameText.setText(artistName);
        }
        if (!TextUtils.isEmpty(artistIconUrl)) {
            //mArtistIconImg.setImageResource();
            ImageTool.loadImage(getApplicationContext(), mArtistIconImg, URLVerifyTools.formatPicListImageUrl(artistIconUrl), 0, R.mipmap.middle, null, null);
        }
    }

    @Override
    public void createBorderView(Activity activity) {
        borderView = new FocusBorderView(activity);
        topLayout = (FrameLayout) getRootView(activity);
        borderView.setBorderViewHide();
        if (topLayout != null) {
            topLayout.addView(borderView);
        }
    }

    private void initListener() {
        getRootView(this).getViewTreeObserver().addOnGlobalFocusChangeListener(this);
//      mViewTreeObserver = mActivityRootView.getViewTreeObserver();
//      mViewTreeObserver.addOnGlobalFocusChangeListener(this);

        mGridViewResult.setOnItemClickListener(this);
        //   mGridViewResult.setOnFocusChangeListener(this);
        mGridViewResult.setOnItemSelectedListener(this);
    }

    private void initObject() {
        mRequestParameterNearPlay = new RequestParameterBean();
        mArtistSpecialListAdapter = new ArtistSpecialListAdapter(this);
        mGridViewResult.setAdapter(mArtistSpecialListAdapter);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    break;

            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {
        //super.onGlobalFocusChanged(oldFocus, newFocus);
    }

    /**
     * pass
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object object = parent.getAdapter().getItem(position);
        String albumId = "";
        if (object instanceof ArtistAlbumListBean.DataBean.ListBean) {
            ArtistAlbumListBean.DataBean.ListBean bean = (ArtistAlbumListBean.DataBean.ListBean) object;
            albumId = bean.getAlbumId();
            ActivityUtility.goSpecialDetailActivity(this, StrTool.parsetNum(albumId));
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        setFocusView2(view);
        if (position == mGridViewResult.getCount() - 1) {
            //Tools.showTip(ArtistSpecialListActivity.this, "需要加载数据了getNumColumns=="+mGridViewResult.getNumColumns() );
            mRequestParameterNearPlay.pageNo += 1;
            getData(artistId, mRequestParameterNearPlay.pageNo, 10);
            //        test();
        }
    }

    private void setFocusView2(View currentFocusView) {
        if (currentFocusView == null)
            return;
        // borderView.setBorderBitmapResId(R.mipmap.leftnav_focus_current);
        borderView.setBorderBitmapResId(R.mipmap.listpic_focus_bg);
        borderView.setHideView(false);//这一句非常重要
        borderView.setFocusView(currentFocusView, 1.0f);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        //super.onFocusChange(v,hasFocus);
    }

    private void getData(int artisId, int pageno, int size) {
        final String url = HostConfig.host + HostConfig.path + "hifiData/albumListByArtis.action?artisId=" + artisId + "&pageno=" + pageno + "&size=" + size;
        // final String url = path+"/hifiData/albumListByArtis.action?artisId=18&pageno=1&size=8";
        try {
            asyncHttpRequest.getArtistSpecialList(this, url, new IVolleyRequestSuccess() {
                @Override
                public void onSucceeded(String method, String key, Object object) {
                    ArtistAlbumListBean mArtistAlbumListBean = null;
                    if (object instanceof ArtistAlbumListBean) {
                        mArtistAlbumListBean = (ArtistAlbumListBean) object;
                    }
                    XLog.d("getJsonResponseData url==" + url);
                    if (mArtistAlbumListBean != null) {
                        XLog.d("getData mArtistAlbumListBean==" + mArtistAlbumListBean.toString());
                    }
                    setDataToApdater(mArtistAlbumListBean);
                }
            }, null);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setDataToApdater(ArtistAlbumListBean mArtistAlbumListBean) {
        if (mArtistAlbumListBean == null || mArtistAlbumListBean.getData() == null || mArtistAlbumListBean.getData().getList() == null) {
            if (mRequestParameterNearPlay.pageNo > 1) {
//                Tools.showTip(this, "没有更多数据了");
            } else {
//                Tools.showTip(this, "没有数据");
            }
            mRequestParameterNearPlay.pageNo -= 1;
            return;
        }
        mRequestParameterNearPlay.totalPage = mArtistAlbumListBean.getData().getTotalPage();
        if (mArtistSpecialListAdapter != null) {
            mArtistSpecialListAdapter.addDatas(mArtistAlbumListBean.getData().getList());
        }
        setPageView(mRequestParameterNearPlay.pageNo, mRequestParameterNearPlay.totalPage);
    }

    private void setPageView(int currentPage, int totalPage) {
        if (mImagePageNext == null || mTxtSearchPageNav == null)
            return;
        if (currentPage < totalPage) {
            mImagePageNext.setImageResource(R.mipmap.page_next_have_more);
        } else {
            mImagePageNext.setImageResource(R.mipmap.page_next_have_more_not);
        }
        mTxtSearchPageNav.setText(String.format("%d/%d", currentPage, totalPage));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
