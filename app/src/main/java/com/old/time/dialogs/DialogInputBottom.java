package com.old.time.dialogs;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.interfaces.OnClickManagerCallBack;

/**
 * Created by NING on 2018/3/16.
 */

public class DialogInputBottom extends BaseDialog {

    private OnClickManagerCallBack mOnClickManagerCallBack;

    public DialogInputBottom(@NonNull Activity context, OnClickManagerCallBack mOnClickManagerCallBack) {
        super(context, R.style.dialog_setting_edt);
        this.mOnClickManagerCallBack = mOnClickManagerCallBack;

    }

    public void showDialog(int hintId, int btnId) {
        show();
        if (edt_input_content == null || tv_send_true == null) {

            return;
        }
        edt_input_content.setFocusable(true);
        edt_input_content.setFocusableInTouchMode(true);
        edt_input_content.requestFocus();
        edt_input_content.setText("");
        edt_input_content.setHint(getContext().getString(hintId));
        tv_send_true.setText(getContext().getString(btnId));

    }

    private EditText edt_input_content;
    private TextView tv_send_true;

    @Override
    protected void initDialogView() {
        edt_input_content = findViewById(R.id.edt_input_content);
        tv_send_true = findViewById(R.id.tv_send_true);
        tv_send_true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickManagerCallBack != null) {
                    String contentStr = edt_input_content.getText().toString().trim();
                    if (!TextUtils.isEmpty(contentStr)) {
                        mOnClickManagerCallBack.onClickRankManagerCallBack(0, contentStr);
                        dismiss();

                    }
                }
            }
        });
    }

    @Override
    protected int setContentView() {
        return R.layout.dialog_input_text;
    }
}
