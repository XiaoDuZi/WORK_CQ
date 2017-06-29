package com.cq.ln.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.cq.ln.R;

import java.util.List;

import cqdatasdk.bean.DiscRecommendFilterBean;
import cqdatasdk.bean.DiscRecommendListBean;

/**
 * Created by fute on 16/9/14.
 */
public class ChildTypeActivity extends SearchActivity {

    int columnId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLeftWarp.setVisibility(View.GONE);
        mLeftWarp.setFocusable(false);
        mLeftWarp.setFocusableInTouchMode(false);
        mBtnOnlineSearch.setFocusable(false);
        mBtnOnlineSearch.setFocusableInTouchMode(false);
        mBtnDiscRecommend.setFocusable(false);
        mBtnDiscRecommend.setFocusableInTouchMode(false);
        mBtnMusicStyle.setFocusable(false);
        mBtnMusicStyle.setFocusableInTouchMode(false);
        mBtnSigner.setFocusable(false);
        mBtnSigner.setFocusableInTouchMode(false);
        FrameLayout.LayoutParams param1 = (FrameLayout.LayoutParams) mOrtherRightWarp.getLayoutParams();
        param1.leftMargin = 0;
        param1.width = (int) getResources().getDimension(R.dimen.dp1250);
        mOrtherRightWarp.setLayoutParams(param1);

        FrameLayout.LayoutParams param2 = (FrameLayout.LayoutParams) flGridViewResultDVD.getLayoutParams();
        param2.width = (int) getResources().getDimension(R.dimen.dp1090);
        flGridViewResultDVD.setLayoutParams(param2);
        mGridViewResult.setNumColumns(5);

        mBtnSearchCommint.setNextFocusLeftId(R.id.listFilterChildSong);

        columnId = getIntent().getIntExtra("columnId", 0);
        if(columnId==0){
            mLeftWarp.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object object = parent.getAdapter().getItem(position);
        if (object instanceof DiscRecommendListBean.DataBean.ListBean) {
            DiscRecommendListBean.DataBean.ListBean bean = (DiscRecommendListBean.DataBean.ListBean) object;
            Intent intent = new Intent();
            intent.setClass(ChildTypeActivity.this, SpecialDetailActivity.class);
            intent.putExtra("albumId", bean.getId());
            intent.putExtra("fileType", bean.getFileType());
            startActivity(intent);
        } else {
            super.onItemClick(parent, view, position, id);
        }
    }

    @Override
    public void getSearchResult(int pageno, String keyWord) {
        asyncHttpRequest.getChildSearchResultByKeyWord(this, keyWord, pageno, this, this, pageno == 1 ? "加载中..." : null);
    }

    @Override
    public void getDiscRecommendFilterList() {
        if (discRecommendFilterBeanList == null || discRecommendFilterBeanList.isEmpty()) {
            asyncHttpRequest.getChildSongFilterList(this, this, this);
        } else {
            refreshLeftFilterList(discRecommendFilterBeanList);
        }
    }

    @Override
    public <T> void refreshLeftFilterList(final List<T> list) {
        DiscRecommendFilterBean.DataBean bean = new DiscRecommendFilterBean.DataBean();
        bean.setCode("0");
        bean.setId(-1);
        bean.setName("在线搜索");
        list.add(0, (T) bean);
        super.refreshLeftFilterList(list);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int position = 0;
                for (int i = 0; i < list.size(); i++) {
                    DiscRecommendFilterBean.DataBean beanPostion = (DiscRecommendFilterBean.DataBean) list.get(i);
                    if (columnId == beanPostion.getId()) {
                        position = i;
                        break;
                    }
                }
                if(position<mListViewFilter.getChildCount()){
                    mListViewFilter.setSelection(position);
                    mListViewFilter.getChildAt(position).requestFocusFromTouch();
                    mListViewFilter.getChildAt(position).requestFocus();
                }
            }
        }, 500);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                View view = mListViewFilter.getChildAt(0).findViewById(R.id.txtView);
                if (view.isSelected()) {
                    mBtnSearchCommint.requestFocus();
                    mBtnSearchCommint.requestFocusFromTouch();
                    return false;
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (mListViewFilter.getSelectedItemPosition() == 0) {
            IS_PAD_RIGHT_EVENT = false;
        }
        super.onFocusChange(v, hasFocus);
    }

    @Override
    public void loadDataByFilter(AdapterView<?> adapterView, int i) throws Exception {
        if (currentTheFirstFilterView.equals(mBtnDiscRecommend)) {//唱片推荐
            if (mListViewFilter.getSelectedItemPosition() == 0) {
                mGridViewResult.setVisibility(View.GONE);
                mImagePagePre.setVisibility(View.GONE);
                mImagePageNext.setVisibility(View.GONE);
                mOnlineRightWarp.setVisibility(View.VISIBLE);
            } else {
                mGridViewResult.setVisibility(View.VISIBLE);
                mImagePagePre.setVisibility(View.VISIBLE);
                mImagePageNext.setVisibility(View.VISIBLE);
                mOnlineRightWarp.setVisibility(View.GONE);
                super.loadDataByFilter(adapterView, i);
            }
        } else {
            super.loadDataByFilter(adapterView, i);
        }
    }

    /**
     * @param columnId 获取唱片推荐数据
     */
    @Override
    public void getDiscRecommendList(int columnId, int pageNo) {
        if (columnId == -1) {
            return;
        }
        if (pageNo == 1) {//拿取一页，先拿缓存
            List<DiscRecommendListBean.DataBean.ListBean> cacheMap = disRecomendDataMap.get(columnId);
            if (cacheMap != null && !cacheMap.isEmpty()) {
                refreshRightGridViewList(cacheMap);
                int totalPage = -1;
                totalPage = discRecomendTotalPageMap.get(columnId);
                setOtherInfo(1, totalPage);
            } else
                asyncHttpRequest.getChildRecommendList(this, columnId, pageNo, this, this, pageNo == 1 ? null : null);
        } else {//拿取多页，不走缓存
            asyncHttpRequest.getChildRecommendList(this, columnId, pageNo, this, this, pageNo == 1 ? null : null);
        }
    }
}
