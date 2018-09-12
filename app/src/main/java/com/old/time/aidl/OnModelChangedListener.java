package com.old.time.aidl;

import android.os.IBinder;

/**
 * Created by wcl on 2018/9/12.
 */

public abstract class OnModelChangedListener extends IOnModelChangedListener.Stub {

    @Override
    public IBinder asBinder() {
        return super.asBinder();
    }
}
