package com.old.time.views;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.activitys.MusicPlayActivity;
import com.old.time.aidl.ChapterBean;
import com.old.time.aidl.OnModelChangedListener;
import com.old.time.aidl.PlayServiceIBinder;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.service.PlayServiceConnection;
import com.old.time.service.manager.PlayServiceManager;
import com.old.time.utils.SpUtils;
import com.old.time.utils.UIHelper;

import java.util.List;

/**
 * Created by NING on 2018/9/20.
 */

public class PlayMusicBottomView extends LinearLayout {

    public PlayMusicBottomView(Context context) {
        super(context);
        initView();
    }

    public PlayMusicBottomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PlayMusicBottomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private View play_music_view;
    private ImageView img_music_pic, img_play_btn;
    private ValueAnimator rotateAnim;
    private Activity mContext;


    private TextView tv_music_title;
    private CompletedView tasks_view;


    private void initView() {
        this.mContext = (Activity) getContext();
        play_music_view = View.inflate(getContext(), R.layout.play_music_bottom, this);
        img_music_pic = play_music_view.findViewById(R.id.img_music_pic);
        img_play_btn = play_music_view.findViewById(R.id.img_play_btn);
        tv_music_title = play_music_view.findViewById(R.id.tv_music_title);
        tasks_view = play_music_view.findViewById(R.id.tasks_view);
        rotateAnim = ObjectAnimator.ofFloat(0, 360);
        rotateAnim.setDuration(10 * 1000);
        rotateAnim.setRepeatMode(ValueAnimator.RESTART);
        rotateAnim.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnim.setInterpolator(new LinearInterpolator());
        rotateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                img_music_pic.setRotation(value);

            }
        });
        bindService();
    }

    private OnModelChangedListener onModelChangedListener;
    private PlayServiceConnection mPlayServiceConnection;
    private PlayServiceManager mPlayServiceManager;
    private String albumId;

    private void bindService() {
        onModelChangedListener = new OnModelChangedListener() {
            @Override
            public void updatePlayModel(final ChapterBean mChapterBean, final boolean isPlaying) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                    }
                });
            }

            @Override
            public void updateProgress(final int progress, final int total) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                    }
                });
            }

            @Override
            public void updateError() {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UIHelper.ToastMessage(mContext, "播放出错");

                    }
                });
            }

            @Override
            public void close() {

            }
        };

        mPlayServiceManager = new PlayServiceManager(mContext);
        mPlayServiceConnection = new PlayServiceConnection(mContext, new PlayServiceConnection.OnServiceConnectedListener() {
            @Override
            public void onServiceConnected() {
                mPlayServiceConnection.registerIOnModelChangedListener(onModelChangedListener);
                albumId = SpUtils.getObject(PlayServiceIBinder.SP_PLAY_ALBUM_ID);

            }

            @Override
            public void onServiceDisconnected() {
                mPlayServiceConnection.unregisterIOnModelChangedListener(onModelChangedListener);

            }
        });
        mPlayServiceManager.bindService(mPlayServiceConnection);

    }

    /**
     * 更新UI
     *
     * @param mChapterBean
     * @param isPlaying
     */
    private void switchSongUI(ChapterBean mChapterBean, final boolean isPlaying) {
        if (mChapterBean == null) {

            return;
        }
        GlideUtils.getInstance().setImageView(mContext, mChapterBean.getPicUrl(), img_music_pic);
        tv_music_title.setText(mChapterBean.getTitle());
        img_play_btn.setImageResource(isPlaying ? R.mipmap.ic_player_pause : R.mipmap.ic_player_start);

    }

    public void startSpin() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (rotateAnim.isPaused()) {
                rotateAnim.resume();

            } else {
                rotateAnim.start();

            }
        } else {
            rotateAnim.start();

        }
    }

    public void stopSpin() {
        if (rotateAnim.isRunning()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                rotateAnim.pause();

            } else {
                rotateAnim.cancel();

            }
        }
    }

    public void onDestroy() {
        stopSpin();

    }
}