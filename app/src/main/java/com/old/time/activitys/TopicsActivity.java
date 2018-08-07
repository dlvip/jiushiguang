package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.old.time.R;
import com.old.time.adapters.TopicAdapter;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.RecyclerItemDecoration;

public class TopicsActivity extends CBaseActivity {

    /**
     * 话题列表
     *
     * @param mContext
     */
    public static void startTopicsActivity(Activity mContext) {
        Intent intent = new Intent(mContext, TopicsActivity.class);
        ActivityUtils.startActivity(mContext, intent);

    }

    private TopicAdapter mTopicAdapter;

    @Override
    protected void initView() {
        super.initView();
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        mTopicAdapter = new TopicAdapter(strings);
        mRecyclerView.setAdapter(mTopicAdapter);

    }

    @Override
    public void getDataFromNet(boolean isRefresh) {
        mSwipeRefreshLayout.setRefreshing(false);

    }
}
