package com.old.time.manager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;

/**
 * Created by DuanJiaNing on 2017/5/25.
 */

public final class PermissionManager {

    private static PermissionManager mInstance;

    private PermissionManager() {
    }

    public static PermissionManager getInstance() {
        if (mInstance == null) {
            mInstance = new PermissionManager();
        }
        return mInstance;
    }

    public static final class PerMap {

        public static final int CATEGORY_MEDIA_READ = 0x1;

        //权限名称
        private String name;

        //说明
        private String des;

        //类别，同时作为 activity onRequestPermissionsResult 方法的 requestCode
        private int category;

        //权限
        private String[] permissions;

        /**
         * 构造要申请的权限
         *
         * @param name        权限的名称（弹出请求权限对话框时将作为标题）
         * @param des         对权限用途的描述
         * @param category    权限所属类别
         * @param permissions
         */
        public PerMap(String name, String des, int category, String[] permissions) {
            this.name = name;
            this.des = des;
            this.category = category;
            this.permissions = permissions;
        }
    }

    //检查权限是否获取
    public boolean checkPermission_(Context context, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int re = ContextCompat.checkSelfPermission(context, permission);
            return re == PackageManager.PERMISSION_GRANTED;
        } else
            return false;
    }

    //检查权限是否获取
    public boolean checkPermission(Context context, String... permission) {

        boolean nr = true;

        for (int i = 0; i < permission.length; i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                nr = context.checkSelfPermission(permission[i])
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                nr = PermissionChecker.checkSelfPermission(context, permission[i])
                        == PermissionChecker.PERMISSION_GRANTED;
            }

            if (!nr) {
                break;
            }
        }
        return nr;
    }
}
