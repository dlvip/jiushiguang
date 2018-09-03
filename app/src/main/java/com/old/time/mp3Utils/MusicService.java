package com.old.time.mp3Utils;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.dueeeke.videoplayer.listener.PlayerEventListener;
import com.dueeeke.videoplayer.player.IjkPlayer;
import com.old.time.constants.Constant;
import com.old.time.utils.DebugLog;
import com.old.time.utils.SpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MusicService extends Service implements PlayerEventListener {

    private static final String TAG = "MusicService";
    private List<Mp3Info> mMusic_list = new ArrayList<>();
    private Messenger mMessenger;
    private static IjkPlayer mPlayer;
    private MusicBroadReceiver receiver;
    private int mPosition;
    public static int playMode = 2;//1.单曲循环 2.列表循环 0.随机播放
    private Random mRandom;
    public static int prv_position;
    private Message mMessage;
    private NotificationManager notificationManager;

    @Override
    public void onCreate() {
        regFilter();
        initPlayer();

        //创建audioManger
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mRandom = new Random();
        super.onCreate();
    }

    private void initPlayer() {
        if (mPlayer == null) {
            mPlayer = new IjkPlayer(this);

        }
        mPlayer.bindVideoView(this);
        mPlayer.initPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            mMusic_list = intent.getParcelableArrayListExtra("music_list");
            mMessenger = (Messenger) intent.getExtras().get("messenger");
            mPosition = SpUtils.getInt("music_current_position", 0);

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        cancelMusic();
        if (receiver != null) {
            getApplicationContext().unregisterReceiver(receiver);

        }
    }

    private void cancelMusic() {
        notificationManager.cancel(Constant.NOTIFICATION_CEDE);
        mMessage = Message.obtain();
        mMessage.what = Constant.MSG_CANCEL;
        try {
            mMessenger.send(mMessage);

        } catch (RemoteException e) {
            e.printStackTrace();

        }
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void sentPreparedMessageToMain() {
        Message mMessage = new Message();
        mMessage.what = Constant.MSG_PREPARED;
        mMessage.arg1 = mPosition;
        mMessage.obj = mPlayer.isPlaying();
        try {
            mMessenger.send(mMessage);//发送播放位置

        } catch (RemoteException e) {
            e.printStackTrace();

        }
    }

    private void sentPlayStateToMain() {
        mMessage = Message.obtain();
        mMessage.what = Constant.MSG_PLAY_STATE;
        mMessage.obj = mPlayer.isPlaying();
        try {
            mMessenger.send(mMessage);//发送播放状态

        } catch (RemoteException e) {
            e.printStackTrace();

        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {


        }
    };

    private void sentPositionToMainByTimer() {
        ThreadPoolUtil.getScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mPlayer.isPlaying()) {
                        //1.准备好的时候.告诉activity,当前歌曲的总时长
                        int currentPosition = (int) mPlayer.getCurrentPosition();
                        int totalDuration = (int) mPlayer.getDuration();
                        mMessage = Message.obtain();
                        mMessage.what = Constant.MSG_PROGRESS;
                        mMessage.arg1 = currentPosition;
                        mMessage.arg2 = totalDuration;
                        //2.发送消息
                        mMessenger.send(mMessage);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * 播放
     */
    private void play(int position) {
        if (mPlayer != null && mMusic_list.size() > 0) {
            mPlayer.reset();
            mPlayer.setDataSource(mMusic_list.get(position).getUrl(), null);
            mPlayer.prepareAsync();

        }
    }

    /**
     * 暂停
     */
    private void pause() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            sentPlayStateToMain();

        }
    }

    /**
     * 注册广播
     */
    private void regFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_LIST_ITEM);
        intentFilter.addAction(Constant.ACTION_PAUSE);
        intentFilter.addAction(Constant.ACTION_PLAY);
        intentFilter.addAction(Constant.ACTION_NEXT);
        intentFilter.addAction(Constant.ACTION_PRV);
        intentFilter.addAction(Constant.ACTION_CLOSE);
        intentFilter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        intentFilter.setPriority(1000);
        if (receiver == null) {
            receiver = new MusicBroadReceiver();

        }
        getApplicationContext().registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onError() {
        Intent intent = new Intent();
        intent.setAction(Constant.ACTION_NEXT);
        sendBroadcast(intent);

    }

    @Override
    public void onCompletion() {
        Intent intent = new Intent();
        intent.setAction(Constant.ACTION_NEXT);
        sendBroadcast(intent);

    }

    @Override
    public void onInfo(int what, int extra) {

    }

    @Override
    public void onPrepared() {
        mPlayer.start();//开始播放
        if (mMessenger != null) {
            sentPreparedMessageToMain();
            sentPositionToMainByTimer();

        }
    }

    @Override
    public void onVideoSizeChanged(int width, int height) {


    }

    /**
     * 广播接收者
     */
    public class MusicBroadReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Constant.ACTION_LIST_ITEM://点击左侧菜单
                    mPosition = intent.getIntExtra("position", 0);
                    play(mPosition);

                    break;
                case Constant.ACTION_PAUSE://暂停播放
                    pause();

                    break;
                case Constant.ACTION_PLAY://开始播放
                    if (mPlayer != null) {
                        mPlayer.start();
                        sentPlayStateToMain();

                    } else {
                        initPlayer();
                        play(mPosition);

                    }
                    break;
                case Constant.ACTION_NEXT: //下一首
                    prv_position = mPosition;
                    if (playMode % 3 == 1) {//1.单曲循环
                        play(mPosition);

                    } else if (playMode % 3 == 2) {//2.列表播放
                        mPosition++;
                        if (mPosition <= mMusic_list.size() - 1) {
                            play(mPosition);

                        } else {
                            mPosition = 0;
                            play(mPosition);

                        }
                    } else if (playMode % 3 == 0) {// 0.随机播放
                        play(getRandom());

                    }

                    break;
                case Constant.ACTION_PRV: //上一首
                    prv_position = mPosition;
                    if (playMode % 3 == 1) {//1.单曲循环
                        play(mPosition);

                    } else if (playMode % 3 == 2) {//2.列表播放
                        mPosition--;
                        if (mPosition < 0) {
                            mPosition = mMusic_list.size() - 1;
                            play(mPosition);

                        } else {
                            play(mPosition);

                        }
                    } else if (playMode % 3 == 0) {// 0.随机播放
                        play(getRandom());

                    }
                    break;
                case Constant.ACTION_CLOSE:
                    cancelMusic();

                    break;
                case AudioManager.ACTION_AUDIO_BECOMING_NOISY://如果耳机拨出时暂停播放
                    Intent intent_pause = new Intent();
                    intent_pause.setAction(Constant.ACTION_PAUSE);
                    sendBroadcast(intent_pause);

                    break;
            }
        }
    }

    private int getRandom() {
        mPosition = mRandom.nextInt(mMusic_list.size());

        return mPosition;
    }

    public static boolean isPlaying() {
        if (mPlayer != null) {

            return mPlayer.isPlaying();
        }
        return false;
    }
}
