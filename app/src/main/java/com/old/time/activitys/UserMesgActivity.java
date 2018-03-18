package com.old.time.activitys;

import android.view.View;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.dialogs.DialogInputBottom;
import com.old.time.interfaces.OnClickManagerCallBack;

public class UserMesgActivity extends BaseActivity {

    private TextView tv_edt_phone, tv_edt_nick, tv_edt_address, tv_edt_brief;

    @Override
    protected void initView() {
        tv_edt_phone = findViewById(R.id.tv_edt_phone);
        tv_edt_nick = findViewById(R.id.tv_edt_nick);
        tv_edt_address = findViewById(R.id.tv_edt_address);
        tv_edt_brief = findViewById(R.id.tv_edt_brief);


    }

    @Override
    protected void initEvent() {
        super.initEvent();
        findViewById(R.id.linear_layout_phone).setOnClickListener(this);
        findViewById(R.id.linear_layout_address).setOnClickListener(this);
        findViewById(R.id.linear_layout_brief).setOnClickListener(this);

    }

    private DialogInputBottom mDialogInputBottom;
    private int mDialogType;

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.linear_layout_phone:
                getmDialogInputBottom(0);
                mDialogInputBottom.showDialog(R.string.edt_nickname, R.string.dialog_true);

                break;
            case R.id.linear_layout_address:


                break;
            case R.id.linear_layout_brief:
                getmDialogInputBottom(1);
                mDialogInputBottom.showDialog(R.string.user_set_brief, R.string.dialog_true);

                break;
        }
    }

    /**
     * 获取输入框弹窗
     */
    private void getmDialogInputBottom(int DialogType) {
        this.mDialogType = DialogType;
        if (mDialogInputBottom == null) {
            mDialogInputBottom = new DialogInputBottom(mContext, new OnClickManagerCallBack() {
                @Override
                public void onClickRankManagerCallBack(int typeId, String typeName) {
                    switch (mDialogType) {
                        case 0:
                            if (tv_edt_nick == null) {

                                return;
                            }
                            tv_edt_nick.setText(typeName);

                            break;
                        case 1:
                            if (tv_edt_brief == null) {

                                return;
                            }
                            tv_edt_brief.setText(typeName);

                            break;
                    }
                }
            });
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_user_mesg;
    }
}
