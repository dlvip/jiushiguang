package com.old.time.views;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.old.time.R;
import com.old.time.activitys.PhotoPagerActivity;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.utils.ScreenTools;
import com.old.time.utils.UIHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shoyu
 * @ClassName MultiImageView.java
 * @Description: 显示1~N张图片的View
 */

public class MultiImageView extends LinearLayout {

    public static int MAX_WIDTH = 0;

    // 照片的Url列表
    private List<String> imagesList;
    private List<PhotoWHBean> photoWHBeans = new ArrayList<>();

    /**
     * 长度 单位为Pixel
     **/
    private int pxOneMaxWandH;  // 单张图最大允许宽高
    private int pxMoreWandH = 0;// 多张图的宽高
    private int pxImagePadding = UIHelper.dip2px(3);// 图片间的间距

    private int MAX_PER_ROW_COUNT = 3;// 每行显示最大数

    private LayoutParams onePicPara;
    private LayoutParams morePara, moreParaColumnFirst;
    private LayoutParams rowPara;

    private int weightW = ScreenTools.instance(getContext()).getScreenWidth();
    private int hidthW = ScreenTools.instance(getContext()).getScreenHeight();

    public MultiImageView(Context context) {
        super(context);
    }

    public MultiImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param lists
     * @param picattribute 图片宽高信息
     * @throws IllegalArgumentException
     */
    public void setList(List<String> lists, String picattribute) throws IllegalArgumentException {
        if (lists == null) {
            throw new IllegalArgumentException("imageList is null...");
        }
        photoWHBeans.clear();
        if (!TextUtils.isEmpty(picattribute)) {
            JSONArray jsonArray;
            try {
                jsonArray = new JSONArray(picattribute);
                for (int i = 0; i < jsonArray.length(); i++) {
                    PhotoWHBean photoWHBean = new PhotoWHBean();
                    photoWHBean.width = jsonArray.getJSONObject(i).getInt("width");
                    photoWHBean.height = jsonArray.getJSONObject(i).getInt("height");
                    photoWHBeans.add(photoWHBean);

                }
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
        imagesList = lists;
        if (MAX_WIDTH > 0) {
            pxMoreWandH = (MAX_WIDTH - pxImagePadding * 2) / 3; //解决右侧图片和内容对不齐问题
            pxOneMaxWandH = MAX_WIDTH * 2 / 3;
            initImageLayoutParams();

        }

        initView();
    }

    public void setListSelf(List<String> lists) throws IllegalArgumentException {
        if (lists == null) {

            throw new IllegalArgumentException("imageList is null...");
        }
        imagesList = lists;
        if (MAX_WIDTH > 0) {
            pxMoreWandH = (MAX_WIDTH - pxImagePadding * 2) / 3; //解决右侧图片和内容对不齐问题
            pxOneMaxWandH = MAX_WIDTH * 2 / 3;
            initImageLayoutParams();

        }

        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MAX_WIDTH == 0) {
            int width = measureWidth(widthMeasureSpec);
            if (width > 0) {
                MAX_WIDTH = width;
                if (imagesList != null && imagesList.size() > 0) {
                    setListSelf(imagesList);

                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * Determines the width of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;

        } else {
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);

            }
        }
        return result;
    }

    private void initImageLayoutParams() {
        int wrap = LayoutParams.WRAP_CONTENT;
        int match = LayoutParams.WRAP_CONTENT;

        onePicPara = new LayoutParams(wrap, wrap);

        moreParaColumnFirst = new LayoutParams(pxMoreWandH, pxMoreWandH);

        morePara = new LayoutParams(pxMoreWandH, pxMoreWandH);

        morePara.setMargins(pxImagePadding, 0, 0, 0);

        rowPara = new LayoutParams(match, wrap);
    }

    // 根据imageView的数量初始化不同的View布局,还要为每一个View作点击效果
    private void initView() {
        this.setOrientation(VERTICAL);
        this.removeAllViews();
        if (MAX_WIDTH == 0) {
            //为了触发onMeasure()来测量MultiImageView的最大宽度，MultiImageView的宽设置为match_parent
            addView(new View(getContext()));
            return;
        }

        if (imagesList == null || imagesList.size() == 0) {
            return;
        }

        if (imagesList.size() == 1) {
            addView(createImageView(0, false));

        } else {
            int allCount = imagesList.size();
            if (allCount == 4) {
                MAX_PER_ROW_COUNT = 2;

            } else {
                MAX_PER_ROW_COUNT = 3;

            }
            int rowCount = allCount / MAX_PER_ROW_COUNT + (allCount % MAX_PER_ROW_COUNT > 0 ? 1 : 0);// 行数
            for (int rowCursor = 0; rowCursor < rowCount; rowCursor++) {
                LinearLayout rowLayout = new LinearLayout(getContext());
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);

                rowLayout.setLayoutParams(rowPara);
                if (rowCursor != 0) {
                    rowLayout.setPadding(0, pxImagePadding, 0, 0);
                }

                int columnCount = allCount % MAX_PER_ROW_COUNT == 0 ? MAX_PER_ROW_COUNT : allCount % MAX_PER_ROW_COUNT;//每行的列数
                if (rowCursor != rowCount - 1) {
                    columnCount = MAX_PER_ROW_COUNT;
                }
                addView(rowLayout);

                int rowOffset = rowCursor * MAX_PER_ROW_COUNT;// 行偏移
                for (int columnCursor = 0; columnCursor < columnCount; columnCursor++) {
                    int position = columnCursor + rowOffset;
                    rowLayout.addView(createImageView(position, true));

                }
            }
        }
    }

    private ImageView createImageView(final int position, final boolean isMultiImage) {
        final String photoInfo = imagesList.get(position);
        final ImageView imageView = new ImageView(getContext());
        if (isMultiImage) {//多张图片
            imageView.setScaleType(ScaleType.CENTER_CROP);
            imageView.setLayoutParams(position % MAX_PER_ROW_COUNT == 0 ? moreParaColumnFirst : morePara);
            GlideUtils.getInstance().setImageViewWH(getContext(), photoInfo, imageView, pxMoreWandH, R.drawable.shape_666_bg);

        } else {//单张图片
            imageView.setAdjustViewBounds(true);
            LinearLayout.LayoutParams params;
            int imgW;
            int imgH;
            if (photoWHBeans != null && photoWHBeans.size() == 1) {//一张图片的宽高
                int wrap = photoWHBeans.get(0).width;
                int match = photoWHBeans.get(0).height;
                int maxW = 360 * weightW / 750;
                if (wrap > match * 3) {//特款图
                    imageView.setScaleType(ScaleType.CENTER);
                    imgW = maxW;
                    imgH = maxW / 3;

                } else if (match > wrap * 3) {//特高图
                    imageView.setScaleType(ScaleType.CENTER);
                    imgW = maxW / 3;
                    imgH = maxW;

                } else {
                    imageView.setScaleType(ScaleType.FIT_XY);
                    if (wrap > match) {//宽大于高
                        imgW = maxW;
                        imgH = imgW * match / wrap;

                    } else if (match > wrap) {//高大于宽
                        imgH = maxW;
                        imgW = wrap * imgH / match;

                    } else {
                        imgW = maxW;
                        imgH = maxW;
                    }
                }
                params = new LayoutParams(imgW, imgH);

            } else {//没有图片宽高的就给一个默认的宽高
                imageView.setScaleType(ScaleType.CENTER_CROP);
                imgW = UIHelper.dip2px(150);
                imgH = UIHelper.dip2px(100);
                params = new LayoutParams(imgW, imgH);

            }
            imageView.setLayoutParams(params);
            GlideUtils.getInstance().setImageView(getContext(), photoInfo, imageView, imgW, imgH, R.drawable.shape_666_bg);
        }

        imageView.setId(photoInfo.hashCode());
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPagerActivity.startPhotoPagerActivity((Activity) getContext(), (Serializable) imagesList, position);

            }
        });
        imageView.setBackgroundColor(getResources().getColor(R.color.color_666));

        return imageView;
    }

    public class PhotoWHBean implements Serializable {
        public int width;
        public int height;

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }
}