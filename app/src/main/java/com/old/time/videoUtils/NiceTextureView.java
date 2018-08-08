package com.old.time.videoUtils;

import android.content.Context;
import android.view.TextureView;

/**
 * Created by XiaoJianjun on 2017/6/21.
 * 重写TextureView，适配视频的宽高和旋转.
 * （参考自节操播放器 https://github.com/lipangit/JieCaoVideoPlayer）
 */
public class NiceTextureView extends TextureView {

    private int videoHeight;
    private int videoWidth;
    private int screenType;

    public NiceTextureView(Context context) {
        super(context);
    }

    public void adaptVideoSize(int videoWidth, int videoHeight) {
        if (this.videoWidth != videoWidth && this.videoHeight != videoHeight) {
            this.videoWidth = videoWidth;
            this.videoHeight = videoHeight;
            requestLayout();
        }
    }

    /**
     * 设置视频比例
     */
    public void setScreenScale(int type) {
        screenType = type;
        requestLayout();
    }

    @Override
    public void setRotation(float rotation) {
        if (rotation != getRotation()) {
            super.setRotation(rotation);
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        float viewRotation = getRotation();

        // 如果判断成立，则说明显示的TextureView和本身的位置是有90度的旋转的，所以需要交换宽高参数。
        if (viewRotation == 90f || viewRotation == 270f) {
            int tempMeasureSpec = widthMeasureSpec;
            widthMeasureSpec = heightMeasureSpec;
            heightMeasureSpec = tempMeasureSpec;
        }

        int width = getDefaultSize(videoWidth, widthMeasureSpec);
        int height = getDefaultSize(videoHeight, heightMeasureSpec);


        //如果设置了比例
        switch (screenType) {
            case NiceVideoPlayer.SCREEN_SCALE_ORIGINAL:
                width = videoWidth;
                height = videoHeight;
                break;
            case NiceVideoPlayer.SCREEN_SCALE_16_9:
                if (height > width / 16 * 9) {
                    height = width / 16 * 9;
                } else {
                    width = height / 9 * 16;
                }
                break;
            case NiceVideoPlayer.SCREEN_SCALE_4_3:
                if (height > width / 4 * 3) {
                    height = width / 4 * 3;
                } else {
                    width = height / 3 * 4;
                }
                break;
            case NiceVideoPlayer.SCREEN_SCALE_MATCH_PARENT:
                width = widthMeasureSpec;
                height = heightMeasureSpec;
                break;
            case NiceVideoPlayer.SCREEN_SCALE_CENTER_CROP:
                if (videoHeight > 0 && videoHeight > 0) {
                    if (videoHeight * height > width * videoHeight) {
                        width = height * videoHeight / videoHeight;
                    } else {
                        height = width * videoHeight / videoHeight;
                    }
                }
                break;
            default:
                if (videoHeight > 0 && videoHeight > 0) {

                    int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
                    int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
                    int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
                    int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

                    if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.EXACTLY) {
                        // the size is fixed
                        width = widthSpecSize;
                        height = heightSpecSize;

                        // for compatibility, we adjust size based on aspect ratio
                        if (videoHeight * height < width * videoHeight) {
                            //Log.i("@@@", "image too wide, correcting");
                            width = height * videoHeight / videoHeight;
                        } else if (videoHeight * height > width * videoHeight) {
                            //Log.i("@@@", "image too tall, correcting");
                            height = width * videoHeight / videoHeight;
                        }
                    } else if (widthSpecMode == MeasureSpec.EXACTLY) {
                        // only the width is fixed, adjust the height to match aspect ratio if possible
                        width = widthSpecSize;
                        height = width * videoHeight / videoHeight;
                        if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                            // couldn't match aspect ratio within the constraints
                            height = heightSpecSize;
                        }
                    } else if (heightSpecMode == MeasureSpec.EXACTLY) {
                        // only the height is fixed, adjust the width to match aspect ratio if possible
                        height = heightSpecSize;
                        width = height * videoHeight / videoHeight;
                        if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                            // couldn't match aspect ratio within the constraints
                            width = widthSpecSize;
                        }
                    } else {
                        // neither the width nor the height are fixed, try to use actual video size
                        width = videoHeight;
                        height = videoHeight;
                        if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                            // too tall, decrease both width and height
                            height = heightSpecSize;
                            width = height * videoHeight / videoHeight;
                        }
                        if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                            // too wide, decrease both width and height
                            width = widthSpecSize;
                            height = width * videoHeight / videoHeight;
                        }
                    }
                } else {
                    // no size yet, just adopt the given spec sizes
                }
                break;
        }

        setMeasuredDimension(width, height);
    }
}
