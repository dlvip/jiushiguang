package com.old.time.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.old.time.aidl.PlayServiceIBinder;

/**
 * Created by NING on 2018/9/12.
 */

public class PlayMusicService extends Service {

    private static final String TAG = "PlayMusicService";

    /**
     * 接口通道
     */
    private PlayServiceIBinder mPlayServiceIBinder;
    public static final String SERVICE_BUNDLE_EXTRA = "service_bundle_extra";
    public static final String SERVICE_MODEL_LIST = "service_model_list";
    public static final String SERVICE_PLAY_INDEX = "service_play_index";

    @Override
    public void onCreate() {
        mPlayServiceIBinder = new PlayServiceIBinder(this);

        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {

        return mPlayServiceIBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mPlayServiceIBinder.close();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
