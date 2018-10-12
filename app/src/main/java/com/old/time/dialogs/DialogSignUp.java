package com.old.time.dialogs;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.old.time.R;

/**
 * Created by NING on 2018/10/12.
 */

public class DialogSignUp extends BaseDialog {

    /**
     * 报名弹框
     *
     * @param context
     */
    public DialogSignUp(@NonNull Activity context) {
        super(context, R.style.transparentFrameWindowStyle);

    }

    @Override
    protected void initDialogView() {


    }

    @Override
    protected int setContentView() {
        return R.layout.dialog_sign_up;
    }
}
