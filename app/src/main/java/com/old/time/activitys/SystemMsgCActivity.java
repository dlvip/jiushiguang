package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;

import com.old.time.adapters.SystemMsgAdapter;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.DataUtils;
import com.old.time.utils.RecyclerItemDecoration;

public class SystemMsgCActivity extends BaseCActivity {

    /**
     * 消息通知
     *
     * @param mContext
     */
    public static void startUserMsgActivity(Activity mContext) {
        Intent intent = new Intent(mContext, SystemMsgCActivity.class);
        ActivityUtils.startActivity(mContext, intent);

    }

    private SystemMsgAdapter mAdapter;

    @Override
    protected void initView() {
        super.initView();
        setTitleText("消息通知");
        mAdapter = new SystemMsgAdapter(DataUtils.getDateStrings(20));
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void getDataFromNet(boolean isRefresh) {
        mSwipeRefreshLayout.setRefreshing(false);

    }

}
