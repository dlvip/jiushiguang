package com.old.time.okhttps.subscriber;

import android.content.Context;

import com.old.time.okhttps.BaseSubscriber;
import com.old.time.okhttps.exception.ApiException;
import com.old.time.utils.DebugLog;
import com.old.time.utils.NetworkUtil;
import com.old.time.utils.UIHelper;
import com.old.time.views.ProgressCDialog;

/**
 * Created by gaosheng on 2016/11/6.
 * 22:42
 * com.example.gaosheng.myapplication.subscriber
 */

public abstract class CommonSubscriber<T> extends BaseSubscriber<T> {

    private static final String TAG = "CommonSubscriber";

    private Context context;

    public CommonSubscriber(Context context) {
        this.context = context;

    }

    public CommonSubscriber(Context context, boolean isShowProgress) {
        this.context = context;
        if (mProgressCDialog == null) {
            mProgressCDialog = new ProgressCDialog(context);

        }
    }

    private ProgressCDialog mProgressCDialog;

    @Override
    public void onStart() {
        if (!NetworkUtil.isNetworkAvailable(context)) {
            UIHelper.ToastMessage(context, "网络不可用");

            return;
        }
        if (mProgressCDialog != null) {
            mProgressCDialog.showProgressDialog();

        }
    }

    @Override
    protected void onError(ApiException e) {
        if (mProgressCDialog != null && mProgressCDialog.isShowing()) {
            mProgressCDialog.dismiss();

        }
        UIHelper.ToastMessage(context, e.message);
    }

    @Override
    public void onCompleted() {
        if (mProgressCDialog != null && mProgressCDialog.isShowing()) {
            mProgressCDialog.dismiss();

        }
        DebugLog.e(TAG, "成功了");

    }
}
