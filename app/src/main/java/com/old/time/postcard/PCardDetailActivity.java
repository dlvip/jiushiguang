package com.old.time.postcard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.activitys.BaseActivity;
import com.old.time.activitys.RQCodeActivity;
import com.old.time.activitys.UserMesgActivity;
import com.old.time.beans.PhoneBean;
import com.old.time.beans.PhoneInfo;
import com.old.time.beans.ResultBean;
import com.old.time.constants.Constant;
import com.old.time.dialogs.DialogListManager;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.interfaces.OnClickManagerCallBack;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.Base64Utils;
import com.old.time.utils.DataUtils;
import com.old.time.utils.MyLinearLayoutManager;
import com.old.time.utils.PhoneUtils;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;

import java.util.List;

import io.rong.callkit.RongCallKit;

public class PCardDetailActivity extends BaseActivity {

    /**
     * 联系人详情页
     *
     * @param context
     */
    public static void startPCardDetailActivity(Context context, PhoneBean phoneBean) {
        Intent intent = new Intent(context, PCardDetailActivity.class);
        intent.putExtra(PHONE_INFO, phoneBean);
        ActivityUtils.startActivity((Activity) context, intent);

    }

    public static final String PHONE_INFO = "phoneInfo";

    private BaseQuickAdapter<PhoneInfo, BaseViewHolder> adapter;

    private ImageView img_header_bg, img_user_pic, img_more;
    private RecyclerView recycler_view_call;
    private TextView tv_user_name;

    @Override
    protected void initView() {
        mPhoneBean = (PhoneBean) getIntent().getSerializableExtra(PHONE_INFO);
        img_more = findViewById(R.id.img_more);
        img_more.setImageResource(R.mipmap.menu_qrcode);
        findViewById(R.id.view_line_bg).setVisibility(View.VISIBLE);
        img_header_bg = findViewById(R.id.img_header_bg);
        img_user_pic = findViewById(R.id.img_user_pic);
        tv_user_name = findViewById(R.id.tv_user_name);
        recycler_view_call = findViewById(R.id.recycler_view_call);

        img_user_pic.setOnClickListener(this);
        findViewById(R.id.tv_call_phone).setOnClickListener(this);
        findViewById(R.id.tv_call_video).setOnClickListener(this);
        findViewById(R.id.relative_layout_more).setOnClickListener(this);
        findViewById(R.id.relative_layout_title).setBackgroundResource(R.color.transparent);

        setTitleText(mPhoneBean.getName());
        tv_user_name.setText(mPhoneBean.getName());
        GlideUtils.getInstance().setImgTransRes(mContext, mPhoneBean.getPhoto(), img_header_bg, 0, 0);
        GlideUtils.getInstance().setRadiusImageView(mContext, mPhoneBean.getPhoto(), img_user_pic, 10);
        recycler_view_call.setLayoutManager(new MyLinearLayoutManager(mContext));
        recycler_view_call.addItemDecoration(new RecyclerItemDecoration(mContext));
        adapter = new BaseQuickAdapter<PhoneInfo, BaseViewHolder>(R.layout.adapter_phone_detail) {
            @Override
            protected void convert(BaseViewHolder helper, PhoneInfo item) {
                helper.setText(R.id.tv_phone_num, item.getPhone())//
                        .setText(R.id.tv_phone_dress, item.getPhoneDress())//
                        .setVisible(R.id.view_line, helper.getLayoutPosition() != 0);
                if (TextUtils.isEmpty(item.getPhoneDress())) {
                    DataUtils.getPhoneMsg(item.getPhone());

                }
            }
        };
        recycler_view_call.setAdapter(adapter);
        getPhoneDressList();
    }

    /**
     * 获取手机号归属地
     */
    private void getPhoneDressList() {
        HttpParams params = new HttpParams();
        params.put("mobile", mPhoneBean.getNumber());
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
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_call_phone:
                showCallPhoneDialog();

                break;
            case R.id.tv_call_video:
                showVideoAndVoiceDialog();

                break;
            case R.id.relative_layout_more:
                RQCodeActivity.startRQCodeActivity(mContext, Base64Utils.encodeToString(mPhoneBean));

                break;
            case R.id.img_user_pic:

                break;
        }
    }

    private DialogListManager mCallDialog;
    private PhoneBean mPhoneBean;

    /**
     * 拨打电话弹框
     */
    private void showCallPhoneDialog() {
        if (mCallDialog == null) {
            mCallDialog = new DialogListManager(mContext, new OnClickManagerCallBack() {
                @Override
                public void onClickRankManagerCallBack(int position, String typeName) {
                    PhoneUtils.callPhone(mContext, mPhoneBean.getNumber().split(",")[0]);

                }
            });
        }
        mCallDialog.setDialogViewData("拨号", new String[]{mPhoneBean.getName()});

    }

    private DialogListManager dialogListManager;

    /**
     * 音视频通话弹框
     */
    private void showVideoAndVoiceDialog() {
        if (dialogListManager == null) {
            dialogListManager = new DialogListManager(mContext, new OnClickManagerCallBack() {

                @Override
                public void onClickRankManagerCallBack(int position, String typeName) {
                    String phone = mPhoneBean.getNumber().split(",")[0];
                    switch (position) {
                        case 0:
                            RongCallKit.startSingleCall(mContext, phone, RongCallKit.CallMediaType.CALL_MEDIA_TYPE_AUDIO);

                            break;
                        case 1:
                            RongCallKit.startSingleCall(mContext, phone, RongCallKit.CallMediaType.CALL_MEDIA_TYPE_VIDEO);

                            break;
                    }
                }
            });
        }
        dialogListManager.setDialogViewData("音视频通话", new String[]{"语音通话", "视频通话"});
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_pcard_detail;
    }
}
