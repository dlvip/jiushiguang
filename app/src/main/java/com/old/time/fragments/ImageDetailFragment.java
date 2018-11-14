package com.old.time.fragments;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.zxing.utils.ReadCodeUtils;
import com.old.time.R;
import com.old.time.activitys.WebViewActivity;
import com.old.time.dialogs.DialogListManager;
import com.old.time.glideUtils.GlideDealUtils;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.interfaces.ImageDownLoadCallBack;
import com.old.time.interfaces.OnClickManagerCallBack;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.UIHelper;
import com.old.time.views.largeImageUtils.LargeImageView;

import java.util.ArrayList;

/**
 * 单张图片显示Fragment
 */
public class ImageDetailFragment extends BaseFragment {
    private LargeImageView mImageView;

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);

        return f;
    }

    private String imgUrl;

    @Override
    protected int setContentView() {
        return R.layout.fragment_image_detail;
    }

    @Override
    protected void lazyLoad() {
        this.imgUrl = (getArguments() != null ? getArguments().getString("url") : null);

        mImageView = findViewById(R.id.image);
        mImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return false;
            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.finishPicActivity((Activity) getContext());

            }
        });
        GlideUtils.getInstance().getRequestManager(mContext).asDrawable().load(imgUrl).into(new SimpleTarget<Drawable>() {

            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                if (resource != null) {
                    mImageView.setImage(resource);

                } else {
                    UIHelper.ToastMessage(mContext, "加载失败");

                }
            }
        });
        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String imgFileUrl = GlideUtils.getInstance().getImgPathFromCache(mContext, imgUrl);
                        codeUrl = ReadCodeUtils.scanningImage(imgFileUrl);
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showMoreDialog();

                            }
                        });
                    }
                }).start();

                return false;
            }
        });
    }

    private DialogListManager dialogListManager;
    private String codeUrl;

    private void showMoreDialog() {
        if (TextUtils.isEmpty(imgUrl)) {

            return;
        }
        if (dialogListManager == null) {
            dialogListManager = new DialogListManager(mContext, new OnClickManagerCallBack() {
                @Override
                public void onClickRankManagerCallBack(int position, String typeName) {
                    if (position == 0) {
                        GlideDealUtils.onDownLoad(imgUrl, new ImageDownLoadCallBack() {
                            @Override
                            public void onDownLoadSuccess(Bitmap bitmap) {
                                mContext.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        UIHelper.ToastMessage(mContext, "保存成功");

                                    }
                                });
                            }
                        });
                    } else {
                        WebViewActivity.startWebViewActivity(mContext, codeUrl);

                    }
                }
            });
        }
        if (TextUtils.isEmpty(codeUrl)) {
            dialogListManager.setDialogViewData("操作提示", new String[]{"保存图片"});

        } else {
            dialogListManager.setDialogViewData("操作提示", new String[]{"保存图片", "识别图中二维码"});

        }
    }

    @Override
    public void getDataFromNet(boolean isRefresh) {

    }
}
