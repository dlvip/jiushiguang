package com.old.time.fragments;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.old.time.R;
import com.old.time.glideUtils.ProgressTarget;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.DebugLog;
import com.old.time.views.largeImageUtils.LargeImageView;
import com.old.time.views.largeImageUtils.factory.FileBitmapDecoderFactory;

import java.io.File;

/**
 * 单张图片显示Fragment
 */
public class ImageDetailFragment extends Fragment {
    private String mImageUrl;
    private LargeImageView mImageView;
    private ProgressBar progressBar;

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String imgUrl = (getArguments() != null ? getArguments().getString("url") : null);
        if (!TextUtils.isEmpty(imgUrl) && imgUrl.contains("storage")) {
            imgUrl = "file:///" + imgUrl;

        } else if (!TextUtils.isEmpty(imgUrl) && imgUrl.contains("http")) {

        }
        this.mImageUrl = imgUrl;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_detail, container, false);
        mImageView = (LargeImageView) v.findViewById(R.id.image);
        mImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        progressBar = (ProgressBar) v.findViewById(R.id.loading);

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
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Glide.with(this).load(mImageUrl).downloadOnly(new ProgressTarget<String, File>(mImageUrl, null) {
            @Override
            public void onLoadStarted(Drawable placeholder) {
                super.onLoadStarted(placeholder);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onProgress(long bytesRead, long expectedLength) {
                int p = 0;
                if (expectedLength >= 0) {
                    p = (int) (100 * bytesRead / expectedLength);

                }
                DebugLog.e("onProgress::", p + "");
            }

            @Override
            public void onResourceReady(File resource, GlideAnimation<? super File> animation) {
                super.onResourceReady(resource, animation);
                progressBar.setVisibility(View.GONE);
                mImageView.setImage(new FileBitmapDecoderFactory(resource));
            }

            @Override
            public void getSize(SizeReadyCallback cb) {
                cb.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                progressBar.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
