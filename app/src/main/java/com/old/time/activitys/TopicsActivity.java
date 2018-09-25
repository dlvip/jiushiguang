package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.adapters.TopicAdapter;
import com.old.time.beans.ResultBean;
import com.old.time.beans.TopicBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;
import com.old.time.utils.UserLocalInfoUtils;
import com.old.time.views.CustomNetView;

import java.util.ArrayList;
import java.util.List;

public class TopicsActivity extends CBaseActivity {

    /**
     * 话题列表
     *
     * @param mContext
     */
    public static void startTopicsActivity(Activity mContext) {
        Intent intent = new Intent(mContext, TopicsActivity.class);
        ActivityUtils.startActivity(mContext, intent);

    }

    private CustomNetView mCustomNetView;
    private TopicAdapter mTopicAdapter;
    private List<TopicBean> topicBeans = new ArrayList<>();

    @Override
    protected void initView() {
        super.initView();
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        mTopicAdapter = new TopicAdapter(topicBeans);
        mRecyclerView.setAdapter(mTopicAdapter);
        View headerView = View.inflate(mContext, R.layout.header_create_talk, null);
        mTopicAdapter.removeAllHeaderView();
        mTopicAdapter.addHeaderView(headerView);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateTopicActivity.startCreateTalkActivity(mContext);

            }
        });
        mCustomNetView = new CustomNetView(mContext, CustomNetView.NO_DATA);
        mTopicAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getDataFromNet(false);

            }
        }, mRecyclerView);
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
        params.put("userId", UserLocalInfoUtils.instance().getUserId());
        params.put("pageNum", pageNum);
        params.put("pageSize", Constant.PageSize);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_TOPIC_LIST, new JsonCallBack<ResultBean<List<TopicBean>>>() {
            @Override
            public void onSuccess(ResultBean<List<TopicBean>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (isRefresh) {
                    topicBeans.clear();
                    mTopicAdapter.setNewData(topicBeans);

                }
                if (mResultBean.data.size() < Constant.PageSize) {
                    mTopicAdapter.loadMoreEnd();

                } else {
                    mTopicAdapter.loadMoreComplete();

                }
                mTopicAdapter.addData(mResultBean.data);
                if (mTopicAdapter.getItemCount() - mTopicAdapter.getHeaderLayoutCount() == 0) {
                    mCustomNetView.setDataForView(CustomNetView.NO_DATA);
                    mTopicAdapter.setEmptyView(mCustomNetView);

                }
            }

            @Override
            public void onError(ResultBean<List<TopicBean>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                UIHelper.ToastMessage(mContext, mResultBean.msg);
                if (mTopicAdapter.getItemCount() == 0) {
                    mCustomNetView.setDataForView(CustomNetView.NET_ERREY);
                    mTopicAdapter.setEmptyView(mCustomNetView);

                } else {
                    mTopicAdapter.loadMoreFail();

                }
            }
        });
    }
}
