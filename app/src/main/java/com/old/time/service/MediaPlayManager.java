package com.old.time.service;

import android.content.Context;
import android.text.TextUtils;

import com.dueeeke.videoplayer.listener.PlayerEventListener;
import com.dueeeke.videoplayer.player.IjkPlayer;
import com.old.time.utils.DebugLog;

/**
 * Created by NING on 2018/9/12.
 */

public class MediaPlayManager implements PlayerEventListener {

    private static final String TAG = "MediaPlayManager";

    private OnMediaPlayCallBackListener onMediaPlayCallBackListener;
    private IjkPlayer mPlayer;

    private Context mContext;

    /**
     * 音频播放管理类
     */
    public MediaPlayManager(Context mContext, OnMediaPlayCallBackListener onMediaPlayCallBackListener) {
        this.mContext = mContext;
        this.onMediaPlayCallBackListener = onMediaPlayCallBackListener;
        if (mPlayer == null) {
            initIjkPlayer();

        }
    }

    /**
     * 初始化播放器
     */
    private void initIjkPlayer() {
        mPlayer = new IjkPlayer(mContext);
        mPlayer.bindVideoView(this);
        mPlayer.initPlayer();

    }

    /**
     * 播放
     */
    public void play(String playUrl) {
        DebugLog.d(TAG, "play-->playUrl=" + playUrl);
        if (TextUtils.isEmpty(playUrl)) {

            return;
        }
        if (mPlayer == null) {
            initIjkPlayer();

        }
        if (playUrl.equals(this.playUrl)) {
            play();

            return;
        }
        this.playUrl = playUrl;
        mPlayer.reset();
        mPlayer.setDataSource(playUrl, null);
        mPlayer.prepareAsync();

    }

    /**
     * 播放
     */
    public void play() {
        DebugLog.d(TAG, "play");
        if (mPlayer != null) {
            mPlayer.start();

        }
    }

    /**
     * 暂停
     */
    public void pause() {
        DebugLog.d(TAG, "pause");
        if (mPlayer != null) {
            mPlayer.pause();

        }
    }

    /**
     * 获取播放状态
     *
     * @return
     */
    public boolean isPlaying() {
        DebugLog.d(TAG, "isPlaying");
        if (mPlayer != null) {

            return mPlayer.isPlaying();
        }

        return false;
    }

    /**
     * 获取播放进度
     *
     * @return
     */
    public int getProgress() {
        DebugLog.d(TAG, "getProgress");
        if (mPlayer != null) {

            return (int) mPlayer.getCurrentPosition();
        }
        return 0;
    }

    /**
     * 获取播放总进度
     *
     * @return
     */
    public int getTotalProgress() {
        DebugLog.d(TAG, "getTotalProgress");
        if (mPlayer != null) {

            return (int) mPlayer.getDuration();
        }

        return 0;
    }

    /**
     * 设置播放速率
     *
     * @param speed
     */
    public void setSpeed(float speed) {
        DebugLog.d(TAG, "setSpeed-->speed=" + speed);
        if (mPlayer != null) {
            mPlayer.setSpeed(speed);

        }
    }

    /**
     * 获取播放速率
     *
     * @return
     */
    public float getSpeed() {
        DebugLog.d(TAG, "getSpeed");
        if (mPlayer != null) {

            return mPlayer.getTcpSpeed();
        }
        return 0;
    }

    /**
     * 设置进度
     *
     * @param time
     */
    public void seekTo(long time) {
        DebugLog.d(TAG, "seekTo-->time=" + time);
        if (mPlayer != null) {
            mPlayer.seekTo(time);

        }
    }

    /**
     * 播放地址
     */
    private String playUrl;

    /**
     * 设置播放地址
     *
     * @param playUrl
     */
    public void setPlayUrl(String playUrl) {
        DebugLog.d(TAG, "setPlayUrl-->playUrl=" + playUrl);
        if (TextUtils.isEmpty(playUrl)) {

            return;
        }
        this.playUrl = playUrl;

    }

    @Override
    public void onError() {
        DebugLog.d(TAG, "onError");
        if (onMediaPlayCallBackListener != null) {
            onMediaPlayCallBackListener.onError();

        }
    }

    @Override
    public void onCompletion() {
        DebugLog.d(TAG, "onCompletion");
        if (onMediaPlayCallBackListener != null) {
            onMediaPlayCallBackListener.onCompletion();

        }
    }

    @Override
    public void onInfo(int what, int extra) {

    }

    @Override
    public void onPrepared() {

    }

    @Override
    public void onVideoSizeChanged(int width, int height) {

    }

    /**
     * 停止播放器
     */
    public void stop() {
        if (this.mPlayer != null) {
            this.mPlayer.stop();
            this.mPlayer.release();
            this.mPlayer = null;
            this.mContext = null;

        }
    }

    /**
     * 播放回调
     */
    public interface OnMediaPlayCallBackListener {

        /**
         * 播放失败
         */
        void onError();

        /**
         * 播放完成
         */
        void onCompletion();

    }
}
