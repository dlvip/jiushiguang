package com.old.time.okhttps.subscriber;

import android.content.Context;

import com.old.time.okhttps.BaseSubscriber;
import com.old.time.okhttps.exception.ApiException;
import com.old.time.utils.DebugLog;
import com.old.time.utils.NetworkUtil;

/**
 * Created by gaosheng on 2016/11/6.
 * 22:42
 * com.example.gaosheng.myapplication.subscriber
 */

public abstract class CommonSubscriber<T> extends BaseSubscriber<T> {

    private Context context;

    public CommonSubscriber(Context context) {
        this.context = context;
    }

    private static final String TAG = "CommonSubscriber";

    @Override
    public void onStart() {
        if (!NetworkUtil.isNetworkAvailable(context)) {
            DebugLog.e(TAG, "网络不可用");

        } else {
            DebugLog.e(TAG, "网络可用");

        }
    }

    @Override
    protected void onError(ApiException e) {
        DebugLog.e(TAG, "错误信息为 " + "code:" + e.code + "   message:" + e.message);

    }

    @Override
    public void onCompleted() {
        DebugLog.e(TAG, "成功了");

    }

}
