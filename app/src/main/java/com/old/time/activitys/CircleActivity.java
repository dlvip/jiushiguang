package com.old.time.activitys;

import android.view.View;

import com.old.time.R;
import com.old.time.adapters.CircleAdapter;
import com.old.time.constants.Constant;
import com.old.time.utils.RecyclerItemDecoration;

public class CircleActivity extends CBaseActivity {

    private CircleAdapter mAdapter;

    @Override
    protected void initView() {
        super.initView();
        strings.clear();
        for (int i = 0; i < 20; i++) {
            strings.add(Constant.PHOTO_PIC_URL);

        }
        mAdapter = new CircleAdapter(strings);
        View headerView = View.inflate(mContext, R.layout.header_circle, null);
        mAdapter.addHeaderView(headerView);
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext));
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_circle;
    }

    @Override
    public void getDataFromNet(boolean isRefresh) {
        mSwipeRefreshLayout.setRefreshing(false);

    }
}
