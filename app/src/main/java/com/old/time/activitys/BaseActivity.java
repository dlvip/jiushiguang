package com.old.time.activitys;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.permission.PermissionUtil;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.UIHelper;

/**
 * Created by NING on 2018/2/23.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    public Activity mContext;
    public String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.TAG = getClass().getName();
        this.mContext = this;
        setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getLayoutID());
        initView();

        initEvent();

    }

    /**
     * 设置title
     *
     * @param text
     */
    public void setTitleText(int text) {
        TextView tv_title = findViewById(R.id.top_title);
        if (tv_title != null)
            tv_title.setText(text);
    }

    /**
     * 设置title
     *
     * @param text
     */
    public void setTitleText(String text) {
        TextView tv_title = (TextView) findViewById(R.id.top_title);
        if (tv_title != null)
            tv_title.setText(text);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onPermissionResult(this, permissions, grantResults, new PermissionUtil.PermissionCallBack() {
            @Override
            public void onSuccess() {
                UIHelper.ToastMessage(mContext, getString(R.string.permissions_apply_success));
            }

            @Override
            public void onShouldShow() {

            }

            @Override
            public void onFailed() {
                UIHelper.ToastMessage(mContext, getString(R.string.permissions_apply_fail));

            }
        });
    }

    /**
     * 获得状态栏高度
     */
    public int getStatusBarHeight() {
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");

        return this.getResources().getDimensionPixelSize(resourceId);
    }

    @Override
    public void onClick(View view) {


    }

    protected void initEvent() {


    }

    public void back(View view) {
        ActivityUtils.finishActivity(this);

    }

    public void setOrientation(int orientation) {
        setRequestedOrientation(orientation);

    }

    protected abstract void initView();

    protected abstract @LayoutRes
    int getLayoutID();

}
