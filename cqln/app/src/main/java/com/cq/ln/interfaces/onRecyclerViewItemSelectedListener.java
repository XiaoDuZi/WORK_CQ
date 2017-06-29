package com.cq.ln.interfaces;

import android.view.View;

/**
 * Created by Administrator on 2016/5/16.
 */
public interface onRecyclerViewItemSelectedListener {
    boolean onItemFocus(boolean isfocus, View view);
    void onItemClick(int position);
}
