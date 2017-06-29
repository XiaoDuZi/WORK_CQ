package com.cq.ln.views;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AutoCompleteTextView;

public class CusAutoCompleteTextView extends AutoCompleteTextView {

    // private Drawable rightDrawable;
    // private Drawable rightDrawableSelected;
    private int clickWidth;
    private boolean status;
    private boolean isShowList; // 记录下拉列表是否已显示

    public CusAutoCompleteTextView(Context context) {
        super(context);
    }

    public CusAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // rightDrawable = getResources().getDrawable(R.drawable.get_more);
        // rightDrawableSelected =
        // getResources().getDrawable(R.drawable.get_more_selected);
        // setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable,
        // null);
        // clickWidth = rightDrawable.getBounds().width() + getPaddingRight() *
        // 2;
        clickWidth = 10 + getPaddingRight() * 2;
    }

    public CusAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (status)
                    setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_DOWN:
                if (event.getX() >= (getWidth() - clickWidth)) {
                    // 如果在可点击范围内
                    setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    if (!isShowList) {
                        showDropDown();
                    } else {
                        dismissDropDown();
                    }
                    status = true;
                } else {
                    status = false;
                }
                break;
        }
        return status ? true : super.onTouchEvent(event);
    }

    @Override
    public void showDropDown() {
        if (!isShowList) {
            super.showDropDown();
            isShowList = true;
        }
    }

    @Override
    public void dismissDropDown() {
        isShowList = false;
        super.dismissDropDown();
    }

    public boolean isShowList() {
        return isShowList;
    }

    public void setShowList(boolean isShowList) {
        this.isShowList = isShowList;
    }

}

