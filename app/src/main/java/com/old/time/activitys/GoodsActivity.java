package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.adapters.GoodsAdapter;
import com.old.time.beans.GoodsBean;
import com.old.time.beans.ResultBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.UIHelper;
import com.old.time.utils.UserLocalInfoUtils;
import com.old.time.views.CustomNetView;

import java.util.ArrayList;
import java.util.List;

public class GoodsActivity extends CBaseActivity {

    /**
     * 宝贝列表
     *
     * @param mContext
     */
    public static void startGoodsActivity(Activity mContext) {
        if (!UserLocalInfoUtils.instance().isUserLogin()) {
            UserLoginActivity.startUserLoginActivity(mContext);

            return;
        }
        Intent intent = new Intent(mContext, GoodsActivity.class);
        ActivityUtils.startActivity(mContext, intent);

    }

    private List<GoodsBean> goodsBeans = new ArrayList<>();

    private GoodsAdapter adapter;

    private CustomNetView mCustomNetView;

    @Override
    protected void initView() {
        super.initView();
        adapter = new GoodsAdapter(goodsBeans);
        mRecyclerView.setAdapter(adapter);
        mCustomNetView = new CustomNetView(mContext, CustomNetView.NO_DATA);
        setTitleText("宝贝列表");

        if (!UserLocalInfoUtils.instance().isAdmin()) {
            findViewById(R.id.right_layout_send).setVisibility(View.VISIBLE);
            TextView tv_send = findViewById(R.id.tv_send);
            tv_send.setText("新建");

        }
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
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_GOODS_LIST, new JsonCallBack<ResultBean<List<GoodsBean>>>() {
            @Override
            public void onSuccess(ResultBean<List<GoodsBean>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (isRefresh) {
                    goodsBeans.clear();
                    adapter.setNewData(goodsBeans);

                }
                if (mResultBean.data.size() < Constant.PageSize) {
                    adapter.loadMoreEnd();

                } else {
                    adapter.loadMoreComplete();

                }
                adapter.addData(mResultBean.data);
                if (adapter.getItemCount() - adapter.getHeaderLayoutCount() == 0) {
                    mCustomNetView.setDataForView(CustomNetView.NO_DATA);
                    adapter.setEmptyView(mCustomNetView);

                }
            }

            @Override
            public void onError(ResultBean<List<GoodsBean>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                UIHelper.ToastMessage(mContext, mResultBean.msg);
                if (adapter.getItemCount() == 0) {
                    mCustomNetView.setDataForView(CustomNetView.NET_ERREY);
                    adapter.setEmptyView(mCustomNetView);

                } else {
                    adapter.loadMoreFail();

                }
            }
        });
    }
}
