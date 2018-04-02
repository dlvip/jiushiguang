package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.constants.Constant;
import com.old.time.okhttps.Http;
import com.old.time.okhttps.exception.ApiException;
import com.old.time.okhttps.subscriber.CommonSubscriber;
import com.old.time.okhttps.transformer.CommonTransformer;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.MapParams;
import com.old.time.utils.StringUtils;
import com.old.time.utils.UIHelper;

import java.util.Timer;
import java.util.TimerTask;

public class UserRegisterActivity extends BaseActivity {

    /**
     * 注册、修改密码
     *
     * @param mContext
     */
    public static void startUserRegisterActivity(Activity mContext, String mRegisterTag, String mPhoneStr) {
        Intent intent = new Intent(mContext, UserRegisterActivity.class);
        intent.putExtra("mRegisterTag", mRegisterTag);
        intent.putExtra("mPhoneStr", mPhoneStr);
        ActivityUtils.startActivity(mContext, intent);

    }

    private Timer timer;
    private int times = 0;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what > 0 && msg.what <= 100) {
                times = msg.what;
                tv_get_code.setText(msg.what + " S");

            } else {
                times = 0;
                tv_get_code.setText("获取验证码");
                timer.cancel();

            }
        }
    };

    private String mRegisterTag, mPhoneStr;

    private TextView input_phone, tv_get_code, tv_pass_word;
    private EditText input_code, input_pass_word;

    @Override
    protected void initView() {
        mRegisterTag = getIntent().getStringExtra("mRegisterTag");
        mPhoneStr = getIntent().getStringExtra("mPhoneStr");
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        tv_get_code = findViewById(R.id.tv_get_code);
        tv_pass_word = findViewById(R.id.tv_pass_word);
        input_code = findViewById(R.id.input_code);
        input_pass_word = findViewById(R.id.input_pass_word);
        input_phone = findViewById(R.id.input_phone);
        input_phone.setText(mPhoneStr);
        if ("0".equals(mRegisterTag)) {
            setTitleText("注册");
            tv_pass_word.setText("忘记密码");

        } else {
            setTitleText("忘记密码");
            tv_pass_word.setText("注册");

        }
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        findViewById(R.id.btn_submit).setOnClickListener(this);
        findViewById(R.id.tv_get_code).setOnClickListener(this);
        findViewById(R.id.tv_pass_word).setOnClickListener(this);
        findViewById(R.id.tv_user_login).setOnClickListener(this);

    }

    /**
     * 获取验证码
     */
    private void getPhoneCode() {
        if (times != 0) {//时间在倒计时

            return;
        }
        String phoneStr = input_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phoneStr)) {
            UIHelper.ToastMessage(mContext, "请输入手机号");

            return;
        }
        if (!StringUtils.isMobileNO(phoneStr)) {
            UIHelper.ToastMessage(mContext, "请输入正确的手机号");

            return;
        }
        MapParams mMapParams = new MapParams();
        mMapParams.putParams("phone", phoneStr);
        Http.getHttpService().login(Constant.GET_PHONE_CODE, mMapParams.getParamString()).compose(new CommonTransformer<>()).subscribe(new CommonSubscriber<Object>(mContext) {
            @Override
            public void onNext(Object loginBean) {
                timer = new Timer();// 开启计时器
                timer.schedule(new TimerTask() {
                    int i = 60;// 倒数60秒

                    @Override
                    public void run() {// 定义一个消息传过去
                        Message msg = new Message();
                        msg.what = i--;
                        handler.sendMessage(msg);

                    }
                }, 0, 1000);// 开始倒计时，倒计时间隔为1秒
            }

            @Override
            protected void onError(ApiException e) {
                super.onError(e);

            }
        });
    }

    /**
     * 用户注册
     */
    private void userRegister() {
        String phoneStr = input_phone.getText().toString().trim();
        if (!StringUtils.isMobileNO(phoneStr)) {
            UIHelper.ToastMessage(mContext, "请输入正确的手机号");

            return;
        }
        String codeStr = input_code.getText().toString().trim();
        if (TextUtils.isEmpty(codeStr)) {
            UIHelper.ToastMessage(mContext, "请输入验证码");

            return;
        }
        String passWordStr = input_pass_word.getText().toString().trim();
        if (TextUtils.isEmpty(passWordStr)) {
            UIHelper.ToastMessage(mContext, "请输入密码");

            return;
        }
        MapParams mMapParams = new MapParams();
        mMapParams.putParams("usename", phoneStr);
        mMapParams.putParams("password", passWordStr);
        mMapParams.putParams("code", codeStr);
        Http.getHttpService().login(Constant.USER_REGISTER, mMapParams.getParamString()).compose(new CommonTransformer<>()).subscribe(new CommonSubscriber<Object>(mContext) {
            @Override
            public void onNext(Object loginBean) {
                Intent intent = new Intent(mContext, MainActivity.class);
                ActivityUtils.startActivity(mContext, intent);
                ActivityUtils.finishActivity(mContext);

            }

            @Override
            protected void onError(ApiException e) {
                super.onError(e);

            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_get_code:
                getPhoneCode();

                break;
            case R.id.tv_pass_word:
                String phoneStr = input_phone.getText().toString().trim();
                ActivityUtils.finishActivity(mContext);
                startUserRegisterActivity(mContext, "1", phoneStr);

                break;
            case R.id.tv_user_login:
                ActivityUtils.finishActivity(mContext);

                break;
            case R.id.btn_submit:
                userRegister();

                break;
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_user_register;
    }
}
