package com.cq.ln.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.cq.ln.R;
import com.cq.ln.utils.XLog;
import com.cq.ln.views.FocusBorderView;

/**
 * Created by Godfather on 16/4/15.
 * 尚未使用
 */
public class MusicPlayerDialog extends Dialog implements View.OnFocusChangeListener {

    private static Dialog dialog, listDialog;
    private static ListView dataList;
    private static Dialog DoloadDialog;
    private boolean canCancel = true;
    private static MusicPlayerDialog mMyDialog;


    private FrameLayout topLayout;
    private final float scale = 1.0f;

    public FocusBorderView borderView;
    private Context context;

    public MusicPlayerDialog(Context context) {
        super(context);
        this.context = context;
    }

    public static MusicPlayerDialog getInstance(Context context) {
        if (mMyDialog == null)
            mMyDialog = new MusicPlayerDialog(context);
        return mMyDialog;
    }


    public MusicPlayerDialog(Context ctx, int res, boolean cancelable) {
        super(ctx, R.style.MyDialog);
        this.canCancel = cancelable;
        setContentView(res);


    }

    public static void Close() {
        if (dialog != null)
            dialog.dismiss();
    }


    public void createBorderView(Activity activity) {
        borderView = new FocusBorderView(context);
        borderView.setBorderBitmapResId(R.mipmap.button_bg_white_color_border_focus);
        topLayout = (FrameLayout) getRootView();
        borderView.setBorderViewHide();


        if (topLayout != null) {
            topLayout.addView(borderView);

        }
        XLog.d("BaseActivity == onActivityCreated ----> Create  FocusBorderView!");

    }

    public View getRootView() {
        return ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0).findViewById(R.id.activity_RootView);
    }

    /**
     * 遍历所有view,实现focus监听
     *
     * @param activity
     */
    public void traversalView(Activity activity) {
        ViewGroup viewGroup = (ViewGroup) getRootView();
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
        if (tag.equals(context.getResources().getString(R.string.tagForFocus))) {
            view.setFocusable(true);
            view.setOnFocusChangeListener(this);
        }
    }


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
