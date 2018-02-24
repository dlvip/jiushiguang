package com.old.time.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.old.time.R;

/**
 * Created by NING on 2018/1/9.
 */

public abstract class BaseDialog extends Dialog {

    public Activity mContext;
    private View view;
    private int dialog_location;

    public BaseDialog(@NonNull Activity context, @StyleRes int dialogLoca) {
        super(context, dialogLoca);
        this.mContext = context;
        this.dialog_location = dialogLoca;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(true);
        view = View.inflate(getContext(), setContentView(), null);
        switch (dialog_location) {
            case R.style.dialog_setting://中间显示
                setContentView(view);
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                view.requestFocus();

                break;
            case R.style.transparentFrameWindowStyle://底部弹出
                setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                Window window = getWindow();
                window.setWindowAnimations(R.style.main_menu_animstyle);
                WindowManager.LayoutParams wl = window.getAttributes();
                wl.x = 0;
                wl.y = mContext.getWindowManager().getDefaultDisplay().getHeight();
                wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
                wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

                onWindowAttributesChanged(wl);
                view.requestFocus();

                break;
        }

        initDialogView();
    }

    protected abstract void initDialogView();

    /**
     * 设置Fragment要显示的布局
     *
     * @return 布局的layoutId
     */
    protected abstract int setContentView();

    /**
     * 获取设置的布局
     *
     * @return
     */
    protected View getContentView() {
        return view;
    }

    /**
     * 找出对应的控件
     *
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T findViewbyId(int id) {

        return (T) getContentView().findViewById(id);
    }
}
