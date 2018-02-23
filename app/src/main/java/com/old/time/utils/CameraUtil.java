package com.old.time.utils;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.DisplayMetrics;
import android.view.Surface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class CameraUtil {

    public static float scale;

    public static int densityDpi;

    public static float fontScale;

    public static int screenWidth;

    public static int screenHeight;

    public static void init(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        scale = dm.density;
        densityDpi = dm.densityDpi;
        fontScale = dm.scaledDensity;
        if (dm.widthPixels < dm.heightPixels) {
            screenWidth = dm.widthPixels;
            screenHeight = dm.heightPixels;

        } else {
            screenWidth = dm.heightPixels;
            screenHeight = dm.widthPixels;

        }
    }

    /**
     * 保证预览方向正确
     *
     * @param activity
     * @param cameraId
     * @param camera
     */
    public static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;

                break;
            case Surface.ROTATION_90:
                degrees = 90;

                break;
            case Surface.ROTATION_180:
                degrees = 180;

                break;
            case Surface.ROTATION_270:
                degrees = 270;

                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror

        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;

        }
        camera.setDisplayOrientation(result);
    }

    /**
     * 获取所有支持的返回图片尺寸
     */
    public static Size getPropPictureSize(List<Size> list, int minWidth) {
        Collections.sort(list, new Comparator<Size>() {
            @Override
            public int compare(Size lhs, Size rhs) {//降序
                if (lhs.width == rhs.width) {

                    return 0;
                } else if (lhs.width < rhs.width) {

                    return 1;
                } else {

                    return -1;
                }
            }
        });
        int i = 0;
        for (Size s : list) {
            if ((s.width >= minWidth)) {

                break;
            }
            i++;
        }
        if (i == list.size()) {
            i = 0;//如果没找到，就选最小的size

        }
        return list.get(i);
    }

    /**
     * 最小预览界面的分辨率
     */
    private static final int MIN_PREVIEW_PIXELS = 480 * 320;

    /**
     * 最大宽高比差
     */
    private static final double MAX_ASPECT_DISTORTION = 0.15;

    /**
     * 预览效果
     *
     * @param mCamera
     * @return
     */
    public static Size findBestPreviewResolution(Camera mCamera) {
        Camera.Parameters cameraParameters = mCamera.getParameters();
        Size defaultPreviewResolution = cameraParameters.getPreviewSize();

        List<Size> rawSupportedSizes = cameraParameters.getSupportedPreviewSizes();
        if (rawSupportedSizes == null) {

            return defaultPreviewResolution;
        }

        // 按照分辨率从大到小排序
        List<Size> supportedPreviewResolutions = new ArrayList<>(rawSupportedSizes);
        Collections.sort(supportedPreviewResolutions, new Comparator<Size>() {
            @Override
            public int compare(Size a, Size b) {
                int aPixels = a.height * a.width;
                int bPixels = b.height * b.width;
                if (bPixels < aPixels) {

                    return -1;
                }
                if (bPixels > aPixels) {

                    return 1;
                }
                return 0;
            }
        });

        StringBuilder previewResolutionSb = new StringBuilder();
        for (Size supportedPreviewResolution : supportedPreviewResolutions) {
            previewResolutionSb.append(supportedPreviewResolution.width).append('x').append(supportedPreviewResolution.height)
                    .append(' ');
        }

        // 移除不符合条件的分辨率
        double screenAspectRatio = (double) (screenWidth / screenHeight);
        Iterator<Size> it = supportedPreviewResolutions.iterator();
        while (it.hasNext()) {
            Size supportedPreviewResolution = it.next();
            int width = supportedPreviewResolution.width;
            int height = supportedPreviewResolution.height;

            // 移除低于下限的分辨率，尽可能取高分辨率
            if (width * height < MIN_PREVIEW_PIXELS) {
                it.remove();
                continue;
            }

            // 在camera分辨率与屏幕分辨率宽高比不相等的情况下，找出差距最小的一组分辨率
            // 由于camera的分辨率是width>height，我们设置的portrait模式中，width<height
            // 因此这里要先交换然preview宽高比后在比较
            boolean isCandidatePortrait = width > height;
            int maybeFlippedWidth = isCandidatePortrait ? height : width;
            int maybeFlippedHeight = isCandidatePortrait ? width : height;
            double aspectRatio = (double) maybeFlippedWidth / (double) maybeFlippedHeight;
            double distortion = Math.abs(aspectRatio - screenAspectRatio);

            // 找到与屏幕分辨率完全匹配的预览界面分辨率直接返回
            if (maybeFlippedWidth == screenWidth
                    && maybeFlippedHeight == screenHeight) {
                return supportedPreviewResolution;
            }

            if (distortion > MAX_ASPECT_DISTORTION) {
                it.remove();

                continue;
            }
        }

        // 如果没有找到合适的，并且还有候选的像素，则设置其中最大比例的，对于配置比较低的机器不太合适
        if (!supportedPreviewResolutions.isEmpty()) {
            Size largestPreview = supportedPreviewResolutions.get(0);

            return largestPreview;
        }

        // 没有找到合适的，就返回默认的
        return defaultPreviewResolution;
    }
}
