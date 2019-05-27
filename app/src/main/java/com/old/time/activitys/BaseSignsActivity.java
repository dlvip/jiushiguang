package com.old.time.activitys;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.old.time.adapters.SignNameAdapter;
import com.old.time.beans.ResultBean;
import com.old.time.beans.SignNameEntity;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.views.CustomNetView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wcliang on 2019/5/27.
 */

public abstract class BaseSignsActivity extends BaseCActivity {

    private List<SignNameEntity> signNameEntities = new ArrayList<>();
    private SignNameAdapter adapter;

    @Override
    protected void initView() {
        super.initView();
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        adapter = new SignNameAdapter(signNameEntities);
        mRecyclerView.setAdapter(adapter);
        adapter.setHeaderAndEmpty(true);

        EventBus.getDefault().register(this);

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void createSigned(SignNameEntity mSignNameEntity) {
        if (mSignNameEntity == null || adapter == null) {

            return;
        }
        adapter.addData(0, mSignNameEntity);
        seleteToPosition(0);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

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
