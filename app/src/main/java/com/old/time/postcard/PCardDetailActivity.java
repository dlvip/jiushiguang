package com.old.time.postcard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.activitys.BaseActivity;
import com.old.time.beans.PhoneApiBean;
import com.old.time.beans.PhoneBean;
import com.old.time.beans.PhoneInfo;
import com.old.time.constants.Constant;
import com.old.time.dialogs.DialogListManager;
import com.old.time.dialogs.DialogQRCode;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.interfaces.OnClickManagerCallBack;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.BitmapUtils;
import com.old.time.utils.DataUtils;
import com.old.time.utils.DebugLog;
import com.old.time.utils.MyLinearLayoutManager;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.rong.callkit.RongCallKit;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallCommon;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

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

    private BaseQuickAdapter<String, BaseViewHolder> adapter;

    private ImageView img_header_bg, img_user_pic, img_more;
    private RecyclerView recycler_view_call;
    private TextView tv_user_name;

    private List<PhoneInfo> phoneInfoList;

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

        phoneInfoList = DataUtils.getPhoneBeans(mContext);

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
        adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.adapter_phone_detail) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_phone_num, item)//
                        .setText(R.id.tv_phone_dress, getPhoneInfoStr(item))//
                        .setVisible(R.id.view_line, helper.getLayoutPosition() != 0);

            }
        };
        recycler_view_call.setAdapter(adapter);
        adapter.setNewData(Arrays.asList(mPhoneBean.getNumber().split(",")));

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        connect(Constant.RONG_TOKEN);

//            }
//        }).start();
    }

    private boolean isConnect;

    /**
     * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在 {@link #init(Context)} 之后调用。</p>
     * <p>如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
     *
     * @param token    从服务端获取的用户身份令牌（Token）。
     * @param callback 连接回调。
     * @return RongIM  客户端核心类的实例。
     */
    private void connect(String token) {

        RongIM.connect(token, new RongIMClient.ConnectCallback() {

            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
                UIHelper.ToastMessage(mContext, "Token 错误");
            }

            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
                Log.d("LoginActivity", "--onSuccess" + userid);
                isConnect = true;

            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                UIHelper.ToastMessage(mContext, errorCode.getMessage());

            }
        });
    }

    private String getPhoneInfoStr(String phone) {
        String phoneStr = "";
        if (TextUtils.isEmpty(phone) || phoneInfoList == null || phoneInfoList.size() == 0) {

            return phoneStr;
        }
        for (PhoneInfo phoneInfo : phoneInfoList) {
            if (phone.equals(phoneInfo.getPhone())) {
                phoneStr = phoneInfo.getCompany() //
                        + " - " + phoneInfo.getProvince() //
                        + "、" + phoneInfo.getCity();
            }
        }
        if (TextUtils.isEmpty(phoneStr)) {
            getPhoneMsg(phone);

        }
        return phoneStr;
    }

    private void getPhoneMsg(final String numStr) {
        HttpParams params = new HttpParams();
        params.put("phone", numStr);
        params.put("key", Constant.PHONE_KEY);
        params.put("dtype", "json");
        OkGoUtils.getInstance().getNetForData(params, Constant.PHONE_DRESS, new JsonCallBack<PhoneApiBean>() {

            @Override
            public void onSuccess(PhoneApiBean mResultBean) {
                if (mResultBean == null || mResultBean.getResult() == null) {

                    return;
                }
                PhoneInfo phoneInfo = mResultBean.getResult();
                phoneInfo.setPhone(numStr);
                DebugLog.d(TAG, new Gson().toJson(phoneInfo));

            }

            @Override
            public void onError(PhoneApiBean mResultBean) {
                DebugLog.d(TAG, mResultBean.toString());

            }
        });
    }

    private DialogListManager mCallDialog;
    private PhoneBean mPhoneBean;

    private void showCallPhoneDialog() {
        if (mCallDialog == null) {
            mCallDialog = new DialogListManager(mContext, new OnClickManagerCallBack() {
                @Override
                public void onClickRankManagerCallBack(int position, String typeName) {
                    callPhone(mPhoneBean.getNumber().split(",")[0]);

                }
            });
        }
        mCallDialog.setDialogViewData("拨号", new String[]{mPhoneBean.getName()});

    }

    /**
     * 拨打电话（直接拨打电话）
     *
     * @param phoneNum 电话号码
     */
    private void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        ActivityUtils.startActivity(mContext, intent);

    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void callPhoneBySelf(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        ActivityUtils.startActivity(mContext, intent);

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
                showQRCodeDialog();

                break;
        }
    }

    private DialogListManager dialogListManager;

    private void showVideoAndVoiceDialog() {
        if (!isConnect) {
            UIHelper.ToastMessage(mContext, "链接融云失败");

            return;
        }
        if (dialogListManager == null) {
            dialogListManager = new DialogListManager(mContext, new OnClickManagerCallBack() {
                @Override
                public void onClickRankManagerCallBack(int position, String typeName) {
                    switch (position) {
                        case 0:
                            RongCallKit.startSingleCall(mContext, "15093073252", RongCallKit.CallMediaType.CALL_MEDIA_TYPE_AUDIO);

                            break;
                        case 1:
                            RongCallKit.startSingleCall(mContext, "15093073252", RongCallKit.CallMediaType.CALL_MEDIA_TYPE_VIDEO);

                            break;
                    }
                }
            });
        }
        dialogListManager.setDialogViewData("音视频通话", new String[]{"语音通话", "视频通话"});
    }

    private DialogQRCode mDialogQRCode;

    private void showQRCodeDialog() {
        if (mDialogQRCode == null) {
            mDialogQRCode = new DialogQRCode(mContext, new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showListDialog(v);

                    return false;
                }
            });
        }
        mDialogQRCode.show();
    }

    private DialogListManager mDialogListManager;
    private View qrCodeView;

    private void showListDialog(View mQRCodeView) {
        this.qrCodeView = mQRCodeView;
        if (mDialogListManager == null) {
            mDialogListManager = new DialogListManager(mContext, new OnClickManagerCallBack() {
                @Override
                public void onClickRankManagerCallBack(int position, String typeName) {
                    switch (position) {
                        case 0:
                            Uri uri = BitmapUtils.saveBitmap(mContext, qrCodeView);
                            DebugLog.d(TAG, uri.getPath());///storage/emulated/0/Pictures/觅邮/1550632689371.jpg
//                            Intent imageIntent = new Intent(Intent.ACTION_SEND);
//                            imageIntent.setType("image/jpeg");
//                            imageIntent.putExtra(Intent.EXTRA_STREAM, uri);
//                            startActivity(Intent.createChooser(imageIntent, "分享"));

                            break;
                    }
                }
            });
        }
        mDialogListManager.setDialogViewData("二维码操作", new String[]{"保存到手机"});

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDialogQRCode != null) {
            mDialogQRCode.dismiss();
            mDialogQRCode = null;

        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_pcard_detail;
    }
}
