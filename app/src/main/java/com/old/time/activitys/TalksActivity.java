package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.old.time.R;
import com.old.time.adapters.TalkAdapter;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.RecyclerItemDecoration;

public class TalksActivity extends CBaseActivity {

    public static void startTalksActivity(Context mContext) {
        Intent intent = new Intent(mContext, TalksActivity.class);
        ActivityUtils.startActivity((Activity) mContext, intent);

    }

    private TalkAdapter mTalkAdapter;

    @Override
    protected void initView() {
        super.initView();
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        mTalkAdapter = new TalkAdapter(R.layout.adapter_talk, strings);
        mRecyclerView.setAdapter(mTalkAdapter);

    }

    @Override
    public void getDataFromNet(boolean isRefresh) {
        mSwipeRefreshLayout.setRefreshing(false);

    }
}
