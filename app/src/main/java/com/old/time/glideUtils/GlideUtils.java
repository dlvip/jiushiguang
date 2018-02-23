package com.old.time.glideUtils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.old.time.R;
import com.old.time.utils.DebugLog;
import com.old.time.utils.ScreenTools;


import jp.wasabeef.glide.transformations.BlurTransformation;

import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by wangfang on 2017/4/26.
 */

public class GlideUtils {

    private static String TAG = "GlideUtils";
    private static GlideUtils mInstance;

    /**
     * 单例
     *
     * @return
     */
    public static GlideUtils getInstance() {
        if (mInstance == null) {
            mInstance = new GlideUtils();

        }
        return mInstance;
    }

    public RequestManager getRequestManager(Context activity) {
        RequestManager manager = null;
        try {
            manager = Glide.with(activity);

        } catch (Exception e) {
            DebugLog.i(TAG, "RequestManager is null-->" + e);

        }
        return manager;
    }

    /**
     * 设置资源图片
     *
     * @param context
     * @param imageView
     * @param resourceId
     */
    public void setImageView(Context context, ImageView imageView, int resourceId) {
        RequestManager manager = getRequestManager(context);
        if (manager == null) {

            return;
        }
        manager.load(resourceId).placeholder(resourceId).into(imageView);
    }

    /**
     * 设置圆形资源图片
     *
     * @param context
     * @param imageView
     * @param resourceId
     */
    public void setRoundResource(Context context, ImageView imageView, int resourceId) {
        RequestManager manager = getRequestManager(context);
        if (manager == null) {

            return;
        }
        manager.load(resourceId).bitmapTransform(new FitCenter(context), new CropCircleTransformation(context)).into(imageView);
    }

    /**
     * 设置图片
     *
     * @param url
     * @param imageView
     * @paramcontext
     */
    public void setImageView(Context context, String url, ImageView imageView) {
        setImageView(context, url, imageView, R.mipmap.ic_launcher);

    }

    /**
     * 设置图片,带回调
     *
     * @param url
     * @param imageView
     * @paramcontext
     */
    public void setImageViewBack(Context context, String url, ImageView imageView) {

        if (TextUtils.isEmpty(url)) {
            setImageView(context, imageView, R.mipmap.ic_launcher);
            return;
        }
        url = getPicUrl(url);
        if (context == null) {

            return;
        }
        RequestManager manager = getRequestManager(context);
        if (manager == null) {

            return;
        }
        manager.load(url)// 加载图片资源
                .skipMemoryCache(false)//是否将图片放到内存中
                .diskCacheStrategy(DiskCacheStrategy.ALL)//磁盘图片缓存策略
                .crossFade(500)// 默认淡入淡出动画300ms
                .thumbnail(0.1f)
                .dontAnimate()
                .priority(Priority.HIGH)// 当前线程的优先级
                .signature(new StringSignature(url))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);

    }

    /**
     * 设置图片  模糊
     *
     * @param context
     * @param url
     * @param imageView
     */
    public void setImgTransRes(Context context, String url, ImageView imageView, int W, int H) {
        setImageViewTrans(context, url, imageView, W, H, R.mipmap.ic_launcher);

    }

    /**
     * 设置指定大小的图片
     *
     * @param url
     * @param imageView
     * @param width
     * @param height
     * @paramcontext
     */
    public void setImageView(Context context, String url, ImageView imageView, int width, int height) {
        setImageView(context, url, imageView, width, height, R.mipmap.ic_launcher);

    }

    /**
     * 设置指定大小的图片  宽高相同
     *
     * @param url
     * @param imageView
     * @param wh
     * @paramcontext
     */
    public void setImageViewWH(Context context, String url, ImageView imageView, int wh) {
        setImageView(context, url, imageView, wh, wh, R.mipmap.ic_launcher);

    }

    /**
     * 设置指定大小的图片  宽高相同
     *
     * @param url
     * @param imageView
     * @param wh
     * @paramcontext
     */
    public void setImageViewWH(Context context, String url, ImageView imageView, int wh, int resourceId) {
        setImageView(context, url, imageView, wh, wh, resourceId);

    }

    /**
     * 设置指定大小的图片
     *
     * @param context
     * @param url
     * @param imageView
     * @param w
     * @param h
     */
    public void setimageView(Context context, String url, ImageView imageView, int w, int h) {
        setImageView(context, url, imageView, w, h, R.mipmap.ic_launcher);

    }

    /**
     * 设置圆形图片
     *
     * @param url
     * @param imageView
     */
    public void setRoundImageView(Context context, String url, ImageView imageView) {
        setRoundImageView(context, url, imageView, R.mipmap.head_circle);

    }

    /**
     * 设置图片  模糊
     *
     * @param url
     * @param imageView
     * @param resourceId
     * @paramcontext
     */
    public void setImageViewTrans(Context context, String url, ImageView imageView, int W, int H, int resourceId) {
        if (TextUtils.isEmpty(url)) {
            setImageView(context, imageView, resourceId);
            return;
        }
        url = getPicUrl(url, W, H);
        if (context == null) {

            return;
        }
        RequestManager manager = getRequestManager(context);
        if (manager == null) {

            return;
        }
        manager.load(url)// 加载图片资源
                .skipMemoryCache(false)//是否将图片放到内存中
                .diskCacheStrategy(DiskCacheStrategy.ALL)//磁盘图片缓存策略
                .crossFade(500)// 默认淡入淡出动画500ms
                .error(resourceId)
                .thumbnail(0.1f)// 占位图片
                .centerCrop()//  fitCenter()
                .bitmapTransform(new BlurTransformation(context, 20))// bitmap操作
                .priority(Priority.HIGH)// 当前线程的优先级
                .signature(new StringSignature(url))
                .into(imageView);
    }

    /**
     * 设置图片  模糊
     *
     * @param url
     * @param imageView
     * @paramcontext
     */
    public void setImageViewTransRadius(Context context, String url, ImageView imageView, int W, int H, int radius) {
        if (TextUtils.isEmpty(url)) {

            return;
        }
        url = getPicUrl(url, W, H);
        if (context == null) {

            return;
        }
        RequestManager manager = getRequestManager(context);
        if (manager == null) {

            return;
        }
        manager.load(url)// 加载图片资源
                .skipMemoryCache(false)//是否将图片放到内存中
                .diskCacheStrategy(DiskCacheStrategy.ALL)//磁盘图片缓存策略
                .crossFade(500)// 默认淡入淡出动画500ms
                .error(R.mipmap.ic_launcher)
                .thumbnail(0.1f)// 占位图片
                .centerCrop()
                .bitmapTransform(new BlurTransformation(context, radius)
                        , new ColorFilterTransformation(context, 0x80000000))// bitmap操作
                .priority(Priority.HIGH)// 当前线程的优先级
                .signature(new StringSignature(url))
                .into(imageView);
    }

    /**
     * 设置圆角图片
     *
     * @param url
     * @param imageView
     * @param dp        角度
     */
    public void setRadiusImageView(Context context, String url, ImageView imageView, int resId, int dp) {
        if (TextUtils.isEmpty(url)) {
            setImageView(context, imageView, resId);

            return;
        }
        url = getPicUrl(url);
        if (context == null) {

            return;
        }
        RequestManager manager = getRequestManager(context);
        if (manager == null) {

            return;
        }
        manager.load(url)// 加载图片资源
                .skipMemoryCache(false)//是否将图片放到内存中
                .diskCacheStrategy(DiskCacheStrategy.ALL)//磁盘图片缓存策略
                .crossFade(500)// 默认淡入淡出动画300ms
                .thumbnail(0.1f)
                .dontAnimate()
                .centerCrop()
                .error(resId)
                .transform(new CenterCrop(context), new GlideRoundTransform(context, dp))
                .priority(Priority.HIGH)// 当前线程的优先级
                .signature(new StringSignature(url))
                .into(imageView);

    }

    /**
     * 设置图片
     *
     * @param url
     * @param imageView
     * @param resourceId
     * @paramcontext
     */
    public void setImageView(Context context, String url, ImageView imageView, int resourceId) {
        if (TextUtils.isEmpty(url)) {
            setImageView(context, imageView, resourceId);

            return;
        }
        url = getPicUrl(url);
        if (context == null) {

            return;
        }
        RequestManager manager = getRequestManager(context);
        if (manager == null) {

            return;
        }
        manager.load(url)// 加载图片资源
                .skipMemoryCache(false)//是否将图片放到内存中
                .diskCacheStrategy(DiskCacheStrategy.ALL)//磁盘图片缓存策略
                .crossFade(500)// 默认淡入淡出动画300ms
                .thumbnail(0.1f)
                .dontAnimate()
                .priority(Priority.HIGH)// 当前线程的优先级
                .signature(new StringSignature(url))
                .error(resourceId)
                .into(imageView);
    }

    /**
     * 设置指定大小的图片
     *
     * @param url
     * @param imageView
     * @param width
     * @param height
     * @param resourceId
     * @paramcontext
     */
    public void setImageView(Context context, String url, ImageView imageView, int width, int height, int resourceId) {
        if (TextUtils.isEmpty(url)) {
            setImageView(context, imageView, resourceId);

            return;
        }
        url = getPicUrl(url, width, height);
        if (context == null) {

            return;
        }
        RequestManager manager = getRequestManager(context);
        if (manager == null) {

            return;
        }
        manager.load(url)// 加载图片资源
                .skipMemoryCache(false)//是否将图片放到内存中
                .diskCacheStrategy(DiskCacheStrategy.ALL)//磁盘图片缓存策略
                .crossFade(500)// 默认淡入淡出动画300ms
                .thumbnail(0.1f)
                .dontAnimate()
                .placeholder(resourceId)
                .priority(Priority.HIGH)// 当前线程的优先级
                .signature(new StringSignature(url))
                .error(resourceId)
                .into(imageView);
    }

    /**
     * 设置圆形图片
     *
     * @param url
     * @param imageView
     * @param resourceId
     */
    public void setRoundImageView(Context context, String url, ImageView imageView, int resourceId) {
        if (TextUtils.isEmpty(url)) {
            setRoundResource(context, imageView, resourceId);

            return;
        }
        url = getPicUrl(url, ScreenTools.instance(context).dip2px(200), ScreenTools.instance(context).dip2px(200));
        if (context == null) {

            return;
        }
        RequestManager manager = getRequestManager(context);
        if (manager == null) {

            return;
        }
        manager.load(url)// 加载图片资源
                .skipMemoryCache(false)//是否将图片放到内存中
                .diskCacheStrategy(DiskCacheStrategy.ALL)//磁盘图片缓存策略
                .crossFade(500)// 默认淡入淡出动画300ms
                .thumbnail(0.1f)
                .dontAnimate()
                .centerCrop()
                .error(resourceId)
                .bitmapTransform(new FitCenter(context), new CropCircleTransformation(context))
                .priority(Priority.HIGH)// 当前线程的优先级
                .signature(new StringSignature(url))
                .into(imageView);
    }

    /**
     * 通过视频地址设置图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public void setVideoToImageView(Context context, String url, ImageView imageView) {
        setVideoToImageView(context, url, imageView, R.mipmap.ic_launcher);
    }

    /**
     * 通过视频地址设置图片
     *
     * @param context
     * @param url
     * @param imageView
     * @param resourceId
     */
    public void setVideoToImageView(Context context, String url, ImageView imageView, int resourceId) {
        if (TextUtils.isEmpty(url)) {
            setImageView(context, imageView, resourceId);
            return;
        }
        RequestManager manager = getRequestManager(context);
        if (manager == null) {
            return;
        }
        manager.load(url)// 加载图片资源
                .crossFade(500)// 默认淡入淡出动画300ms
                .thumbnail(0.1f)
                .dontAnimate()
                .placeholder(resourceId)
                .priority(Priority.HIGH)// 当前线程的优先级
                .error(resourceId)
                .into(imageView);
    }

    /**
     * 获取图片全路径
     *
     * @param url
     * @return
     */
    public static String getPicUrl(String url) {

        return getPicUrl(url, 0, 0);
    }

    public static String getPicUrl(String url, int width, int height) {
        if (TextUtils.isEmpty(url)) {

            return "?x-oss-process=image/resize";
        }
        if (!TextUtils.isEmpty(url) && url.contains("storage")) {//手机图片
            if (!url.startsWith("file:///")) {
                url = "file:///" + url;
            }
        } else if (!TextUtils.isEmpty(url) && url.contains("http")) {//网络图片


        } else {//图片路径为空  或 只有图片key
//            if (!TextUtils.isEmpty(url) && "x-oss-process=image/resize".contains(url)) {//防止二次拼接后缀
//                url = AppSyetemInit.getInstance().getOssUrl() + url;
//
//            } else {
//                if (width == 0 || height == 0) {
//                    url = AppSyetemInit.getInstance().getOssUrl() + url;
//
//                } else {
//                    url = AppSyetemInit.getInstance().getOssUrl() + url + "?x-oss-process=image/resize,m_fill,w_" + width + ",h_" + height;
//
//                }
//            }
        }
        return url;
    }
}
