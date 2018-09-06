package com.old.time.mp3Utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.dueeeke.videoplayer.listener.PlayerEventListener;
import com.dueeeke.videoplayer.player.IjkPlayer;
import com.old.time.R;
import com.old.time.activitys.MusicPlayActivity;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.interfaces.ImageDownLoadCallBack;
import com.old.time.receivers.MusicBroadReceiver;
import com.old.time.utils.DebugLog;
import com.old.time.utils.SpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MusicService extends Service implements PlayerEventListener, MusicBroadReceiver.MusicPlayCallBackListener {

    public static int playMode = 2;//1.单曲循环 2.列表循环 0.随机播放
    public static int PLAY_SPEED = 1;//播放速率 0.7、1.0、1.5、2.0、2.5、3.0
    private static final String TAG = "MusicService";
    public static boolean isPlaying = false;

    /**
     * 是否是第一次播放
     */
    private boolean isFirst = true;
    private int mPosition;
    private IjkPlayer mPlayer;
    private MusicBroadReceiver receiver;
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

        createNotification();
        super.onCreate();
    }

    private RemoteViews remoteViews;
    private Notification notification = null;
    private NotificationManager mNotificationManager;
    private static final String DEFAULT_CHANNEL_ID = "1234567";
    private static final String MY_CHANNEL_ID = "my_channel_01";

    /**
     * 创建通知栏
     */
    @SuppressLint("NewApi")
    private void createNotification() {
        remoteViews = new RemoteViews(getPackageName(), R.layout.customnotice);//通知栏布局
        //创建mNotificationManager
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // 点击跳转到主界面
        Intent intent_main = new Intent(this, MusicPlayActivity.class);
        PendingIntent pending_intent_go = PendingIntent.getActivity(this, 1//
                , intent_main, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notice, pending_intent_go);

        // 4个参数context, requestCode, intent, flags
        Intent intent_cancel = new Intent();
        intent_cancel.setAction(Constant.ACTION_CLOSE);
        PendingIntent pending_intent_close = PendingIntent.getBroadcast(this, 2//
                , intent_cancel, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_close, pending_intent_close);

        // 上一曲
        Intent intent_prv = new Intent();
        intent_prv.setAction(Constant.ACTION_PRV);
        PendingIntent pending_intent_prev = PendingIntent.getBroadcast(this, 3//
                , intent_prv, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_prev, pending_intent_prev);

        // 设置播放暂停
        Intent intent_play_pause;
        PendingIntent pending_intent_play;
        if (isPlaying) {//如果正在播放——》暂停
            intent_play_pause = new Intent();
            intent_play_pause.setAction(Constant.ACTION_PAUSE);
            pending_intent_play = PendingIntent.getBroadcast(this, 4//
                    , intent_play_pause, PendingIntent.FLAG_UPDATE_CURRENT);

        } else {//如果暂停——》播放
            intent_play_pause = new Intent();
            intent_play_pause.setAction(Constant.ACTION_PLAY);
            pending_intent_play = PendingIntent.getBroadcast(this, 5//
                    , intent_play_pause, PendingIntent.FLAG_UPDATE_CURRENT);

        }
        remoteViews.setOnClickPendingIntent(R.id.widget_play, pending_intent_play);
        // 下一曲
        Intent intent_next = new Intent();
        intent_next.setAction(Constant.ACTION_NEXT);
        PendingIntent pending_intent_next = PendingIntent.getBroadcast(this, 6//
                , intent_next, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_next, pending_intent_next);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(MY_CHANNEL_ID, DEFAULT_CHANNEL_ID//
                    , NotificationManager.IMPORTANCE_LOW);
            mNotificationManager.createNotificationChannel(mChannel);

        }
        notification = new Notification.Builder(this, DEFAULT_CHANNEL_ID)//
                .setSmallIcon(R.mipmap.ic_launcher)//
                .setCustomContentView(remoteViews)//
                .setChannelId(MY_CHANNEL_ID)//
                .setOngoing(true).build();
    }

    /**
     * 更新通知栏btn
     */
    private void updateNotification() {
        if (remoteViews == null) {

            return;
        }
        remoteViews.setTextViewText(R.id.widget_title, mMusic_list.get(mPosition).getTitle());
        GlideUtils.getInstance().downLoadBitmap(this, mMusic_list.get(mPosition).getPicUrl(), new ImageDownLoadCallBack() {
            @Override
            public void onDownLoadSuccess(Bitmap resource) {
                remoteViews.setImageViewBitmap(R.id.widget_album, resource);

            }
        });
        Intent intent_play_pause;
        PendingIntent pending_intent_play;
        // 设置播放
        if (isPlaying) {//如果正在播放——》暂停
            remoteViews.setImageViewResource(R.id.widget_play, R.drawable.widget_play);
            intent_play_pause = new Intent();
            intent_play_pause.setAction(Constant.ACTION_PAUSE);
            pending_intent_play = PendingIntent.getBroadcast(this, 4//
                    , intent_play_pause, PendingIntent.FLAG_UPDATE_CURRENT);

        } else {//如果暂停——》播放
            remoteViews.setImageViewResource(R.id.widget_play, R.drawable.widget_pause);
            intent_play_pause = new Intent();
            intent_play_pause.setAction(Constant.ACTION_PLAY);
            pending_intent_play = PendingIntent.getBroadcast(this, 5//
                    , intent_play_pause, PendingIntent.FLAG_UPDATE_CURRENT);

        }
        remoteViews.setOnClickPendingIntent(R.id.widget_play, pending_intent_play);
        mNotificationManager.notify(Constant.NOTIFICATION_CEDE, notification);
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

    private float[] floats = {0.7f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f};

    /**
     * 切换播放
     */
    private void start() {
        if (mPlayer != null && mMusic_list.size() > 0 && mPosition < mMusic_list.size()) {
            isFirst = false;
            mPlayer.reset();
            mPlayer.setDataSource(mMusic_list.get(mPosition).getUrl(), null);
            mPlayer.prepareAsync();
            mPlayer.setSpeed(floats[PLAY_SPEED % floats.length]);
            isPlaying = true;
            sentPositionToMainByTimer();
            updateNotification();

        }
    }

    @Override
    public void start(int position) {
        DebugLog.d(TAG, "start");
        this.mPosition = position;
        start();
    }

    @Override
    public void play() {
        DebugLog.d(TAG, "play");
        if (mPlayer != null && !isPlaying && !isFirst) {
            mPlayer.start();
            isPlaying = true;
            updateNotification();

        } else {
            start();

        }
    }

    /**
     * 暂停
     */
    @Override
    public void pause() {
        DebugLog.d(TAG, "pause");
        if (mPlayer != null && isPlaying) {
            mPlayer.pause();
            isPlaying = false;
            updateNotification();

        }
    }

    @Override
    public void previous() {
        DebugLog.d(TAG, "previous");
        if (mPlayer != null) {
            if (mPosition - 1 > 0) {
                mPosition--;

            } else {
                mPosition = mMusic_list.size() - 1;

            }
            start();
        }
    }

    @Override
    public void next() {
        DebugLog.d(TAG, "next");
        if (mPlayer != null) {
            if (mPosition + 1 < mMusic_list.size()) {
                mPosition++;

            } else {
                mPosition = 0;

            }
            start();
        }
    }

    @Override
    public void progress(int current, int total) {
        DebugLog.d(TAG, "progress");

    }

    @Override
    public void close() {
        DebugLog.d(TAG, "close");
        if (mNotificationManager != null) {
            mNotificationManager.cancel(Constant.NOTIFICATION_CEDE);

        }
    }

    @Override
    public void speed(float speed) {
        DebugLog.d(TAG, "speed");
        if (mPlayer != null) {
            mPlayer.setSpeed(speed);

        }
    }

    @Override
    public void onError() {
        DebugLog.d(TAG, "onError");
        Intent intent = new Intent();
        intent.setAction(Constant.ACTION_NEXT);
        sendBroadcast(intent);

    }

    /**
     * 播放完成
     */
    @Override
    public void onCompletion() {
        DebugLog.d(TAG, "onCompletion");
        Intent intent = new Intent();
        intent.setAction(Constant.ACTION_NEXT);
        sendBroadcast(intent);

    }

    /**
     * 准备完成
     */
    @Override
    public void onPrepared() {
        DebugLog.d(TAG, "onPrepared");

    }

    @Override
    public void onInfo(int what, int extra) {


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
        if (mNotificationManager != null) {
            mNotificationManager.cancel(Constant.NOTIFICATION_CEDE);

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
