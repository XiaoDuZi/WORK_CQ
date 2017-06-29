package com.cq.ln.helper;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2016/5/16.
 * <p/>
 * RecyclerView 没有Item间距设置，可以用这个item的装饰类设置丰富多彩的属性
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildPosition(view) != 0) {
            outRect.left = space;
        }
    }

}
