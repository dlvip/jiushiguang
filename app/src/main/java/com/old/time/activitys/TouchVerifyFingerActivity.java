package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.dialogs.DialogTouch;
import com.old.time.postcard.PostCardActivity;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.PreferenceCache;
import com.old.time.utils.UIHelper;
import com.wei.android.lib.fingerprintidentify.FingerprintIdentify;
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint;

import static com.old.time.activitys.TouchPatternPswActivity.TOUCH_PASS_WORD;

public class TouchVerifyFingerActivity extends BaseActivity {

    /**
     * 手势解锁
     *
     * @param context
     */
    public static void startTouchVerifyFingerActivity(Context context) {
        Intent intent = new Intent(context, TouchVerifyFingerActivity.class);
        ActivityUtils.startActivity((Activity) context, intent);
        ActivityUtils.finishActivity((Activity) context);

    }

    private ImageView imageView;
    private FingerprintIdentify mFingerprintIdentify;
    private TextView tv_hand_login;
    private DialogTouch mDialogTouch;
    private TextView tv_main_login;
    private ImageView iv_finger_icon;
    private boolean isClick;

    @Override
    protected void initView() {
        iv_finger_icon = findViewById(R.id.iv_finger_icon);
        mFingerprintIdentify = new FingerprintIdentify(this);

        //弹出dialog，自动弹出
        if (mDialogTouch == null) {
            mDialogTouch = new DialogTouch(mContext, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        mDialogTouch.show();
        TextView tv = mDialogTouch.findViewById(R.id.tv_cancel);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialogTouch.dismiss();

            }
        });
        if (mDialogTouch.isShowing()) {
            initVerify();

        }
        tv_hand_login = findViewById(R.id.tv_hand_login);
        if (PreferenceCache.getGestureFlag()) {
            tv_hand_login.setVisibility(View.VISIBLE);

        } else {
            tv_hand_login.setVisibility(View.GONE);

        }

        tv_hand_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TouchPatternPswActivity.startTouchPatternPswActivity(mContext, TouchPatternPswActivity.TOUCH_PASS_WORD);

            }
        });
        imageView = findViewById(R.id.iv_verify_finger);
        imageView.setOnClickListener(this);
        tv_main_login = findViewById(R.id.tv_mian_login);
        tv_main_login.setOnClickListener(this);

    }

    private void initVerify() {
        mFingerprintIdentify.startIdentify(4, new BaseFingerprint.FingerprintIdentifyListener() {

            @Override
            public void onSucceed() {
                PostCardActivity.startPostCardActivity(mContext);

            }

            @Override
            public void onNotMatch(int availableTimes) {
                UIHelper.ToastMessage(mContext, "验证失败，您还有" + availableTimes + "次机会");

            }

            @Override
            public void onFailed(boolean isDeviceLocked) {
                UIHelper.ToastMessage(mContext, "验证失败指纹暂被锁定");
                isClick = true;
                if (mDialogTouch.isShowing()) {
                    mDialogTouch.dismiss();

                }
            }

            @Override
            public void onStartFailedByDeviceLocked() {
                UIHelper.ToastMessage(mContext, "验证失败，指纹已被锁定");

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_verify_finger:
                if (!isClick) {
                    if (!mDialogTouch.isShowing()) {
                        mDialogTouch.show();

                    }
                }

                break;
            case R.id.tv_mian_login:
                UIHelper.ToastMessage(mContext, "到自己登录页面重新登陆,处理逻辑");

                break;
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_verify_finger;
    }
}
