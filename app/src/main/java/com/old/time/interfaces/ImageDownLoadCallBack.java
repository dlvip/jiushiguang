package com.old.time.interfaces;

import android.graphics.Bitmap;

/**
 * Created by diliang on 2016/12/30.
 */
public interface ImageDownLoadCallBack {
    void onDownLoadSuccess(Bitmap bitmap);

    void onDownLoadFailed();
}
