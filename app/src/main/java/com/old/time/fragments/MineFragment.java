package com.old.time.fragments;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.activity.CaptureActivity;
import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.activitys.PicsManageActivity;
import com.old.time.activitys.SettingActivity;
import com.old.time.activitys.UserDressActivity;
import com.old.time.activitys.UserLoginActivity;
import com.old.time.activitys.UserMesgActivity;
import com.old.time.activitys.SystemMsgActivity;
import com.old.time.activitys.UserOrderActivity;
import com.old.time.beans.ResultBean;
import com.old.time.beans.UserInfoBean;
import com.old.time.constants.Code;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.DebugLog;
import com.old.time.utils.StringUtils;
import com.old.time.utils.UIHelper;
import com.old.time.utils.UserLocalInfoUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private ImageView img_user_header;
    private TextView tv_user_name, tv_user_detail;
    private LinearLayout linear_layout_login;
    private RelativeLayout relative_layout_header;

    @Override
    protected void lazyLoad() {
        TextView top_title = findViewById(R.id.top_title);
        top_title.setText(getString(R.string.main_tab_mine));
        mSwipeRefreshLayout = findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light, R.color.holo_orange_light, R.color.holo_red_light);
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

        linear_layout_login = findViewById(R.id.linear_layout_login);
        relative_layout_header = findViewById(R.id.relative_layout_header);
        img_user_header = findViewById(R.id.img_user_header);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_user_detail = findViewById(R.id.tv_user_detail);

        linear_layout_login.setOnClickListener(this);
        relative_layout_header.setOnClickListener(this);
        findViewById(R.id.relative_layout_message).setOnClickListener(this);
        findViewById(R.id.relative_layout_order).setOnClickListener(this);
        findViewById(R.id.relative_layout_setting).setOnClickListener(this);
        findViewById(R.id.relative_layout_dress).setOnClickListener(this);
        findViewById(R.id.relative_layout_pics).setOnClickListener(this);

    }

    @Override
    public void getDataFromNet(boolean isRefresh) {
        if (!UserLocalInfoUtils.instance().isUserLogin()) {
            mSwipeRefreshLayout.setRefreshing(false);

            return;
        }
    }

    /**
     * 设置数据
     */
    private void setDataForView(UserInfoBean mUserInfoBean) {
        if (mUserInfoBean == null) {

            return;
        }
        UserLocalInfoUtils.instance().setmUserInfoBean(mUserInfoBean);
        GlideUtils.getInstance().setRoundImageView(mContext, mUserInfoBean.getAvatar(), img_user_header);
        tv_user_name.setText(mUserInfoBean.getUserName());
        tv_user_detail.setText(mUserInfoBean.getVocation());

    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.linear_layout_login:
                UserLoginActivity.startUserLoginActivity(mContext);

                break;
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
            ActivityUtils.fStartActivtiyForResult(this, intent, CaptureActivity.REQ_CODE);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {

            return;
        }
        switch (requestCode) {
            case CaptureActivity.REQ_CODE:
                String urlPath = data.getStringExtra(CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN);
                UIHelper.ToastMessage(mContext, urlPath);

                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        UserLocalInfoUtils mUserLocalInfoUtils = UserLocalInfoUtils.instance();
        if (mUserLocalInfoUtils.isUserLogin()) {
            linear_layout_login.setVisibility(View.GONE);
            relative_layout_header.setVisibility(View.VISIBLE);
            setDataForView(mUserLocalInfoUtils.getmUserInfoBean());

        } else {
            linear_layout_login.setVisibility(View.VISIBLE);
            relative_layout_header.setVisibility(View.GONE);

        }
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_mine;
    }
}
