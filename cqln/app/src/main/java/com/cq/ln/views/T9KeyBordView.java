package com.cq.ln.views;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cq.ln.R;
import com.cq.ln.interfaces.onKeybordListener;
import com.cq.ln.utils.MyPopupWindow;
import com.cq.ln.utils.XLog;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/4/6.
 * T9键盘View
 */
public class T9KeyBordView extends FrameLayout implements View.OnClickListener {
    private static final String TAG = "T9KeyBordView";


    public onKeybordListener getListener() {
        return listener;
    }

    public void setListener(onKeybordListener listener) {
        this.listener = listener;
    }

    onKeybordListener listener;
    T9KeyboardExtendsView extendsView;
    MyPopupWindow popupWindow;
    private Context context;


    public T9KeyBordView(Context context) {
        super(context);
        this.context = context;
    }

    public T9KeyBordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View root = LayoutInflater.from(context).inflate(R.layout.t9_keyboad_layout, this);
        ButterKnife.bind(root);
        setFocusable(false);
        setFocusableInTouchMode(false);
        setEnabled(false);
        traversal((ViewGroup) root);
    }


    private void traversal(ViewGroup viewGroup) {
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            doView(view);
            if (view instanceof ViewGroup)
                traversal((ViewGroup) view);
        }
    }

    private void doView(View view) {
        if (view == null)
            return;
        if (view instanceof Button) {
            view.setOnClickListener(this);
            setTextSize((Button) view);
        }
    }

    /**
     * TODO 在模拟器无效果，需要在重网广电机顶盒测试效果
     *
     * @param button 动态设置文字不同的大小
     */
    private void setTextSize(Button button) {
        String string = button.getText().toString();
        if (!string.contains("\n"))
            return;
        SpannableString sStr = new SpannableString(string);
        sStr.setSpan(new TextAppearanceSpan(context, R.style.t9_keyboard_small_numsize), 0, string.indexOf("\n"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sStr.setSpan(new TextAppearanceSpan(context, R.style.t9_keyboard_big_lettersize), string.indexOf("\n") + 1, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        button.setText(sStr, TextView.BufferType.SPANNABLE);
        XLog.d(TAG, "设置size==== 数字：" + string.substring(0, string.indexOf("\n")) + "  英文：" + string.substring(string.indexOf("\n") + 1, string.length()));
    }

    private T9KeyboardExtendsView getExtendsView() {
        if (extendsView == null)
            extendsView = new T9KeyboardExtendsView(context);
        return extendsView;
    }

    private MyPopupWindow getPopupWindow(View parentView) {
        if (popupWindow == null)
            popupWindow = new MyPopupWindow();
        popupWindow.setParentView(parentView);
        popupWindow.setExtendsView(getExtendsView());
        return popupWindow;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (popupWindow != null && popupWindow.isShowingPopWindow()) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    if (listener != null)
                        listener.OnKeyClick(extendsView.getLeftKey());
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    if (listener != null)
                        listener.OnKeyClick(extendsView.getTopKey());
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (listener != null)
                        listener.OnKeyClick(extendsView.getRightKey());
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    if (listener != null)
                        listener.OnKeyClick(extendsView.getBottomKey());
                    break;
                case KeyEvent.KEYCODE_ENTER:
                    if (listener != null) {
                        String key = extendsView.getCenterKey();
                        if (key.equals("OK"))
                            key = extendsView.getTopKey();
                        listener.OnKeyClick(key);
                    }
                    break;
            }
            popupWindow.closeWindow();
            return true;//说明处理了此事件
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onClick(View parentView) {
        switch (parentView.getId()) {
            case R.id.string_dele:
            case R.id.string_clear:
            case R.id.key0:
            case R.id.key1:
                if (listener != null)
                    listener.OnKeyClick(((Button) (parentView)).getText().toString().trim());
                return;
            case R.id.key2:
                getExtendsView().setViewKeyValues("A", "2", "B", "C", "OK");
                break;
            case R.id.key3:
                getExtendsView().setViewKeyValues("D", "3", "E", "F", "OK");
                break;
            case R.id.key4:
                getExtendsView().setViewKeyValues("G", "4", "H", "I", "OK");
                break;
            case R.id.key5:
                getExtendsView().setViewKeyValues("J", "5", "K", "L", "OK");
                break;
            case R.id.key6:
                getExtendsView().setViewKeyValues("M", "6", "N", "O", "OK");
                break;
            case R.id.key7:
                getExtendsView().setViewKeyValues("P", "Q", "R", "S", "7");
                break;
            case R.id.key8:
                getExtendsView().setViewKeyValues("T", "8", "U", "V", "OK");
                break;
            case R.id.key9:
                getExtendsView().setViewKeyValues("W", "X", "Y", "Z", "9");
                break;
        }
        getPopupWindow(parentView).showWindow();
    }
}
