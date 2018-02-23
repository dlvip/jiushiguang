package com.old.time.glideUtils;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.old.time.views.largeImageUtils.ILargeImageView;
import com.old.time.views.largeImageUtils.factory.FileBitmapDecoderFactory;

import java.io.File;

/**
 * A base {@link com.bumptech.glide.request.target.Target} for displaying resources in
 * {@link ImageView}s.
 *
 * @param <Z> The type of resource that this target will display in the wrapped {@link ImageView}.
 */
public class LargeImageViewTarget extends ViewTarget<View, File> {
    private ILargeImageView largeImageView;
    public <V extends View & ILargeImageView> LargeImageViewTarget(V view) {
        super(view);
        this.largeImageView = view;
    }

    /**
     * Sets the given {@link Drawable} on the view using
     * {@link ImageView#setImageDrawable(Drawable)}.
     *
     * @param placeholder {@inheritDoc}
     */
    @Override
    public void onLoadStarted(Drawable placeholder) {
        largeImageView.setImageDrawable(placeholder);
    }

    /**
     * Sets the given {@link Drawable} on the view using
     * {@link ImageView#setImageDrawable(Drawable)}.
     *
     * @param errorDrawable {@inheritDoc}
     */
    @Override
    public void onLoadFailed(Exception e, Drawable errorDrawable) {
        largeImageView.setImageDrawable(errorDrawable);
    }

    @Override
    public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
        largeImageView.setImage(new FileBitmapDecoderFactory(resource));
    }

    /**
     * Sets the given {@link Drawable} on the view using
     * {@link ImageView#setImageDrawable(Drawable)}.
     *
     * @param placeholder {@inheritDoc}
     */
    @Override
    public void onLoadCleared(Drawable placeholder) {
        largeImageView.setImageDrawable(placeholder);
    }
}

