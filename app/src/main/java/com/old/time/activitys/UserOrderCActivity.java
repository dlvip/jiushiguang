package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;

import com.old.time.adapters.UserOrderAdapter;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.RecyclerItemDecoration;

public class UserOrderCActivity extends BaseCActivity {

    /**
     * 我的订单管理
     *
     * @param mContext
     */
    public static void startUserOrderActivity(Activity mContext) {
        Intent intent = new Intent(mContext, UserOrderCActivity.class);
        ActivityUtils.startActivity(mContext, intent);

    }

    private UserOrderAdapter mAdapter;

    @Override
    protected void initView() {
        super.initView();
        setTitleText("订单管理");
        mAdapter = new UserOrderAdapter(strings);
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void getDataFromNet(boolean isRefresh) {
        mSwipeRefreshLayout.setRefreshing(false);


    }
}
