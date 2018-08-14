package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.dueeeke.videoplayer.demo.DataUtil;
import com.old.time.R;
import com.old.time.adapters.VideoFindAdapter;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.MyGridLayoutManager;
import com.old.time.utils.RecyclerItemDecoration;

public class VideosActivity extends CBaseActivity {

    public static void startVideosActivity(Context mContext) {
        Intent intent = new Intent(mContext, VideosActivity.class);
        ActivityUtils.startActivity((Activity) mContext, intent);

    }

    private VideoFindAdapter mVideoFindAdapter;

    @Override
    protected void initView() {
        super.initView();
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        mVideoFindAdapter = new VideoFindAdapter(DataUtil.getVideoPagerList());
        mRecyclerView.setLayoutManager(new MyGridLayoutManager(mContext, 3));
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        mRecyclerView.setAdapter(mVideoFindAdapter);

    }

    @Override
    public void getDataFromNet(boolean isRefresh) {
        mSwipeRefreshLayout.setRefreshing(false);

    }
}
