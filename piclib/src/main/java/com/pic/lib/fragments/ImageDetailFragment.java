package com.pic.lib.fragments;


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

import com.pic.lib.PicCode;
import com.pic.lib.task.CallBackTask;
import com.pic.lib.task.TaskManager;
import com.pic.lib.utils.ActivityUtils;
import com.pic.lib.utils.ScreenTools;
import com.pic.lib.R;
import com.pic.lib.dialogs.DialogListManager;
import com.pic.lib.glideUtils.GlideDealUtils;
import com.pic.lib.glideUtils.GlideUtils;
import com.pic.lib.largeImageUtils.LargeImageView;
import com.pic.lib.utils.ReadCodeUtils;

/**
 * 单张图片显示Fragment
 */
public class ImageDetailFragment extends BaseFragment {

    private LargeImageView mImageView;
    private boolean isShowScan = false;

    public static ImageDetailFragment newInstance(String imageUrl, boolean isShowScan) {
        final ImageDetailFragment f = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        args.putBoolean("isShowScan", isShowScan);
        f.setArguments(args);

        return f;
    }

    private String imgUrl;

    @Override
    protected void loadView() {
        Bundle bundle = getArguments();
        assert bundle != null;
        this.imgUrl = bundle.getString("url");
        this.isShowScan = bundle.getBoolean("isShowScan", false);

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
                    ScreenTools.ToastMessage(mContext, "加载失败");

                }
            }
        });
        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!isShowScan) {

                    return false;
                }
                TaskManager.getInstance().delTask(PicCode.IMAGEDOWNLOAD_THREAD_NAME);
                TaskManager.getInstance().addTask(new CallBackTask(PicCode.IMAGEDOWNLOAD_THREAD_NAME) {
                    @Override
                    protected void doTask() {
                        String imgFileUrl = GlideUtils.getInstance().getImgPathFromCache(mContext, imgUrl);
                        codeUrl = ReadCodeUtils.scanningImage(imgFileUrl);
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showMoreDialog();

                            }
                        });
                    }
                });
                return false;
            }
        });
    }

    @Override
    protected int setContentView() {
        return R.layout.piclib_fragment_image_detail;
    }

    private DialogListManager dialogListManager;
    private String codeUrl;

    private void showMoreDialog() {
        if (TextUtils.isEmpty(imgUrl)) {

            return;
        }
        if (dialogListManager == null) {
            dialogListManager = new DialogListManager(mContext, new DialogListManager.OnClickManagerCallBack() {
                @Override
                public void onClickRankManagerCallBack(int position, String typeName) {
                    if (position == 0) {
                        GlideDealUtils.onDownLoad(mContext, imgUrl, new GlideDealUtils.ImageDownLoadCallBack() {
                            @Override
                            public void onDownLoadSuccess(Bitmap bitmap) {
                                mContext.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ScreenTools.ToastMessage(mContext, "保存成功");

                                    }
                                });
                            }
                        });
                    } else {
//                        WebViewActivity.startWebViewActivity(mContext, codeUrl);

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
}
