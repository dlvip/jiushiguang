package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.old.time.permission.PermissionUtil;
import com.old.time.utils.ActivityUtils;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class DownLoadActivity extends CBaseActivity {

    public static void startDownLoadActivity(Context mContext) {
        if (!PermissionUtil.checkAndRequestPermissionsInActivity((Activity) mContext, WRITE_EXTERNAL_STORAGE)) {

            return;
        }

        Intent intent = new Intent(mContext, DownLoadActivity.class);
        ActivityUtils.startActivity((Activity) mContext, intent);

    }

    @Override
    public void getDataFromNet(boolean isRefresh) {

    }
}
