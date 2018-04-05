package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.DataCleanManager;
import com.old.time.utils.SpUtils;
import com.old.time.utils.StringUtils;

public class SettingActivity extends BaseActivity {

    public static void startSettingActivity(Activity mContext) {
        Intent intent = new Intent(mContext, SettingActivity.class);
        ActivityUtils.startActivity(mContext, intent);

    }

    private TextView tv_clear_num, tv_app_version;

    @Override
    protected void initView() {
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        setTitleText("设置");

        tv_app_version = findViewById(R.id.tv_app_version);
        tv_app_version.setText("V " + StringUtils.getVersion(this));
        tv_clear_num = findViewById(R.id.tv_clear_num);
        String cacheText = DataCleanManager.getFormatSize(mContext);
        tv_clear_num.setText(cacheText);

    }

    @Override
    protected void initEvent() {
        super.initEvent();
        findViewById(R.id.relative_layout_memory).setOnClickListener(this);
        findViewById(R.id.relative_layout_about).setOnClickListener(this);
        findViewById(R.id.tv_user_logout).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.relative_layout_memory:
                DataCleanManager.deleteFileMemory(mContext);
                tv_clear_num.setText(DataCleanManager.getFormatSize(mContext));

                break;
            case R.id.relative_layout_about:
                WebViewActivity.startWebViewActivity(mContext);

                break;
            case R.id.tv_user_logout:
                UserLoginActivity.startUserLoginActivity(mContext);
                ActivityUtils.finishActivity(mContext);

                break;

        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_setting;
    }
}
