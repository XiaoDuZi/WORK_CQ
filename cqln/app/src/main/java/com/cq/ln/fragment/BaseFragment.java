package com.cq.ln.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.cq.ln.R;
import com.cq.ln.utils.XLog;
import com.cq.ln.views.FocusBorderView;

import java.util.ArrayList;
import java.util.List;

import cqdatasdk.network.AsyncHttpRequest;

/**
 * Created by YS on 2016/2/26.
 * 类说明：
 */
public class BaseFragment extends Fragment implements View.OnFocusChangeListener {
    private static final String TAG = "BaseFragment";
    private final float scale = 1.15f;
    public FocusBorderView borderView;
    private FrameLayout topLayout;
    private List<FocusBorderView> borderList = new ArrayList<>();
    public AsyncHttpRequest asyncHttpRequest = new AsyncHttpRequest();
    public boolean hasLoadLastUpdataData = false;//是否加载了最新数据
    public boolean hasLoadCacheData = false;//是否加载了缓存数据

    public void setBorderViewHide() {
        if (borderList != null && !borderList.isEmpty())
            for (FocusBorderView border : borderList) {
                if (border != null) {
                    if (border.getVisibility() == View.VISIBLE) {
                        border.setBorderViewHide();//
                        border.setHideView(true);//解决动画延时不会被隐藏
                    }
                }
            }
    }

    /**
     * @param rootView 创建边框View
     */
    public void createBorderView(View rootView) {
        borderView = new FocusBorderView(getActivity());
        borderView.setBorderBitmapResId(R.mipmap.pic_focus_bg);
        topLayout = (FrameLayout) getRootView(rootView);
        borderView.setBorderViewHide();
        borderList.add(borderView);

        if (topLayout != null) {
            topLayout.addView(borderView);
        }
    }

    public View getRootView(View rootView) {
        XLog.d(TAG, "rootView = " + rootView.hashCode());
        return ((ViewGroup) rootView.findViewById(R.id.Fragment_RootView));
    }

    /**
     * 遍历所有view,实现focus监听
     *
     * @param viewGroup
     */
    public void traversalView(ViewGroup viewGroup) {
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            doView(view);
            if (view instanceof ViewGroup)
                traversalView((ViewGroup) view);
        }
    }

    /**
     * 处理view
     *
     * @param view
     */
    private void doView(View view) {
        if (view == null)
            return;
        Object tagO = view.getTag();
        if (tagO == null)
            return;
        String tag = tagO.toString();
        if (TextUtils.isEmpty(tag))
            return;
        if (tag.equals(this.getResources().getString(R.string.tagForFocus))) {
            view.setFocusable(true);
            view.setOnFocusChangeListener(this);
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
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


}
