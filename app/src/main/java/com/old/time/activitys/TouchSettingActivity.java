package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.old.time.R;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.PreferenceCache;
import com.old.time.utils.UIHelper;
import com.wei.android.lib.fingerprintidentify.FingerprintIdentify;

public class TouchSettingActivity extends BaseActivity {

    /**
     * 开启手势/指纹锁
     *
     * @param context
     */
    public static void startSettingTouchActivity(Context context) {
        Intent intent = new Intent(context, TouchSettingActivity.class);
        ActivityUtils.startActivity((Activity) context, intent);

    }

    private ImageView mIvHandSwitch;
    private ImageView mIvFingerSwitch;
    private LinearLayout mLinearLayoutSetting;
    private View mView, linear_layout_touch;
    private FingerprintIdentify mFingerprintIdentify;
    private ImageView iv_icon_main;

    @Override
    protected void initView() {
        mIvHandSwitch = findViewById(R.id.iv_hand_switch);
        mIvFingerSwitch = findViewById(R.id.iv_fingerprint_switch);
        mIvFingerSwitch.setOnClickListener(this);
        mIvHandSwitch.setOnClickListener(this);
        mLinearLayoutSetting = findViewById(R.id.ll_setting_hand);
        mLinearLayoutSetting.setOnClickListener(this);
        mView = findViewById(R.id.view_second);
        linear_layout_touch = findViewById(R.id.linear_layout_touch);
        mFingerprintIdentify = new FingerprintIdentify(this);
        iv_icon_main = findViewById(R.id.iv_icon_main);

        setBtnState();
    }

    private void setBtnState() {
        if (PreferenceCache.getGestureFlag()) {
            mIvHandSwitch.setImageResource(R.mipmap.auto_bidding_off);
            mLinearLayoutSetting.setVisibility(View.VISIBLE);
            mView.setVisibility(View.VISIBLE);

        } else {
            mIvHandSwitch.setImageResource(R.mipmap.auto_bidding_on);
            mLinearLayoutSetting.setVisibility(View.GONE);
            mView.setVisibility(View.GONE);

        }
        if (mFingerprintIdentify.isHardwareEnable()) {
            linear_layout_touch.setVisibility(View.VISIBLE);

        } else {
            linear_layout_touch.setVisibility(View.GONE);

        }

        if (PreferenceCache.getFingerFlg()) {
            mIvFingerSwitch.setImageResource(R.mipmap.auto_bidding_off);

        } else {
            mIvFingerSwitch.setImageResource(R.mipmap.auto_bidding_on);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_hand_switch:
                if (!PreferenceCache.getGestureFlag()) {
                    TouchSettingPswActivity.startTouchSettingPswActivity(mContext);

                } else {
                    TouchPatternPswActivity.startTouchPatternPswActivity(mContext, TouchPatternPswActivity.DELETE_PASS_WORD);

                }
                break;
            case R.id.ll_setting_hand:
                TouchPatternPswActivity.startTouchPatternPswActivity(mContext, TouchPatternPswActivity.UPDATE_PASS_WORD);

                break;
            case R.id.iv_fingerprint_switch:
                if (!mFingerprintIdentify.isHardwareEnable()) {

                    break;
                }
                //指纹可用
                if (mFingerprintIdentify.isFingerprintEnable()) {
                    if (PreferenceCache.getFingerFlg()) {
                        //取消指纹
                        mIvFingerSwitch.setImageResource(R.mipmap.auto_bidding_on);
                        UIHelper.ToastMessage(mContext, "指纹验证功能已取消");
                        PreferenceCache.putFingerFlg(false);

                    } else {
                        //打开指纹
                        mIvFingerSwitch.setImageResource(R.mipmap.auto_bidding_off);
                        UIHelper.ToastMessage(mContext, "指纹验证功能已打开");
                        PreferenceCache.putFingerFlg(true);

                    }

                } else {
                    UIHelper.ToastMessage(mContext, "请先去录入指纹");

                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            setBtnState();

        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_setting_touch;
    }
}
