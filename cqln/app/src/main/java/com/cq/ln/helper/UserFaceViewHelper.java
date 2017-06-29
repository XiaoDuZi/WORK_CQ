package com.cq.ln.helper;

import android.widget.ImageButton;

import com.cq.ln.R;

/**
 * Created by Administrator on 2016/5/20.
 */
public class UserFaceViewHelper {


    public static void setUserFaceImage(int index, ImageButton mUserInfoHeadIcon) {
        int resId = -1;
        switch (index) {
            case 0:
                resId = R.mipmap.head_1;
                break;
            case 1:
                resId = R.mipmap.head_2;
                break;
            case 2:
                resId = R.mipmap.head_3;
                break;
            case 3:
                resId = R.mipmap.head_4;
                break;
            case 4:
                resId = R.mipmap.head_5;
                break;
            case 5:
                resId = R.mipmap.head_6;
                break;
            case 6:
                resId = R.mipmap.head_7;
                break;
            case 7:
                resId = R.mipmap.head_8;
                break;
        }
        if (resId != -1)
            mUserInfoHeadIcon.setImageResource(resId);
    }


}
