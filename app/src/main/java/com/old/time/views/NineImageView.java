package com.old.time.views;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.old.time.R;
import com.old.time.activitys.PhotoPagerActivity;
import com.old.time.beans.PhotoInfoBean;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.utils.ScreenTools;
import com.old.time.utils.UIHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NING on 2018/4/21.
 */

public class NineImageView extends LinearLayout {

    public NineImageView(Context context) {
        super(context);
        initMultiImageView();
    }

    public NineImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initMultiImageView();

    }

    private int MAX_PER_ROW_COUNT = 3;// 每行显示最大数

    private Context mContext;

    private List<String> picKeys;

    // 图片间距
    private int pxImagePadding;
    private int pxMultiImageViewWidth;

    /**
     * 数据初始化
     */
    private void initMultiImageView() {
        mContext = getContext();
        picKeys = new ArrayList<>();
        pxImagePadding = UIHelper.dip2px(3);
        pxMultiImageViewWidth = ScreenTools.instance(mContext).getScreenWidth() - UIHelper.dip2px(90);

    }

    private boolean isScrolling;

    public void setRecyclerViewOnScrolling(boolean isScrolling) {
        this.isScrolling = isScrolling;

    }

    private List<PhotoInfoBean> photoInfoBeans;

    /**
     * 设置数据
     *
     * @param photoInfoBeans
     */
    public void setDataForView(List<PhotoInfoBean> photoInfoBeans) {
        if (photoInfoBeans == null || photoInfoBeans.size() == 0) {

            return;
        }
        this.photoInfoBeans = photoInfoBeans;
        addChildMultiImageView();

    }

    /**
     * 图片宽高
     */
    private int imgViewW, imgViewH;

    /**
     * 图片显示形式
     */
    private ImageView.ScaleType imageScaleType;

    /**
     * 添加图片
     */
    private void addChildMultiImageView() {
        this.setOrientation(VERTICAL);
        this.removeAllViews();
        imageScaleType = ImageView.ScaleType.CENTER;
        if (photoInfoBeans.size() == 1) {
            int[] imageWH = getOneImageViewWH();
            imgViewW = imageWH[0];
            imgViewH = imageWH[1];
            addView(createImageView(0));

        } else {
            int allCount = photoInfoBeans.size();
            if (allCount == 4 || allCount == 2) {
                MAX_PER_ROW_COUNT = 2;
                imgViewW = (pxMultiImageViewWidth * 2 / 3 - pxImagePadding) / 2;
                imgViewH = imgViewW;

            } else {
                MAX_PER_ROW_COUNT = 3;
                imgViewW = (pxMultiImageViewWidth - pxImagePadding * 2) / 3;
                imgViewH = imgViewW;

            }
            int layoutW = imgViewW * MAX_PER_ROW_COUNT + pxImagePadding * (MAX_PER_ROW_COUNT - 1);
            LayoutParams rowParams = new LayoutParams(layoutW, imgViewH);
            int rowCount = allCount / MAX_PER_ROW_COUNT + (allCount % MAX_PER_ROW_COUNT > 0 ? 1 : 0);//行数
            for (int rowCursor = 0; rowCursor < rowCount; rowCursor++) {
                LinearLayout rowLayout = new LinearLayout(getContext());
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                rowParams.setMargins(0, rowCursor == 0 ? 0 : pxImagePadding, 0, 0);
                rowLayout.setLayoutParams(rowParams);
                addView(rowLayout);

                int columnCount = allCount % MAX_PER_ROW_COUNT == 0 ? MAX_PER_ROW_COUNT : allCount % MAX_PER_ROW_COUNT;//列数
                if (rowCursor != rowCount - 1) {
                    columnCount = MAX_PER_ROW_COUNT;

                }
                int rowOffset = rowCursor * MAX_PER_ROW_COUNT;// 行偏移
                for (int columnCursor = 0; columnCursor < columnCount; columnCursor++) {
                    int position = columnCursor + rowOffset;
                    rowLayout.addView(createImageView(position));

                }
            }
        }
    }

    /**
     * 计算单张图片的宽高
     */
    private int[] getOneImageViewWH() {
        int imgViewW, imgViewH;
        int wrap = photoInfoBeans.get(0).with;
        int match = photoInfoBeans.get(0).height;
        int maxW = 360 * pxMultiImageViewWidth / 750;
        if (wrap > match * 3) {//特款图
            imageScaleType = ImageView.ScaleType.CENTER;
            imgViewW = maxW;
            imgViewH = maxW / 3;

        } else if (match > wrap * 3) {//特高图
            imageScaleType = ImageView.ScaleType.CENTER;
            imgViewW = maxW / 3;
            imgViewH = maxW;

        } else {
            imageScaleType = ImageView.ScaleType.FIT_XY;
            if (wrap > match) {//宽大于高
                imgViewW = maxW;
                imgViewH = imgViewW * match / wrap;

            } else if (match > wrap) {//高大于宽
                imgViewH = maxW;
                imgViewW = wrap * imgViewH / match;

            } else {
                imgViewW = maxW;
                imgViewH = maxW;

            }
        }

        return new int[]{imgViewW, imgViewH};
    }

    /**
     * 图片
     *
     * @param position
     * @return
     */
    private ImageView createImageView(final int position) {
        PhotoInfoBean photoInfo = photoInfoBeans.get(position);
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(imageScaleType);
        imageView.setId(photoInfo.hashCode());
        LayoutParams imgParams = new LayoutParams(imgViewW, imgViewH);
        imgParams.setMargins(0, 0, (position + 1) % MAX_PER_ROW_COUNT != 0 ? pxImagePadding : 0, 0);
        imageView.setLayoutParams(imgParams);
        if (isScrolling) {
            imageView.setImageResource(R.drawable.shape_666_bg);

        } else {
            GlideUtils.getInstance().setImageView(getContext(), photoInfo.picKey, imageView, imgViewW, imgViewH, R.drawable.shape_666_bg);

        }
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                picKeys.clear();
                for (PhotoInfoBean photoInfoBean : photoInfoBeans) {
                    picKeys.add(photoInfoBean.picKey);

                }
                PhotoPagerActivity.startPhotoPagerActivity((Activity) getContext(), (Serializable) picKeys, position);

            }
        });

        return imageView;
    }
}