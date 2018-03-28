package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.utils.ActivityUtils;

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

    private String mRegisterTag, mPhoneStr;

    private TextView input_phone;

    @Override
    protected void initView() {
        mRegisterTag = getIntent().getStringExtra("mRegisterTag");
        mPhoneStr = getIntent().getStringExtra("mPhoneStr");
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        input_phone = findViewById(R.id.input_phone);
        setTitleText("0".equals(mRegisterTag) ? "注册" : "忘记密码");
        input_phone.setText(mPhoneStr);

    }

    @Override
    protected void initEvent() {
        super.initEvent();
        findViewById(R.id.tv_pass_word).setOnClickListener(this);
        findViewById(R.id.tv_user_register).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_pass_word:
                String phoneStr = input_phone.getText().toString().trim();
                startUserRegisterActivity(mContext, "1", phoneStr);

                break;
            case R.id.tv_user_register:
                ActivityUtils.finishActivity(mContext);

                break;
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_user_register;
    }
}
