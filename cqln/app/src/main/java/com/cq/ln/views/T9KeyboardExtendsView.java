package com.cq.ln.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.cq.ln.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/4/7.
 * T9键盘扩展View,用于弹出popupwindows
 */
public class T9KeyboardExtendsView extends RelativeLayout {
    @Bind(R.id.btn_center)
    Button mBtnCenter;
    @Bind(R.id.btn_top)
    Button mBtnTop;
    @Bind(R.id.btn_Bottom)
    Button mBtnBottom;
    @Bind(R.id.btn_left)
    Button mBtnLeft;
    @Bind(R.id.btn_right)
    Button mBtnRight;


    public T9KeyboardExtendsView(Context context) {
        super(context);
        init(context);
    }

    public T9KeyboardExtendsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.t9_keyboard_extend, this);
        ButterKnife.bind(root);
    }

    /**
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param center
     */
    public void setViewKeyValues(String left, String top, String right, String bottom, String center) {
        mBtnLeft.setText(left);
        mBtnTop.setText(top);
        mBtnRight.setText(right);
        mBtnBottom.setText(bottom);
        mBtnCenter.setText(center);
    }

    public String getLeftKey() {
        return mBtnLeft.getText().toString();
    }

    public String getTopKey() {
        return mBtnTop.getText().toString();
    }

    public String getRightKey() {
        return mBtnRight.getText().toString();
    }

    public String getBottomKey() {
        return mBtnBottom.getText().toString();
    }

    public String getCenterKey() {
        return mBtnCenter.getText().toString();
    }
}
