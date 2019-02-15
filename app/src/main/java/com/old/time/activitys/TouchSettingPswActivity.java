package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.old.time.R;
import com.old.time.permission.PermissionUtil;
import com.old.time.postcard.PostCardActivity;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.PreferenceCache;
import com.old.time.views.ChaosGestureView;

import java.util.List;

public class TouchSettingPswActivity extends BaseActivity implements ChaosGestureView.GestureCallBack {

    /**
     * 设置手势密码
     *
     * @param context
     */
    public static void startTouchSettingPswActivity(Context context) {
        Intent intent = new Intent(context, TouchSettingPswActivity.class);
        ActivityUtils.startActivityForResult((Activity) context, intent, 1);
        ActivityUtils.finishActivity((Activity) context);

    }

    private ChaosGestureView gestureView;
    private int jumpFlg;
    private int flag;

    @Override
    protected void initView() {
        setTitleText("手势密码");
        jumpFlg = getIntent().getIntExtra("jumpFlg", 0);
        flag = getIntent().getIntExtra("flag", 0);
        gestureView = findViewById(R.id.gesture);
        gestureView.setGestureCallBack(this);
        //不调用这个方法会造成第二次启动程序直接进入手势识别而不是手势设置
        gestureView.clearCache();

    }

    @Override
    public void gestureVerifySuccessListener(int stateFlag, List<ChaosGestureView.GestureBean> data, boolean success) {
        if (stateFlag == ChaosGestureView.STATE_LOGIN) {
            PreferenceCache.putGestureFlag(true);
            PostCardActivity.startPostCardActivity(mContext);

        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_setting_pattern_psw;
    }
}
