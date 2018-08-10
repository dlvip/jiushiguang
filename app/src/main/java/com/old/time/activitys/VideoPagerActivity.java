package com.old.time.activitys;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dueeeke.videoplayer.adapters.VideoPagerAdapter;
import com.dueeeke.videoplayer.controller.VideoPagerController;
import com.dueeeke.videoplayer.demo.DataUtil;
import com.dueeeke.videoplayer.demo.VideoBean;
import com.dueeeke.videoplayer.player.IjkVideoView;
import com.dueeeke.videoplayer.player.PlayerConfig;
import com.old.time.R;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

public class VideoPagerActivity extends BaseActivity {

    private VideoPagerController mVideoPagerController;
    private VerticalViewPager mVerticalViewPager;
    private IjkVideoView mIjkVideoView;

    private VideoPagerAdapter mVideoPagerAdapter;
    private List<VideoBean> mVideoList;
    private List<View> mViews = new ArrayList<>();

    private int mCurrentPosition;
    private int mPlayingPosition;

    @Override
    protected void initView() {
        setStatusBarTransparent();

        mVerticalViewPager = findViewById(R.id.vertical_view_pager);
        mIjkVideoView = new IjkVideoView(this);
        PlayerConfig config = new PlayerConfig.Builder().setLooping().build();
        mIjkVideoView.setPlayerConfig(config);
        mVideoPagerController = new VideoPagerController(this);
        mIjkVideoView.setVideoController(mVideoPagerController);
        mVideoList = DataUtil.getVideoPagerList();
        for (VideoBean item : mVideoList) {
            View view = LayoutInflater.from(this).inflate(R.layout.adapter_video_pager, null);
            ImageView imageView = view.findViewById(R.id.thumb);
            Glide.with(this).load(item.getThumb()).into(imageView);
            mViews.add(view);

        }
        mVideoPagerAdapter = new VideoPagerAdapter(mViews);
        mVerticalViewPager.setAdapter(mVideoPagerAdapter);
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
        mVideoPagerController.getThumb().setImageDrawable(imageView.getDrawable());
        frameLayout.addView(mIjkVideoView);
        mIjkVideoView.setUrl(mVideoList.get(mCurrentPosition).getUrl());
        mIjkVideoView.setScreenScale(IjkVideoView.SCREEN_SCALE_CENTER_CROP);
        mIjkVideoView.start();
        mPlayingPosition = mCurrentPosition;

    }

    /**
     * 把状态栏设成透明
     */
    private void setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            return;
        }
        View decorView = VideoPagerActivity.this.getWindow().getDecorView();
        decorView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

                    return windowInsets;
                }
                windowInsets = view.onApplyWindowInsets(windowInsets);
                windowInsets = windowInsets.replaceSystemWindowInsets(windowInsets.getSystemWindowInsetLeft()//
                        , 0, windowInsets.getSystemWindowInsetRight()//
                        , windowInsets.getSystemWindowInsetBottom());
                return windowInsets;
            }
        });
        ViewCompat.requestApplyInsets(decorView);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
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

    @Override
    protected int getLayoutID() {
        return R.layout.activity_video_pager;
    }
}
