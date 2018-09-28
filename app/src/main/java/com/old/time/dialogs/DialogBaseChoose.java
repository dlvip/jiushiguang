package com.old.time.dialogs;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.views.WheelView;

/**
 * Created by NING on 2018/1/8.
 */

public class DialogBaseChoose extends BaseDialog {

    public TextView tv_cancel, tv_title, tv_true;
    public WheelView wv_first, wv_second, wv_third;

    public DialogBaseChoose(@NonNull Context context, int dialogLoca) {
        super((Activity) context, dialogLoca);
        this.mContext = (Activity) context;

    }

    /**
     * 设置title
     *
     * @param titleStr
     */
    public void setDialogTitle(String titleStr) {
        if (tv_title != null) {
            tv_title.setText(titleStr);

        }
    }

    @Override
    protected void initDialogView() {
        tv_cancel = findViewbyId(R.id.tv_cancel);
        tv_title = findViewbyId(R.id.tv_title);
        tv_true = findViewbyId(R.id.tv_true);
        wv_first = findViewbyId(R.id.wv_first);
        wv_second = findViewbyId(R.id.wv_second);
        wv_third = findViewbyId(R.id.wv_third);
        wv_first.setVisibility(View.GONE);
        wv_second.setVisibility(View.GONE);
        wv_third.setVisibility(View.GONE);

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });

        tv_true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTrueView();

            }
        });
    }

    /**
     * 点击确认按钮
     */
    public void onClickTrueView() {
        dismiss();

    }

    @Override
    protected int setContentView() {
        return R.layout.dialog_base_choose;
    }
}
