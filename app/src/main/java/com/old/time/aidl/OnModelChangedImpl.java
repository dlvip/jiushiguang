package com.old.time.aidl;

import android.os.IBinder;
import android.os.RemoteException;

/**
 * Created by NING on 2018/9/14.
 */

public class OnModelChangedImpl extends IOnModelChangedListener.Stub  {

    @Override
    public IBinder asBinder() {
        return super.asBinder();
    }

    @Override
    public void updatePlayModel(ChapterBean mChapterBean, boolean isPlaying) throws RemoteException {

    }

    @Override
    public void updateProgress(int progress, int total) throws RemoteException {

    }

    @Override
    public void updateError() throws RemoteException {

    }

    @Override
    public void close() throws RemoteException {

    }
}
