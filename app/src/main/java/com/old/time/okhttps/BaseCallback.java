package com.old.time.okhttps;

import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

/**
 * Created by wcl on 2018/7/17.
 */

public abstract  class BaseCallback<T> implements Callback<T> {

    @Override
    public void onStart(Request request) {

    }

    @Override
    public void onCacheSuccess(Response response) {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void uploadProgress(Progress progress) {

    }

    @Override
    public void downloadProgress(Progress progress) {

    }
}
