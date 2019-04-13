package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.adapters.DynamicAdapter;
import com.old.time.adapters.TopicDAdapter;
import com.old.time.beans.DynamicBean;
import com.old.time.beans.ResultBean;
import com.old.time.beans.TopicBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.pops.PostCartPop;
import com.old.time.postcard.FastMailActivity;
import com.old.time.postcard.UserCardActivity;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.PictureUtil;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UserLocalInfoUtils;
import com.old.time.views.CustomNetView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class DynamicsActivity extends BaseCActivity {

    private List<DynamicBean> mDynamicBeans = new ArrayList<>();
    private CustomNetView mCustomNetView;
    private DynamicAdapter mAdapter;
    private View relative_layout_more, relative_layout_user;

    /**
     * 旧时光圈子
     *
     * @param mContext
     */
    public static void startDynamicsActivity(Activity mContext) {
        Intent intent = new Intent(mContext, DynamicsActivity.class);
        ActivityUtils.startActivity(mContext, intent);
        ActivityUtils.finishActivity(mContext);

    }

    private List<TopicBean> topicBeans = new ArrayList<>();
    private TopicDAdapter topicDAdapter;
    private ImageView img_sign, img_more;

    @Override
    protected void initView() {
        super.initView();
        img_sign = findViewById(R.id.img_sign);
        img_sign.setVisibility(View.VISIBLE);
        img_more = findViewById(R.id.img_more);
        img_more.setImageResource(R.mipmap.icon_black_more);
        findViewById(R.id.left_layout).setVisibility(View.GONE);
        relative_layout_more = findViewById(R.id.relative_layout_more);
        relative_layout_more.setVisibility(View.VISIBLE);
        setTitleText("期待觅邮");
        mDynamicBeans.clear();
        mAdapter = new DynamicAdapter(mDynamicBeans);
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        mRecyclerView.setAdapter(mAdapter);

        View headerView = View.inflate(mContext, R.layout.header_post_cart, null);
        relative_layout_user = headerView.findViewById(R.id.relative_layout_user);
        topicDAdapter = new TopicDAdapter(topicBeans);
        RecyclerView recycler_view = headerView.findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new GridLayoutManager(mContext, 2));
        recycler_view.setAdapter(topicDAdapter);
        mAdapter.removeAllHeaderView();
        mAdapter.addHeaderView(headerView);
        mAdapter.setNewData(mDynamicBeans);
        mAdapter.setHeaderAndEmpty(true);

        mCustomNetView = new CustomNetView(mContext, CustomNetView.NO_DATA);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
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
        relative_layout_user.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                BooksActivity.startBooksActivity(mContext);

            }
        });
        relative_layout_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreBtnPopWindow();

            }
        });
        img_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignListActivity.startSignListActivity(mContext);

            }
        });
    }

    /**
     * 发布弹框
     */
    private PostCartPop mPostCartPop;

    private void showMoreBtnPopWindow() {
        if (mPostCartPop == null) {
            mPostCartPop = new PostCartPop(mContext, new OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                    if (mPostCartPop != null && mPostCartPop.isShowing()) {
                        mPostCartPop.dismiss();

                    }
                    String string = (String) adapter.getItem(position);
                    assert string != null;
                    switch (string) {
                        case "扫一扫":
                            PictureUtil.captureCode(mContext);

                            break;
                        case "服务号":
                            FastMailActivity.startFastMailActivity(mContext);

                            break;
                        case "个人中心":
                            UserCardActivity.startUserCardActivity(mContext, UserLocalInfoUtils.instance().getUserId());

                            break;
                        default:

                            break;
                    }
                }
            });
        }
        mPostCartPop.showPopWindow(relative_layout_more, new String[]{"扫一扫", "服务号", "个人中心"});

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
     *
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }
}
