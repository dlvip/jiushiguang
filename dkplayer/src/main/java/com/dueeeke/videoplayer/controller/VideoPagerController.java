package com.dueeeke.videoplayer.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.dueeeke.videoplayer.R;
import com.dueeeke.videoplayer.controller.base.BaseVideoController;
import com.dueeeke.videoplayer.player.IjkVideoView;

/**
 * Created by NING on 2018/8/10.
 */

public class VideoPagerController extends BaseVideoController {

    public VideoPagerController(@NonNull Context context) {
        super(context);
    }

    public VideoPagerController(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoPagerController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private ImageView thumb;

    @Override
    protected void initView() {
        super.initView();
        thumb = controllerView.findViewById(R.id.iv_thumb);
    }

    @Override
    public void setPlayState(int playState) {
        super.setPlayState(playState);

        switch (playState) {
            case IjkVideoView.STATE_IDLE:
                thumb.setVisibility(VISIBLE);
                break;
            case IjkVideoView.STATE_PLAYING:
                thumb.setVisibility(GONE);
                break;
            case IjkVideoView.STATE_PREPARED:
                break;
        }
    }

    public ImageView getThumb() {
        return thumb;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.controller_video_pager;
    }
}
