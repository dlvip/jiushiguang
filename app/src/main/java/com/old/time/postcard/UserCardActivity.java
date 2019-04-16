package com.old.time.postcard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.activitys.BaseActivity;
import com.old.time.activitys.PhotoPagerActivity;
import com.old.time.activitys.RQCodeActivity;
import com.old.time.activitys.TouchSettingActivity;
import com.old.time.activitys.UserLoginActivity;
import com.old.time.activitys.UserMsgActivity;
import com.old.time.beans.PhoneInfo;
import com.old.time.beans.RQCodeBean;
import com.old.time.beans.ResultBean;
import com.old.time.beans.SignNameEntity;
import com.old.time.beans.UserInfoBean;
import com.old.time.constants.Constant;
import com.old.time.dialogs.DialogListManager;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.interfaces.OnClickManagerCallBack;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.Base64Utils;
import com.old.time.utils.MyGridLayoutManager;
import com.old.time.utils.MyLinearLayoutManager;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;
import com.old.time.utils.UserLocalInfoUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserCardActivity extends BaseActivity {

    private static final String FRIEND_ID = "friendId";

    /**
     * 个人中心
     *
     * @param context
     */
    public static void startUserCardActivity(Context context, String friendId) {
        if (!UserLocalInfoUtils.instance().isUserLogin()) {
            UserLoginActivity.startUserLoginActivity((Activity) context);

            return;
        }
        Intent intent = new Intent(context, UserCardActivity.class);
        intent.putExtra(FRIEND_ID, friendId);
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

    private View view_line_bg, relative_layout_phone, linear_layout_call;

    private String friendId;

    @Override
    protected void initView() {
        friendId = getIntent().getStringExtra(FRIEND_ID);
        img_header_bg = findViewById(R.id.img_header_bg);
        img_user_pic = findViewById(R.id.img_user_pic);
        tv_user_name = findViewById(R.id.tv_user_name);

        findViewById(R.id.relative_layout_title).setBackgroundResource(R.color.transparent);

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

        img_more = findViewById(R.id.img_more);
        view_line_bg = findViewById(R.id.view_line_bg);
        relative_layout_phone = findViewById(R.id.relative_layout_phone);
        linear_layout_call = findViewById(R.id.linear_layout_call);
        if (friendId.equals(UserLocalInfoUtils.instance().getUserId())) {
            view_line_bg.setVisibility(View.GONE);
            img_more.setImageResource(R.mipmap.btn_more);
            relative_layout_phone.setVisibility(View.VISIBLE);
            linear_layout_call.setVisibility(View.GONE);

        } else {
            view_line_bg.setVisibility(View.VISIBLE);
            img_more.setImageResource(R.mipmap.menu_qrcode);
            relative_layout_phone.setVisibility(View.GONE);
            linear_layout_call.setVisibility(View.VISIBLE);

        }

        getUserInfo();

        getUserSigns();
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        HttpParams params = new HttpParams();
        params.put("userId", friendId);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_USER_INFO, new JsonCallBack<ResultBean<UserInfoBean>>() {
            @Override
            public void onSuccess(ResultBean<UserInfoBean> mResultBean) {
                setDateForView(mResultBean.data);

            }

            @Override
            public void onError(ResultBean<UserInfoBean> mResultBean) {
                UIHelper.ToastMessage(mContext, mResultBean.msg);

            }
        });
    }

    private void setDateForView(UserInfoBean mUserInfoBean) {
        if (mUserInfoBean == null) {

            return;
        }
        this.userInfoBean = mUserInfoBean;
        if (friendId.equals(UserLocalInfoUtils.instance().getUserId())) {
            UserLocalInfoUtils.instance().setmUserInfoBean(mUserInfoBean);

        }
        setTitleText(userInfoBean.getUserName());
        tv_user_name.setText(userInfoBean.getUserName());
        GlideUtils.getInstance().setImgTransRes(mContext, userInfoBean.getAvatar(), img_header_bg, 0, 0);
        GlideUtils.getInstance().setRadiusImageView(mContext, userInfoBean.getAvatar(), img_user_pic, 10);

        getPhoneDressList();

    }

    @Override
    protected void initEvent() {
        super.initEvent();
        img_header_bg.setOnClickListener(this);
        img_user_pic.setOnClickListener(this);
        linear_layout_call.setOnClickListener(this);
        findViewById(R.id.view_signs).setOnClickListener(this);
        findViewById(R.id.relative_layout_more).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (userInfoBean == null) {

            return;
        }
        switch (view.getId()) {
            case R.id.img_header_bg:

                break;
            case R.id.img_user_pic:
                List<String> picS = Collections.singletonList(userInfoBean.getAvatar());
                PhotoPagerActivity.startPhotoPagerActivity(mContext//
                        , (Serializable) picS, 0);
                break;
            case R.id.view_signs:
                UserSignActivity.startUserSignActivity(mContext, userInfoBean);

                break;
            case R.id.relative_layout_more:
                showMoreSetDialog();

                break;
            case R.id.linear_layout_call:
                UIHelper.ToastMessage(mContext, "非通讯录好友暂未开放");

                break;
        }
    }

    private DialogListManager dialogListManager;

    /**
     * 更多设置
     */
    private void showMoreSetDialog() {
        if (!friendId.equals(UserLocalInfoUtils.instance().getUserId())) {
            RQCodeActivity.startRQCodeActivity(mContext//
                    , Base64Utils.encodeToString(RQCodeBean.getInstance(RQCodeBean.MSG_TAG_USER_INFO//
                            , friendId)), userInfoBean.getAvatar());
            return;
        }
        if (dialogListManager == null) {
            dialogListManager = new DialogListManager(mContext, new OnClickManagerCallBack() {
                @Override
                public void onClickRankManagerCallBack(int position, String typeName) {
                    switch (typeName) {
                        case "编辑信息":
                            UserMsgActivity.startUserMsgActivity(mContext);

                            break;
                        case "名片二维码":
                            RQCodeActivity.startRQCodeActivity(mContext//
                                    , Base64Utils.encodeToString(RQCodeBean.getInstance(RQCodeBean.MSG_TAG_USER_INFO//
                                            , friendId)), userInfoBean.getAvatar());

                            break;
                        case "退出登陆":
                            UserLocalInfoUtils.instance().setUserLogOut();
                            ActivityUtils.finishActivity(mContext);
//                            TouchSettingActivity.startSettingTouchActivity(mContext);

                            break;
                    }
                }
            });
        }
        dialogListManager.setDialogViewData("更多设置", new String[]{"编辑信息", "名片二维码", "退出登陆"});

    }

    /**
     * 获取用户个性书签
     */
    private void getUserSigns() {
        HttpParams params = new HttpParams();
        params.put("userId", UserLocalInfoUtils.instance().getUserId());
        params.put("friendId", friendId);
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
        params.put("mobile", userInfoBean.getMobile());
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
    protected void onResume() {
        super.onResume();
        if (friendId.equals(UserLocalInfoUtils.instance().getUserId()) && userInfoBean != null)
            setDateForView(UserLocalInfoUtils.instance().getmUserInfoBean());

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_user_card;
    }
}
