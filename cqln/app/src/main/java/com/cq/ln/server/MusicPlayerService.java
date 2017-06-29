package com.cq.ln.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import ipaneltv.toolkit.fragment.PlayActivityInterface;


/**
 * 播放音乐服务,尚未使用
 */
public class MusicPlayerService extends Service implements PlayActivityInterface.VodPlayBaseInterface.Callback {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onContextReady(String s) {

    }

    @Override
    public void onVodDuration(long l) {

    }

    @Override
    public void onSeeBackPeriod(long l, long l1) {

    }

    @Override
    public void onPlayStart(boolean b) {

    }

    @Override
    public void onSourceRate(float v) {

    }

    @Override
    public void onSourceSeek(long l) {

    }

    @Override
    public void onSyncMediaTime(long l) {

    }

    @Override
    public void onPlayEnd(float v) {

    }

    @Override
    public void onPlayMsg(String s) {

    }

    @Override
    public void onPlayError(String s) {

    }
}
