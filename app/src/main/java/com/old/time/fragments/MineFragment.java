package com.old.time.fragments;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.activitys.PicsManageActivity;
import com.old.time.activitys.SettingActivity;
import com.old.time.activitys.UserDressActivity;
import com.old.time.activitys.UserMesgActivity;
import com.old.time.activitys.WebViewActivity;
import com.old.time.utils.ActivityUtils;

/**
 * Created by NING on 2018/3/5.
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {

    @Override
    public void getDataFromNet(boolean isRefresh) {

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void lazyLoad() {
        TextView top_title = findViewById(R.id.top_title);
        top_title.setText(getString(R.string.main_tab_mine));

        findViewById(R.id.relative_layout_header).setOnClickListener(this);
        findViewById(R.id.relative_layout_message).setOnClickListener(this);
        findViewById(R.id.relative_layout_setting).setOnClickListener(this);
        findViewById(R.id.relative_layout_dress).setOnClickListener(this);
        findViewById(R.id.relative_layout_pics).setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.relative_layout_header:
                intent = new Intent(mContext, UserMesgActivity.class);

                break;
            case R.id.relative_layout_message:
                intent = new Intent(mContext, WebViewActivity.class);

                break;
            case R.id.relative_layout_pics:
                intent = new Intent(mContext, PicsManageActivity.class);

                break;
            case R.id.relative_layout_dress:
                intent = new Intent(mContext,UserDressActivity.class);

                break;
            case R.id.relative_layout_setting:
                intent = new Intent(mContext, SettingActivity.class);

                break;
        }
        if (intent != null) {
            ActivityUtils.startActivity((Activity) mContext, intent);

        }
    }
}
