package com.old.time.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.old.time.R;

/**
 * Created by NING on 2018/3/29.
 */

public class ProgressCDialog extends ProgressDialog {
    public ProgressCDialog(Context context) {
        super(context, R.style.CustomDialog);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initProgressCDialog();

    }

    private TextView tv_load_text;

    private void initProgressCDialog() {
        //设置不可取消，点击其他区域不能取消，实际中可以抽出去封装供外包设置
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_progress);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);

        tv_load_text = findViewById(R.id.tv_load_text);

    }

    /**
     * 显示进度框
     */
    public void showProgressDialog() {
        super.show();

    }

    /**
     * 显示进度框
     */
    public void showProgressDialog(String dialogText) {
        super.show();
        tv_load_text.setText(dialogText);

    }
}