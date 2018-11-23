package com.old.time.activitys;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.old.time.MyApplication;
import com.old.time.R;
import com.old.time.beans.SplashBean;
import com.old.time.constants.Constant;
import com.old.time.service.manager.PlayServiceManager;
import com.old.time.task.CallBackTask;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.AnimUtil;
import com.old.time.utils.ComputeUtils;
import com.old.time.utils.DebugLog;
import com.old.time.utils.SplashDownLoadService;
import com.old.time.utils.StringUtils;

public class SplishActivity extends BaseActivity {

    private RelativeLayout rootLayout, relative_layout_next;
    private TextView versionText, tv_time_next;
    private ImageView img_splish, img_logo;
    private int[] WH;

    @Override
    protected void initView() {
        WH = ComputeUtils.computeImageHeight_135_197(this);
        rootLayout = findViewById(R.id.header_mainr);
        relative_layout_next = findViewById(R.id.relative_layout_next);
        relative_layout_next.setPadding(0, getStatusBarHeight(), 0, 0);
        versionText = findViewById(R.id.tv_version);
        tv_time_next = findViewById(R.id.tv_time_next);
        img_splish = findViewById(R.id.img_splish);
        img_logo = findViewById(R.id.img_logo);

        versionText.setText(getString(R.string.app_name) + " V：" + StringUtils.getVersion(this));
        rootLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                systemUpGrade();

            }
        }, 1000);
        tv_time_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.startMainActivity(mContext);

            }
        });
        img_splish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity();

            }
        });
        img_splish.startAnimation(AnimUtil.getAnimSet(null, AnimUtil.getAlphaAnim(), AnimUtil.getScaleAnim()));


        PlayServiceManager.startPlayService(mContext);

        startClock();
    }

    private void startClock() {
        countDownTimer.start();
    }

    /**
     * 应用是否升级
     */
    private void systemUpGrade() {
        showSplash();
        startImageDownLoad();

    }

    private SplashBean mSplash;

    private void showSplash() {
        try {
            mSplash = SplashDownLoadService.getSplashLocal(Constant.SPLASH_PATH);
            if (mSplash != null && !TextUtils.isEmpty(mSplash.savePath)) {
                img_splish.setVisibility(View.VISIBLE);
                img_logo.setVisibility(View.VISIBLE);
                DebugLog.e(TAG, "SplashActivity 获取本地序列化成功" + mSplash);
                Glide.with(this).load(mSplash.savePath).into(img_splish);
                startClock();

            } else {
                startActivity();

            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    /**
     * 下载启动页面
     */
    private void startImageDownLoad() {
        SplashDownLoadService.startDownLoadSplashImage(this, WH);

    }

    /**
     * 启动应用
     */
    private void startActivity() {
        cancelDownTimer();
        MyApplication.getInstance().getTaskManager().delTask(Constant.IMAGEDOWNLOAD_THREAD_NAME);
        MyApplication.getClient().getTaskManager().addTask(new CallBackTask(Constant.IMAGEDOWNLOAD_THREAD_NAME) {
            @Override
            protected void doTask() {
                MainActivity.startMainActivity(mContext);

            }
        });
    }

    /**
     * 关闭计时器
     */
    private void cancelDownTimer() {
        if (countDownTimer == null) {

            return;
        }
        countDownTimer.cancel();

    }

    private CountDownTimer countDownTimer = new CountDownTimer(3200, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            relative_layout_next.setVisibility(View.VISIBLE);
            tv_time_next.setText("跳过(" + millisUntilFinished / 1000 + "s)");

        }

        @Override
        public void onFinish() {
            tv_time_next.setText("跳过(" + 0 + "s)");
            startActivity();

        }
    };

    @Override
    protected int getLayoutID() {
        return R.layout.activity_splish;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtils.removeActivity(this);
        cancelDownTimer();
        MyApplication.getInstance().getTaskManager().delTask(Constant.IMAGEDOWNLOAD_THREAD_NAME);

    }
}
