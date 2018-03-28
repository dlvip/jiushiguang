package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;

import com.old.time.adapters.MsgAdapter;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.RecyclerItemDecoration;

public class SystemMsgActivity extends CBaseActivity {

    /**
     * 消息通知
     *
     * @param mContext
     */
    public static void startUserMsgActivity(Activity mContext) {
        Intent intent = new Intent(mContext, SystemMsgActivity.class);
        ActivityUtils.startActivity(mContext, intent);

    }

    private MsgAdapter mAdapter;

    @Override
    protected void initView() {
        super.initView();
        setTitleText("消息通知");
        mAdapter = new MsgAdapter(strings);
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void getDataFromNet(boolean isRefresh) {


    }

}
