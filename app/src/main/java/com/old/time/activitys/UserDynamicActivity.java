package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.old.time.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.model.HttpParams;
import com.old.time.adapters.DynamicAdapter;
import com.old.time.beans.DynamicBean;
import com.old.time.beans.ResultBean;
import com.old.time.beans.UserInfoBean;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;
import com.old.time.views.CustomNetView;

import java.util.ArrayList;
import java.util.List;

public class UserDynamicActivity extends BaseSActivity {

    private static final String USER_INFO_BEAN = "mUserInfoBean";

    /**
     * 用户动态中心
     *
     * @param context
     */
    public static void startUserDynamicActivity(Context context, UserInfoBean userInfoBean) {
        Intent intent = new Intent(context, UserDynamicActivity.class);
        intent.putExtra(USER_INFO_BEAN, userInfoBean);
        ActivityUtils.startActivity((Activity) context, intent);

    }

    private UserInfoBean userInfoBean;
    private TextView tv_user_name;
    private ImageView img_header_bg, img_user_pic;

    private List<DynamicBean> dynamicBeans = new ArrayList<>();
    private DynamicAdapter adapter;

    @Override
    protected void initView() {
        userInfoBean = (UserInfoBean) getIntent().getSerializableExtra(USER_INFO_BEAN);
        super.initView();
        View headerView = View.inflate(mContext, R.layout.header_user_sign, null);
        tv_user_name = headerView.findViewById(R.id.tv_user_name);
        img_user_pic = headerView.findViewById(R.id.img_user_pic);
        img_header_bg = headerView.findViewById(R.id.img_header_bg);

        setTitleText(userInfoBean.getUserName());
        tv_user_name.setText(userInfoBean.getUserName());
        GlideUtils.getInstance().setImgTransRes(mContext, userInfoBean.getAvatar(), img_header_bg, 0, 0);
        GlideUtils.getInstance().setRadiusImageView(mContext, userInfoBean.getAvatar(), img_user_pic, 10);


        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        adapter = new DynamicAdapter(dynamicBeans);
        adapter.removeAllHeaderView();
        adapter.addHeaderView(headerView);
        mRecyclerView.setAdapter(adapter);
        adapter.setHeaderAndEmpty(true);

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


    private int pageNum = 0;

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        if (isRefresh) {
            pageNum = 0;

        }
        HttpParams params = new HttpParams();
        params.put("userId", userInfoBean.getUserId());
        params.put("pageNum", pageNum);
        params.put("pageSize", Constant.PageSize);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_USER_DYNAMIC_LIST, new JsonCallBack<ResultBean<List<DynamicBean>>>() {
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
}
