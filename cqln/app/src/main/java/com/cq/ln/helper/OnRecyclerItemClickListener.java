package com.cq.ln.helper;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Godfather on 16/5/16.
 * <p>
 * 针对RecyclerView 实现的单击和长按事件帮助类
 */
public abstract class OnRecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    public abstract void onItemClick(RecyclerView.ViewHolder viewHolder);

    public abstract void onItemLongClick(RecyclerView.ViewHolder viewHolder);


    private GestureDetectorCompat gestureDetectorCompat;
    private RecyclerView recyclerView;

    public OnRecyclerItemClickListener(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        gestureDetectorCompat = new GestureDetectorCompat(recyclerView.getContext(), new ItemTouchHelperGestureListener());

    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetectorCompat.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetectorCompat.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }


    class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {


        /**
         * @param e 单击
         * @return
         */
        @Override
        public boolean onSingleTapUp(MotionEvent e) {

            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(child);
                onItemClick(holder);
            }
            return super.onSingleTapUp(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(child);
                onItemLongClick(holder);
            }

        }


    }


}


