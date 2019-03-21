package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lzy.okgo.model.HttpParams;
import com.old.time.adapters.TopicAdapter;
import com.old.time.beans.ResultBean;
import com.old.time.beans.TopicBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.MyGridLayoutManager;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;
import com.old.time.utils.UserLocalInfoUtils;
import com.old.time.views.CustomNetView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class TopicsCActivity extends BaseCActivity {

    /**
     * 话题列表
     *
     * @param mContext
     */
    public static void startTopicsActivity(Activity mContext) {
        Intent intent = new Intent(mContext, TopicsCActivity.class);
        ActivityUtils.startActivity(mContext, intent);

    }

    private CustomNetView mCustomNetView;
    private TopicAdapter mTopicAdapter;
    private List<TopicBean> topicBeans = new ArrayList<>();

    @Override
    protected void initView() {
        super.initView();
        setTitleText("话题");
        mRecyclerView.setLayoutManager(new MyGridLayoutManager(mContext, 2));
        mTopicAdapter = new TopicAdapter(topicBeans);
        mRecyclerView.setAdapter(mTopicAdapter);
        mCustomNetView = new CustomNetView(mContext, CustomNetView.NO_DATA);
        mTopicAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getDataFromNet(false);

            }
        }, mRecyclerView);
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                showSuspensionPopupWindow();

            }
        });
        EventBus.getDefault().register(this);

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                TopicBean topicBean = mTopicAdapter.getData().get(position);
                EventBus.getDefault().post(topicBean);
                ActivityUtils.finishActivity(mContext);

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void createmTopic(TopicBean mTopicBean) {
        if (mTopicBean == null || mTopicAdapter == null) {

            return;
        }
        mTopicAdapter.addData(0, mTopicBean);
        seleteToPosition(0);

    }

    @Override
    public void setSuspensionPopupWindowClick() {
        super.setSuspensionPopupWindowClick();
        CreateTopicActivity.startCreateTalkActivity(mContext);

    }

    private int pageNum = 0;

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        if (isRefresh) {
            pageNum = 0;

        }
        HttpParams params = new HttpParams();
        params.put("userId", UserLocalInfoUtils.instance().getUserId());
        params.put("pageNum", pageNum);
        params.put("pageSize", Constant.PageSize);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_TOPIC_LIST, new JsonCallBack<ResultBean<List<TopicBean>>>() {
            @Override
            public void onSuccess(ResultBean<List<TopicBean>> mResultBean) {
                pageNum++;
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
                if (mTopicAdapter.getItemCount() - mTopicAdapter.getHeaderLayoutCount() == 0) {
                    mCustomNetView.setDataForView(CustomNetView.NET_ERREY);
                    mTopicAdapter.setEmptyView(mCustomNetView);

                } else {
                    mTopicAdapter.loadMoreFail();

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }
}
