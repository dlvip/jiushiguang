package com.old.time.activitys;

import android.view.View;

import com.old.time.R;

public class SettingActivity extends BaseActivity {

    @Override
    protected void initView() {
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        setTitleText("设置");

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_setting;
    }
}
