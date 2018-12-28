package com.old.time.views;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.activitys.MusicPlayActivity;
import com.old.time.aidl.ChapterBean;
import com.old.time.aidl.OnModelChangedListener;
import com.old.time.aidl.PlayServiceIBinder;
import com.old.time.beans.ResultBean;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.service.PlayServiceConnection;
import com.old.time.service.manager.PlayNotifyManager;
import com.old.time.service.manager.PlayServiceManager;
import com.old.time.utils.SpUtils;

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

    private ImageView img_music_pic, img_play_btn;
    private ValueAnimator rotateAnim;
    private Activity mContext;

    private TextView tv_music_title;
    private CompletedView tasks_view;

    private void initView() {
        this.mContext = (Activity) getContext();
        View play_music_view = View.inflate(getContext(), R.layout.play_music_bottom, this);
        img_music_pic = play_music_view.findViewById(R.id.img_music_pic);
        img_play_btn = play_music_view.findViewById(R.id.img_play_btn);
        tv_music_title = play_music_view.findViewById(R.id.tv_music_title);
        tasks_view = play_music_view.findViewById(R.id.tasks_view);
        FrameLayout frame_layout_play = play_music_view.findViewById(R.id.frame_layout_play);
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
        play_music_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicPlayActivity.startMusicPlayActivity(mContext, albumId);

            }
        });
        frame_layout_play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayServiceConnection != null) {
                    if(!mPlayServiceConnection.isPlaying()){
                        mPlayServiceConnection.setStartList(chapterBeans, position);

                    }
                    mPlayServiceConnection.play(mPlayServiceConnection.isPlaying());

                }
            }
        });
    }

    private OnModelChangedListener onModelChangedListener;
    private PlayServiceConnection mPlayServiceConnection;
    private PlayNotifyManager playNotifyManager;
    private PlayServiceManager mPlayServiceManager;
    private String albumId;
    private int position;

    private List<ChapterBean> chapterBeans;

    private void bindService() {
        onModelChangedListener = new OnModelChangedListener() {
            @Override
            public void updatePlayModel(final ChapterBean mChapterBean, final boolean isPlaying) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PlayMusicBottomView.this.switchSongUI(mChapterBean, isPlaying);

                    }
                });
            }

            @Override
            public void updateProgress(final int progress, final int total) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (tasks_view != null && total != 0) {
                            tasks_view.setProgress(progress * 100 / total);

                        }
                    }
                });
            }

            @Override
            public void updateError() {

            }

            @Override
            public void close() {

            }
        };
        playNotifyManager = PlayNotifyManager.getInstance(mContext);
        mPlayServiceManager = new PlayServiceManager(mContext);
        mPlayServiceConnection = new PlayServiceConnection(new PlayServiceConnection.OnServiceConnectedListener() {
            @Override
            public void onServiceConnected() {
                mPlayServiceConnection.registerIOnModelChangedListener(playNotifyManager);
                mPlayServiceConnection.registerIOnModelChangedListener(onModelChangedListener);
                if (chapterBeans != null && chapterBeans.size() > position) {
                    switchSongUI(chapterBeans.get(position), mPlayServiceConnection.isPlaying());

                }
            }

            @Override
            public void onServiceDisconnected() {
                mPlayServiceConnection.unregisterIOnModelChangedListener(playNotifyManager);
                mPlayServiceConnection.unregisterIOnModelChangedListener(onModelChangedListener);

            }
        });
        albumId = SpUtils.getString(mContext, PlayServiceIBinder.SP_PLAY_ALBUM_ID, PlayServiceIBinder.DEFAULT_ALBUM_ID);
        position = SpUtils.getInt(PlayServiceIBinder.SP_PLAY_POSITION, 0);
        getMusicList();

    }

    /**
     * 获取播放列表
     */
    private void getMusicList() {
        HttpParams params = new HttpParams();
        params.put("albumId", albumId);
        params.put("pageNum", 0);
        params.put("pageSize", Constant.PAGE_ALL);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_MUSIC_LIST, new JsonCallBack<ResultBean<List<ChapterBean>>>() {
            @Override
            public void onSuccess(ResultBean<List<ChapterBean>> mResultBean) {
                chapterBeans = mResultBean.data;
                mPlayServiceManager.bindService(mPlayServiceConnection);

            }

            @Override
            public void onError(ResultBean<List<ChapterBean>> mResultBean) {

            }
        });
    }

    /**
     * 更新UI
     *
     * @param mChapterBean
     * @param isPlaying
     */
    private void switchSongUI(ChapterBean mChapterBean, boolean isPlaying) {
        if (mChapterBean == null) {

            return;
        }
        GlideUtils.getInstance().setRadiusImageView(mContext, mChapterBean.getPicUrl(), img_music_pic, 10);
        tv_music_title.setText(mChapterBean.getTitle());
        img_play_btn.setImageResource(isPlaying ? R.mipmap.ic_player_pause : R.mipmap.ic_player_start);
//        if (isPlaying) {
//            startSpin();
//
//        } else {
//            stopSpin();
//
//        }
    }

    private void startSpin() {
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

    private void stopSpin() {
        if (rotateAnim.isRunning()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                rotateAnim.pause();

            } else {
                rotateAnim.cancel();

            }
        }
    }

    public void onDestroy() {
        if (mPlayServiceConnection != null && mContext != null) {
            mPlayServiceConnection.unregisterIOnModelChangedListener(onModelChangedListener);
            mContext.unbindService(mPlayServiceConnection);

        }
    }
}
