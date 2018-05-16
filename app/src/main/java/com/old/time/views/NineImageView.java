package com.old.time.views;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.old.time.R;
import com.old.time.activitys.PhotoPagerActivity;
import com.old.time.beans.PhotoInfoBean;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.utils.MyGridLayoutManager;
import com.old.time.utils.ScreenTools;
import com.old.time.utils.UIHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NING on 2018/4/21.
 */

public class NineImageView extends LinearLayout {

    public static final String TAG = "NineImageView";

    public NineImageView(Context context) {
        super(context);
        initNineImageView();

    }

    public NineImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initNineImageView();

    }

    public NineImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initNineImageView();

    }

    private boolean isScrolling;

    public void setRecyclerViewOnScrolling(boolean isScrolling) {
        this.isScrolling = isScrolling;

    }

    private BaseQuickAdapter<PhotoInfoBean, BaseViewHolder> adapter;
    private MyGridLayoutManager myGridLayoutManager;
    private RecyclerView mRecyclerView;
    private LayoutParams oneParams;
    private List<PhotoInfoBean> picPaths = new ArrayList<>();

    private void initNineImageView() {
        weightW = ScreenTools.instance(getContext()).getScreenWidth();
        maxW = 3 * weightW / 5;
        View view = View.inflate(getContext(), R.layout.nine_image_view, this);
        oneParams = new LayoutParams(0, 0);

        mRecyclerView = view.findViewById(R.id.recycler_view_more_pic);
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecyclerView.getItemAnimator().setChangeDuration(0);
        myGridLayoutManager = new MyGridLayoutManager(getContext(), 2);
        myGridLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(myGridLayoutManager);
        adapter = new BaseQuickAdapter<PhotoInfoBean, BaseViewHolder>(R.layout.img_view_item, picPaths) {

            @Override
            protected void convert(final BaseViewHolder helper, PhotoInfoBean item) {
                FrameLayout frame_layout = helper.getView(R.id.frame_layout_one_pic);
                oneParams.width = imgW;
                oneParams.height = imgH;
                frame_layout.setLayoutParams(oneParams);
                ImageView imageView = helper.getView(R.id.img_pic);
                imageView.setPadding(pxOneMaxWandH, 0, 0, pxOneMaxWandH);
                imageView.setScaleType(mScaleType);
                if (isScrolling) {
                    imageView.setImageResource(R.drawable.shape_666_bg);

                } else {
                    GlideUtils.getInstance().setImageView(getContext(), item.picKey, imageView, imgW, imgH, R.drawable.shape_666_bg);

                }
            }
        };
        if (mRecyclerView.getAdapter() == null) {
            mRecyclerView.setAdapter(adapter);

        }
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                picKeys.clear();
                for (PhotoInfoBean photoInfoBean : picPaths) {
                    picKeys.add(photoInfoBean.picKey);

                }
                PhotoPagerActivity.startPhotoPagerActivity((Activity) getContext(), (Serializable) picKeys, position);

            }
        });
    }

    private List<String> picKeys = new ArrayList<>();

    private int imgW, imgH, pxOneMaxWandH;

    /**
     * 设置数据
     *
     * @param photoInfoBeans 图片路径
     */
    public void setDataForView(List<PhotoInfoBean> photoInfoBeans) {
        if (photoInfoBeans == null || photoInfoBeans.size() == 0) {
            setVisibility(GONE);

            return;
        }
        this.picPaths.clear();
        this.picPaths.addAll(photoInfoBeans);
        this.setVisibility(VISIBLE);
        pxOneMaxWandH = UIHelper.dip2px(2);
        if (picPaths.size() == 1) {
            pxOneMaxWandH = 0;
            PhotoInfoBean photoInfoBean = picPaths.get(0);
            setOneImageView(photoInfoBean.with, photoInfoBean.height);
            myGridLayoutManager.setSpanCount(1);

        } else if (picPaths.size() == 2 || picPaths.size() == 4) {
            mScaleType = ImageView.ScaleType.CENTER_CROP;
            int paramsWH = (weightW - UIHelper.dip2px(90)) * 3 / 4;
            imgW = paramsWH / 2;
            imgH = imgW;
            myGridLayoutManager.setSpanCount(2);

        } else {
            mScaleType = ImageView.ScaleType.CENTER_CROP;
            int paramsWH = weightW - UIHelper.dip2px(90);
            imgW = paramsWH / 3;
            imgH = imgW;
            myGridLayoutManager.setSpanCount(3);

        }
        adapter.setNewData(picPaths);
    }


    private ImageView.ScaleType mScaleType;
    private int weightW, maxW;

    /**
     * 设置图片的宽高
     *
     * @param picW
     * @param picH
     */
    private void setOneImageView(int picW, int picH) {
        if (picW > picH * 3) {//特款图
            mScaleType = ImageView.ScaleType.CENTER;
            imgW = maxW;
            imgH = maxW / 3;

        } else if (picH > picW * 3) {//特高图
            mScaleType = ImageView.ScaleType.CENTER;
            imgW = maxW / 3;
            imgH = maxW;

        } else {
            mScaleType = ImageView.ScaleType.FIT_XY;
            if (picW > picH) {//宽大于高
                imgW = maxW;
                imgH = imgW * picH / picW;

            } else if (picH > picW) {//高大于宽
                imgH = maxW;
                imgW = picW * imgH / picH;

            } else {
                imgW = maxW;
                imgH = maxW;

            }
        }
    }
}
