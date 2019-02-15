package com.old.time.dialogs;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

import com.old.time.R;

public class DialogTouch extends BaseDialog{

    private View.OnClickListener onClickViewCallBack;

    public DialogTouch(@NonNull Activity context, View.OnClickListener onClickViewCallBack) {
        super(context, R.style.dialog_setting);
        this.onClickViewCallBack = onClickViewCallBack;

    }

    @Override
    protected void initDialogView() {
        setCancelable(false);
        getContentView().setFocusable(false);
        getContentView().setFocusableInTouchMode(false);
        getContentView().requestFocus();
        findViewbyId(R.id.tv_cancel).setOnClickListener(onClickViewCallBack);

    }

    @Override
    protected int setContentView() {
        return R.layout.dialog_touch;
    }
}
