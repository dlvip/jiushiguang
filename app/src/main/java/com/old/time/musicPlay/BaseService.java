package com.old.time.musicPlay;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.dueeeke.videoplayer.listener.PlayerEventListener;
import com.old.time.receivers.MusicBroadReceiver;
import com.old.time.utils.DebugLog;

/**
 * Created by wcl on 2018/9/9.
 */

public class BaseService extends Service implements PlayerEventListener, MusicBroadReceiver.MusicPlayCallBackListener {

    private static final String TAG = "BaseService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onError() {

    }

    @Override
    public void onCompletion() {

    }

    @Override
    public void start(int position) {

    }

    @Override
    public void play() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void previous() {

    }

    @Override
    public void next() {

    }

    @Override
    public void close() {

    }

    @Override
    public void speed(float speed) {

    }

    @Override
    public void progress(int current, int total) {
        DebugLog.d(TAG, "progress");

    }

    /**
     * 准备完成
     */
    @Override
    public void onPrepared() {
        DebugLog.d(TAG, "onPrepared");

    }

    @Override
    public void onInfo(int what, int extra) {


    }


    @Override
    public void onVideoSizeChanged(int width, int height) {


    }
}
