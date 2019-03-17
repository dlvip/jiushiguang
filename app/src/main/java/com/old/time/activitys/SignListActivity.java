package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.adapters.SignNameAdapter;
import com.old.time.beans.ResultBean;
import com.old.time.beans.SignNameEntity;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UserLocalInfoUtils;
import com.old.time.views.CustomNetView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.old.time.views.CustomNetView.NET_ERREY;
import static com.old.time.views.CustomNetView.NO_DATA;

public class SignListActivity extends BaseCActivity {

    /**
     * 打卡列表
     *
     * @param context
     */
    public static void startSignListActivity(Context context) {
        Intent intent = new Intent(context, SignListActivity.class);
        ActivityUtils.startActivity((Activity) context, intent);

    }

    private List<SignNameEntity> signNameEntities = new ArrayList<>();
    private SignNameAdapter adapter;
    private CustomNetView mCustomNetView;

    @Override
    protected void initView() {
        super.initView();
        setTitleText("每日书签");
        setSendText("创建");
        findViewById(R.id.right_layout_send).setVisibility(View.VISIBLE);
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        adapter = new SignNameAdapter(signNameEntities);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getDataFromNet(false);

            }
        }, mRecyclerView);
        mCustomNetView = new CustomNetView(mContext, NO_DATA);
        EventBus.getDefault().register(this);
    }

    private int startNum;

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        if (isRefresh) {
            startNum = 0;

        }
        HttpParams params = new HttpParams();
        params.put("userId", UserLocalInfoUtils.instance().getUserId());
        params.put("friendId", "");
        params.put("pageNum", startNum);
        params.put("pageSize", Constant.PageSize);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_SIGN_NAME_LIST, new JsonCallBack<ResultBean<List<SignNameEntity>>>() {
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
                    mCustomNetView.setDataForView(NO_DATA);
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
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void createSigned(SignNameEntity mSignNameEntity) {
        if (mSignNameEntity == null) {

            return;
        }
        adapter.addData(0, mSignNameEntity);
        seleteToPosition(0);

    }

    @Override
    public void save(View view) {
        super.save(view);
        SignCreateActivity.startSignCreateActivity(mContext);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
