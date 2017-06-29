package com.cq.ln.activity;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

/**
 * Created by godfather on 2016/3/19.
 * 最高层的Activity
 */
public abstract class RooterActivity extends FragmentActivity {
    abstract void createBorderView(Activity activity);

}
