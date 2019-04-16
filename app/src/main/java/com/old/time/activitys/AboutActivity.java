package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.old.time.R;
import com.old.time.utils.ActivityUtils;

public class AboutActivity extends BaseActivity {

    public static void startAboutActivity(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        ActivityUtils.startActivity((Activity) context, intent);

    }

    @Override
    protected void initView() {
        setTitleText("联系我们");

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_about;
    }
}
