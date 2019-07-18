package com.old.time.fragments;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.activitys.TopicsCActivity;
import com.old.time.adapters.DynamicAdapter;
import com.old.time.adapters.TopicDAdapter;
import com.old.time.beans.DynamicBean;
import com.old.time.beans.ResultBean;
import com.old.time.beans.TopicBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.RongIMUtils;
import com.old.time.views.CustomNetView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wcliang on 2019/7/18.
 */

public class DynamicsFragment extends CBaseFragment {

    /**
     * 动态
     *
     * @return
     */
    public static DynamicsFragment getInstance() {

        return new DynamicsFragment();
    }

    private List<DynamicBean> mDynamicBeans = new ArrayList<>();
    private DynamicAdapter mAdapter;

    private List<TopicBean> topicBeans = new ArrayList<>();
    private TopicDAdapter topicDAdapter;

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        LinearLayout linear_layout_header = findViewById(R.id.linear_layout_header);
        linear_layout_header.setVisibility(View.VISIBLE);
        View view = View.inflate(mContext, R.layout.header_dynamics, null);
        linear_layout_header.addView(view);

        mAdapter = new DynamicAdapter(mDynamicBeans);
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        mRecyclerView.setAdapter(mAdapter);

        View headerView = View.inflate(mContext, R.layout.header_post_cart, null);
        topicDAdapter = new TopicDAdapter(topicBeans);
        RecyclerView recycler_view = headerView.findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new GridLayoutManager(mContext, 2));
        recycler_view.setAdapter(topicDAdapter);
        mAdapter.removeAllHeaderView();
        mAdapter.addHeaderView(headerView);
        mAdapter.setNewData(mDynamicBeans);
        mAdapter.setHeaderAndEmpty(true);

        EventBus.getDefault().register(this);


        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getDataFromNet(false);

            }
        }, mRecyclerView);
        recycler_view.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                TopicBean topicBean = topicDAdapter.getItem(position);
                if (topicBean == null) {

                    return;
                }
                RongIMUtils.RongIMConnect(mContext, String.valueOf(topicBean.getId()));

            }
        });
        linear_layout_header.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TopicsCActivity.startTopicsActivity(mContext);

            }
        });
    }

    private int startNum;

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        if (isRefresh) {
            startNum = 0;
            getTopices();

        }
        HttpParams params = new HttpParams();
        params.put("pageNum", startNum);
        params.put("pageSize", Constant.PageSize);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_DYNAMIC_LIST, new JsonCallBack<ResultBean<List<DynamicBean>>>() {
            @Override
            public void onSuccess(ResultBean<List<DynamicBean>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                startNum++;
                if (isRefresh) {
                    mDynamicBeans.clear();
                    mAdapter.setNewData(mDynamicBeans);

                }
                if (mResultBean.data.size() < Constant.PageSize) {
                    mAdapter.loadMoreEnd();

                } else {
                    mAdapter.loadMoreComplete();

                }
                mAdapter.addData(mResultBean.data);
                if (mAdapter.getData().size() == 0) {
                    mCustomNetView.setDataForView(CustomNetView.NO_DATA);
                    mAdapter.setEmptyView(mCustomNetView);

                }
            }

            @Override
            public void onError(ResultBean<List<DynamicBean>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (mAdapter.getData().size() == 0) {
                    mCustomNetView.setDataForView(CustomNetView.NET_ERREY);
                    mAdapter.setEmptyView(mCustomNetView);

                } else {
                    mAdapter.loadMoreFail();

                }
            }
        });
    }

    /**
     * 获取推荐话题
     */
    private void getTopices() {
        HttpParams params = new HttpParams();
        params.put("pageNum", "0");
        params.put("pageSize", "4");
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_TOPIC_LIST, new JsonCallBack<ResultBean<List<TopicBean>>>() {
            @Override
            public void onSuccess(ResultBean<List<TopicBean>> mResultBean) {
                if (mResultBean == null || mResultBean.data == null || mResultBean.data.size() == 0) {

                    return;
                }
                topicDAdapter.setNewData(mResultBean.data);

            }

            @Override
            public void onError(ResultBean<List<TopicBean>> mResultBean) {

            }
        });
    }

    @Override
    public void setSuspensionPopupWindowClick() {
        super.setSuspensionPopupWindowClick();
        TopicsCActivity.startTopicsActivity(mContext);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void createDynamic(DynamicBean mDynamicBean) {
        if (mDynamicBean == null || mAdapter == null) {

            return;
        }
        mAdapter.addData(0, mDynamicBean);
        seleteToPosition(0);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
