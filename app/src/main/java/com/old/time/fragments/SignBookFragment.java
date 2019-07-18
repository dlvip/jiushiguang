package com.old.time.fragments;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.model.HttpParams;
import com.old.time.adapters.SignNameAdapter;
import com.old.time.beans.ResultBean;
import com.old.time.beans.SignNameEntity;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UserLocalInfoUtils;
import com.old.time.views.CustomNetView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wcliang on 2019/7/18.
 */

public class SignBookFragment extends CBaseFragment {

    /**
     * 书签
     *
     * @return
     */
    public static SignBookFragment getInstance() {

        return new SignBookFragment();
    }

    private List<SignNameEntity> signNameEntities = new ArrayList<>();
    private SignNameAdapter adapter;

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        adapter = new SignNameAdapter(signNameEntities);
        mRecyclerView.setAdapter(adapter);

        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getDataFromNet(false);

            }
        }, mRecyclerView);
    }

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        this.isRefresh = isRefresh;
        if (isRefresh) {
            startNum = 0;

        }
        HttpParams params = new HttpParams();
        params.put("userId", UserLocalInfoUtils.instance().getUserId());
        params.put("friendId", "");
        params.put("pageNum", startNum);
        params.put("pageSize", Constant.PageSize);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_SIGN_NAME_LIST, jsonCallBack);

    }

    protected int startNum = 0;
    protected boolean isRefresh;

    protected JsonCallBack jsonCallBack = new JsonCallBack<ResultBean<List<SignNameEntity>>>() {
        @Override
        public void onSuccess(ResultBean<List<SignNameEntity>> mResultBean) {
            mSwipeRefreshLayout.setRefreshing(false);
            startNum++;
            if (isRefresh) {
                signNameEntities.clear();
                adapter.setNewData(signNameEntities);

            }
            if (mResultBean.data.size() < Constant.PageSize) {
                adapter.loadMoreEnd();

            } else {
                adapter.loadMoreComplete();

            }
            adapter.addData(mResultBean.data);
            if (adapter.getItemCount() == 0) {
                mCustomNetView.setDataForView(CustomNetView.NO_DATA);
                adapter.setEmptyView(mCustomNetView);

            }
        }

        @Override
        public void onError(ResultBean<List<SignNameEntity>> mResultBean) {
            mSwipeRefreshLayout.setRefreshing(false);
            if (adapter.getItemCount() == 0) {
                mCustomNetView.setDataForView(CustomNetView.NET_ERREY);
                adapter.setEmptyView(mCustomNetView);

            } else {
                adapter.loadMoreFail();

            }
        }
    };
}
