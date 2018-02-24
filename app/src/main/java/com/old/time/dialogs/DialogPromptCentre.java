package com.old.time.dialogs;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.interfaces.OnClickViewCallBack;

/**
 * Created by NING on 2018/1/12.
 * 统一提示框
 */

public class DialogPromptCentre extends BaseDialog {

    private OnClickViewCallBack onClickViewCallBack;

    public DialogPromptCentre(@NonNull Context context, OnClickViewCallBack onClickViewCallBack) {
        super((Activity) context, R.style.dialog_setting);
        this.onClickViewCallBack = onClickViewCallBack;

    }

    public TextView tv_prompt_content, tv_cancel, tv_true;

    @Override
    protected void initDialogView() {
        tv_prompt_content = findViewbyId(R.id.tv_prompt_content);
        tv_cancel = findViewbyId(R.id.tv_cancel);
        tv_true = findViewbyId(R.id.tv_true);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickViewCallBack == null) {

                    return;
                }
                dismiss();
                onClickViewCallBack.onClickCancelView();
            }
        });
        tv_true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickViewCallBack == null) {

                    return;
                }
                dismiss();
                onClickViewCallBack.onClickTrueView();
            }
        });
    }

    /**
     * 显示dialog
     *
     * @param contentStr
     */
    public void showDialog(String contentStr) {
        show();
        setPromptContent(contentStr);

    }

    /**
     * 设置显示内容
     *
     * @param contentStr
     */
    private void setPromptContent(String contentStr) {
        if (tv_prompt_content == null) {

            return;
        }
        tv_prompt_content.setText(contentStr);
    }

    @Override
    protected int setContentView() {

        return R.layout.dialog_prompt_centre;
    }
}
