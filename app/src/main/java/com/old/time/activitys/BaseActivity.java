package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.dialogs.DialogPromptCentre;
import com.old.time.interfaces.OnClickViewCallBack;
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

    private TextView tv_title;

    /**
     * 设置title
     *
     * @param text
     */
    public void setTitleText(int text) {
        if (tv_title == null) {
            tv_title = findViewById(R.id.top_title);

        }
        if (tv_title != null) tv_title.setText(text);
    }

    /**
     * 设置title
     *
     * @param text
     */
    public void setTitleText(String text) {
        if (tv_title == null) {
            tv_title = findViewById(R.id.top_title);

        }
        if (tv_title != null) tv_title.setText(text);

    }

    private DialogPromptCentre mDialogPromptCentre;

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionUtil.onPermissionResult(this, permissions, grantResults, new PermissionUtil.PermissionCallBack() {
//            @Override
//            public void onSuccess() {
//                UIHelper.ToastMessage(mContext, getString(R.string.permissions_apply_success));
//
//            }
//
//            @Override
//            public void onShouldShow() {
//
//            }
//
//            @Override
//            public void onFailed() {
//                showDialogPermission();
//                UIHelper.ToastMessage(mContext, getString(R.string.permissions_apply_fail));
//
//            }
//        });
//    }

    /**
     * 申请权限的弹框
     */
    protected void showDialogPermission() {
        if (mDialogPromptCentre == null) {
            mDialogPromptCentre = new DialogPromptCentre(mContext, new OnClickViewCallBack() {
                @Override
                public void onClickTrueView() {
                    Intent localIntent = new Intent();
                    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (Build.VERSION.SDK_INT >= 9) {
                        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        localIntent.setData(Uri.fromParts("package", getPackageName(), null));

                    } else if (Build.VERSION.SDK_INT <= 8) {
                        localIntent.setAction(Intent.ACTION_VIEW);
                        localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                        localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());

                    }
                    ActivityUtils.startActivity(mContext, localIntent);

                }

                @Override
                public void onClickCancelView() {

                }
            });
        }
        mDialogPromptCentre.showDialog("申请权限失败，请手动开启相关权限");
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

    public void save(View view) {

    }

    public void setOrientation(int orientation) {
        setRequestedOrientation(orientation);

    }

    protected abstract void initView();

    protected abstract @LayoutRes
    int getLayoutID();

}
