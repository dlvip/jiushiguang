package com.old.time.views.banner;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.old.time.R;
import com.old.time.beans.BookEntity;
import com.old.time.utils.MyLinearLayoutManager;
import com.old.time.views.banner.adapter.MzBannerAdapter;
import com.old.time.views.banner.layoutmanager.BannerLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class BannerLayout extends FrameLayout {

    protected int autoPlayDuration;//刷新间隔时间

    protected boolean showIndicator;//是否显示指示器
    protected RecyclerView indicatorContainer;
    protected Drawable mSelectedDrawable;
    protected Drawable mUnselectedDrawable;
    protected IndicatorAdapter indicatorAdapter;
    protected int indicatorMargin;//指示器间距

    protected RecyclerView mRecyclerView;

    protected BannerLayoutManager mLayoutManager;

    protected int WHAT_AUTO_PLAY = 1000;

    protected boolean hasInit;
    protected int bannerSize = 1;
    protected int currentIndex = 1000;
    protected boolean isPlaying = false;

    protected boolean isAutoPlaying = true;

    private MzBannerAdapter mMzBannerAdapter;

    protected Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == WHAT_AUTO_PLAY) {
                currentIndex++;
                mRecyclerView.smoothScrollToPosition(currentIndex % bannerSize);
                mHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, autoPlayDuration);

            }
            return false;
        }
    });

    public BannerLayout(Context context) {
        this(context, null);
    }

    public BannerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private Activity mContext;

    protected void initView(Context context, AttributeSet attrs) {
        this.mContext = (Activity) context;
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.RecyclerViewBannerBase);
        showIndicator = a.getBoolean(R.styleable.RecyclerViewBannerBase_showIndicator, true);
        autoPlayDuration = a.getInt(R.styleable.RecyclerViewBannerBase_interval, 4000);
        isAutoPlaying = a.getBoolean(R.styleable.RecyclerViewBannerBase_autoPlaying, true);
        mSelectedDrawable = a.getDrawable(R.styleable.RecyclerViewBannerBase_indicatorSelectedSrc);
        mUnselectedDrawable = a.getDrawable(R.styleable.RecyclerViewBannerBase_indicatorUnselectedSrc);
        if (mSelectedDrawable == null) {
            //绘制默认选中状态图形
            GradientDrawable selectedGradientDrawable = new GradientDrawable();
            selectedGradientDrawable.setShape(GradientDrawable.OVAL);
            selectedGradientDrawable.setColor(getColor(R.color.colorAccent));
            selectedGradientDrawable.setSize(dp2px(5), dp2px(5));
            selectedGradientDrawable.setCornerRadius(dp2px(5) / 2);
            mSelectedDrawable = new LayerDrawable(new Drawable[]{selectedGradientDrawable});
        }
        if (mUnselectedDrawable == null) {
            //绘制默认未选中状态图形
            GradientDrawable unSelectedGradientDrawable = new GradientDrawable();
            unSelectedGradientDrawable.setShape(GradientDrawable.OVAL);
            unSelectedGradientDrawable.setColor(getColor(R.color.colorPrimaryDark));
            unSelectedGradientDrawable.setSize(dp2px(5), dp2px(5));
            unSelectedGradientDrawable.setCornerRadius(dp2px(5) / 2);
            mUnselectedDrawable = new LayerDrawable(new Drawable[]{unSelectedGradientDrawable});
        }

        indicatorMargin = a.getDimensionPixelSize(R.styleable.RecyclerViewBannerBase_indicatorSpace, dp2px(4));
        int marginLeft = a.getDimensionPixelSize(R.styleable.RecyclerViewBannerBase_indicatorMarginLeft, dp2px(0));
        int marginRight = a.getDimensionPixelSize(R.styleable.RecyclerViewBannerBase_indicatorMarginRight, dp2px(0));
        int marginBottom = a.getDimensionPixelSize(R.styleable.RecyclerViewBannerBase_indicatorMarginBottom, dp2px(0));
        int g = a.getInt(R.styleable.RecyclerViewBannerBase_indicatorGravity, 0);
        int gravity;
        if (g == 0) {
            gravity = GravityCompat.START;
        } else if (g == 2) {
            gravity = GravityCompat.END;
        } else {
            gravity = Gravity.CENTER;
        }
        int o = a.getInt(R.styleable.RecyclerViewBannerBase_orientation, 0);
        int orientation = 0;
        if (o == 0) {
            orientation = OrientationHelper.HORIZONTAL;
        } else if (o == 1) {
            orientation = OrientationHelper.VERTICAL;
        }
        a.recycle();
        //recyclerView部分
        mRecyclerView = new RecyclerView(mContext);
        LayoutParams vpLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        vpLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        addView(mRecyclerView, vpLayoutParams);
        mLayoutManager = new BannerLayoutManager(mContext, orientation);
        mRecyclerView.setLayoutManager(mLayoutManager);
        new PagerSnapHelper().attachToRecyclerView(mRecyclerView);

        this.mMzBannerAdapter = new MzBannerAdapter(new ArrayList<BookEntity>());
        mRecyclerView.setAdapter(mMzBannerAdapter);
        mMzBannerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {


            }
        });

        //指示器部分
        indicatorContainer = new RecyclerView(context);
        MyLinearLayoutManager indicatorLayoutManager = new MyLinearLayoutManager(context, orientation, false);
        indicatorContainer.setLayoutManager(indicatorLayoutManager);
        indicatorAdapter = new IndicatorAdapter();
        indicatorContainer.setAdapter(indicatorAdapter);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | gravity;
        params.setMargins(marginLeft, 0, marginRight, marginBottom);
        addView(indicatorContainer, params);
        if (!showIndicator) {
            indicatorContainer.setVisibility(GONE);

        }
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                refreshIndicator();

            }
        });
    }

    /**
     * 设置是否自动播放（上锁）
     *
     * @param playing 开始播放
     */
    protected synchronized void setPlaying(boolean playing) {
        if (isAutoPlaying && hasInit) {
            if (!isPlaying && playing) {
                mHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, autoPlayDuration);
                isPlaying = true;

            } else if (isPlaying && !playing) {
                mHandler.removeMessages(WHAT_AUTO_PLAY);
                isPlaying = false;

            }
        }
    }

    private List<BookEntity> bannerBeans = new ArrayList<>();

    /**
     * 设置轮播数据集
     */
    public void initBannerImageView(final List<BookEntity> list) {
        if (list == null || list.size() == 0) {

            return;
        }
        bannerBeans.clear();
        bannerBeans.addAll(list);
        bannerSize = bannerBeans.size();
        indicatorAdapter.notifyDataSetChanged();
        mMzBannerAdapter.setNewData(bannerBeans);
        mRecyclerView.scrollToPosition(currentIndex % bannerSize);
        hasInit = true;
        setPlaying(true);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setPlaying(false);

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setPlaying(true);

                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setPlaying(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setPlaying(false);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE) {
            setPlaying(true);
        } else {
            setPlaying(false);
        }
    }

    /**
     * 标示点适配器
     */
    protected class IndicatorAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ImageView bannerPoint = new ImageView(getContext());
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(indicatorMargin, indicatorMargin, indicatorMargin, indicatorMargin);
            bannerPoint.setScaleType(ImageView.ScaleType.CENTER_CROP);
            bannerPoint.setLayoutParams(lp);
            return new RecyclerView.ViewHolder(bannerPoint) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ImageView bannerPoint = (ImageView) holder.itemView;
            bannerPoint.setImageDrawable(currentIndex % bannerSize == position ? mSelectedDrawable : mUnselectedDrawable);

        }

        @Override
        public int getItemCount() {
            return bannerSize;
        }
    }

    protected int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    /**
     * 获取颜色
     */
    protected int getColor(@ColorRes int color) {
        return ContextCompat.getColor(getContext(), color);
    }

    /**
     * 改变导航的指示点
     */
    protected synchronized void refreshIndicator() {
        if (indicatorAdapter != null && showIndicator && bannerSize > 1) {
            indicatorAdapter.notifyDataSetChanged();

        }
    }

    /**
     * 设置是否禁止滚动播放
     */
    public void setAutoPlaying(boolean isAutoPlaying) {
        this.isAutoPlaying = isAutoPlaying;
        setPlaying(this.isAutoPlaying);

    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setShowIndicator(boolean showIndicator) {
        this.showIndicator = showIndicator;
        indicatorContainer.setVisibility(showIndicator ? VISIBLE : GONE);
    }

    /**
     * 设置轮播间隔时间
     *
     * @param autoPlayDuration 时间毫秒
     */
    public void setAutoPlayDuration(int autoPlayDuration) {
        this.autoPlayDuration = autoPlayDuration;
    }


    public interface OnBannerItemClickListener {
        void onItemClick(int position);

    }
}