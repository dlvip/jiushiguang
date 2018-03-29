package com.old.time.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.old.time.MyApplication;
import com.old.time.views.CustomDialog;

import java.lang.reflect.Field;

public class UIHelper {

    /**
     * dip转换px
     */
    public static int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, MyApplication.getInstance().getResources().getDisplayMetrics());
    }

    /**
     * pxz转换dip
     */
    public static int px2dip(int px) {
        final float scale = MyApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
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
     * 显示吐司
     *
     * @param msg
     */
    public static void ToastMessage(String msg) {
        Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示吐司
     *
     * @param msg
     */
    public static void ToastCenterMessage(String msg) {
        Toast toast = Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 显示进度框
     */
    public static ProgressDialog showProgressMessageDialog(Context context, String Message) {
        CustomDialog pdDialog = new CustomDialog(context);
//        pdDialog.setMessage(Message);
//        pdDialog.setCancelable(true);
//        pdDialog.setIndeterminate(false);
//        pdDialog.setCanceledOnTouchOutside(false);
//        pdDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdDialog.show();
        return pdDialog;
    }


    /**
     * 显示下载进度框
     */
    public static ProgressDialog showProgressDialog(String str) {
        ProgressDialog pdDialog = new ProgressDialog(MyApplication.getInstance());
        pdDialog.setMessage(str);
        pdDialog.setCancelable(true);
        pdDialog.setIndeterminate(false);
        pdDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdDialog.show();
        return pdDialog;
    }

    /**
     * 隐藏进度框
     */
    public static void dissmissProgressDialog(ProgressDialog pdDialog) {
        if (pdDialog != null) {
            pdDialog.dismiss();

        }
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 再不下载图片的前提下获取图片的宽高
     *
     * @param path
     * @return
     */
    public static int[] getImageWidthHeight(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        return new int[]{options.outWidth, options.outHeight};
    }
}
