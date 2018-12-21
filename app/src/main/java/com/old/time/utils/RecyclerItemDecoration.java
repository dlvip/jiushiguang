package com.old.time.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.old.time.R;

/**
 * Created by diliang on 2017/2/13.
 */
public class RecyclerItemDecoration extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private int mOrientation;
    private int spacing;
    private Paint mPaint;

    public RecyclerItemDecoration(Context context) {
        setOrientation(LinearLayoutManager.VERTICAL);
        this.spacing = UIHelper.dip2px(1);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(context.getResources().getColor(R.color.line_bg));
        mPaint.setStyle(Paint.Style.FILL);

    }

    public RecyclerItemDecoration(Context context, int orientation) {
        setOrientation(orientation);
        this.spacing = UIHelper.dip2px(1);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(context.getResources().getColor(R.color.line_bg));
        mPaint.setStyle(Paint.Style.FILL);

    }

    public RecyclerItemDecoration(Context context, int orientation, int spacing) {
        setOrientation(orientation);
        this.spacing = UIHelper.dip2px(spacing);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(context.getResources().getColor(R.color.line_bg));
        mPaint.setStyle(Paint.Style.FILL);

    }

    public RecyclerItemDecoration(Context context, int orientation, int spacing, @ColorRes int resId) {
        setOrientation(orientation);
        this.spacing = UIHelper.dip2px(spacing);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(context.getResources().getColor(resId));
        mPaint.setStyle(Paint.Style.FILL);

    }

    public RecyclerItemDecoration(int orientation, int spacing, int resId, Context context) {
        setOrientation(orientation);
        this.spacing = spacing;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(resId);
        mPaint.setStyle(Paint.Style.FILL);

    }

    /**
     * 默认纵向
     *
     * @param orientation
     */
    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            orientation = VERTICAL_LIST;

        }
        mOrientation = orientation;
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top;
            c.drawRect(left, top, right, bottom, mPaint);

        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getRight() + params.rightMargin;
            int right = left;
            c.drawRect(left, top, right, bottom, mPaint);

        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);

        } else {
            drawHorizontal(c, parent);

        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mOrientation == VERTICAL_LIST) {
            if (spacing == UIHelper.dip2px(10)) {
                outRect.set(0, 0, 0, spacing);

            } else {
                outRect.set(0, spacing, 0, 0);

            }
        } else {
            outRect.set(spacing, 0, 0, 0);

        }
    }
}