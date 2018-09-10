package com.old.time.activitys;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.old.time.R;
import com.old.time.beans.CourseBean;
import com.old.time.permission.PermissionUtil;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.DebugLog;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MusicActivity extends BaseActivity {

    /**
     * 课程详情
     *
     * @param mContext
     * @param mCourseBean
     */
    public static void startMusicActivity(Context mContext, CourseBean mCourseBean) {
        if (!PermissionUtil.checkAndRequestPermissionsInActivity((Activity) mContext//
                , WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)) {

            return;
        }
        Intent intent = new Intent(mContext, MusicActivity.class);
        intent.putExtra("mCourseBean", mCourseBean);
        ActivityUtils.startLoginActivity((Activity) mContext, intent);

    }

    @Override
    protected void initView() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.old.time.mp3Utils", "com.old.time.mp3Utils.MusicService"));
        bindService(intent, conn, Context.BIND_AUTO_CREATE);

    }

    /**
     * 服务绑定状态回调
     */
    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DebugLog.d(TAG, "服务绑定成功：onServiceConnected");

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            DebugLog.d(TAG, "服务解绑定成功：onServiceDisconnected");

        }
    };

    @Override
    protected int getLayoutID() {

        return R.layout.activity_music_play;
    }
}
