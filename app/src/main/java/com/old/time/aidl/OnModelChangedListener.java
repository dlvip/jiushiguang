package com.old.time.aidl;

import android.os.IBinder;

/**
 * Created by wcl on 2018/9/12.
 */

/**
 * 服务回调  在非主线程
 */
public abstract class OnModelChangedListener extends IOnModelChangedListener.Stub {


    private static final String TAG = "OnModelChangedListener";

    @Override
    public IBinder asBinder() {
        return super.asBinder();
    }

    public abstract void updatePlayModel(ChapterBean mChapterBean);

    public abstract void updateProgress(int progress, int total);

    public abstract void updateIsPlaying(boolean isPlaying);

    public abstract void updateError();
}
