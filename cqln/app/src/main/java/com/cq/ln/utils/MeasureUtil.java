package com.cq.ln.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016/3/7.
 */
public class MeasureUtil {

    public static int[] getScreenSize(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        manager.getDefaultDisplay().getSize(point);
        int[] result = new int[2];
        result[0] = point.x;
        result[1] = point.y;
        return result;
    }
}
