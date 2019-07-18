package com.old.time.fragments;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.old.time.utils.ScreenTools;
import com.old.time.utils.UIHelper;
import com.old.time.views.SuspensionPopupWindow;

/**
 * Created by NING on 2018/2/23.
 */

public abstract class BaseFragment extends LazyLoadFragment {

    public String TAG;
    public Activity mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = getActivity();
        TAG = getClass().getName();
    }

    /**
     * 网络请求数据
     *
     * @param isRefresh
     */
    public abstract void getDataFromNet(boolean isRefresh);

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public int getWindowWidth() {

        return ScreenTools.instance(mContext).getScreenWidth();
    }

    private int showX, showY;
    private SuspensionPopupWindow mSuspensionPopupWindow;

    /**
     * 发送内容入口
     */
    public void showSuspensionPopupWindow() {
        if (mSuspensionPopupWindow == null) {
            ScreenTools mScreenTools = ScreenTools.instance(mContext);
            int w = mScreenTools.getScreenWidth();
            int h = mScreenTools.getScreenHeight();
            showX = w / 2 - UIHelper.dip2px(40);
            showY = h - UIHelper.dip2px(80);
            mSuspensionPopupWindow = new SuspensionPopupWindow(mContext, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSuspensionPopupWindowClick();

                }
            });
        }
        mSuspensionPopupWindow.showAtLocationXY(mContext.getWindow().getDecorView(), Gravity.TOP, showX, showY);

    }

    /**
     * 按钮点击事件
     */
    public void setSuspensionPopupWindowClick() {


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSuspensionPopupWindow != null) {
            mSuspensionPopupWindow.dismiss();
            mSuspensionPopupWindow = null;

        }
    }
}
