package com.old.time.rongIM;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.old.time.R;
import com.old.time.activitys.BaseActivity;
import com.old.time.utils.ActivityUtils;

public class ConversationListActivity extends BaseActivity {

    /**
     * 会话列表 Activity
     */
    public static void startConversationListActivity(Context context) {
        Intent intent = new Intent(context, ConversationListActivity.class);
        ActivityUtils.startActivity((Activity) context, intent);

    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_conversation_list;
    }
}
