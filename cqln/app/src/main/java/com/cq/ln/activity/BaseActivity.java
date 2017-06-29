package com.cq.ln.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cq.ln.R;
import com.cq.ln.utils.AppTools;
import com.cq.ln.utils.HiFiDialogTools;
import com.cq.ln.utils.XLog;
import com.cq.ln.views.FocusBorderView;

import java.util.ArrayList;
import java.util.List;

import cqdatasdk.network.AsyncHttpRequest;

/**
 * Created by YS on 2016/2/26.
 * 类说明：
 */
public class BaseActivity extends RooterActivity implements View.OnFocusChangeListener
        /**
         * li.gang add interfaces 2016.3.26
         */
        , View.OnClickListener
        , AdapterView.OnItemSelectedListener
        , AdapterView.OnItemClickListener
        , ViewTreeObserver.OnTouchModeChangeListener,   // 用于监听 Touch 和非 Touch 模式的转换
        ViewTreeObserver.OnGlobalLayoutListener,     // 用于监听布局之类的变化，比如某个空间消失了
        ViewTreeObserver.OnPreDrawListener,        // 用于在屏幕上画 View 之前，要做什么额外的工作
        ViewTreeObserver.OnGlobalFocusChangeListener  // 用于监听焦点的变化
         {
    private static String TAG = "BaseActivity";
    private final float scale = 1.0f;
    public FocusBorderView borderView;
    public AsyncHttpRequest asyncHttpRequest = new AsyncHttpRequest();
    public boolean showPlaying = true;
    public boolean showHomeBg = true;
    public HiFiDialogTools hiFiDialogTools = new HiFiDialogTools();

    private RelativeLayout rl_music_info;
    private FrameLayout topLayout;
    private List<FocusBorderView> borderList = new ArrayList<>();
    /**
     * 根视野
     */
    private FrameLayout mContentContainer;
    /**
     * 浮动视野
     */
    private View mFloatView;
    private TextView tv_songName;
    private ImageView image_Big;
    private Animation rotateAnim;
    private RelativeLayout rl_image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (showHomeBg){
            getWindow().setBackgroundDrawableResource(R.mipmap.home_bg);
        }
        TAG = TAG + getResources().getString(R.string.test_tip);
        if (showPlaying && !AppTools.isSsLife()) { //时尚生活不显示正在播放
            ViewGroup mDecorView = (ViewGroup) getWindow().getDecorView();
            mContentContainer = (FrameLayout) ((ViewGroup) mDecorView.getChildAt(0)).getChildAt(1);
            mFloatView = LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_play_music, null);
            mFloatView.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (showPlaying && !AppTools.isSsLife()) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.BOTTOM;
            layoutParams.bottomMargin = (int) getResources().getDimension(R.dimen.dp5);
            mContentContainer.addView(mFloatView, layoutParams);

            initPlayBar();
        }
    }


    private void initPlayBar() {
        rl_music_info = (RelativeLayout) mFloatView.findViewById(R.id.rl_music_info);
        tv_songName = (TextView) mFloatView.findViewById(R.id.tv_songName);
        rl_image = (RelativeLayout) mFloatView.findViewById(R.id.rl_image);
        image_Big = (ImageView) mFloatView.findViewById(R.id.Image_Big);
        rl_music_info.setOnFocusChangeListener(this);
        rl_music_info.setOnClickListener(this);
    }


    /**
     * @param activity
     */
    @Override
    public void createBorderView(Activity activity) {
        borderView = new FocusBorderView(this);
        borderView.setBorderBitmapResId(R.mipmap.button_bg_white_color_border_focus);
        topLayout = (FrameLayout) getRootView(activity);
        borderView.setBorderViewHide();
        borderList.add(borderView);

        if (topLayout != null) {
            topLayout.addView(borderView);

        }
        XLog.d("BaseActivity == onActivityCreated ----> Create  FocusBorderView!");

    }

    public View getRootView(Activity context) {
        return ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0).findViewById(R.id.activity_RootView);
    }

    /**
     * 遍历所有view,实现focus监听
     *
     * @param activity
     */
    public void traversalView(Activity activity) {
        ViewGroup viewGroup = (ViewGroup) getRootView(activity);
        traversal(viewGroup);

    }

    private void traversal(ViewGroup viewGroup) {
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            doView(view);
            if (view instanceof ViewGroup)
                traversal((ViewGroup) view);
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


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        boolean needBringFront = true;
        if (v.getId() == R.id.rl_music_info) {
            if (hasFocus) {
                needBringFront = false;
                if (borderView != null) {
                    borderView.setVisibility(View.GONE);
                    return;
                }
            }
        }
        if (borderView == null) {
            return;
        }
        if (hasFocus) {
            if (needBringFront) {
                v.bringToFront();
                v.getParent().bringChildToFront(v);
            }
            v.requestLayout();
            v.invalidate();
            borderView.setHideView(false);//这一句非常重要
            borderView.setFocusView(v, scale);
        } else {
            borderView.setUnFocusView(v);
        }
    }

    @Override
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {

    }

    @Override
    public void onGlobalLayout() {

    }

    @Override
    public boolean onPreDraw() {
        return false;
    }

    @Override
    public void onTouchModeChanged(boolean isInTouchMode) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rl_music_info) {
//            Intent intent = new Intent(this, MusicPlayActivity.class);
//            startActivity(intent);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);//设置切换没有动画，用来实现活动之间的无缝切换
        super.startActivity(intent);
    }

    /**
     * 重点，在这里设置按下返回键，或者返回button的时候无动画
     */
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);//设置返回没有动画
    }

    /**
     * 播放歌曲时，旋转歌曲图标
     */
    private void startRotateIcon() {
        if (rotateAnim == null) {
            rotateAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_center);
            LinearInterpolator lin = new LinearInterpolator();
            rotateAnim.setInterpolator(lin);
        }
        rl_image.clearAnimation();
        rl_image.startAnimation(rotateAnim);
    }

    /**
     * 停止歌曲图标旋转动画
     */
    private void stopRotateIcon() {
        rl_image.clearAnimation();
    }

}


