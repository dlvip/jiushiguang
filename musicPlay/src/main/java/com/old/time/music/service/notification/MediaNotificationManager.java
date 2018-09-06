package com.old.time.music.service.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;

import com.old.time.music.R;
import com.old.time.music.service.MusicService;
import com.old.time.music.service.utils.ResourceUtil;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Keeps track of a notification and updates it automatically for a given
 * MediaSession. Maintaining a visible notification (usually) guarantees that the music service
 * won't be killed during playback.
 */
public class MediaNotificationManager extends BroadcastReceiver {
    private static final String TAG = "MediaNotification";

    // notification id
    private static final int NOTIFICATION_ID = 412;
    //
    private static final int REQUEST_CODE = 100;

    /**
     * action
     */
    public static final String ACTION_PAUSE = "com.netease.awakeing.music.pause";
    public static final String ACTION_PLAY = "com.netease.awakeing.music.play";
    public static final String ACTION_PREV = "com.netease.awakeing.music.prev";
    public static final String ACTION_NEXT = "com.netease.awakeing.music.next";
    public static final String ACTION_STOP_CASTING = "com.netease.awakeing.music.stop_cast";

    /**
     * service
     */
    // MusicService
    private final MusicService mMusicService;
    // SessionToken
    private MediaSessionCompat.Token mSessionToken;

    /**
     * client
     */
    // 创建了一个MediaControllerCompat
    private MediaControllerCompat mMediaController;
    // TransportControls
    private MediaControllerCompat.TransportControls mTransportControls;
    // 数据
    private MediaMetadataCompat mMediaMetadata;
    // 播放状态
    private PlaybackStateCompat mPlaybackState;

    // 暂停
    private final PendingIntent mPauseIntent;
    // 播放
    private final PendingIntent mPlayIntent;
    // 上一个
    private final PendingIntent mPreviousIntent;
    // 下一个
    private final PendingIntent mNextIntent;

    /**
     * 获取NotificationManager
     */
    private final NotificationManager mNotificationManager;

    /**
     * 数据
     */
    // notification 颜色
    private final int mNotificationColor;
    private boolean mStarted = false;


    /**
     * 构造方法
     *
     * @param service
     * @throws RemoteException
     */
    public MediaNotificationManager(MusicService service) throws RemoteException {
        // MusicService
        mMusicService = service;

        // 创建了 {@link MediaControllerCompat} 获取了{@link MediaControllerCompat.TransportControls}
        getMediaControllerBySessionToken();

        // NotificationManagerCompat
        mNotificationManager = (NotificationManager) mMusicService.getSystemService(NOTIFICATION_SERVICE);

        // notification 颜色
        mNotificationColor = ResourceUtil.getThemeColor(mMusicService, R.color.trans, Color.DKGRAY);

        String pkg = mMusicService.getPackageName();
        // 暂定
        mPauseIntent = PendingIntent.getBroadcast(mMusicService, REQUEST_CODE//
                , new Intent(ACTION_PAUSE).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);
        // 播放
        mPlayIntent = PendingIntent.getBroadcast(mMusicService, REQUEST_CODE//
                , new Intent(ACTION_PLAY).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);
        // 上一个
        mPreviousIntent = PendingIntent.getBroadcast(mMusicService, REQUEST_CODE//
                , new Intent(ACTION_PREV).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);
        // 下一个
        mNextIntent = PendingIntent.getBroadcast(mMusicService, REQUEST_CODE//
                , new Intent(ACTION_NEXT).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);

        // 默认  移除所有的通知栏消息
        if (mNotificationManager != null) {
            mNotificationManager.cancel(NOTIFICATION_ID);

        }
    }

    /**
     * Posts the notification and starts tracking the session to keep it
     * updated. The notification will automatically be removed if the session is
     * destroyed before {@link #stopNotification} is called.
     */
    public void startNotification() {
        //
        if (!mStarted) {
            // 音频数据
            mMediaMetadata = mMediaController.getMetadata();
            // 播放状态
            mPlaybackState = mMediaController.getPlaybackState();

            Notification notification = createNotification();
            setIcon(mMediaMetadata);
            if (notification != null) {
                mMediaController.registerCallback(mMediaControllerCallBack);
                IntentFilter filter = new IntentFilter();
                filter.addAction(ACTION_NEXT);
                filter.addAction(ACTION_PAUSE);
                filter.addAction(ACTION_PLAY);
                filter.addAction(ACTION_PREV);
                filter.addAction(ACTION_STOP_CASTING);
                mMusicService.registerReceiver(this, filter);
                mMusicService.startForeground(NOTIFICATION_ID, notification);
                mStarted = true;

            }
        }
    }


    /**
     * Removes the notification and stops tracking the session. If the session
     * was destroyed this has no effect.
     */
    public void stopNotification() {
        if (mStarted) {
            mStarted = false;
            mMediaController.unregisterCallback(mMediaControllerCallBack);
            try {
                mNotificationManager.cancel(NOTIFICATION_ID);
                mMusicService.unregisterReceiver(this);

            } catch (IllegalArgumentException ex) {
                Log.e(TAG, ex.getMessage());

            }
            mMusicService.stopForeground(true);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        Log.d(TAG, "Received intent with action " + action);
        if (TextUtils.isEmpty(action)) {

            return;
        }
        switch (action) {
            case ACTION_PAUSE:
                mTransportControls.pause();

                break;
            case ACTION_PLAY:
                mTransportControls.play();

                break;
            case ACTION_NEXT:
                mTransportControls.skipToNext();

                break;
            case ACTION_PREV:
                mTransportControls.skipToPrevious();

                break;
            case ACTION_STOP_CASTING:
                Intent i = new Intent(context, MusicService.class);
                i.setAction(MusicService.ACTION_CMD);
                i.putExtra(MusicService.CMD_NAME, MusicService.CMD_STOP_CASTING);
                mMusicService.startService(i);

                break;
            default:

                break;
        }
    }

    /**
     * 创建了 {@link MediaControllerCompat} 获取了{@link MediaControllerCompat.TransportControls}
     * <p>
     * Update the state based on a change on the session token. Called either when
     * we are running for the first time or when the media session owner has destroyed the session
     * (see {@link android.media.session.MediaController.Callback#onSessionDestroyed()})
     * <p>
     */
    private void getMediaControllerBySessionToken() throws RemoteException {
        // 获取token
        MediaSessionCompat.Token freshToken = mMusicService.getSessionToken();
        if (mSessionToken == null && freshToken != null || mSessionToken != null && !mSessionToken.equals(freshToken)) {
            if (mMediaController != null) {
                mMediaController.unregisterCallback(mMediaControllerCallBack);
            }
            mSessionToken = freshToken;
            if (mSessionToken != null) {
                // 创建MediaControllerCompat
                mMediaController = new MediaControllerCompat(mMusicService, mSessionToken);
                mTransportControls = mMediaController.getTransportControls();
                if (mStarted) {

                    mMediaController.registerCallback(mMediaControllerCallBack);
                }
            }
        }
    }

    private PendingIntent createContentIntent(MediaDescriptionCompat description) {
        try {
            Intent intent = mMusicService.getPackageManager().getLaunchIntentForPackage(mMusicService.getPackageName());
            if (intent != null) {
                intent.setPackage(null);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

            }
            return PendingIntent.getActivity(mMusicService, REQUEST_CODE, intent, 0);
        } catch (Exception e) {

            return null;
        }
    }


    private void setIcon(MediaMetadataCompat metadata) {
        if (metadata == null || metadata.getDescription() == null || metadata.getDescription().getIconUri() == null) {

            return;
        }
        MediaDescriptionCompat description = metadata.getDescription();
        if (description.getIconUri() != null) {
            String artUrl = description.getIconUri().toString();
            if (!TextUtils.isEmpty(artUrl)) {
                fetchBitmapFromURLAsync(artUrl);

            }
        }
    }

    // #######################################################################################

    private Notification refreshNotification() {
        try {

            return createNotification();
        } catch (Exception e) {

            return null;
        }
    }

    private static final String DEFAULT_CHANNEL_ID = "1234567";
    private static final String MY_CHANNEL_ID = "my_channel_01";

    /**
     * 创建通知栏
     *
     * @return
     */
    private Notification createNotification() {
        Log.d(TAG, "createNotification. mMediaMetadata=" + mMediaMetadata);
        if (mMediaMetadata == null || mPlaybackState == null) {

            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(MY_CHANNEL_ID, DEFAULT_CHANNEL_ID//
                    , NotificationManager.IMPORTANCE_LOW);
            mNotificationManager.createNotificationChannel(mChannel);

        }
        // 通知栏
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mMusicService, DEFAULT_CHANNEL_ID);
        // 上一曲
        notificationBuilder.addAction(R.mipmap.ic_skip_previous_white_36dp, mMusicService.getString(R.string.label_previous), mPreviousIntent);

        // 添加 暂停 播放
        addPlayPauseAction(notificationBuilder);
        // 下一曲
        notificationBuilder.addAction(R.mipmap.ic_skip_next_white_36dp, mMusicService.getString(R.string.label_next), mNextIntent);

        MediaDescriptionCompat description = mMediaMetadata.getDescription();

        Bitmap art = null;
        if (description.getIconUri() != null) {
            String artUrl = description.getIconUri().toString();
            art = AlbumArtCache.getInstance().getBigImage(artUrl);
            if (art == null) {
                art = BitmapFactory.decodeResource(mMusicService.getResources(), R.mipmap.ic_notification);

            }
        }

        notificationBuilder.setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()//
                .setShowActionsInCompactView(new int[]{0, 1, 2})//
                .setMediaSession(mSessionToken))//
                .setPriority(NotificationCompat.PRIORITY_MAX)//
                .setColor(mNotificationColor)//
                .setSmallIcon(R.mipmap.ic_notification)//
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)//
                .setUsesChronometer(true).setShowWhen(false)//
                .setContentIntent(createContentIntent(description))//
                .setContentTitle(description.getTitle())//
                .setContentText(description.getSubtitle())//
                .setLargeIcon(art);
        setNotificationPlaybackState(notificationBuilder);

        return notificationBuilder.build();
    }

    /**
     * 添加暂停、播放
     *
     * @param builder
     */
    private void addPlayPauseAction(NotificationCompat.Builder builder) {
        Log.d(TAG, "updatePlayPauseAction");
        String label;
        int icon;
        PendingIntent intent;

        if (mPlaybackState.getState() == PlaybackStateCompat.STATE_PLAYING) {
            label = mMusicService.getString(R.string.label_pause);
            icon = R.mipmap.ic_pause_white_36dp;
            intent = mPauseIntent;

        } else {
            label = mMusicService.getString(R.string.label_play);
            icon = R.mipmap.ic_play_white_36dp;
            intent = mPlayIntent;

        }
        builder.addAction(new NotificationCompat.Action(icon, label, intent));
    }

    private void setNotificationPlaybackState(NotificationCompat.Builder builder) {
        if (mPlaybackState == null || !mStarted) {
            mMusicService.stopForeground(true);

            return;
        }
        // Make sure that the notification can be dismissed by the user when we are not playing:
        builder.setOngoing(mPlaybackState.getState() == PlaybackStateCompat.STATE_PLAYING);
    }

    private void fetchBitmapFromURLAsync(final String bitmapUrl) {
        AlbumArtCache.getInstance().fetch(bitmapUrl, new AlbumArtCache.FetchListener() {
            @Override
            public void onFetched(String artUrl, Bitmap bitmap, Bitmap icon) {
                if (mMediaMetadata != null && mMediaMetadata.getDescription().getIconUri() != null//
                        && mMediaMetadata.getDescription().getIconUri().toString().equals(artUrl)) {
                    mNotificationManager.notify(NOTIFICATION_ID, createNotification());

                }
            }
        });
    }


    // ############################################################################################


    private final MediaControllerCompat.Callback mMediaControllerCallBack = new MediaControllerCompat.Callback() {
        @Override
        public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
            mPlaybackState = state;
            Log.d(TAG, "Received new playback state" + state);
            if (state.getState() == PlaybackStateCompat.STATE_STOPPED || state.getState() == PlaybackStateCompat.STATE_NONE) {
                stopNotification();

            } else {
                Notification notification = refreshNotification();
                if (notification != null) {
                    mNotificationManager.notify(NOTIFICATION_ID, notification);

                }
            }
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            mMediaMetadata = metadata;
            Log.d(TAG, "Received new metadata " + metadata);
            Notification notification = refreshNotification();
            setIcon(metadata);
            if (notification != null) {
                mNotificationManager.notify(NOTIFICATION_ID, notification);
            }
        }

        @Override
        public void onSessionDestroyed() {
            super.onSessionDestroyed();
            Log.d(TAG, "Session was destroyed, resetting to the new session token");
            try {
                getMediaControllerBySessionToken();

            } catch (RemoteException e) {
                Log.e(TAG, "could not connect media controller");
            }
        }
    };
}