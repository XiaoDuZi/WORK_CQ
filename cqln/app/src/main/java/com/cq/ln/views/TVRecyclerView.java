package com.cq.ln.views;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import com.cq.ln.utils.XLog;

/**
 * Created by Administrator on 2016/5/16.
 */
public class TVRecyclerView extends RecyclerView {


    public TVRecyclerView(Context context) {
        super(context);
        init(context, null);
    }

    public TVRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    //解决在最后一个focus 时候还会继续移动产生的错位问题！
                    if (getChildAt(getChildCount() - 1).equals(getFocusedChild())) {
                        return true;
                    }

                    break;
            }
        }
        return super.dispatchKeyEvent(event);
    }


    /**
     * 崩溃了.
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        try {
            super.dispatchDraw(canvas);
        } catch (Exception e) {
            XLog.d("e = " + e);
        }
    }

    WidgetTvViewBring mWidgetTvViewBring;

    private void init(Context context, AttributeSet attrs) {
        this.setChildrenDrawingOrderEnabled(true);
        mWidgetTvViewBring = new WidgetTvViewBring(this);
    }

    @Override
    public void bringChildToFront(View child) {
        mWidgetTvViewBring.bringChildToFront(this, child);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return mWidgetTvViewBring.getChildDrawingOrder(childCount, i);
    }


}
