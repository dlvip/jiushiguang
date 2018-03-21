package com.old.time.activitys;

import android.view.View;

import com.old.time.R;

public class CreateDressActivity extends BaseActivity {

    @Override
    protected void initView() {
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        setTitleText("添加地址");

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_creat_dress;
    }
}
