package com.old.time.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
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
        Window window;
        switch (dialog_location) {
            case R.style.dialog_setting://中间显示
                setContentView(view);
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                view.requestFocus();

                break;
            case R.style.transparentFrameWindowStyle://底部弹出
                setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                window = getWindow();
                assert window != null;
                window.setWindowAnimations(R.style.main_menu_animstyle);
                WindowManager.LayoutParams wl = window.getAttributes();
                wl.x = 0;
                wl.y = mContext.getWindowManager().getDefaultDisplay().getHeight();
                wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
                wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

                onWindowAttributesChanged(wl);
                view.requestFocus();

                break;
            case R.style.dialog_setting_edt:
                setCanceledOnTouchOutside(true);
                setContentView(view);
                window = getWindow();//设置dialog的显示位置
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.BOTTOM;
                wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
                wlp.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;//显示dialog的时候,就显示软键盘
                wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;//就是这个属性导致不能获取焦点,默认的是FLAG_NOT_FOCUSABLE,故名思义不能获取输入焦点,
                wlp.dimAmount = 0.5f;//设置对话框的透明程度背景(非布局的透明度)
                window.setAttributes(wlp);

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
