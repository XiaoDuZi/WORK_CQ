package com.cq.ln.utils;

/**
 * Created by Godfather on 16/4/23.
 * 遥控器按键控制
 */
public class KeyActionControlUtils {

    private static final long INTERVAL = 300;

    public enum MyPadKey {
        Left, Up, Right, Down, Enter
    }


    private static long lastPadLeftClickTime;// 记录最后一次点击时间
    private static long lastPadUpClickTime;// 记录最后一次点击时间
    private static long lastPadRightClickTime;// 记录最后一次点击时间
    private static long lastPadDownClickTime;// 记录最后一次点击时间
    private static long lastPadEnterClickTime;// 记录最后一次点击时间


    /**
     * 防止快速点击,间隔300毫秒
     *
     * @param direction 方向键
     * @return
     */
    public static boolean isFastClick(MyPadKey direction) {
        long lasttime = 0;
        switch (direction) {
            case Left:
                lasttime = lastPadLeftClickTime;
                break;
            case Up:
                lasttime = lastPadUpClickTime;
                break;
            case Right:
                lasttime = lastPadRightClickTime;
                break;
            case Down:
                lasttime = lastPadDownClickTime;
                break;
            case Enter:
                lasttime = lastPadEnterClickTime;
                break;
        }

        long time = System.currentTimeMillis();
        long timeD = time - lasttime;
        if (timeD > 0 && timeD < INTERVAL) {
            return true;
        }

        switch (direction) {
            case Left:
                lastPadLeftClickTime = time;
                break;
            case Up:
                lastPadUpClickTime = time;
                break;
            case Right:
                lastPadRightClickTime = time;
                break;
            case Down:
                lastPadDownClickTime = time;
                break;
            case Enter:
                lastPadEnterClickTime = time;
                break;
        }
        return false;
    }


}
