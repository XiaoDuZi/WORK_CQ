package com.cq.ln.utils;

import android.content.Context;
import android.media.AudioManager;

/**
 * Created by godfather on 2016/4/8.
 * 声音管理，设置系统声音为静音，达到禁止focus 等等操作出现声音的情况，
 * TODO 待在重网机顶盒测试会不会影响到其他应用的情况
 */
public class MyAudioManagerUtils {
    Context context;
    static MyAudioManagerUtils mMyAudioManagerUtils;
    static AudioManager mAudioManager;
    private int currentSystemVoice;

    public MyAudioManagerUtils(Context context) {
        this.context = context;
    }

    public static MyAudioManagerUtils getInstance(Context context) {
        if (mMyAudioManagerUtils == null)
            mMyAudioManagerUtils = new MyAudioManagerUtils(context);
        mMyAudioManagerUtils.getmAudioManager();
        return mMyAudioManagerUtils;
    }

    private AudioManager getmAudioManager() {
        if (mAudioManager == null)
            mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int result = mAudioManager.requestAudioFocus(new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_GAIN:
                        forbidSystemVoice();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS://暂不恢复
//                        resetSystemVoice();
                        break;
                }
            }
        }, AudioManager.STREAM_SYSTEM, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            XLog.d("申请成功！");
        } else {
            XLog.d("申请失败！");
        }
        return mAudioManager;
    }

    /**
     * 恢复系统声音
     */
    public void resetSystemVoice() {
        if (mAudioManager != null) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 80, 0);
            XLog.d("恢复系统声音");
        }
    }

    /**
     * 禁止系统声音，将声音关闭
     */
    public void forbidSystemVoice() {
        if (mAudioManager != null) {
            currentSystemVoice = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
            mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, 0);
            XLog.d("禁止了系统声音");
        }


//        //通话音量
//        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
//        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
//        //系统音量
//        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
//        current = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
//        //铃声音量
//        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
//        current = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
//        //音乐音量
//        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//        current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//        //提示声音音量
//        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
//        current = mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);

    }

}
