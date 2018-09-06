package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.old.time.R;
import com.old.time.beans.CourseBean;
import com.old.time.permission.PermissionUtil;
import com.old.time.utils.ActivityUtils;

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

    }

    @Override
    protected int getLayoutID() {

        return R.layout.activity_music_play;
    }
}
