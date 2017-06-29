package com.cq.ln.utils;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

public class MyPopupWindow {
    private static final String TAG = "MyPopupWindow";
    private PopupWindow popupWindow;
    private View extendsView;
    private View parentView;
    int extendViewWith;
    int extendViewHeight;


    public View getParentView() {
        return parentView;
    }

    public View getExtendsView() {
        return extendsView;
    }

    public void setExtendsView(View extendsView) {
        this.extendsView = extendsView;
    }


    public void setParentView(View parentView) {
        this.parentView = parentView;
    }

    public void showWindow() {
        popupWindow = new PopupWindow(extendsView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
        popupWindow.setOutsideTouchable(false);// 设置不允许在外点击消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        extendViewWith = extendsView.getWidth();
        extendViewHeight = extendsView.getHeight();
        if (extendViewWith == 0) {//note : 首次extendView未加载显示，测手动给值，跟t9_keyboard_extend.xml中的值对应
            //方式1，灵活性差
//            extendViewWith = context.getResources().getDimensionPixelOffset(R.dimen.dp228);
//            extendViewHeight = context.getResources().getDimensionPixelOffset(R.dimen.dp228);

            //TODO 方式2，灵活性高，待重网广电盒子测试
            Point point = CommonUtils.getViewMeasureSize(extendsView);
            if (point != null) {
                extendViewWith = point.x;
                extendViewHeight = point.y;
                XLog.d(TAG, "1 extendViewWith = " + extendViewWith + "\nextendViewHeight = " + extendViewHeight);
            }
        }
        XLog.d(TAG, "2 extendViewWith = " + extendViewWith + "\nextendViewHeight = " + extendViewHeight);
        show();
    }

    private void show() {
        int parentWidth = parentView.getWidth();
        int parentHeight = parentView.getHeight();
        int offX = (extendViewWith - parentWidth) / 2;
        int offY = parentHeight + (extendViewHeight - parentHeight) / 2;
        popupWindow.showAsDropDown(parentView, -offX, -offY);
    }

    /**
     * @param view 获得view在屏幕的绝对位置
     * @return
     */
    public Rect findLocationWithView(View view) {
        ViewGroup root = (ViewGroup) view.getRootView();
        Rect rect = new Rect();
        root.offsetDescendantRectToMyCoords(view, rect);
        return rect;
    }


    public boolean isShowingPopWindow() {
        if (popupWindow == null)
            return false;
        return popupWindow.isShowing();
    }


    public void closeWindow() {
        if (popupWindow != null)
            popupWindow.dismiss();
    }
}
