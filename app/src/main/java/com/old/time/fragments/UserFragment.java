package com.old.time.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.activitys.UserLoginActivity;
import com.old.time.activitys.UserMsgActivity;
import com.old.time.adapters.RBookAdapter;
import com.old.time.beans.BookEntity;
import com.old.time.beans.ResultBean;
import com.old.time.beans.UserInfoBean;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.MyGridLayoutManager;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;
import com.old.time.utils.UserLocalInfoUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wcliang on 2019/7/12.
 */

public class UserFragment extends BaseFragment implements View.OnClickListener {

    /**
     * 我的书架
     *
     * @return
     */
    public static UserFragment getInstance() {

        return new UserFragment();
    }

    private ImageView img_user_header;
    private TextView tv_user_name, tv_user_detail;
    private LinearLayout linear_layout_login;
    private RelativeLayout relative_layout_header;

    private RBookAdapter rBookAdapter;
    private List<BookEntity> bookEntities = new ArrayList<>();

    @Override
    protected int setContentView() {
        return R.layout.fragment_user;
    }

    @Override
    protected void lazyLoad() {
        findViewById(R.id.left_layout).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.top_title)).setText("我的");

        linear_layout_login = findViewById(R.id.linear_layout_login);
        relative_layout_header = findViewById(R.id.relative_layout_header);
        img_user_header = findViewById(R.id.img_user_header);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_user_detail = findViewById(R.id.tv_user_detail);

        linear_layout_login.setOnClickListener(this);
        relative_layout_header.setOnClickListener(this);
        RecyclerView mRecyclerView = findViewById(R.id.c_recycler_view);
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.HORIZONTAL_LIST, 10));
        mRecyclerView.setLayoutManager(new MyGridLayoutManager(mContext, 3));
        mRecyclerView.setBackgroundResource(R.color.color_fff);
        rBookAdapter = new RBookAdapter(bookEntities);
        mRecyclerView.setAdapter(rBookAdapter);
    }

    @Override
    public void getDataFromNet(boolean isRefresh) {
        HttpParams params = new HttpParams();
        params.put("userId", UserLocalInfoUtils.instance().getUserId());
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_USER_INFO, new JsonCallBack<ResultBean<UserInfoBean>>() {
            @Override
            public void onSuccess(ResultBean<UserInfoBean> mResultBean) {
                if (mResultBean == null || mResultBean.data == null) {

                    return;
                }
                UserInfoBean mUserInfoBean = mResultBean.data;
                UserLocalInfoUtils.instance().setmUserInfoBean(mUserInfoBean);
                setDataForView(mUserInfoBean);

            }

            @Override
            public void onError(ResultBean<UserInfoBean> mResultBean) {
                UIHelper.ToastMessage(mContext, mResultBean.msg);

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
        UserLocalInfoUtils.instance().setmUserInfoBean(mUserInfoBean);
        GlideUtils.getInstance().setRoundImageView(mContext, mUserInfoBean.getAvatar(), img_user_header);
        tv_user_name.setText(mUserInfoBean.getUserName());
        tv_user_detail.setText(mUserInfoBean.getVocation());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_layout_login:
                UserLoginActivity.startUserLoginActivity(mContext);

                break;
            case R.id.relative_layout_header:
                UserMsgActivity.startUserMsgActivity(mContext);

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
        List<BookEntity> entities = DataSupport.findAll(BookEntity.class);
        if (entities == null) {

            return;
        }
        bookEntities.clear();
        bookEntities.addAll(entities);
        rBookAdapter.setNewData(bookEntities);
    }
}
