package com.old.time.postcard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.old.time.activitys.BaseCActivity;
import com.old.time.activitys.WebViewActivity;
import com.old.time.beans.FastMailBean;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.RecyclerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class FastMailActivity extends BaseCActivity {

    /**
     * 快递列表
     *
     * @param mContext
     */
    public static void startFastMailActivity(Context mContext) {
        Intent intent = new Intent(mContext, FastMailActivity.class);
        ActivityUtils.startActivity((Activity) mContext, intent);

    }

    private FastMailAdapter adapter;
    private List<FastMailBean> fastMailBeans = new ArrayList<>();

    @Override
    protected void initView() {
        super.initView();
        setTitleText("服务号");
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext));
        adapter = new FastMailAdapter(fastMailBeans);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                FastMailBean fastMailBean = (FastMailBean) adapter.getItem(position);
                WebViewActivity.startWebViewActivity(mContext, fastMailBean.getUrl());

            }
        });
    }

    @Override
    public void getDataFromNet(boolean isRefresh) {
        mSwipeRefreshLayout.setRefreshing(false);
        adapter.setNewData(FastMailBean.getFastMailBeans());

    }
}
