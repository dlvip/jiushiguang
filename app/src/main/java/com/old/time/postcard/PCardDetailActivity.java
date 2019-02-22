package com.old.time.postcard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import java.util.Arrays;
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

    }

    /**
     * 获取手机号归属地
     *
     * @param phone
     * @return
     */
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

//    private void logonToServer() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String deviceId = PhoneInfoUtils.instance().getDeviceId(mContext);
//                if (TextUtils.isEmpty(deviceId)) {
//                    deviceId = RongRTCSessionManager.getInstance().getString(RongRTCContext.RONGRTC_UUID);
//
//                }
//                String serverURL = "https://rtcapi.ronghub.com/voiptoken";
//                final String token = RongRTCHttpClient.getInstance().doPost(serverURL, "uid=" + deviceId + "&appid=" + AppKey);
//                if (TextUtils.isEmpty(token) || TextUtils.isEmpty(deviceId)) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            UIHelper.ToastMessage(mContext, "获取token/deviceID失败，请重新尝试！");
//
//                        }
//                    });
//                    return;
//                }
//                RongRTCEngine.getInstance().joinChannel(deviceId, "15093073252", token, "123456");
//            }
//        }).start();
//
//        RongRTCEngine.getInstance().setRongRTCEngineEventHandler(new RongRTCEngineEventHandler() {
//
//            @Override
//            public void onConnectionStateChanged(int i) {
//                UIHelper.ToastMessage(mContext, "onConnectionStateChanged");
//            }
//
//            @Override
//            public void onLeaveComplete(boolean b) {
//                UIHelper.ToastMessage(mContext, "onLeaveComplete");
//            }
//
//            @Override
//            public void onUserJoined(String s, String s1, RongRTCEngine.UserType userType, long l, int i) {
//                UIHelper.ToastMessage(mContext, "onUserJoined");
//            }
//
//            @Override
//            public void onNotifyUserVideoCreated(String s, String s1, RongRTCEngine.UserType userType, long l, int i) {
//                UIHelper.ToastMessage(mContext, "onNotifyUserVideoCreated");
//            }
//
//            @Override
//            public void onUserLeft(String s) {
//                UIHelper.ToastMessage(mContext, "onUserLeft");
//            }
//
//            @Override
//            public void OnNotifyUserVideoDestroyed(String s) {
//                UIHelper.ToastMessage(mContext, "OnNotifyUserVideoDestroyed");
//            }
//
//            @Override
//            public void onWhiteBoardURL(String s) {
//                UIHelper.ToastMessage(mContext, "onWhiteBoardURL");
//            }
//
//            @Override
//            public void onNetworkSentLost(int i) {
//                UIHelper.ToastMessage(mContext, "onNetworkSentLost");
//            }
//
//            @Override
//            public void onNetworkReceiveLost(int i) {
//                UIHelper.ToastMessage(mContext, "onNetworkReceiveLost");
//            }
//
//            @Override
//            public void onControlAudioVideoDevice(int i) {
//                UIHelper.ToastMessage(mContext, "onControlAudioVideoDevice");
//            }
//
//            @Override
//            public void onNotifyControlAudioVideoDevice(String s, RongRTCEngine.RongRTCDeviceType rongRTCDeviceType, boolean b) {
//                UIHelper.ToastMessage(mContext, "onNotifyControlAudioVideoDevice");
//            }
//
//            @Override
//            public void onNotifyCreateWhiteBoard(String s) {
//                UIHelper.ToastMessage(mContext, "onNotifyCreateWhiteBoard");
//            }
//
//            @Override
//            public void onNotifySharingScreen(String s, boolean b) {
//                UIHelper.ToastMessage(mContext, "onNotifySharingScreen");
//            }
//        });
//    }

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
