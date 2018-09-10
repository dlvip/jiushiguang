package com.old.time.interfaces;

/**
 * Created by DuanJiaNing on 2017/7/15.
 */

public interface OnUpdateStatusChanged {

    void onCompleted();

    void onStart();

    void onError(Throwable e);
}
