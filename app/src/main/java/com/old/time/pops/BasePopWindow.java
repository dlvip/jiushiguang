package com.old.time.pops;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.old.time.R;

public abstract class BasePopWindow extends PopupWindow {

    protected Context context;
    protected View parentView;

    public BasePopWindow(Context context) {
        this.context = context;
        parentView = View.inflate(context, getLayoutId(), null);
        initView();
        setContentView(parentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(context.getResources().getDrawable(R.color.transparent));

    }

    protected <T extends View> T findViewById(@IdRes int id) {

        return parentView.findViewById(id);
    }

    protected abstract void initView();

    protected abstract int getLayoutId();

    //pop显示位置   7.0以后突然popwindows显示位置往上飘
    public void showAtLocation(View view) {
        if (android.os.Build.VERSION.SDK_INT == 24) {//7.1已经修复改bug
            int[] a = new int[2];
            view.getLocationInWindow(a);
            showAtLocation(((Activity) context).getWindow().getDecorView(), Gravity.NO_GRAVITY, 0, a[1] + view.getHeight());

        } else {
            showAsDropDown(view);

        }
    }
}
