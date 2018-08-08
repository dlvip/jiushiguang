package com.dueeeke.dkplayer.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dueeeke.dkplayer.adapters.DouYinAdapter;
import com.dueeeke.dkplayer.beans.VideoBean;
import com.dueeeke.dkplayer.controller.DouYinController;
import com.dueeeke.dkplayer.player.IjkVideoView;
import com.dueeeke.dkplayer.player.PlayerConfig;
import com.dueeeke.dkplayer.util.DataUtil;

import java.util.ArrayList;
import java.util.List;

import earnmoney.download.com.dkplayer.R;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;

/**
 * 模仿抖音短视频
 * Created by xinyu on 2018/1/6.
 */

public class DouYinActivity extends AppCompatActivity {

    private static final String TAG = "DouYinActivity";
    private IjkVideoView mIjkVideoView;
    private DouYinController mDouYinController;
    private VerticalViewPager mVerticalViewPager;
    private DouYinAdapter mDouYinAdapter;
    private List<VideoBean> mVideoList;
    private List<View> mViews = new ArrayList<>();
    private int mCurrentPosition;
    private int mPlayingPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.str_douyin);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setContentView(R.layout.activity_douyin);

        mIjkVideoView = new IjkVideoView(this);
        PlayerConfig config = new PlayerConfig.Builder().setLooping().build();
        mIjkVideoView.setPlayerConfig(config);
        mDouYinController = new DouYinController(this);
        mIjkVideoView.setVideoController(mDouYinController);
        mVerticalViewPager = findViewById(R.id.vvp);
        mVideoList = DataUtil.getDouYinVideoList();
        for (VideoBean item : mVideoList) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_douyin, null);
            ImageView imageView = view.findViewById(R.id.thumb);
            Glide.with(this).load(item.getThumb()).into(imageView);
            mViews.add(view);
        }

        mDouYinAdapter = new DouYinAdapter(mViews);
        mVerticalViewPager.setAdapter(mDouYinAdapter);

        mVerticalViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mPlayingPosition == mCurrentPosition) return;
                if (state == VerticalViewPager.SCROLL_STATE_IDLE) {
                    mIjkVideoView.release();
                    ViewParent parent = mIjkVideoView.getParent();
                    if (parent != null && parent instanceof FrameLayout) {
                        ((FrameLayout) parent).removeView(mIjkVideoView);
                    }
                    startPlay();
                }
            }
        });
        //自动播放第一条
        mVerticalViewPager.post(new Runnable() {
            @Override
            public void run() {
                startPlay();

            }
        });
    }

    private void startPlay() {
        View view = mViews.get(mCurrentPosition);
        FrameLayout frameLayout = view.findViewById(R.id.container);
        ImageView imageView = view.findViewById(R.id.thumb);
        mDouYinController.getThumb().setImageDrawable(imageView.getDrawable());
        frameLayout.addView(mIjkVideoView);
        mIjkVideoView.setUrl(mVideoList.get(mCurrentPosition).getUrl());
        mIjkVideoView.setScreenScale(IjkVideoView.SCREEN_SCALE_CENTER_CROP);
        mIjkVideoView.start();
        mPlayingPosition = mCurrentPosition;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIjkVideoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIjkVideoView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIjkVideoView.release();
    }
}
