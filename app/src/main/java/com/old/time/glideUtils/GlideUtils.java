package com.old.time.glideUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.NotificationTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.old.time.R;
import com.old.time.constants.Constant;
import com.old.time.interfaces.ImageDownLoadCallBack;
import com.old.time.utils.DebugLog;
import com.old.time.utils.ScreenTools;
import com.old.time.utils.UIHelper;

import java.io.File;
import java.util.concurrent.ExecutionException;


/**
 * Created by NING on 2017/4/26.
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
     * 获取glide之前缓存过的图片地址
     *
     * @param url 网络图片的地址
     * @return
     */
    public String getImgPathFromCache(Context context, String url) {
        FutureTarget<File> future = getRequestManager(context).load(url).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        try {
            File cacheFile = future.get();
            String absolutePath = cacheFile.getAbsolutePath();
            return absolutePath;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
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
        manager.load(resourceId).apply(new RequestOptions().placeholder(resourceId)).into(imageView);
    }

    /**
     * 设置资源图片
     *
     * @param context
     * @param imageView
     * @param resourceId
     */
    public void setImageView(Context context, String picUrl, NotificationTarget imageView, int resourceId) {
        RequestManager manager = getRequestManager(context);
        if (manager == null) {

            return;
        }
        manager.asBitmap().load(picUrl).apply(new RequestOptions().placeholder(resourceId)).into(imageView);
    }

    /**
     * 设置图片
     *
     * @param url
     * @param imageView
     * @paramcontext
     */
    public void setImageView(Context context, String url, ImageView imageView) {
        setImageView(context, url, imageView, R.drawable.shape_radius_666);

    }

    /**
     * 设置图片  模糊
     *
     * @param context
     * @param url
     * @param imageView
     */
    public void setImgTransRes(Context context, String url, ImageView imageView, int W, int H) {
        setImageViewTrans(context, url, imageView, W, H, R.drawable.shape_radius_666);

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
        setImageView(context, url, imageView, width, height, R.drawable.shape_radius_666);

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
        setImageView(context, url, imageView, wh, wh, R.drawable.shape_radius_666);

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
        setImageView(context, url, imageView, w, h, R.drawable.shape_radius_666);

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
                .apply(new RequestOptions().transform(new BlurTransformation(context, 20)))//
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
                .apply(new RequestOptions().override(W, H).transform(new BlurTransformation(context, radius)))//
                .into(imageView);
    }

    /**
     * 设置圆角图片
     *
     * @param url
     * @param imageView
     * @param radius    角度
     */
    public void setRadiusImageView(Context context, String url, ImageView imageView, int radius) {
        if (TextUtils.isEmpty(url)) {
            setImageView(context, imageView, R.color.color_666);

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
                .apply(new RequestOptions()//
                        .skipMemoryCache(false)//
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//
                        .transform(new GlideRoundTransform(context, radius))//
                        .dontAnimate()//
                        .error(R.color.color_666)
//                        .centerCrop()//
                        .priority(Priority.HIGH))//
                .transition(DrawableTransitionOptions.withCrossFade())//
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
                .apply(new RequestOptions().placeholder(resourceId).error(resourceId))//
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
        RequestOptions options = new RequestOptions()//
                .placeholder(resourceId)//
                .override(width, height)//
                .error(resourceId);
        manager.load(url).apply(options).into(imageView);
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
                .apply(new RequestOptions().circleCrop())//
                .into(imageView);
    }

    /**
     * 设置原型图片
     *
     * @param context
     * @param imageView
     * @param resourceId
     */
    private void setRoundResource(Context context, ImageView imageView, int resourceId) {
        RequestManager manager = getRequestManager(context);
        if (manager == null) {

            return;
        }
        manager.load("")//
                .apply(new RequestOptions().placeholder(resourceId).circleCrop())//
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
        setVideoToImageView(context, url, imageView, R.drawable.shape_radius_666);

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
                .apply(new RequestOptions().placeholder(resourceId).error(resourceId))//
                .into(imageView);
    }

    /**
     * 获取图片
     *
     * @param context
     * @param url
     * @param w
     * @param h
     * @return
     */
    public Bitmap getBitmap(Context context, String url, int w, int h) {
        RequestManager manager = getRequestManager(context);
        if (manager == null || TextUtils.isEmpty(url)) {

            return null;
        }
        url = getPicUrl(url, w, h);
        Bitmap bitmap = null;
        try {
            bitmap = manager.asBitmap().load(url).submit(w, h).get();

        } catch (Exception e) {
            e.printStackTrace();

        }
        return bitmap;
    }

    /**
     * 获取图片
     *
     * @param context
     * @param url
     * @return
     */
    public Bitmap getBitmap(Context context, String url) {
        RequestManager manager = getRequestManager(context);
        if (manager == null || TextUtils.isEmpty(url)) {

            return null;
        }
        url = getPicUrl(url);
        Bitmap bitmap = null;
        try {
            bitmap = manager.asBitmap().load(url).submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();

        } catch (Exception e) {
            e.printStackTrace();

        }
        return bitmap;
    }

    /**
     * 获取图片
     *
     * @param context
     * @param url
     * @param downLoadCallBack
     */
    public void downLoadBitmap(Context context, String url, int[] WH, final ImageDownLoadCallBack downLoadCallBack) {
        RequestManager manager = getRequestManager(context);
        if (manager == null) {

            return;
        }
        url = getPicUrl(url, UIHelper.dip2px(50), UIHelper.dip2px(50));
        RequestBuilder<Bitmap> requestBuilder;
        if (TextUtils.isEmpty(url)) {
            requestBuilder = manager.asBitmap().load(R.mipmap.ic_launcher);

        } else {
            requestBuilder = manager.asBitmap().load(url);

        }
        requestBuilder.apply(new RequestOptions().override(UIHelper.dip2px(50))).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                if (downLoadCallBack != null) {
                    downLoadCallBack.onDownLoadSuccess(resource);

                }
            }
        });
    }

    /**
     * 获取图片
     *
     * @param context
     * @param url
     * @param radius
     * @param downLoadCallBack
     */
    public void downLoadBitmap(Context context, String url, int radius, final ImageDownLoadCallBack downLoadCallBack) {
        RequestManager manager = getRequestManager(context);
        if (manager == null) {

            return;
        }
        url = getPicUrl(url, UIHelper.dip2px(50), UIHelper.dip2px(50));
        RequestBuilder<Bitmap> requestBuilder;
        if (TextUtils.isEmpty(url)) {
            requestBuilder = manager.asBitmap().load(R.mipmap.ic_launcher);

        } else {
            requestBuilder = manager.asBitmap().load(url);

        }
        requestBuilder.apply(new RequestOptions().transform(new GlideRoundTransform(context, radius))//
                .override(UIHelper.dip2px(50))).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                if (downLoadCallBack != null) {
                    downLoadCallBack.onDownLoadSuccess(resource);

                }
            }
        });
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

            return "x-oss-process=image/resize";
        }
        if (!TextUtils.isEmpty(url) && url.contains("storage")) {//手机图片
            if (!url.startsWith("file:///") && !url.endsWith(".mp4")) {
                url = "file:///" + url;

            }
        } else if (!TextUtils.isEmpty(url) && url.contains("http")) {//网络图片


        } else {//图片路径为空  或 只有图片key
            if (!TextUtils.isEmpty(url) && "x-oss-process=image/resize".contains(url)) {//防止二次拼接后缀
                url = Constant.OSSURL + url;

            } else {
                if (width == 0 || height == 0) {
                    url = Constant.OSSURL + url;

                } else {
                    url = Constant.OSSURL + url + "?x-oss-process=image/resize,m_fill,w_" + width + ",h_" + height;

                }
            }
        }
        return url;
    }
}
