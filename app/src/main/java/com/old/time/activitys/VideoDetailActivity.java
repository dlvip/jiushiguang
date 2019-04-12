package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;

import com.dueeeke.videoplayer.controller.StandardVideoController;
import com.dueeeke.videoplayer.player.IjkPlayer;
import com.dueeeke.videoplayer.player.IjkVideoView;
import com.dueeeke.videoplayer.player.PlayerConfig;
import com.old.time.R;
import com.old.time.constants.Constant;
import com.old.time.utils.ActivityUtils;
import com.old.time.views.WebViewFragment;

public class VideoDetailActivity extends BaseActivity {

    public static String PLAY_URL = "playUrl";

    /**
     * 视频详情页
     *
     * @param mContext
     */
    public static void startVideoDetailActivity(Context mContext, String playUrl) {
        Intent intent = new Intent(mContext, VideoDetailActivity.class);
        intent.putExtra(PLAY_URL, playUrl);
        ActivityUtils.startLoginActivity((Activity) mContext, intent);

    }

    private IjkVideoView mMNVideoPlayer;
    private WebViewFragment mWebFragment;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void initView() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mWebFragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(WebViewFragment.WEB_VIEW_URL, Constant.MP4_PATH_URL);
        mWebFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.web_layout, mWebFragment);
        fragmentTransaction.commit();

        mMNVideoPlayer = findViewById(R.id.video_player);
        StandardVideoController controller = new StandardVideoController(this);
        //高级设置（可选，须在start()之前调用方可生效）
        mMNVideoPlayer.setPlayerConfig(new PlayerConfig.Builder()//
                .autoRotate()//自动旋转屏幕
//                .enableCache()//启用边播边存
                .savingProgress() //保存播放进度
//                .enableMediaCodec()//启动硬解码
//                .usingSurfaceView()//使用SurfaceView
                .setCustomMediaPlayer(new IjkPlayer(this))//
//                .setCustomMediaPlayer(new AndroidMediaPlayer(this))//
                .build());
        mMNVideoPlayer.setUrl(getIntent().getStringExtra(PLAY_URL));
        mMNVideoPlayer.setVideoController(controller);
        mMNVideoPlayer.start();

    }


    @Override
    public void back(View view) {
        ActivityUtils.finishLoginActivity(mContext);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMNVideoPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMNVideoPlayer.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMNVideoPlayer.release();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_video_detail;
    }
}
