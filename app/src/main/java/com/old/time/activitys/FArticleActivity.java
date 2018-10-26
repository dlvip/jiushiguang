package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.model.HttpParams;
import com.old.time.adapters.ArticleAdapter;
import com.old.time.beans.ArticleBean;
import com.old.time.beans.ResultBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;
import com.old.time.views.CustomNetView;

import java.util.ArrayList;
import java.util.List;

public class FArticleActivity extends BaseCActivity {

    public static void startFArticleActivity(Activity mContext) {
        Intent intent = new Intent(mContext, FArticleActivity.class);
        ActivityUtils.startActivity(mContext, intent);

    }

    private ArticleAdapter mAdapter;
    private CustomNetView mCustomNetView;
    private List<ArticleBean> articleBeans = new ArrayList<>();

    @Override
    protected void initView() {
        super.initView();
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        mAdapter = new ArticleAdapter(articleBeans);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getDataFromNet(false);

            }
        }, mRecyclerView);
        mCustomNetView = new CustomNetView(mContext, CustomNetView.NO_DATA);

    }

    private int pageNum = 0;

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        if (isRefresh) {
            pageNum = 0;

        } else {
            pageNum++;

        }
        HttpParams params = new HttpParams();
        params.put("pageNum", pageNum);
        params.put("pageSize", Constant.PageSize);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_ARTICLE_LIST, new JsonCallBack<ResultBean<List<ArticleBean>>>() {
            @Override
            public void onSuccess(ResultBean<List<ArticleBean>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (isRefresh) {
                    articleBeans.clear();
                    mAdapter.setNewData(articleBeans);

                }
                if (mResultBean.data.size() < Constant.PageSize) {
                    mAdapter.loadMoreEnd();

                } else {
                    mAdapter.loadMoreComplete();

                }
                mAdapter.addData(mResultBean.data);
                if (mAdapter.getItemCount() - mAdapter.getHeaderLayoutCount() == 0) {
                    mCustomNetView.setDataForView(CustomNetView.NO_DATA);
                    mAdapter.setEmptyView(mCustomNetView);

                }
            }

            @Override
            public void onError(ResultBean<List<ArticleBean>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                UIHelper.ToastMessage(mContext, mResultBean.msg);
                if (mAdapter.getItemCount() == 0) {
                    mCustomNetView.setDataForView(CustomNetView.NET_ERREY);
                    mAdapter.setEmptyView(mCustomNetView);

                } else {
                    mAdapter.loadMoreFail();

                }
            }
        });
    }
}
