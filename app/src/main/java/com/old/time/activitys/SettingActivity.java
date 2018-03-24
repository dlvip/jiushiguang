package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.old.time.R;
import com.old.time.utils.ActivityUtils;

public class SettingActivity extends BaseActivity {

    public static void startSettingActivity(Activity mContext) {
        Intent intent = new Intent(mContext, SettingActivity.class);
        ActivityUtils.startActivity(mContext, intent);
    }

    @Override
    protected void initView() {
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        setTitleText("设置");

    }

    @Override
    protected void initEvent() {
        super.initEvent();
        findViewById(R.id.tv_user_logout).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_user_logout:
                UserLoginActivity.startUserLoginActivity(mContext);

                break;
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_setting;
    }
}
