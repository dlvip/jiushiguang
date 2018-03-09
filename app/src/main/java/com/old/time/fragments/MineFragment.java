package com.old.time.fragments;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.activitys.SettingActivity;
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

        findViewById(R.id.relative_layout_setting).setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relative_layout_setting:
                Intent intent = new Intent(mContext, SettingActivity.class);
                ActivityUtils.startActivity((Activity) mContext, intent);

                break;
        }
    }
}
