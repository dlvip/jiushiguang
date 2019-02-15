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

    public static void startTouchPatternPswActivity(Context context, int gestureFlg) {
        Intent intent = new Intent(context, TouchPatternPswActivity.class);
        intent.putExtra(GES_TURE_FLG, gestureFlg);
        ActivityUtils.startActivity((Activity) context, intent);
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
                PostCardActivity.startPostCardActivity(mContext);

            } else if (gestureFlg == 2) {//修改密码
                UIHelper.ToastMessage(mContext, "验证手势密码成功,请重新设置");
                TouchSettingPswActivity.startTouchSettingPswActivity(mContext);

            } else if (gestureFlg == 3) {
                PostCardActivity.startPostCardActivity(mContext);

            }
        } else {

        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_close_pattern_psw;
    }
}
