package com.old.time.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.DimenRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.old.time.R;

/**
 * Created by wcliang on 2019/7/17.
 */

public class LBTextView extends AppCompatTextView {

    public LBTextView(Context context) {
        super(context);
    }

    public LBTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttribute(context, attrs);

    }

    public LBTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttribute(context, attrs);

    }

    /**
     * 边框宽度
     */
    private float strokeWidth;

    /**
     * 圆角半径
     */
    private float roundRadius;
    private float roundRadiusTopLeft;
    private float roundRadiusTopRight;
    private float roundRadiusBottomLeft;
    private float roundRadiusBottomRight;

    /**
     * 边框颜色
     */
    private int strokeColor;

    /**
     * 内部填充颜色
     */
    private int fillColor;

    /**
     * drawable
     */
    private GradientDrawable gd;

    /**
     * 获取属性  改变背景
     *
     * @param mContext
     * @param attrs
     */
    private void initAttribute(Context mContext, @Nullable AttributeSet attrs) {
        if (mContext == null || attrs == null) {

            return;
        }
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.LBTextView);
        strokeWidth = array.getDimension(R.styleable.LBTextView_stroke_width, getResources().getDimension(R.dimen.line_height));
        roundRadius = array.getDimension(R.styleable.LBTextView_round_radius, getResources().getDimension(R.dimen.layout_0));
        roundRadiusTopLeft = array.getDimension(R.styleable.LBTextView_round_radius_top_left, getResources().getDimension(R.dimen.layout_0));
        roundRadiusTopRight = array.getDimension(R.styleable.LBTextView_round_radius_top_right, getResources().getDimension(R.dimen.layout_0));
        roundRadiusBottomLeft = array.getDimension(R.styleable.LBTextView_round_radius_bottom_left, getResources().getDimension(R.dimen.layout_0));
        roundRadiusBottomRight = array.getDimension(R.styleable.LBTextView_round_radius_bottom_right, getResources().getDimension(R.dimen.layout_0));
        fillColor = array.getColor(R.styleable.LBTextView_fill_color, getResources().getColor(R.color.color_fff));
        strokeColor = array.getColor(R.styleable.LBTextView_stroke_color, fillColor);
        array.recycle();

        submit();

    }

    /**
     * 修改边框宽度
     *
     * @param strokeWidth
     * @return
     */
    public LBTextView setStrokeWidth(@DimenRes int strokeWidth) {
        this.strokeWidth = getResources().getDimension(strokeWidth);

        return this;
    }

    /**
     * 设置边框颜色
     *
     * @param strokeColor
     * @return
     */
    public LBTextView setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;

        return this;
    }

    /**
     * 设置内部填充颜色
     *
     * @param fillColor
     * @return
     */
    public LBTextView setFillColor(int fillColor) {
        this.fillColor = fillColor;

        return this;
    }

    /**
     * 设置圆角半径
     *
     * @param roundRadius
     * @return
     */
    public LBTextView setRoundRadius(@DimenRes int roundRadius) {
        this.roundRadius = getResources().getDimension(roundRadius);

        return this;
    }

    /**
     * 左上圆角
     *
     * @param roundRadiusTopLeft
     * @return
     */
    public LBTextView setRoundRadiusTopLeft(float roundRadiusTopLeft) {
        this.roundRadiusTopLeft = roundRadiusTopLeft;

        return this;
    }

    /**
     * 右上圆角
     *
     * @param roundRadiusTopRight
     * @return
     */
    public LBTextView setRoundRadiusTopRight(float roundRadiusTopRight) {
        this.roundRadiusTopRight = roundRadiusTopRight;

        return this;
    }

    /**
     * 左上圆角
     *
     * @param roundRadiusBottomLeft
     * @return
     */
    public LBTextView setRoundRadiusBottomLeft(float roundRadiusBottomLeft) {
        this.roundRadiusBottomLeft = roundRadiusBottomLeft;

        return this;
    }

    /**
     * 右上圆角
     *
     * @param roundRadiusBottomRight
     * @return
     */
    public LBTextView setRoundRadiusBottomRight(float roundRadiusBottomRight) {
        this.roundRadiusBottomRight = roundRadiusBottomRight;

        return this;
    }

    /**
     * 提交
     */
    public void submit() {
        if (gd == null) {
            gd = new GradientDrawable();

        }
        gd.setColor(fillColor);
        gd.setCornerRadius(roundRadius);
        if (roundRadius == 0) {
            gd.setCornerRadii(new float[]{roundRadiusTopLeft, roundRadiusTopLeft, roundRadiusTopRight, roundRadiusTopRight, roundRadiusBottomLeft, roundRadiusBottomLeft, roundRadiusBottomRight, roundRadiusBottomRight});

        }
        gd.setStroke((int) strokeWidth, strokeColor);
        setBackgroundDrawable(gd);

    }
}
