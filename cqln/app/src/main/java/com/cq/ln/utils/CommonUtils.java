package com.cq.ln.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.cq.ln.R;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


/**
 * 公共的工具类
 *
 * @author Administrator
 */
@SuppressLint("NewApi")
public class CommonUtils {


    private static long lastClickTime;

    /**
     * 是否快速点击，minTime定义点击间隔
     *
     * @return
     */
    public static boolean isFastClick() {
        return isFastClick(250);
    }

    public static boolean isFastClick(int minTime) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (timeD > 0 && timeD < minTime) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


    static Toast shorttoast, longtoast;

    public static void showTip(Context context, String str) {
        if (shorttoast == null) {
            shorttoast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        } else {
            shorttoast.setText(str);
        }
        shorttoast.show();
    }

    public static void showTip(Context context, int strid) {
        if (shorttoast == null) {
            shorttoast = Toast.makeText(context, context.getResources().getString(strid), Toast.LENGTH_SHORT);
        } else {
            shorttoast.setText(context.getResources().getString(strid));
        }
        shorttoast.show();
    }

    public static void showLongTip(Context context, int strid) {
        if (longtoast == null) {
            longtoast = Toast.makeText(context, context.getResources().getString(strid), Toast.LENGTH_LONG);
        } else {
            longtoast.setText(context.getResources().getString(strid));
        }
        longtoast.show();
    }

    public static void showLongTip(Context context, String str) {
        if (longtoast == null) {
            longtoast = Toast.makeText(context, str, Toast.LENGTH_LONG);
        } else {
            longtoast.setText(str);
        }
        longtoast.show();
    }

    /**
     * 根据绑定了Adapter的ListView，里面item的数量，计算出ListView 的 高度(动态)
     *
     * @param listView
     * @return
     */
    public static int getListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (null == listAdapter) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem != null) {
                listItem.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
                listItem.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            }
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度

        }
        int divilyheight = listView.getDividerHeight() * (listAdapter.getCount() - 1);
        return totalHeight + divilyheight;
    }

    /**
     * 根据绑定了Adapter的GridView,列数，计算出GridView 的 高度(动态)
     *
     * @param gridView
     * @param col
     * @return
     */
    public static int getGridViewHeightBasedOnChildren(GridView gridView, int col) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (null == listAdapter) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < Math.ceil(listAdapter.getCount() / col); i++) {
            View listItem = listAdapter.getView(i, null, gridView);
            if (listItem != null) {
                listItem.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
                listItem.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            }
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度

        }
        int divilyheight = (int) (gridView.getVerticalSpacing() * ((Math.ceil(listAdapter.getCount() / col)) - 1));
        return totalHeight + divilyheight;
    }

    /**
     * 在4.2.2以下系统的 RelativeLayout有BUG
     *
     * @param view 此view 避免使用RelativeLayout布局
     * @return
     */
    public static int getViewMeasureHeightMethod1(final View view) {
        if (view == null)
            return 0;

        int w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredHeight();
    }

    /**
     * 在4.2.2以下系统的 RelativeLayout有BUG
     *
     * @param view 此view 避免使用RelativeLayout布局
     *             注：在小米盒子上测量的值偏小~50%
     * @return
     */
    public static int getViewMeasurewidth(View view) {
        if (view == null)
            return 0;
        int w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredWidth();
    }

    /**
     * 在4.2.2以下系统的 RelativeLayout有BUG
     *
     * @param view 此view 避免使用RelativeLayout布局
     *             注：在小米盒子上测量的值偏小~50%
     * @return
     */
    public static Point getViewMeasureSize(View view) {
        if (view == null)
            return null;
        int w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        Point point = new Point(view.getMeasuredWidth(), view.getMeasuredHeight());
        return point;
    }

    static int height;
    static int width;

    /**
     * 异步方式，适用性比较差，暂无使用
     *
     * @param view
     * @return
     */
    public static int getViewMeasureHeightMethod2(final View view) {
        if (view == null)
            return 0;
        height = 0;
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnPreDrawListener(new OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                height = view.getMeasuredHeight();
                width = view.getMeasuredWidth();
                return false;
            }
        });
        return height;
    }

    /**
     * 测量View
     *
     * @param view
     */
    public static void measureView(View view) {
        if (view == null)
            return;
        int w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
    }

    public static long getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    // 09:50 10:10
    public static long getAppointTime(String timeStr) {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getCurrentTimeStr() {
        return getCurrentTimeStr("yyyy-MM-dd HH:mm:ss");
    }

    @SuppressLint("SimpleDateFormat")
    public static String getCurrentTimeStr(String pattern) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(currentTime);
    }

    public static int getcurrentHour() {
        Locale locale = new Locale("CHINA");
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        Calendar c1 = Calendar.getInstance(locale);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    public static int getcurrentMinu() {
        Locale locale = new Locale("CHINA");
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        Calendar c1 = Calendar.getInstance(locale);
        return c.get(Calendar.MINUTE);
    }

    public static int getcurretnMunute() {
        Locale locale = new Locale("CHINA");
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        Calendar c1 = Calendar.getInstance(locale);
        return c.get(Calendar.MINUTE);
    }

    public static boolean adjustPlaying(String startTime, String duration) {
        try {
            int starthour = Integer.parseInt(startTime.split(":")[0]);
            int startmimu = Integer.parseInt(startTime.split(":")[1]);
            int currenthour = CommonUtils.getcurrentHour();
            int currentMiu = CommonUtils.getcurrentMinu();
            int currentSecond = (currenthour * 60 + currentMiu) * 60;
            int startSecond = (starthour * 60 + startmimu) * 60;
            int dura = Integer.parseInt(duration);

            return currentSecond - startSecond > 0 && currentSecond - startSecond < dura;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * 获取statusbar高度
     *
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        int height = getAppAreaWithoutStatus(activity).top;
        XLog.d("test3", "获取statusbar高度 = " + height);
        return height;
    }

    /**
     * 反射方式获取手机状态栏的高度
     *
     * @param activity
     * @return
     */
    public static int getstatusBarHeight(Activity activity) {
        // Rect frame = new Rect();
        // activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        // return frame.top;

        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = activity.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sbar;

    }

    /**
     * 屏幕区域,暂无效
     *
     * @param activity
     * @return
     */
    public static Point getFullScreenArea(Activity activity) {
        Point point = new Point();
        Display disp = activity.getWindowManager().getDefaultDisplay();
        Point outP = new Point();
        disp.getSize(outP);
        return point;
    }

    /**
     * 应用区域
     *
     * @param activity
     * @return
     */
    public static Rect getAppAreaWithoutStatus(Activity activity) {
        Rect outRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        // System.out.println("top:" + outRect.top + " ; left: " +
        // outRect.left);
        // dimen.mWidth = outRect.width();
        // dimen.mHeight = outRect.height();
        return outRect;
    }

    /**
     * View绘制区域
     *
     * @param activity
     * @return
     */
    public static Rect getAppViewWithoutTitleArea(Activity activity) {
        // 用户绘制区域
        Rect outRect = new Rect();
        activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(outRect);

        // end
        return outRect;
    }

    /**
     * 输入框震动并给出提示
     *
     * @param context
     * @param view
     * @param stringid
     */
    public static void sharkingAndShowTip(Context context, View view, int stringid) {
        if (context == null)
            return;
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.input_shake);
        view.requestFocus();
        view.startAnimation(anim);
        showTip(context, stringid);
    }

    /**
     * 输入框震动并给出提示
     *
     * @param context
     * @param view
     * @param string
     */
    public static void sharkingAndShowTip(Context context, View view, String string) {
        if (context == null)
            return;
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.input_shake);
        view.requestFocus();
        view.startAnimation(anim);
        showTip(context, string);
    }

    /**
     * ******************************************
     *
     * @param context *******************************************
     * @fun 隐藏软键盘
     */
    public static void HideSoftInput(Context context) {
        if (context == null)
            return;
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();// isOpen若返回true，则表示输入法打开
        if (isOpen) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void HideSoftInput(EditText editText) {
        if (editText == null)
            return;
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 隐藏键盘
     *
     * @param activity
     */
    public static void HideSoftInput(Activity activity) {
        if (activity == null)
            return;
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    /**
     * 隐藏键盘,以SOFT_INPUT_STATE_ALWAYS_HIDDEN模式,可以针对顽固不隐藏键盘的情况使用必然有效
     *
     * @param activity
     */
    public static void HideSoftInputAlWays(Activity activity) {
        if (activity == null)
            return;
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * 隐藏键盘
     *
     * @param context
     * @param view
     */
    public static void HideSoftInput(Context context, EditText view) {
        if (context == null || view == null)
            return;
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * ******************************************
     *
     * @param context *******************************************
     * @fun 显示软键盘
     */
    public static void ShowSoftInput(Context context) {
        if (context == null)
            return;
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();// isOpen若返回true，则表示输入法打开
        if (!isOpen) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * ******************************************
     *
     * @param editText *******************************************
     * @fun 显示软键盘
     */
    public static void ShowSoftInput(EditText editText) {
        if (editText == null)
            return;
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private static int[] MutexRad = {0, 0, 0, 0, 0, 0};
    // {0,1,2,3 }
    private static int invokeCount = 0;

    /**
     * 获取随机颜色,用于贺卡
     *
     * @return
     */
    public static String getRadomColor() {
        invokeCount++;
        String[] colors = {"#F6BE29", "#EA76A7", "#6DCFEA", "#E66162", "#BD7EE7", "#7EC84F", "#668CFF", "#FF1AFF", "#fd22c2", "#6a93fc", "#6afcdf",
                "#d6fc01", "#fc7101", "#9273ea"};
        int rad = (int) (Math.random() * colors.length);

        for (int i = 0; i < MutexRad.length; i++) {
            if (rad == MutexRad[i]) {
                rad = (int) (Math.random() * colors.length);
                i = -1;
            }
        }

        MutexRad[invokeCount % MutexRad.length] = rad;
        XLog.d("color", "color MutexRad[" + (invokeCount % MutexRad.length) + "] = " + MutexRad[invokeCount % MutexRad.length]);
        if (invokeCount % MutexRad.length == 0) {
            StringBuffer log = new StringBuffer("MutexRad{ ");
            for (int i = 0; i < MutexRad.length; i++)
                if (i == MutexRad.length - 1)
                    log.append(MutexRad[i] + "}");
                else
                    log.append(MutexRad[i] + ",");

            XLog.d("color", log.toString());
        }
        return colors[rad];
    }

    /**
     * 检测某Activity是否在当前Task的栈顶
     *
     * @param context
     * @param activity
     * @return
     */
    public static boolean isTopActivy(Context context, String activity) {
        XLog.d("PullService", "activity = " + activity);
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        String topActivity = null;
        if (null != runningTaskInfos) {
            topActivity = runningTaskInfos.get(0).topActivity.getClassName().toString();
            XLog.d("PullService", "topActivity = " + topActivity);
        }
        if (null == topActivity)
            return false;
        return topActivity.equals(activity);

    }

    /**
     * 获取栈中第二个Activity名称,有可能为空
     *
     * @param context
     * @return
     */
    public static String getNextActivityName(Context context) throws Exception {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTasks = manager.getRunningTasks(1);
        RunningTaskInfo cinfo = runningTasks.get(0);
        ComponentName component = cinfo.baseActivity;
        XLog.d("PullService", "component.getClassName() = " + component.getClassName());
        return component.getClassName();
    }

}
