package com.cq.ln.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cq.ln.R;

/**
 * Created by Administrator on 2016/3/7.
 */
public class ItemSquare extends FrameLayout {
    private TextView textView;

    public ImageButton getImageButton() {
        return imageButton;
    }

    public TextView getTextView() {
        return textView;
    }

    private ImageButton imageButton;


    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
        try {
            flushDrawable();
        } catch (Exception e) {
        }
    }

    public void setText(String text) {
        this.text = text;
        try {
            flushText();
        } catch (Exception e) {
        }
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public String getText() {
        return text;
    }

    private Drawable drawable;
    private String text;

    public ItemSquare(Context context) {
        super(context);
    }

    public ItemSquare(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_square, this);
        setFocusable(false);
        setFocusableInTouchMode(false);
        setEnabled(false);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.item_square);
        int n = array.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.item_square_src:
                    drawable = array.getDrawable(attr);
                    break;
                case R.styleable.item_square_text:
                    text = array.getString(attr);
                    break;


            }
        }
        imageButton = (ImageButton) findViewById(R.id.item_ImageBtn);
        textView = (TextView) findViewById(R.id.item_txt);
        if (!TextUtils.isEmpty(text))
            textView.setText(text);
        if (drawable != null)
            imageButton.setBackgroundDrawable(drawable);
        array.recycle();
    }

    private void flushDrawable() throws Exception{
        if (drawable != null && imageButton != null)
            imageButton.setBackgroundDrawable(drawable);
    }

    private void flushText() throws Exception{
        if (!TextUtils.isEmpty(text) && textView != null)
            textView.setText(text);
    }

}
