package com.old.time.mp3Utils;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.dueeeke.videoplayer.listener.PlayerEventListener;
import com.dueeeke.videoplayer.player.IjkPlayer;
import com.old.time.constants.Constant;
import com.old.time.receivers.MusicBroadReceiver;
import com.old.time.utils.DebugLog;
import com.old.time.utils.SpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MusicService extends Service implements PlayerEventListener, MusicBroadReceiver.MusicPlayCallBackListener {

    public static boolean isPlaying = false;
    public static int playMode = 2;//1.单曲循环 2.列表循环 0.随机播放
    public static int PLAY_SPEED = 2;//播放速率 0.7、1.0、1.5、2.0、2.5、3.0
    private static final String TAG = "MusicService";

    private int mPosition;
    private IjkPlayer mPlayer;
    private MusicBroadReceiver receiver;
    private NotificationManager notificationManager;
    private List<Mp3Info> mMusic_list = new ArrayList<>();

    @Override
    public void onCreate() {
        //创建广播接受者
        if (receiver == null) {
            receiver = new MusicBroadReceiver(this);

        }
        registerReceiver(receiver, MusicBroadReceiver.getIntentFilter());

        //初始化播放器
        if (mPlayer == null) {
            mPlayer = new IjkPlayer(this);

        }
        mPlayer.bindVideoView(this);
        mPlayer.initPlayer();

        //创建notificationManager
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            mMusic_list = intent.getParcelableArrayListExtra("music_list");
            mPosition = SpUtils.getInt("music_current_position", 0);

        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 开始
     */
    private void sentPreparedMessageToMain() {
        Intent intent = new Intent();
        intent.putExtra(MusicBroadReceiver.START_POSITION, mPosition);
        intent.setAction(Constant.ACTION_START);
        sendBroadcast(intent);

    }

    /**
     * 播放进度
     */
    private void sentPositionToMainByTimer() {
        ThreadPoolUtil.getScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (mPlayer.isPlaying()) {
                    //1.准备好的时候.告诉activity,当前歌曲的总时长
                    int currentPosition = (int) mPlayer.getCurrentPosition();
                    int totalDuration = (int) mPlayer.getDuration();
                    Intent intent = new Intent();
                    intent.putExtra(MusicBroadReceiver.START_CURRENT, currentPosition);
                    intent.putExtra(MusicBroadReceiver.START_TOTAL, totalDuration);
                    intent.setAction(Constant.ACTION_PROGRESS);
                    sendBroadcast(intent);

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
            isPlaying = true;

        }
    }

    @Override
    public void start(int position) {
        DebugLog.d(TAG,"start");
        this.mPosition = position;
        play(mPosition);

    }

    @Override
    public void play() {
        DebugLog.d(TAG,"play");
        play(mPosition);

    }

    /**
     * 暂停
     */
    @Override
    public void pause() {
        DebugLog.d(TAG,"pause");
        if (mPlayer != null) {
            mPlayer.pause();
            isPlaying = false;
        }
    }

    @Override
    public void previous() {
        DebugLog.d(TAG,"previous");
        if (mPlayer != null) {
            if (mPosition - 1 > 0) {
                mPosition--;

            } else {
                mPosition = mMusic_list.size() - 1;

            }
            play(mPosition);
        }
    }

    @Override
    public void next() {
        DebugLog.d(TAG,"next");
        if (mPlayer != null) {
            if (mPosition + 1 < mMusic_list.size()) {
                mPosition++;

            } else {
                mPosition = 0;

            }
            play(mPosition);
        }
    }

    @Override
    public void progress(int current, int total) {
        DebugLog.d(TAG,"progress");

    }

    @Override
    public void close() {
        DebugLog.d(TAG,"close");
        onDestroy();

    }

    @Override
    public void speed(float speed) {
        DebugLog.d(TAG,"speed");
        if (mPlayer != null) {
            mPlayer.setSpeed(speed);

        }
    }

    @Override
    public void onError() {
        DebugLog.d(TAG,"onError");
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
        if (mPlayer != null) {
            mPlayer.start();//开始播放
//            sentPreparedMessageToMain();
            sentPositionToMainByTimer();

        }
    }

    @Override
    public void onVideoSizeChanged(int width, int height) {


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onDestroy() {
        if (notificationManager != null) {
            notificationManager.cancel(Constant.NOTIFICATION_CEDE);

        }
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;

        }
        if (receiver != null) {
            unregisterReceiver(receiver);

        }
        isPlaying = false;
    }
}
