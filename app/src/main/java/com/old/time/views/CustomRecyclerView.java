package com.old.time.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.old.time.R;
import com.old.time.utils.MyGridLayoutManager;
import com.old.time.utils.RecyclerItemDecoration;

public class CustomRecyclerView extends RecyclerView {
    public CustomRecyclerView(Context context) {
        super(context);
        init(context);
    }

    private int spanCount;
    private int orientation;
    private int dividerHeight;
    private int dividerColor;

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomRecyclerView//
                , 0, 0);
        spanCount = typeArray.getInt(R.styleable.CustomRecyclerView_r_spanCount, 1);
        orientation = typeArray.getInt(R.styleable.CustomRecyclerView_r_orientation, OrientationHelper.VERTICAL);
        dividerHeight = typeArray.getDimensionPixelOffset(R.styleable.CustomRecyclerView_r_dividerHeight, 1);
        dividerColor = typeArray.getColor(R.styleable.CustomRecyclerView_r_dividerColor, context.getResources().getColor(R.color.line_bg));
        typeArray.recycle();
        init(context);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomRecyclerView//
                , 0, 0);
        spanCount = typeArray.getInt(R.styleable.CustomRecyclerView_r_spanCount, 1);
        orientation = typeArray.getInt(R.styleable.CustomRecyclerView_r_orientation, OrientationHelper.VERTICAL);
        dividerHeight = typeArray.getDimensionPixelOffset(R.styleable.CustomRecyclerView_r_dividerHeight, 1);
        dividerColor = typeArray.getInt(R.styleable.CustomRecyclerView_r_dividerColor, context.getResources().getColor(R.color.line_bg));
        typeArray.recycle();
        init(context);
    }

    public void init(Context context) {
        MyGridLayoutManager LayoutManager = new MyGridLayoutManager(context, spanCount);
        LayoutManager.setOrientation(orientation);
        setLayoutManager(LayoutManager);
        addItemDecoration(new RecyclerItemDecoration(orientation, dividerHeight, dividerColor, context));

    }
}
