package com.old.time.activitys;

import android.view.View;

import com.old.time.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void initView() {
        findViewById(R.id.relative_layout_take).setOnClickListener(this);

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.relative_layout_take:
                CamerTakeActivity.startCamerActivity(mContext);

                break;
        }
    }
}
