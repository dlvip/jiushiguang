package com.old.time.activitys;

import android.content.Intent;
import android.view.View;

import com.old.time.Constant;
import com.old.time.R;
import com.old.time.utils.ActivityUtils;

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
                Intent intent = new Intent(mContext, CamerTakeActivity.class);
                ActivityUtils.startActivityForResult(mContext, intent, Constant.REQUEST_CODE_30);


                break;
        }
    }
}
