package com.old.time.postcard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.activitys.BaseActivity;
import com.old.time.activitys.SignListActivity;
import com.old.time.beans.PhoneInfo;
import com.old.time.beans.ResultBean;
import com.old.time.beans.SignNameEntity;
import com.old.time.beans.UserInfoBean;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.MyGridLayoutManager;
import com.old.time.utils.MyLinearLayoutManager;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;
import com.old.time.utils.UserLocalInfoUtils;

import java.util.ArrayList;
import java.util.List;

public class UserCardActivity extends BaseActivity {

    /**
     * 个人中心
     *
     * @param context
     */
    public static void startUserCardActivity(Context context) {
        Intent intent = new Intent(context, UserCardActivity.class);
        ActivityUtils.startActivity((Activity) context, intent);

    }

    private ImageView img_header_bg, img_user_pic, img_more;
    private TextView tv_user_name;

    private UserInfoBean userInfoBean;

    private BaseQuickAdapter<SignNameEntity, BaseViewHolder> sAdapter;
    private RecyclerView recycler_view_sign;

    private List<PhoneInfo> phoneInfos = new ArrayList<>();
    private RecyclerView recycler_view_call;
    private PCardAdapter adapter;

    @Override
    protected void initView() {
        userInfoBean = UserLocalInfoUtils.instance().getmUserInfoBean();
        img_more = findViewById(R.id.img_more);
        img_more.setImageResource(R.mipmap.menu_qrcode);
        findViewById(R.id.view_line_bg).setVisibility(View.VISIBLE);
        img_header_bg = findViewById(R.id.img_header_bg);
        img_user_pic = findViewById(R.id.img_user_pic);
        tv_user_name = findViewById(R.id.tv_user_name);

        findViewById(R.id.relative_layout_title).setBackgroundResource(R.color.transparent);

        setTitleText(userInfoBean.getUserName());
        tv_user_name.setText(userInfoBean.getUserName());
        GlideUtils.getInstance().setImgTransRes(mContext, userInfoBean.getAvatar(), img_header_bg, 0, 0);
        GlideUtils.getInstance().setRadiusImageView(mContext, userInfoBean.getAvatar(), img_user_pic, 10);

        recycler_view_call = findViewById(R.id.recycler_view_call);
        recycler_view_call.setLayoutManager(new MyLinearLayoutManager(mContext));
        recycler_view_call.addItemDecoration(new RecyclerItemDecoration(mContext));
        adapter = new PCardAdapter(phoneInfos);
        recycler_view_call.setAdapter(adapter);

        recycler_view_sign = findViewById(R.id.recycler_view_sign);
        recycler_view_sign.setLayoutManager(new MyGridLayoutManager(mContext, 4));
        recycler_view_call.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.HORIZONTAL_LIST, 10));
        sAdapter = new BaseQuickAdapter<SignNameEntity, BaseViewHolder>(R.layout.adapter_pic) {
            @Override
            protected void convert(BaseViewHolder helper, SignNameEntity item) {
                ImageView img_phone_pic = helper.getView(R.id.img_phone_pic);
                GlideUtils.getInstance().setImageViewWH(mContext, item.getPicUrl(), img_phone_pic, UIHelper.dip2px(100));

            }
        };
        recycler_view_sign.setAdapter(sAdapter);

        getPhoneDressList();

        getUserSigns();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        img_header_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        img_user_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        findViewById(R.id.linear_layout_signs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignListActivity.startSignListActivity(mContext, userInfoBean.getUserId());

            }
        });
    }

    /**
     * 获取用户个性书签
     */
    private void getUserSigns() {
        HttpParams params = new HttpParams();
        params.put("userId", UserLocalInfoUtils.instance().getUserId());
        params.put("friendId", UserLocalInfoUtils.instance().getUserId());
        params.put("pageNum", 0);
        params.put("pageSize", 4);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_SIGN_NAME_LIST, new JsonCallBack<ResultBean<List<SignNameEntity>>>() {
            @Override
            public void onSuccess(ResultBean<List<SignNameEntity>> mResultBean) {
                sAdapter.setNewData(mResultBean.data);

            }

            @Override
            public void onError(ResultBean<List<SignNameEntity>> mResultBean) {


            }
        });
    }

    /**
     * 获取手机号归属地
     */
    private void getPhoneDressList() {
        HttpParams params = new HttpParams();
        params.put("mobile", UserLocalInfoUtils.instance().getmUserInfoBean().getMobile());
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_PHONE_DRESS, new JsonCallBack<ResultBean<List<PhoneInfo>>>() {
            @Override
            public void onSuccess(ResultBean<List<PhoneInfo>> mResultBean) {
                adapter.setNewData(mResultBean.data);

            }

            @Override
            public void onError(ResultBean<List<PhoneInfo>> mResultBean) {
                UIHelper.ToastMessage(mContext, mResultBean.msg);
                ActivityUtils.finishActivity(mContext);

            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_user_card;
    }
}
