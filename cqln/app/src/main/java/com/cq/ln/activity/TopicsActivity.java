package com.cq.ln.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.cq.ln.CqApplication;
import com.cq.ln.R;
import com.cq.ln.RecyclerviewAdapter.TopicsAdapter;
import com.cq.ln.helper.SpaceItemDecoration;
import com.cq.ln.interfaces.onRecyclerViewItemSelectedListener;
import com.cq.ln.utils.ImageTool;
import com.cq.ln.utils.XLog;
import com.cq.ln.views.TVRecyclerView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cqdatasdk.bean.TopicsBean;
import cqdatasdk.interfaces.IVolleyRequestSuccess;
import cqdatasdk.interfaces.IVolleyRequestfail;
import cqdatasdk.network.URLVerifyTools;

/**
 * 专题页面
 */
public class TopicsActivity extends BaseActivity implements IVolleyRequestSuccess, IVolleyRequestfail, onRecyclerViewItemSelectedListener, View.OnLayoutChangeListener {

    @Bind(R.id.topics_Recyclerview)
    TVRecyclerView mTopicsRecyclerview;
    @Bind(R.id.iv_bg)
    ImageView ivBg;

    private TopicsAdapter topicsAdapter;
    private List<TopicsBean.ResultBean> topicsList;
    private boolean hadFocus = false;
    private String topicsId = "";
    private int linkType, fileType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);
        ButterKnife.bind(this);
        createBorderView(this);
        topicsId = getIntent().getStringExtra("topicsId");
        linkType = getIntent().getIntExtra("linkType",0);
        fileType = getIntent().getIntExtra("fileType",0);
        getData();
    }

    private void getData() {
        asyncHttpRequest.getTopicsDetail(this, topicsId, this, this, "正在加载中...");
    }

    private void init() {
        if (!topicsList.isEmpty()) {
            TopicsBean.ResultBean bean = topicsList.get(0);
            ImageTool.loadImageAndUserHandleOption(this, URLVerifyTools.formatMainImageUrl(bean.getImg()), ivBg, CqApplication.options, -1);
        }

        topicsAdapter = new TopicsAdapter(this);
        topicsAdapter.refresh(topicsList);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mTopicsRecyclerview.setLayoutManager(layout);
        mTopicsRecyclerview.setAdapter(topicsAdapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.dp15);
        mTopicsRecyclerview.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        topicsAdapter.setListener(this);
        mTopicsRecyclerview.addOnLayoutChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public boolean onItemFocus(boolean hasFocus, View v) {
        borderView.setBorderBitmapResId(R.mipmap.pic_focus_bg);
        if (hasFocus) {
            setViewFocus(v);
        } else {
            borderView.setUnFocusView(v);
        }
        return false;
    }

    @Override
    public void onItemClick(int position) {
        if (!topicsList.isEmpty()) {
            TopicsBean.ResultBean resultBean = topicsList.get(position);
            int albumId = 0;
            try {
                albumId =  Integer.parseInt(Uri.parse(resultBean.getHref()).getQueryParameter("albumId"));
            } catch (Exception e) {
                XLog.d("e=" + e);
            }
            Intent intent = new Intent(TopicsActivity.this, SpecialDetailActivity.class);
            intent.putExtra("albumId", albumId);
            intent.putExtra("fileType", fileType);
            TopicsActivity.this.startActivity(intent);
        }
    }

    private void setViewFocus(View v) {
        v.bringToFront();
        v.getParent().bringChildToFront(v);
        v.requestLayout();
        v.invalidate();
        borderView.setHideView(false);//这一句非常重要
        borderView.setFocusView(v, 1.1f);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (mTopicsRecyclerview.getChildCount() > 0) {
            View firstView = mTopicsRecyclerview.getChildAt(0);
            if (!hadFocus) {
                hadFocus = true;
                firstView.requestLayout();
                firstView.requestFocus();
            }
        }
    }

    @Override
    public void onSucceeded(String method, String key, Object object) throws Exception {
        topicsList = ((TopicsBean) object).getResult();
        init();
    }

    @Override
    public void onFailed(String method, String key, int errorTipId) throws Exception {

    }
}
