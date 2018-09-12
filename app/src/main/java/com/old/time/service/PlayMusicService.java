package com.old.time.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.old.time.aidl.ChapterBean;
import com.old.time.aidl.PlayServiceIBinder;
import com.old.time.utils.DebugLog;

import java.util.List;

/**
 * Created by NING on 2018/9/12.
 */

public class PlayMusicService extends Service {

    private static final String TAG = "PlayMusicService";

    private PlayServiceIBinder mPlayServiceIBinder;
    public static final String SERVICE_BUNDLE_EXTRA = "service_bundle_extra";
    public static final String SERVICE_MODEL_LIST = "service_model_list";
    public static final String SERVICE_PLAY_INDEX = "service_play_index";

    @Override
    public void onCreate() {
        mPlayServiceIBinder = new PlayServiceIBinder(this);

        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        try {
            if (intent != null) {
                Bundle bundle = intent.getBundleExtra(SERVICE_BUNDLE_EXTRA);
                if (bundle != null) {
                    List<ChapterBean> chapterBeans = bundle.getParcelableArrayList(SERVICE_MODEL_LIST);
                    int position = bundle.getInt(SERVICE_PLAY_INDEX, 0);
                    mPlayServiceIBinder.setPlayList(chapterBeans, position);

                }
            }
        } catch (RemoteException e) {
            DebugLog.d(TAG, e.getMessage());

        }

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
