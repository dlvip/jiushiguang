package com.old.time.activitys;

import android.view.View;

import com.old.time.R;
import com.old.time.adapters.CircleAdapter;
import com.old.time.constants.Constant;
import com.old.time.utils.RecyclerItemDecoration;

public class CircleActivity extends SBaseActivity {

    private CircleAdapter mAdapter;

    @Override
    protected void initView() {
        super.initView();
        mAdapter = new CircleAdapter(strings);
        View headerView = View.inflate(mContext, R.layout.header_circle, null);
        mAdapter.addHeaderView(headerView);
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext));
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void getDataFromNet(boolean isRefresh) {
        mSwipeRefreshLayout.setRefreshing(false);

    }
}
