package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lzy.okgo.model.HttpParams;
import com.old.time.adapters.TopicBookAdapter;
import com.old.time.beans.BookEntity;
import com.old.time.beans.ResultBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.MyGridLayoutManager;
import com.old.time.views.CustomNetView;

import java.util.ArrayList;
import java.util.List;

public class SelectBookActivity extends BaseCActivity {

    /**
     * @param context
     */
    public static void startSelectBookActivity(Context context) {
        Intent intent = new Intent(context, SelectBookActivity.class);
        ActivityUtils.startActivity((Activity) context, intent);

    }

    private TopicBookAdapter adapter;
    private CustomNetView mCustomNetView;
    private List<BookEntity> bookEntities = new ArrayList<>();

    @Override
    protected void initView() {
        super.initView();
        mRecyclerView.setLayoutManager(new MyGridLayoutManager(mContext, 3));
        adapter = new TopicBookAdapter(bookEntities);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getDataFromNet(false);

            }
        }, mRecyclerView);

        mCustomNetView = new CustomNetView(mContext, CustomNetView.NO_DATA);

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter mAdapter, View view, int position) {
                CreateComActivity.startCreateComActivity(mContext, adapter.getItem(position));
                ActivityUtils.finishActivity(mContext);

            }
        });
    }

    private int startNum;

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        if (isRefresh) {
            startNum = 0;

        }
        HttpParams params = new HttpParams();
        params.put("pageNum", startNum);
        params.put("pageSize", Constant.PageSize);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_BOOK_LIST, new JsonCallBack<ResultBean<List<BookEntity>>>() {
            @Override
            public void onSuccess(ResultBean<List<BookEntity>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                startNum++;
                if (isRefresh) {
                    bookEntities.clear();
                    adapter.setNewData(bookEntities);

                }
                if (mResultBean.data.size() < Constant.PageSize) {
                    adapter.loadMoreEnd();

                } else {
                    adapter.loadMoreComplete();

                }
                adapter.addData(mResultBean.data);
                if (adapter.getData().size() == 0) {
                    mCustomNetView.setDataForView(CustomNetView.NO_DATA);
                    adapter.setEmptyView(mCustomNetView);

                }
            }

            @Override
            public void onError(ResultBean<List<BookEntity>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (adapter.getData().size() == 0) {
                    mCustomNetView.setDataForView(CustomNetView.NET_ERREY);
                    adapter.setEmptyView(mCustomNetView);

                } else {
                    adapter.loadMoreFail();

                }
            }
        });
    }
}
