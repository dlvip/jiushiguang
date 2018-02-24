package com.old.time.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.old.time.R;

/**
 * Created by diliang on 2017/2/13.
 */
public class RecyclerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private Drawable mDivider;

    private int mOrientation;
    private int spacing;

    public RecyclerItemDecoration(Context context) {
        TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = new ColorDrawable(context.getResources().getColor(R.color.line_bg));
        a.recycle();
        setOrientation(LinearLayoutManager.VERTICAL);
        this.spacing = UIHelper.dip2px(1);

    }

    public RecyclerItemDecoration(Context context, int orientation) {
        TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = new ColorDrawable(context.getResources().getColor(R.color.line_bg));
        a.recycle();
        setOrientation(orientation);
        this.spacing = UIHelper.dip2px(1);

    }

    public RecyclerItemDecoration(Context context, int orientation, int spacing) {
        TypedArray a = context.obtainStyledAttributes(ATTRS);
        this.mDivider = new ColorDrawable(context.getResources().getColor(R.color.line_bg));
        a.recycle();
        setOrientation(orientation);
        this.spacing = UIHelper.dip2px(spacing);
    }

    public RecyclerItemDecoration(Context context, int orientation, int spacing, int resId) {
        TypedArray a = context.obtainStyledAttributes(ATTRS);
        this.mDivider = new ColorDrawable(context.getResources().getColor(resId));
        a.recycle();
        setOrientation(orientation);
        this.spacing = UIHelper.dip2px(spacing);
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
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);

        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int left = child.getRight() + params.rightMargin;
            int right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);

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
            outRect.set(0, 0, 0, spacing);

        } else {
            outRect.set(spacing, 0, 0, 0);

        }
    }
}