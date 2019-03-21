package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.postcard.PostCardActivity;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.PreferenceCache;
import com.old.time.utils.UIHelper;
import com.old.time.views.ChaosGestureView;

import java.util.List;

public class TouchPatternPswActivity extends BaseActivity implements ChaosGestureView.GestureCallBack {

    public static String GES_TURE_FLG = "gestureFlg";

    //1：删除密码
    public static final int DELETE_PASS_WORD = 1;

    //2：修改密码
    public static final int UPDATE_PASS_WORD = 2;

    //3：指纹密码
    public static final int TOUCH_PASS_WORD = 3;

    /**
     * 设置修改手势密码
     *
     * @param context
     * @param gestureFlg 1：删除密码、 2：修改密码、3：指纹密码
     */
    public static void startTouchPatternPswActivity(Context context, int gestureFlg) {
        Intent intent = new Intent(context, TouchPatternPswActivity.class);
        intent.putExtra(GES_TURE_FLG, gestureFlg);
        ActivityUtils.startActivityForResult((Activity) context, intent, 1);
        ActivityUtils.finishActivity((Activity) context);

    }

    private ChaosGestureView gestureView;
    private TextView tv_user_name;
    private int gestureFlg = -1;


    @Override
    protected void initView() {
        gestureFlg = getIntent().getIntExtra(GES_TURE_FLG, -1);
        gestureView = findViewById(R.id.gesture1);
        tv_user_name = findViewById(R.id.tv_user_name);
        gestureView.setGestureCallBack(this);
        gestureView.clearCacheLogin();
    }

    @Override
    public void gestureVerifySuccessListener(int stateFlag, List<ChaosGestureView.GestureBean> data, boolean success) {
        if (success) {
            if (gestureFlg == 1) {//删除密码
                PreferenceCache.putGestureFlag(false);
                gestureView.clearCache();
                UIHelper.ToastMessage(mContext, "清空手势密码成功");
                DynamicsActivity.startDynamicsActivity(mContext);

            } else if (gestureFlg == 2) {//修改密码
                UIHelper.ToastMessage(mContext, "验证手势密码成功,请重新设置");
                DynamicsActivity.startDynamicsActivity(mContext);

            } else if (gestureFlg == 3) {//指纹开启
                DynamicsActivity.startDynamicsActivity(mContext);

            }
        } else {

        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_close_pattern_psw;
    }
}
