package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.old.time.R;
import com.old.time.constants.Constant;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.UIHelper;
import com.old.time.videoPlayers.listener.OnCompletionListener;
import com.old.time.videoPlayers.listener.OnNetChangeListener;
import com.old.time.videoPlayers.listener.OnScreenOrientationListener;
import com.old.time.videoPlayers.player.MNVideoPlayer;

public class VideoDetailActivity extends BaseActivity {

    public static void startVideoDetailActivity(Context mContext) {
        Intent intent = new Intent(mContext, VideoDetailActivity.class);
        ActivityUtils.startActivity((Activity) mContext, intent);

    }

    private MNVideoPlayer mMNVideoPlayer;

    @Override
    protected void initView() {
        mMNVideoPlayer = findViewById(R.id.mn_video_player);
        //初始化相关参数(必须放在Play前面)
        mMNVideoPlayer.setWidthAndHeightProportion(16, 9);   //设置宽高比
        mMNVideoPlayer.setIsNeedNetChangeListen(true);       //设置网络监听
        //第一次进来先设置数据
        mMNVideoPlayer.setDataSource(Constant.MP4_PATH_URL, "标题");

        //播放完成监听
        mMNVideoPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                UIHelper.ToastMessage(mContext, "播放完毕");

            }
        });

        mMNVideoPlayer.setOnScreenOrientationListener(new OnScreenOrientationListener() {
            @Override
            public void orientation_landscape() {

            }

            @Override
            public void orientation_portrait() {

            }
        });

        //网络监听
        mMNVideoPlayer.setOnNetChangeListener(new OnNetChangeListener() {
            @Override
            public void onWifi(MediaPlayer mediaPlayer) {

            }

            @Override
            public void onMobile(MediaPlayer mediaPlayer) {
                UIHelper.ToastMessage(mContext, "当前网络状态切换为3G/4G网络");

            }

            @Override
            public void onNoAvailable(MediaPlayer mediaPlayer) {
                UIHelper.ToastMessage(mContext, "当前网络不可用,检查网络设置");

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMNVideoPlayer.pauseVideo();

    }

    @Override
    public void onBackPressed() {
        if (mMNVideoPlayer.isFullScreen()) {
            mMNVideoPlayer.setOrientationPortrait();

            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (mMNVideoPlayer != null) {
            mMNVideoPlayer.destroyVideo();
            mMNVideoPlayer = null;

        }
        super.onDestroy();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_video_detail;
    }
}
