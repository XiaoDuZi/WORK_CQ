package com.cq.ln.views;

import android.view.View;

/**
 * Created by Administrator on 2016/3/9.
 */
public class ScaleView {
    private View view;
    private int width;
    private int height;

    public ScaleView(View view) {
        this.view = view;
    }

    public int getWidth() {
        return view.getLayoutParams().width;
    }

    public void setWidth(int width) {
        this.width = width;
        view.getLayoutParams().width = width;
        view.requestLayout();
    }

    public int getHeight() {
        return view.getLayoutParams().height;
    }

    public void setHeight(int height) {
        this.height = height;
        view.getLayoutParams().height = height;
        view.requestLayout();
    }

}
