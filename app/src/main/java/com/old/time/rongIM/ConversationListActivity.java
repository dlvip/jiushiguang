package com.old.time.rongIM;

import android.view.View;

import com.old.time.R;
import com.old.time.activitys.BaseActivity;

public class ConversationListActivity extends BaseActivity {

    @Override
    protected void initView() {
        setTitleText("回话列表");
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_conversation_list;
    }
}
