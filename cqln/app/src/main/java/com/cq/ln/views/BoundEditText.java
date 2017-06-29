package com.cq.ln.views;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class BoundEditText extends CusAutoCompleteTextView {
    private Drawable dRight;
    private Drawable dleft;
    private Rect rBounds;
    private boolean hasFocus;
    private OnKeyBoardHideListener onKeyBoardHideListener;

    // 构造器
    public BoundEditText(Context paramContext) {
        super(paramContext);
        initEditText();
    }

    public BoundEditText(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        initEditText();
    }

    public BoundEditText(Context paramContext, AttributeSet paramAttributeSet,
                         int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        initEditText();
    }

    // 初始化edittext 控件
    private void initEditText() {
        setEditTextDrawable();

        addTextChangedListener(new TextWatcher() { // 对文本内容改变进行监听
            public void afterTextChanged(Editable paramEditable) {
            }

            public void beforeTextChanged(CharSequence paramCharSequence,
                                          int paramInt1, int paramInt2, int paramInt3) {
            }

            public void onTextChanged(CharSequence paramCharSequence,
                                      int paramInt1, int paramInt2, int paramInt3) {
                BoundEditText.this.setEditTextDrawable();
            }
        });

        setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                BoundEditText.this.hasFocus = hasFocus;
                BoundEditText.this.setEditTextDrawable();
            }
        });
    }

    // 控制图片的显示
    private void setEditTextDrawable() {
        if (getText().toString().length() > 0 && hasFocus == true) {
            setCompoundDrawables(this.dleft, null, this.dRight, null);
        } else {

            setCompoundDrawables(this.dleft, null, null, null);
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.dRight = null;
        this.rBounds = null;
    }

    // 添加触摸事件
    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        if ((this.dRight != null) && (paramMotionEvent.getAction() == 1)) {
            this.rBounds = this.dRight.getBounds();
            int i = (int) paramMotionEvent.getX();
            if (i > getRight() - 4 * this.rBounds.width()) {
                setText("");
                paramMotionEvent.setAction(MotionEvent.ACTION_CANCEL);
            }
        }
        return super.onTouchEvent(paramMotionEvent);
    }

    // 设置显示的图片资源
    public void setCompoundDrawables(Drawable paramDrawable1,
                                     Drawable paramDrawable2, Drawable paramDrawable3,
                                     Drawable paramDrawable4) {
        if (paramDrawable3 != null)
            this.dRight = paramDrawable3;
        if (paramDrawable1 != null) {
            this.dleft = paramDrawable1;
        }
        super.setCompoundDrawables(paramDrawable1, paramDrawable2,
                paramDrawable3, paramDrawable4);
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent paramKeyEvent) {
        if (paramKeyEvent.getAction() == KeyEvent.ACTION_UP
                && paramKeyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK
                && onKeyBoardHideListener != null) {
            onKeyBoardHideListener.OnHide();
        }

        return super.dispatchKeyEventPreIme(paramKeyEvent);
    }

    public void setOnKeyBoardHideListener(
            OnKeyBoardHideListener onKeyBoardHideListener) {
        this.onKeyBoardHideListener = onKeyBoardHideListener;
    }

    /**
     * 软键盘隐藏监听
     *
     *
     */
    public abstract interface OnKeyBoardHideListener {
        public abstract void OnHide();

    }
}
