package com.old.time.postcard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.activitys.BaseSActivity;
import com.old.time.adapters.SignNameAdapter;
import com.old.time.beans.ResultBean;
import com.old.time.beans.SignNameEntity;
import com.old.time.beans.UserInfoBean;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UserLocalInfoUtils;

import java.util.ArrayList;
import java.util.List;

public class UserSignActivity extends BaseSActivity {

    private static final String USER_INFO_BEAN = "mUserInfoBean";

    public static void startUserSignActivity(Context context, UserInfoBean userInfoBean) {
        Intent intent = new Intent(context, UserSignActivity.class);
        intent.putExtra(USER_INFO_BEAN, userInfoBean);
        ActivityUtils.startActivity((Activity) context, intent);

    }

    private UserInfoBean userInfoBean;
    private TextView tv_user_name;
    private ImageView img_header_bg, img_user_pic;

    private List<SignNameEntity> signNameEntities = new ArrayList<>();
    private SignNameAdapter adapter;

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
        adapter = new SignNameAdapter(signNameEntities);
        adapter.addHeaderView(headerView);
        mRecyclerView.setAdapter(adapter);

    }

    private int startNum;

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        if (isRefresh) {
            startNum = 0;

        }
        HttpParams params = new HttpParams();
        params.put("userId", UserLocalInfoUtils.instance().getUserId());
        params.put("friendId", userInfoBean.getUserId());
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
                adapter.addData(mResultBean.data);
            }

            @Override
            public void onError(ResultBean<List<SignNameEntity>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });
    }
}
