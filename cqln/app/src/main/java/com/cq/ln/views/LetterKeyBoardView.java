package com.cq.ln.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.cq.ln.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/4/6.
 * 全英文键盘，暂无使用
 */
public class LetterKeyBoardView extends FrameLayout implements View.OnClickListener {
    @Bind(R.id.letter_A)
    Button mLetterA;
    @Bind(R.id.letter_B)
    Button mLetterB;
    @Bind(R.id.letter_C)
    Button mLetterC;
    @Bind(R.id.letter_D)
    Button mLetterD;
    @Bind(R.id.letter_E)
    Button mLetterE;
    @Bind(R.id.letter_F)
    Button mLetterF;
    @Bind(R.id.letter_G)
    Button mLetterG;
    @Bind(R.id.letter_H)
    Button mLetterH;
    @Bind(R.id.letter_I)
    Button mLetterI;
    @Bind(R.id.letter_J)
    Button mLetterJ;
    @Bind(R.id.letter_K)
    Button mLetterK;
    @Bind(R.id.letter_L)
    Button mLetterL;
    @Bind(R.id.letter_M)
    Button mLetterM;
    @Bind(R.id.letter_N)
    Button mLetterN;
    @Bind(R.id.letter_O)
    Button mLetterO;
    @Bind(R.id.letter_P)
    Button mLetterP;
    @Bind(R.id.letter_Q)
    Button mLetterQ;
    @Bind(R.id.letter_R)
    Button mLetterR;
    @Bind(R.id.letter_S)
    Button mLetterS;
    @Bind(R.id.letter_T)
    Button mLetterT;
    @Bind(R.id.letter_U)
    Button mLetterU;
    @Bind(R.id.letter_V)
    Button mLetterV;
    @Bind(R.id.letter_W)
    Button mLetterW;
    @Bind(R.id.letter_X)
    Button mLetterX;
    @Bind(R.id.letter_Y)
    Button mLetterY;
    @Bind(R.id.letter_Z)
    Button mLetterZ;
    @Bind(R.id.num_1)
    Button mNum1;
    @Bind(R.id.num_2)
    Button mNum2;
    @Bind(R.id.num_3)
    Button mNum3;
    @Bind(R.id.num_4)
    Button mNum4;
    @Bind(R.id.num_5)
    Button mNum5;
    @Bind(R.id.num_6)
    Button mNum6;
    @Bind(R.id.num_7)
    Button mNum7;
    @Bind(R.id.num_8)
    Button mNum8;
    @Bind(R.id.num_9)
    Button mNum9;
    @Bind(R.id.num_0)
    Button mNum0;
    @Bind(R.id.string_space)
    Button mStringSpace;
    @Bind(R.id.string_dele)
    Button mStringDele;
    @Bind(R.id.string_clear)
    Button mStringClear;

    public onKeybordListener getListener() {
        return listener;
    }

    public void setListener(onKeybordListener listener) {
        this.listener = listener;
    }

    private onKeybordListener listener;

    public interface onKeybordListener {
        void OnKeyClick(String key);
    }

    public LetterKeyBoardView(Context context) {
        super(context);
    }

    public LetterKeyBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View root = LayoutInflater.from(context).inflate(R.layout.keybord_layout, this);
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
        }
    }

    @Override
    public void onClick(View v) {
        if (listener != null)
            listener.OnKeyClick(((Button) v).getText().toString());
    }
}
