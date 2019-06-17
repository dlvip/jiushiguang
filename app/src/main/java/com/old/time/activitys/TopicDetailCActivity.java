package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.adapters.DynamicAdapter;
import com.old.time.beans.DynamicBean;
import com.old.time.beans.ResultBean;
import com.old.time.beans.TopicBean;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;
import com.old.time.views.CustomNetView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class TopicDetailCActivity extends BaseSActivity {

    public static final String TOPIC_BEAN = "TopicBean";

    /**
     * 话题详情
     *
     * @param mContext
     */
    public static void startTopicDetailActivity(Activity mContext, TopicBean mTopicBean) {
        Intent intent = new Intent(mContext, TopicDetailCActivity.class);
        intent.putExtra(TOPIC_BEAN, mTopicBean);
        ActivityUtils.startActivity(mContext, intent);

    }

    private List<DynamicBean> dynamicBeans = new ArrayList<>();
    private DynamicAdapter adapter;
    private TopicBean mTopicBean;

    private View headerView;
    private TextView tv_user_name;
    private ImageView img_header_bg;


    @Override
    protected void initView() {
        this.mTopicBean = (TopicBean) getIntent().getSerializableExtra(TOPIC_BEAN);
        super.initView();
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        headerView = View.inflate(mContext, R.layout.header_topic_detail, null);
        adapter = new DynamicAdapter(dynamicBeans);
        adapter.removeAllHeaderView();
        adapter.addHeaderView(headerView);
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        mRecyclerView.setAdapter(adapter);

        adapter.setHeaderAndEmpty(true);
        setHeaderView(mTopicBean);

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
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                showSuspensionPopupWindow();

            }
        });
    }

    @Override
    public void setSuspensionPopupWindowClick() {
        super.setSuspensionPopupWindowClick();
        CreateDynActivity.startCreateDynActivity(mContext, mTopicBean);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void createDynamic(DynamicBean mDynamicBean) {
        if (mDynamicBean == null || adapter == null) {

            return;
        }
        if (!mTopicBean.getId().equals(mDynamicBean.getTopicId())) {

            return;
        }
        adapter.addData(0, mDynamicBean);
        selectToPosition(0);

    }

    /**
     * 设置头部数据
     */
    private void setHeaderView(TopicBean mTopicBean) {
        if (headerView == null || mTopicBean == null) {

            return;
        }
        if (tv_user_name == null) {
            tv_user_name = headerView.findViewById(R.id.tv_user_name);
            img_header_bg = headerView.findViewById(R.id.img_header_bg);

        }
        setTitleText(mTopicBean.getTopic());
        tv_user_name.setText(mTopicBean.getTopic());
        GlideUtils.getInstance().setImgTransRes(mContext, mTopicBean.getPic(), img_header_bg, 0, 0);

    }

    private int pageNum = 0;

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        if (isRefresh) {
            pageNum = 0;

        }
        HttpParams params = new HttpParams();
        params.put("topicId", mTopicBean.getId());
        params.put("pageNum", pageNum);
        params.put("pageSize", Constant.PageSize);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_TOPIC_DYNAMIC_LIST, new JsonCallBack<ResultBean<List<DynamicBean>>>() {
            @Override
            public void onSuccess(ResultBean<List<DynamicBean>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (isRefresh) {
                    dynamicBeans.clear();
                    adapter.setNewData(dynamicBeans);

                }
                pageNum++;
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
            public void onError(ResultBean<List<DynamicBean>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                UIHelper.ToastMessage(mContext, mResultBean.msg);
                if (adapter.getData().size() == 0) {
                    mCustomNetView.setDataForView(CustomNetView.NET_ERREY);
                    adapter.setEmptyView(mCustomNetView);

                } else {
                    adapter.loadMoreFail();

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
