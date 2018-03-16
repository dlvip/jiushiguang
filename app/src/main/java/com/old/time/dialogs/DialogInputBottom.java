package com.old.time.dialogs;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.TextView;

import com.old.time.R;

/**
 * Created by NING on 2018/3/16.
 */

public class DialogInputBottom extends BaseDialog {


    public DialogInputBottom(@NonNull Activity context) {
        super(context, R.style.dialog_setting_edt);

    }

    private EditText edt_input_content;
    private TextView tv_send_true;

    @Override
    protected void initDialogView() {
        edt_input_content = findViewById(R.id.edt_input_content);
        edt_input_content = findViewById(R.id.tv_send_true);

    }

    @Override
    protected int setContentView() {
        return R.layout.dialog_input_text;
    }
}
