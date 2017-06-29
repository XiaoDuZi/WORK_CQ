package com.cq.ln.utils;

/**
 * Created by Administrator on 2016/4/1.
 */
public class StrTool {

    public static int parsetNum(String str){
        try{
            return Integer.valueOf(str);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    //int时间转换为视频播放进度 01:20:30这种   毫秒单位
    public static String generateTime(long time) {
        int totalSeconds = (int) (time/1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
    }

}
