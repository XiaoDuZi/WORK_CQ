package com.cq.ln.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.cq.ln.R;

/**
 * Created by Administrator on 2016/3/7.
 */
public class ItemHead extends FrameLayout {
    private Drawable draHead, draCheck;

    public ItemHead(Context context) {
        super(context);
    }

    public ItemHead(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_face, this);
        setFocusable(false);
        setFocusableInTouchMode(false);
        setEnabled(false);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.item_face);
        int n = array.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.item_face_head:
                    draHead = array.getDrawable(attr);
                    break;
                case R.styleable.item_face_check:
                    draCheck = array.getDrawable(attr);
                    break;
            }
        }

        ImageView head = (ImageView) findViewById(R.id.Im_head);
        ImageView check = (ImageView) findViewById(R.id.Im_check);

        if (draHead != null)
            head.setImageDrawable(draHead);
        if (draCheck != null)
            check.setImageDrawable(draCheck);
        setClickable(true);

        array.recycle();
    }


}
