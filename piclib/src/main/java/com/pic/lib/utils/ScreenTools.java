package com.pic.lib.utils;

import android.content.Context;
import android.util.TypedValue;
import android.widget.Toast;

public class ScreenTools {

    /**
     * dip转换px
     */
    public static int dip2px(Context mContext, int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip//
                , mContext.getResources().getDisplayMetrics());
    }

    /**
     * 获取屏幕的宽度
     *
     * @param mContext
     * @return
     */
    public static int getScreenWidth(Context mContext) {
        return mContext.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 显示吐司
     *
     * @param msg
     */
    public static void ToastMessage(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * pxz转换dip
     */
    public static int px2dip(Context mContext, int px) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }
}
