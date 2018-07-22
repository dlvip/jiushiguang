package com.old.time.okhttps;

import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.old.time.utils.DebugLog;

/**
 * Created by wcl on 2018/7/17.
 */

public abstract class BaseCallback<T> implements Callback<T> {

    private static final String TAG = BaseCallback.class.getName();

    @Override
    public void onStart(Request request) {
        DebugLog.d(TAG, "onStart");
    }

    @Override
    public void onCacheSuccess(Response response) {
        DebugLog.d(TAG, "onCacheSuccess");
        onSuccess(response);
    }

    @Override
    public void onFinish() {
        DebugLog.d(TAG, "onFinish");
    }

    @Override
    public void uploadProgress(Progress progress) {
        DebugLog.d(TAG, "uploadProgress");
    }

    @Override
    public void downloadProgress(Progress progress) {
        DebugLog.d(TAG, "downloadProgress");
    }
}
