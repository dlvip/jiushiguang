package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.old.time.dialogs.DialogPromptCentre;
import com.old.time.interfaces.OnClickViewCallBack;
import com.old.time.permission.PermissionUtil;
import com.old.time.utils.ActivityUtils;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by NING on 2019/2/23.
 */
public abstract class PermissionActivity extends AppCompatActivity implements PermissionUtil.PermissionCallBack {

    public Activity mContext;
    public String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.TAG = getClass().getName();
        this.mContext = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        loadView();

    }

    /**
     * 初始化页面
     */
    protected abstract void loadView();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onPermissionResult(this, permissions, grantResults, this);

    }

    private DialogPromptCentre mDialogPromptCentre;

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
                    localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    localIntent.setData(Uri.fromParts("package", getPackageName(), null));
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
     * 权限
     *
     * @return
     */
    protected String[] getNeedPermissions() {

        return new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};
    }

    @Override
    public void onSuccess() {
        loadView();

    }

    @Override
    public void onShouldShow() {

    }

    @Override
    public void onFailed() {
        showDialogPermission();

    }
}
