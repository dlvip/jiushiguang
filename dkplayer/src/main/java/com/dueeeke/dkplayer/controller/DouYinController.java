package com.dueeeke.dkplayer.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.dueeeke.dkplayer.player.IjkVideoView;

import earnmoney.download.com.dkplayer.R;

/**
 * 抖音
 * Created by xinyu on 2018/1/6.
 */

public class DouYinController extends BaseVideoController {

    private ImageView thumb;

    public DouYinController(@NonNull Context context) {
        super(context);
    }

    public DouYinController(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DouYinController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_douyin_controller;
    }

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
}
