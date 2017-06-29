package com.cq.ln.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.cq.ln.R;
import com.cq.ln.interfaces.myViewAnimationListener;

public class AnimationUtils {
    public static Animation createScaleAnimation(float fromX, float toX, float fromY, float toY, long paramLong) {
        ScaleAnimation localScaleAnimation = new ScaleAnimation(fromX, toX, fromY, toY, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
        localScaleAnimation.setInterpolator(new AccelerateInterpolator());
        localScaleAnimation.setDuration(paramLong);
        localScaleAnimation.setFillAfter(true);
        return localScaleAnimation;
    }

    public static Animation createAlphaAnimation(float fromAlpha, float toAlpha, long duration) {
        AlphaAnimation localAlphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
        localAlphaAnimation.setStartOffset(0L);
        localAlphaAnimation.setInterpolator(new AccelerateInterpolator());
        localAlphaAnimation.setDuration(duration);
        return localAlphaAnimation;
    }


    public static void fadein(Context context, final View view) {
        fadein(context, view, null);
    }

    public static void fadein(Context context, final View view, final myViewAnimationListener listener) {
        if (view.getVisibility() == View.VISIBLE)
            return;
        if (context == null)
            return;
        Animation anim = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.fade_in);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
                if (null != listener)
                    listener.onAnimationEnd(view);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(anim);
    }

    public static void fadeout(Context context, final View view) {
        if (view.getVisibility() != View.VISIBLE)
            return;
        if (context == null)
            return;
        Animation anim = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.fade_out);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(anim);
    }
}