package com.old.time.service.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import com.bumptech.glide.request.target.NotificationTarget;
import com.old.time.R;
import com.old.time.activitys.MusicPlayActivity;
import com.old.time.aidl.ChapterBean;
import com.old.time.aidl.OnModelChangedListener;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.utils.DebugLog;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.old.time.constants.Constant.NOTIFICATION_CEDE;

/**
 * Created by wcl on 2018/9/12.
 */

public class PlayNotifyManager extends OnModelChangedListener {

    private static final String TAG = "PlayNotifyManager";

    private Activity mContext;
    private RemoteViews remoteViews;
    private Notification notification;
    private NotificationManager mNotificationManager;

    private static final String DEFAULT_CHANNEL_ID = "1234567";
    private static final String MY_CHANNEL_ID = "my_channel_01";

    private static PlayNotifyManager mPlayNotifyManager;

    public static PlayNotifyManager getInstance(Activity activity) {
        if (mPlayNotifyManager == null) {
            mPlayNotifyManager = new PlayNotifyManager(activity);

        }

        return mPlayNotifyManager;
    }

    /**
     * 通知栏
     *
     * @param mContext
     */
    private PlayNotifyManager(Activity mContext) {
        this.mContext = mContext;
        createNotification();

    }

    /**
     * 创建通知栏
     */
    @SuppressLint("NewApi")
    private void createNotification() {
        remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.customnotice);//通知栏布局
        //创建mNotificationManager
        mNotificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        // 点击跳转到主界面
        Intent intent_main = new Intent(mContext, MusicPlayActivity.class);
        PendingIntent pending_intent_go = PendingIntent.getActivity(mContext, 1//
                , intent_main, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notice, pending_intent_go);

        // 4个参数context, requestCode, intent, flags
        Intent intent_cancel = new Intent();
        intent_cancel.setAction(Constant.ACTION_CLOSE);
        PendingIntent pending_intent_close = PendingIntent.getBroadcast(mContext, 2//
                , intent_cancel, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_close, pending_intent_close);

        // 上一曲
        Intent intent_prv = new Intent();
        intent_prv.setAction(Constant.ACTION_PRV);
        PendingIntent pending_intent_prev = PendingIntent.getBroadcast(mContext, 3//
                , intent_prv, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_prev, pending_intent_prev);

        // 设置播放暂停
        Intent intent_play_pause;
        PendingIntent pending_intent_play;
        intent_play_pause = new Intent();
        intent_play_pause.setAction(Constant.ACTION_PLAY);
        pending_intent_play = PendingIntent.getBroadcast(mContext, 5//
                , intent_play_pause, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.widget_play, pending_intent_play);
        // 下一曲
        Intent intent_next = new Intent();
        intent_next.setAction(Constant.ACTION_NEXT);
        PendingIntent pending_intent_next = PendingIntent.getBroadcast(mContext, 6//
                , intent_next, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_next, pending_intent_next);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(MY_CHANNEL_ID, DEFAULT_CHANNEL_ID//
                    , NotificationManager.IMPORTANCE_LOW);
            mNotificationManager.createNotificationChannel(mChannel);

        }
        notification = new Notification.Builder(mContext, DEFAULT_CHANNEL_ID)//
                .setSmallIcon(R.mipmap.ic_launcher)//
                .setCustomContentView(remoteViews)//
                .setChannelId(MY_CHANNEL_ID)//
                .setOngoing(true).build();
    }

    private NotificationTarget notificationTarget;

    /**
     * 更新通知栏
     */
    private void updateNotification(final ChapterBean chapterBean, boolean isPlaying) {
        if (chapterBean == null) {

            return;
        }
        if (notification == null) {
            createNotification();

        }
        Intent intent_play_pause;
        PendingIntent pending_intent_play;
        // 设置播放
        if (isPlaying) {//如果正在播放——》暂停
            remoteViews.setImageViewResource(R.id.widget_play, R.drawable.widget_play);
            intent_play_pause = new Intent();
            intent_play_pause.setAction(Constant.ACTION_PAUSE);
            pending_intent_play = PendingIntent.getBroadcast(mContext, 4//
                    , intent_play_pause, PendingIntent.FLAG_UPDATE_CURRENT);

        } else {//如果暂停——》播放
            remoteViews.setImageViewResource(R.id.widget_play, R.drawable.widget_pause);
            intent_play_pause = new Intent();
            intent_play_pause.setAction(Constant.ACTION_PLAY);
            pending_intent_play = PendingIntent.getBroadcast(mContext, 5//
                    , intent_play_pause, PendingIntent.FLAG_UPDATE_CURRENT);

        }
        remoteViews.setOnClickPendingIntent(R.id.widget_play, pending_intent_play);
        remoteViews.setTextViewText(R.id.widget_title, chapterBean.getTitle());
        notificationTarget = new NotificationTarget(mContext, R.id.widget_album, remoteViews, notification, NOTIFICATION_CEDE);
        GlideUtils.getInstance().setImageView(mContext, chapterBean.getPicUrl(), notificationTarget, R.mipmap.img_default);
        mNotificationManager.notify(NOTIFICATION_CEDE, notification);
    }

    @Override
    public void updatePlayModel(final ChapterBean mChapterBean, final boolean isPlaying) {
        DebugLog.d(TAG, "updatePlayModel");
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateNotification(mChapterBean, isPlaying);

            }
        });
    }

    @Override
    public void updateProgress(int progress, int total) {
        DebugLog.d(TAG, "progress-->" + progress + ":::total-->" + total);

    }

    @Override
    public void close() {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mNotificationManager != null) {
                    mNotificationManager.cancel(NOTIFICATION_CEDE);

                }
            }
        });
    }

    @Override
    public void updateError() {

    }
}
