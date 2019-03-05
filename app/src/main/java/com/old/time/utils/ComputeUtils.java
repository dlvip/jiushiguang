package com.old.time.utils;

import android.content.Context;

import java.math.BigDecimal;

/**
 * Created by wangfang on 2017/4/12.
 */

public class ComputeUtils {

    /**
     * 计算15:12的图片高度
     *
     * @param context
     * @return
     */
    public static int computeImageHeight_15_12(Context context) {
        int mWidth = ScreenTools.instance(context).getScreenWidth();
        BigDecimal b = new BigDecimal(mWidth * 12 / 15);
        int height = (int) b.setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
        return height;
    }

    /**
     * 计算16:9的图片高度
     *
     * @param context
     * @return
     */
    public static int computeImageHeight_16_9(Context context) {
        return computeImageHeight_16_9(context, 0);
    }

    /**
     * 计算16:9的图片高度
     *
     * @param context
     * @param margin
     * @return
     */

    public static int computeImageHeight_16_9(Context context, int margin) {
        int mWidth = ScreenTools.instance(context).getScreenWidth();
        BigDecimal b = new BigDecimal((mWidth - margin) * 9 / 16);
        int height = (int) b.setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
        return height;
    }

    /**
     * 计算15:7的图片高度(榜)
     *
     * @param context
     * @return
     */
    public static int computeImageHeight_15_7(Context context) {
        return computeImageHeight_15_7(context, 0);
    }

    /**
     * 计算15:7的图片高度(榜)
     *
     * @param context
     * @param margin
     * @return
     */

    public static int computeImageHeight_15_7(Context context, int margin) {
        int mWidth = ScreenTools.instance(context).getScreenWidth();
        BigDecimal b = new BigDecimal((mWidth - margin) * 7 / 15);
        int height = (int) b.setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
        return height;
    }

    /**
     * 计算2:1的图片高度(轮播)
     *
     * @param context
     * @return
     */
    public static int computeImageHeight_2_1(Context context) {
        int mWidth = ScreenTools.instance(context).getScreenWidth();
        BigDecimal b = new BigDecimal(mWidth / 2);
        int height = (int) b.setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
        return height;
    }

    /**
     * 计算3:1的图片高度(轮播)
     *
     * @param context
     * @return
     */
    public static int computeImageHeight_3_1(Context context) {
        int mWidth = ScreenTools.instance(context).getScreenWidth();
        BigDecimal b = new BigDecimal(mWidth / 3);
        int height = (int) b.setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
        return height;
    }

    /**
     * 计算69:32的图片高度(首页)
     *
     * @param context
     * @return
     */
    public static int[] computeImageHeight_69_32(Context context) {
        int[] ints = new int[2];
        int mWidth = (ScreenTools.instance(context).getScreenWidth() - UIHelper.dip2px(30)) / 2;
        BigDecimal b = new BigDecimal(mWidth * 32 / 69);
        int height = (int) b.setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
        ints[0] = mWidth;
        ints[1] = height;
        return ints;
    }

    /**
     * 计算69:32的图片高度(首页)
     *
     * @param context
     * @return
     */
    public static int[] computeImageHeight_69_32_1(Context context) {
        int[] ints = new int[2];
        int mWidth = ScreenTools.instance(context).getScreenWidth() - UIHelper.dip2px(30);
        BigDecimal b = new BigDecimal(mWidth * 32 / 69);
        int height = (int) b.setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
        ints[0] = mWidth;
        ints[1] = height;
        return ints;
    }

    /**
     * 计算69:32的图片高度(首页)
     *
     * @param context
     * @return
     */
    public static int[] computeImageHeight_69_32_2(Context context) {
        int[] ints = new int[2];
        int mWidth = (ScreenTools.instance(context).getScreenWidth() - UIHelper.dip2px(30)) * 2 / 5;
        BigDecimal b = new BigDecimal(mWidth * 32 / 69);
        int height = (int) b.setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
        ints[0] = mWidth;
        ints[1] = height;
        return ints;
    }

    /**
     * 活动页展示的图片宽高
     *
     * @param context
     * @return
     */
    public static int[] computeImageHeight_135_197(Context context) {
        ScreenTools screenTools = ScreenTools.instance(context);
        int[] ints = new int[2];
        int mWidth = screenTools.getScreenWidth();
        BigDecimal b1 = new BigDecimal(mWidth * 1576.0f);
        BigDecimal b2 = BigDecimal.valueOf(1080.0f);
        BigDecimal b = b1.divide(b2, 1, BigDecimal.ROUND_HALF_UP);//保留一位小数，并且四舍五入
        int height = (int) b.setScale(0, BigDecimal.ROUND_UP).floatValue();
        ints[0] = mWidth;
        ints[1] = height;
        return ints;
    }

    /**
     * 4:1的图片高度(YST)
     *
     * @param context
     * @return
     */
    public static int[] computeImageHeightYst_3_1(Context context) {
        int[] ints = new int[2];
        int mWidth = ScreenTools.instance(context).getScreenWidth();
        BigDecimal b = new BigDecimal(mWidth / 3);
        int height = (int) b.setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
        ints[0] = mWidth;
        ints[1] = height;
        return ints;
    }

    /**
     * 计算8:5的图片高度
     *
     * @param context
     * @return
     */
    public static int[] computeImageHeight_8_5(Context context) {
        int[] ints = new int[2];
        int mWidth = (ScreenTools.instance(context).getScreenWidth() - UIHelper.dip2px(30)) / 2;
        BigDecimal b = new BigDecimal(mWidth * 5 / 8);
        int mHeight = (int) b.setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
        ints[0] = mWidth;
        ints[1] = mHeight;
        return ints;
    }


    /**
     * 计算15:8的图片高度
     *
     * @param context
     * @return
     */
    public static int computeImageHeight_15_8(Context context) {
        int mWidth = ScreenTools.instance(context).getScreenWidth();
        BigDecimal b = new BigDecimal(mWidth * 8 / 15);
        int height = (int) b.setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
        return height;
    }

    /**
     * 获取礼品图片宽高
     *
     * @param mContext
     * @return
     */
    public static int computeGiftImageWH(Context mContext) {
        int mWidth = ScreenTools.instance(mContext).getScreenWidth();
        BigDecimal b = new BigDecimal((mWidth - UIHelper.dip2px(15) * 4) / 3 - UIHelper.dip2px(10));
        int height = (int) b.setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
        return height;

    }

    /**
     * 计算36:57的图片高度
     *
     * @param context
     * @return
     */
    public static int computeImageHeight_36_57(Context context) {
        int mWidth = ScreenTools.instance(context).getScreenWidth();
        BigDecimal b = new BigDecimal(mWidth * 57 / 36);
        int height = (int) b.setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
        return height;
    }

    /**
     * 计算36:57的图片高度
     *
     * @param context
     * @return
     */
    public static int computeImageHeightAndWidth(Context context, int width, int height) {
        int mWidth = ScreenTools.instance(context).getScreenWidth();
        BigDecimal b = new BigDecimal(mWidth * height / width);
        height = (int) b.setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
        return height;
    }

}
