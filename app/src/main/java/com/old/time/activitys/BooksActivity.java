package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.model.HttpParams;
import com.old.time.adapters.BooksAdapter;
import com.old.time.beans.BookEntity;
import com.old.time.beans.ResultBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.views.CustomNetView;

import java.util.ArrayList;
import java.util.List;

public class BooksActivity extends BaseCActivity {

    /**
     * 图书列表
     *
     * @param context
     */
    public static void startBooksActivity(Context context) {
        Intent intent = new Intent(context, BooksActivity.class);
        ActivityUtils.startActivity((Activity) context, intent);

    }


    private BooksAdapter adapter;
    private CustomNetView mCustomNetView;
    private List<BookEntity> bookEntities = new ArrayList<>();

    @Override
    protected void initView() {
        super.initView();
        setTitleText("图书列表");
        adapter = new BooksAdapter(bookEntities);
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        mRecyclerView.setAdapter(adapter);

        mCustomNetView = new CustomNetView(mContext, CustomNetView.NO_DATA);

    }

    @Override
    protected void initEvent() {
        super.initEvent();
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getDataFromNet(false);

            }
        }, mRecyclerView);
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
