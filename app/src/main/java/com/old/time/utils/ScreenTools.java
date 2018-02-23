
package com.old.time.utils;

import android.content.Context;

public class ScreenTools {

    private static ScreenTools mScreenTools;
    private Context context;

    private ScreenTools(Context context) {
        if (context == null) {

            return;
        }
        this.context = context.getApplicationContext();
    }

    public static ScreenTools instance(Context context) {
        if (mScreenTools == null)
            mScreenTools = new ScreenTools(context);
        return mScreenTools;
    }

    public int dip2px(float f) {
        return (int) (0.5D + (double) (f * getDensity(context)));
    }

    public int dip2px(int i) {
        return (int) (0.5D + (double) (getDensity(context) * (float) i));
    }

    public int get480Height(int i) {
        return (i * getScreenWidth()) / 480;
    }

    public float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public int getScal() {
        return (100 * getScreenWidth()) / 480;
    }

    public int getScreenDensityDpi() {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    public int getScreenHeight() {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public int getScreenWidth() {
        return context.getResources().getDisplayMetrics().widthPixels;
    }


    public float getXdpi() {
        return context.getResources().getDisplayMetrics().xdpi;
    }

    public float getYdpi() {
        return context.getResources().getDisplayMetrics().ydpi;
    }

    public int px2dip(float f) {
        float f1 = getDensity(context);
        return (int) (((double) f - 0.5D) / (double) f1);
    }

    public int px2dip(int i) {
        float f = getDensity(context);
        return (int) (((double) i - 0.5D) / (double) f);
    }

    public float getScaledDensity(Context context) {
        return context.getResources().getDisplayMetrics().scaledDensity;
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public int px2sp(float pxValue) {
        final float fontScale = getScaledDensity(context);
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public int sp2px(float spValue) {
        final float fontScale = getScaledDensity(context);
        return (int) (spValue * fontScale + 0.5f);
    }
}
