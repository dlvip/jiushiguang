package com.old.time.fragments;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.old.time.R;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.views.largeImageUtils.LargeImageView;

/**
 * 单张图片显示Fragment
 */
public class ImageDetailFragment extends BaseFragment {
    private LargeImageView mImageView;
    private String mImageUrl;

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);

        return f;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_image_detail;
    }

    @Override
    protected void lazyLoad() {
        String imgUrl = (getArguments() != null ? getArguments().getString("url") : null);

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
        GlideUtils.getInstance().getRequestManager(mContext).load(imgUrl).into(new ImageViewTarget<Drawable>(mImageView) {
            @Override
            protected void setResource(@Nullable Drawable resource) {

            }
        });
    }

    @Override
    public void getDataFromNet(boolean isRefresh) {

    }
}
