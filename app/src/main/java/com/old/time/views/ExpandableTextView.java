
package com.old.time.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.utils.ScreenTools;

/**
 * des:可伸展textview
 * Created by xsf
 * on 2016.08.24
 */
public class ExpandableTextView extends LinearLayout implements View.OnClickListener {

    /* 默认最高行数 */
    private static final int MAX_COLLAPSED_LINES = 5;

    /* 默认动画执行时间 */
    private static final int DEFAULT_ANIM_DURATION = 200;

    /*内容textview*/
    protected TextView mTvContent;

    /*展开收起textview*/
    protected TextView mTvExpandCollapse;

    /*是否有重新绘制*/
    private boolean mRelayout;

    /*默认展开*/
    private boolean mExpandable = false;

    /*展开图片*/
    private Drawable mExpandDrawable;
    /*收起图片*/
    private Drawable mCollapseDrawable;
    /*动画执行时间*/
    private int mAnimationDuration;
    /*是否正在执行动画*/
    private boolean mAnimating;
    /* 展开收起状态回调 */
    private OnExpandStateChangeListener mListener;

    /*设置内容最大行数，超过隐藏*/
    private int mMaxCollapsedLines;

    /*内容颜色*/
    private int contentTextColor;
    /*收起展开颜色*/
    private int collapseExpandTextColor;
    /*内容字体大小*/
    private float contentTextSize;
    /*收起展字体大小*/
    private float collapseExpandTextSize;
    /*收起文字*/
    private String textCollapse;
    /*展开文字*/
    private String textExpand;

    /*收起展开位置，默认左边*/
    private int grarity;

    /*收起展开图标位置，默认在右边*/
    private int drawableGrarity;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ExpandableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    @Override
    public void setOrientation(int orientation) {
        if (LinearLayout.HORIZONTAL == orientation) {
            throw new IllegalArgumentException("ExpandableTextView only supports Vertical Orientation.");
        }
        super.setOrientation(orientation);
    }
    /**
     * 初始化属性
     * @param attrs
     */
    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        mMaxCollapsedLines = typedArray.getInt(R.styleable.ExpandableTextView_maxCollapsedLines, MAX_COLLAPSED_LINES);
        mAnimationDuration = typedArray.getInt(R.styleable.ExpandableTextView_animDuration, DEFAULT_ANIM_DURATION);
        mExpandDrawable = typedArray.getDrawable(R.styleable.ExpandableTextView_expandDrawable);
        mCollapseDrawable = typedArray.getDrawable(R.styleable.ExpandableTextView_collapseDrawable);

        textCollapse = typedArray.getString(R.styleable.ExpandableTextView_textCollapse);
        textExpand = typedArray.getString(R.styleable.ExpandableTextView_textExpand);

        if (TextUtils.isEmpty(textCollapse)) {
            textCollapse = getContext().getString(R.string.collapse);
        }
        if (TextUtils.isEmpty(textExpand)) {
            textExpand = getContext().getString(R.string.expand);
        }
        contentTextColor = typedArray.getColor(R.styleable.ExpandableTextView_contentTextColor, ContextCompat.getColor(getContext(), R.color.color_000));
        contentTextSize = typedArray.getDimension(R.styleable.ExpandableTextView_contentTextSize, ScreenTools.instance(getContext()).sp2px(14));

        collapseExpandTextColor = typedArray.getColor(R.styleable.ExpandableTextView_collapseExpandTextColor, ContextCompat.getColor(getContext(), R.color.color_000));
        collapseExpandTextSize = typedArray.getDimension(R.styleable.ExpandableTextView_collapseExpandTextSize,  ScreenTools.instance(getContext()).sp2px(14));


        grarity = typedArray.getInt(R.styleable.ExpandableTextView_collapseExpandGrarity, Gravity.CENTER);
        drawableGrarity=typedArray.getInt(R.styleable.ExpandableTextView_drawableGrarity, Gravity.RIGHT);

        typedArray.recycle();
        // enforces vertical orientation
        setOrientation(LinearLayout.VERTICAL);
        // default visibility is gone
        setVisibility(GONE);
    }

    /**
     * 渲染完成时初始化view
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findViews();
    }

    /**
     * 初始化viwe
     */
    private void findViews() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_expand_collapse, this);
        mTvContent = (TextView) findViewById(R.id.expandable_text);
//        mTvContent.setOnClickListener(this);
        mTvExpandCollapse = (TextView) findViewById(R.id.expand_collapse);
        setDrawbleAndText();
        mTvExpandCollapse.setOnClickListener(this);

        mTvContent.setTextColor(contentTextColor);
        mTvContent.getPaint().setTextSize(contentTextSize);

        mTvExpandCollapse.setTextColor(collapseExpandTextColor);
        mTvExpandCollapse.getPaint().setTextSize(collapseExpandTextSize);

        //设置收起展开位置：左或者右
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = grarity;
        mTvExpandCollapse.setLayoutParams(lp);
    }
    /**
     * 点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (mTvExpandCollapse.getVisibility() != View.VISIBLE) {
            return;
        }
        mExpandable = !mExpandable;
        //修改收起/展开图标、文字
        setDrawbleAndText();
        if (mExpandable) {
            mTvContent.setMaxLines(Integer.MAX_VALUE);
        } else {
            mTvContent.setMaxLines(mMaxCollapsedLines);
        }
        if (mListener != null) {
            mListener.onExpandStateChanged(mTvContent, mExpandable);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 当动画还在执行状态时，拦截事件，不让child处理
        return mAnimating;
    }
    /**
     * 重新测量
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // If no change, measure and return
        if (!mRelayout || getVisibility() == View.GONE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        mRelayout = false;

        // Setup with optimistic case
        // i.e. Everything fits. No button needed
        mTvExpandCollapse.setVisibility(View.GONE);
        mTvContent.setMaxLines(Integer.MAX_VALUE);

        // Measure
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //如果内容真实行数小于等于最大行数，不处理
        if (mTvContent.getLineCount() <= mMaxCollapsedLines) {
            return;
        }
        // 获取内容tv真实高度（含padding）
//        mTextHeightWithMaxLines = getRealTextViewHeight(mTvContent);

        // 如果是收起状态，重新设置最大行数
        if (!mExpandable) {
            mTvContent.setMaxLines(mMaxCollapsedLines);
        }
        mTvExpandCollapse.setVisibility(View.VISIBLE);

        // Re-measure with new setup
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    /**
     * 设置收起展开图标位置和文字
     */
    private void setDrawbleAndText(){
        if (mCollapseDrawable != null && mExpandDrawable != null) {
            if (Gravity.LEFT == drawableGrarity) {
                mTvExpandCollapse.setCompoundDrawablesWithIntrinsicBounds(mExpandable ?  mExpandDrawable: mCollapseDrawable, null, null, null);
            } else {
                mTvExpandCollapse.setCompoundDrawablesWithIntrinsicBounds(null, null, mExpandable ? mExpandDrawable : mCollapseDrawable, null);
            }
        }
        mTvExpandCollapse.setText(mExpandable ? textCollapse : textExpand);
    }


    /*********暴露给外部调用方法***********/

    /**
     * 设置收起/展开监听
     * @param listener
     */
    public void setOnExpandStateChangeListener( OnExpandStateChangeListener listener) {
        mListener = listener;
    }

    /**
     * 设置内容
     * @param text
     */
    public void setText( CharSequence text) {
        mRelayout = true;
        mTvContent.setText(text);
        setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
    }

    /**
     * 设置内容，列表情况下，带有保存位置收起/展开状态
     * @param text
     * @param expandeable
     */
    public void setText(CharSequence text, boolean expandeable) {
        //获取状态，如无，默认是true:收起
        mExpandable = expandeable;
        clearAnimation();
        //设置收起/展开图标和文字
        setDrawbleAndText();
        mTvExpandCollapse.setText(expandeable ? textCollapse : textExpand);

        setText(text);
        getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        requestLayout();
    }


    /**
     * 获取内容
     * @return
     */
    public CharSequence getText() {
        if (mTvContent == null) {
            return "";
        }
        return mTvContent.getText();
    }

    /**
     * 定义状态改变接口
     */
    public interface OnExpandStateChangeListener {
        /**
         * @param textView   - TextView being expanded/collapsed
         * @param isExpanded - true if the TextView has been expanded
         */
        void onExpandStateChanged(TextView textView, boolean isExpanded);
    }
}