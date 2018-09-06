package com.old.time.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;

import com.old.time.constants.Constant;

/**
 * Created by NING on 2018/9/4.
 */

public class MusicBroadReceiver extends BroadcastReceiver {

    public static final String START_POSITION = "start_position";
    public static final String START_SPEED = "start_speed";
    public static final String START_CURRENT = "start_current";
    public static final String START_TOTAL = "start_total";

    private MusicPlayCallBackListener mMusicPlayCallBackListener;

    public MusicBroadReceiver(MusicPlayCallBackListener mMusicPlayCallBackListener) {
        this.mMusicPlayCallBackListener = mMusicPlayCallBackListener;

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mMusicPlayCallBackListener == null) {

            return;
        }
        switch (intent.getAction()) {
            case Constant.ACTION_START://开始
            case Constant.ACTION_LIST_ITEM://点击左侧菜单
                int position = intent.getIntExtra(START_POSITION, 0);
                mMusicPlayCallBackListener.start(position);

                break;
            case Constant.ACTION_PAUSE://暂停
                mMusicPlayCallBackListener.pause();

                break;
            case Constant.ACTION_PLAY://播放
                mMusicPlayCallBackListener.play();

                break;
            case Constant.ACTION_PRV: //上一首
                mMusicPlayCallBackListener.previous();

                break;
            case Constant.ACTION_NEXT: //下一首
                mMusicPlayCallBackListener.next();

                break;
            case Constant.ACTION_SPEED://播放速率
                float speed = intent.getFloatExtra(START_SPEED, 1.0f);
                mMusicPlayCallBackListener.speed(speed);

                break;
            case Constant.ACTION_PROGRESS://播放进度
                int current = intent.getIntExtra(START_CURRENT, 0);
                int total = intent.getIntExtra(START_TOTAL, 100);
                mMusicPlayCallBackListener.progress(current, total);

                break;
            case Constant.ACTION_CLOSE://关闭
                mMusicPlayCallBackListener.close();

                break;
            case AudioManager.ACTION_AUDIO_BECOMING_NOISY://如果耳机拨出时暂停播放
                mMusicPlayCallBackListener.pause();

                break;
            case AudioManager.ACTION_HEADSET_PLUG://如果耳机插入时开始播放

                break;
        }
    }

    /**
     * 注册所有通知
     *
     * @return
     */
    public static IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_START);
        intentFilter.addAction(Constant.ACTION_PAUSE);
        intentFilter.addAction(Constant.ACTION_PLAY);
        intentFilter.addAction(Constant.ACTION_NEXT);
        intentFilter.addAction(Constant.ACTION_PRV);
        intentFilter.addAction(Constant.ACTION_SPEED);
        intentFilter.addAction(Constant.ACTION_PROGRESS);
        intentFilter.addAction(Constant.ACTION_CLOSE);
        intentFilter.addAction(Constant.ACTION_LIST_ITEM);
        intentFilter.addAction(AudioManager.ACTION_HEADSET_PLUG);
        intentFilter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        intentFilter.setPriority(1000);

        return intentFilter;
    }

    /**
     * 播放回调
     */
    public interface MusicPlayCallBackListener {

        /**
         * 开始播放
         *
         * @param position 标记索引
         */
        void start(int position);

        /**
         * 播放
         */
        void play();

        /**
         * 暂停
         */
        void pause();

        /**
         * 上一首
         */
        void previous();

        /**
         * 下一首
         */
        void next();

        /**
         * 进度
         *
         * @param current 当前进度
         * @param total   总进度
         */
        void progress(int current, int total);

        /**
         * 关闭
         */
        void close();

        /**
         * 播放速率
         *
         * @param speed 播放速率
         */
        void speed(float speed);

    }
}
