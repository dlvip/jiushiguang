package com.old.time.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.activitys.PicsManageActivity;
import com.old.time.activitys.SettingActivity;
import com.old.time.activitys.UserDressActivity;
import com.old.time.activitys.UserMesgActivity;
import com.old.time.activitys.SystemMsgActivity;
import com.old.time.activitys.UserOrderActivity;
import com.old.time.beans.UserInfoBean;
import com.old.time.constants.Code;
import com.old.time.constants.Constant;
import com.old.time.constants.Key;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.okhttps.Http;
import com.old.time.okhttps.exception.ApiException;
import com.old.time.okhttps.subscriber.CommonSubscriber;
import com.old.time.okhttps.transformer.CommonTransformer;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.MapParams;
import com.old.time.utils.SpUtils;

/**
 * Created by NING on 2018/3/5.
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {

    public SwipeRefreshLayout.OnRefreshListener onRefreshListener;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public Handler loadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Code.HIDE_LOADING:
                    mSwipeRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            mSwipeRefreshLayout.setRefreshing(false);

                        }
                    });
                    break;
            }
        }
    };

    @Override
    public void getDataFromNet(boolean isRefresh) {
        MapParams mMapParams = new MapParams();
        mMapParams.putParams("userid", "d01bbb7c-9f8e-4669-9bbb-7c9f8e1669f0");
        mMapParams.putParams("current_userid", "d01bbb7c-9f8e-4669-9bbb-7c9f8e1669f0");
        Http.getHttpService().getUserInfo(Constant.GET_USER_INFO, mMapParams.getParamString())
                .compose(new CommonTransformer<UserInfoBean>()).subscribe(new CommonSubscriber<UserInfoBean>(mContext) {
            @Override
            public void onNext(UserInfoBean mUserInfoBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                setDataForView(mUserInfoBean);

            }

            @Override
            protected void onError(ApiException e) {
                super.onError(e);
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    /**
     * 设置数据
     */
    private void setDataForView(UserInfoBean mUserInfoBean) {
        if (mUserInfoBean == null) {

            return;
        }
        SpUtils.setObject(Key.GET_USER_INFO_BEAN, mUserInfoBean);
        GlideUtils.getInstance().setRoundImageView(mContext, mUserInfoBean.getLogopath(), img_user_header);
        tv_user_name.setText(mUserInfoBean.getNickName());
        tv_user_detail.setText(mUserInfoBean.getSign());

    }

    private ImageView img_user_header;
    private TextView tv_user_name, tv_user_detail;

    @Override
    protected void lazyLoad() {
        TextView top_title = findViewById(R.id.top_title);
        top_title.setText(getString(R.string.main_tab_mine));
        mSwipeRefreshLayout = findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright
                , R.color.holo_green_light, R.color.holo_orange_light, R.color.holo_red_light);
        onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromNet(true);

            }
        };
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        loadHandler.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true); //手动开启小圆圈loading，但是不会执行刷新数据的监听
                onRefreshListener.onRefresh();           //所有要手动调用回调，

            }
        });

        img_user_header = findViewById(R.id.img_user_header);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_user_detail = findViewById(R.id.tv_user_detail);

        findViewById(R.id.relative_layout_header).setOnClickListener(this);
        findViewById(R.id.relative_layout_message).setOnClickListener(this);
        findViewById(R.id.relative_layout_order).setOnClickListener(this);
        findViewById(R.id.relative_layout_setting).setOnClickListener(this);
        findViewById(R.id.relative_layout_dress).setOnClickListener(this);
        findViewById(R.id.relative_layout_pics).setOnClickListener(this);

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_mine;
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.relative_layout_header:
                intent = new Intent(mContext, UserMesgActivity.class);

                break;
            case R.id.relative_layout_message:
                intent = new Intent(mContext, SystemMsgActivity.class);

                break;
            case R.id.relative_layout_order:
                intent = new Intent(mContext, UserOrderActivity.class);

                break;
            case R.id.relative_layout_pics:
                intent = new Intent(mContext, PicsManageActivity.class);

                break;
            case R.id.relative_layout_dress:
                intent = new Intent(mContext, UserDressActivity.class);

                break;
            case R.id.relative_layout_setting:
                intent = new Intent(mContext, SettingActivity.class);

                break;
        }
        if (intent != null) {
            ActivityUtils.startActivity((Activity) mContext, intent);

        }
    }
}
