package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Result;
import com.old.time.R;
import com.old.time.beans.ResultBean;
import com.old.time.beans.UserInfoBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.StringUtils;
import com.old.time.utils.UIHelper;
import com.old.time.utils.UserLocalInfoUtils;

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

    private Timer timer = new Timer();
    private int times = 100;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what > 0 && msg.what < 100) {
                times = msg.what;
                tv_get_code.setText(msg.what + " S");

            } else {
                times = 100;
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
        if (times == 100) {//时间在倒计时

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
        HttpParams params = new HttpParams();
        params.put("mobile", phoneStr);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_PHONE_CODE, new JsonCallBack<ResultBean<String>>() {
            @Override
            public void onSuccess(ResultBean<String> mResultBean) {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(times--);

                    }
                }, 1000);
            }

            @Override
            public void onError(ResultBean<String> mResultBean) {
                UIHelper.ToastMessage(mContext, mResultBean.msg);

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
        HttpParams params = new HttpParams();
        params.put("mobile", phoneStr);
        params.put("pasWord", StringUtils.encodeByMD5(passWordStr));
        params.put("code", codeStr);
        OkGoUtils.getInstance().postNetForData(params, Constant.USER_REGISTER, new JsonCallBack<ResultBean<UserInfoBean>>() {
            @Override
            public void onSuccess(ResultBean<UserInfoBean> mResultBean) {
                if (mResultBean == null || mResultBean.data == null) {

                    return;
                }
                UserLocalInfoUtils.instance().setmUserInfoBean(mResultBean.data);

            }

            @Override
            public void onError(ResultBean<UserInfoBean> mResultBean) {
                UIHelper.ToastMessage(mContext, mResultBean.msg);

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
