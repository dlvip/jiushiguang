package com.old.time.activitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.constants.Constant;
import com.old.time.mp3Utils.MediaUtil;
import com.old.time.mp3Utils.Mp3Info;
import com.old.time.mp3Utils.MusicPlayerView;
import com.old.time.mp3Utils.MusicService;
import com.old.time.permission.PermissionUtil;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.SpUtils;
import com.old.time.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;

public class MusicActivity extends BaseActivity {

    public static void startMusicActivity(Context mContext) {
        if (!PermissionUtil.checkAndRequestPermissionsInActivity((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE})) {

            return;
        }
        Intent intent = new Intent(mContext, MusicActivity.class);
        ActivityUtils.startActivity((Activity) mContext, intent);

    }

    private static final String TAG = "MusicActivity";
    private MusicPlayerView mpv;
    private LinearLayout mainView;
    private ImageView mNext;
    private ImageView mPrevious;
    private List<Mp3Info> mMusicList = new ArrayList<>();
    private TextView mSong;
    private TextView mSinger;
    private ImageView mPlayMode;
    private int mPosition;
    private boolean mIsPlaying = false;

    private RemoteViews remoteViews;
    private PendingIntent pending_intent_play;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private Mp3Info mMp3Info;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Constant.MSG_PROGRESS) {
                int currentPosition = msg.arg1;
                int totalDuration = msg.arg2;
                mpv.setProgress(currentPosition);
                mpv.setMax(totalDuration);

            }
            if (msg.what == Constant.MSG_PREPARED) {
                mPosition = msg.arg1;
                mIsPlaying = (boolean) msg.obj;
                switchSongUI(mPosition, mIsPlaying);
            }
            if (msg.what == Constant.MSG_PLAY_STATE) {
                mIsPlaying = (boolean) msg.obj;
                refreshPlayStateUI(mIsPlaying);
            }
            if (msg.what == Constant.MSG_CANCEL) {
                mIsPlaying = false;
                finish();
            }
        }
    };

    public void initView() {
        mainView = findViewById(R.id.music_bg);
        mSong = findViewById(R.id.textViewSong);//歌名
        mSinger = findViewById(R.id.textViewSinger);//歌手
        mpv = findViewById(R.id.mpv);//自定义播放控件
        mPrevious = findViewById(R.id.previous);//上一首
        mPlayMode = findViewById(R.id.play_mode);//播放模式
        mNext = findViewById(R.id.next);//下一首
        remoteViews = new RemoteViews(getPackageName(), R.layout.customnotice);//通知栏布局
        createNotification();//创建通知栏

        //音乐列表
        mMusicList = MediaUtil.getMp3Infos(this);
        //启动音乐服务
        startMusicService();
        //消息管理
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //初始化控件UI，默认显示历史播放歌曲
        mPosition = SpUtils.getInt("music_current_position", 0);
        mIsPlaying = MusicService.isPlaying();
        switchSongUI(mPosition, mIsPlaying);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_musice;
    }

    /**
     * 开始音乐服务并传输数据
     */
    private void startMusicService() {
        Intent musicService = new Intent();
        musicService.setClass(getApplicationContext(), MusicService.class);
        musicService.putParcelableArrayListExtra("music_list", (ArrayList<? extends Parcelable>) mMusicList);
        musicService.putExtra("messenger", new Messenger(handler));
        startService(musicService);

    }

    /**
     * 刷新播放控件的歌名，歌手，图片，按钮的形状
     */
    private void switchSongUI(int position, boolean isPlaying) {
        if (mMusicList.size() > 0 && position < mMusicList.size()) {
            // 1.获取播放数据
            mMp3Info = mMusicList.get(position);
            // 2.设置歌曲名，歌手
            String mSongTitle = mMp3Info.getTitle();
            String mSingerArtist = mMp3Info.getArtist();
            mSong.setText(mSongTitle);
            mSinger.setText(mSingerArtist);
            // 3.更新notification通知栏和播放控件UI
            Bitmap mBitmap = MediaUtil.getArtwork(MusicActivity.this, mMp3Info.getId(), mMp3Info.getAlbumId(), true, false);
            remoteViews.setImageViewBitmap(R.id.widget_album, mBitmap);
            remoteViews.setTextViewText(R.id.widget_title, mMp3Info.getTitle());
            remoteViews.setTextViewText(R.id.widget_artist, mMp3Info.getArtist());
            refreshPlayStateUI(isPlaying);
            mpv.setCoverBitmap(mBitmap);
            // 4.更换音乐背景
            assert mBitmap != null;
            Palette.from(mBitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette p) {
                    int mutedColor = p.getMutedColor(Color.BLACK);
                    Palette.Swatch darkMutedSwatch = p.getDarkMutedSwatch();      //获取柔和的黑
                    mainView.setBackgroundColor(darkMutedSwatch != null ? darkMutedSwatch.getRgb() : mutedColor);

                }
            });
        } else {
            UIHelper.ToastMessage(mContext, "当前没有音乐，记得去下载再来。");

        }
    }

    /**
     * 刷新播放控件及通知
     */
    private void refreshPlayStateUI(boolean isPlaying) {
        updateMpv(isPlaying);
        updateNotification();

    }

    /**
     * 更新播放控件
     */
    private void updateMpv(boolean isPlaying) {
        if (isPlaying) {
            mpv.start();

        } else {
            mpv.stop();

        }
    }

    /**
     * 更新通知栏UI
     */
    private void updateNotification() {
        Intent intent_play_pause;
        // 创建并设置通知栏
        if (mIsPlaying) {
            remoteViews.setImageViewResource(R.id.widget_play, R.drawable.widget_play);

        } else {
            remoteViews.setImageViewResource(R.id.widget_play, R.drawable.widget_pause);

        }
        // 设置播放
        if (mIsPlaying) {//如果正在播放——》暂停
            intent_play_pause = new Intent();
            intent_play_pause.setAction(Constant.ACTION_PAUSE);
            pending_intent_play = PendingIntent.getBroadcast(this, 4, intent_play_pause, PendingIntent.FLAG_UPDATE_CURRENT);

        } else {//如果暂停——》播放
            intent_play_pause = new Intent();
            intent_play_pause.setAction(Constant.ACTION_PLAY);
            pending_intent_play = PendingIntent.getBroadcast(this, 5, intent_play_pause, PendingIntent.FLAG_UPDATE_CURRENT);

        }
        remoteViews.setOnClickPendingIntent(R.id.widget_play, pending_intent_play);
        mNotificationManager.notify(Constant.NOTIFICATION_CEDE, mBuilder.build());
    }

    /**
     * 创建通知栏
     */
    @SuppressLint("NewApi")
    private void createNotification() {
        mBuilder = new NotificationCompat.Builder(this);
        // 点击跳转到主界面
        Intent intent_main = new Intent(this, MusicActivity.class);
        PendingIntent pending_intent_go = PendingIntent.getActivity(this, 1, intent_main, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notice, pending_intent_go);

        // 4个参数context, requestCode, intent, flags
        Intent intent_cancel = new Intent();
        intent_cancel.setAction(Constant.ACTION_CLOSE);
        PendingIntent pending_intent_close = PendingIntent.getBroadcast(this, 2, intent_cancel, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_close, pending_intent_close);

        // 上一曲
        Intent intent_prv = new Intent();
        intent_prv.setAction(Constant.ACTION_PRV);
        PendingIntent pending_intent_prev = PendingIntent.getBroadcast(this, 3, intent_prv, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_prev, pending_intent_prev);

        // 设置播放暂停
        Intent intent_play_pause;
        if (mIsPlaying) {//如果正在播放——》暂停
            intent_play_pause = new Intent();
            intent_play_pause.setAction(Constant.ACTION_PAUSE);
            pending_intent_play = PendingIntent.getBroadcast(this, 4, intent_play_pause, PendingIntent.FLAG_UPDATE_CURRENT);

        } else {//如果暂停——》播放
            intent_play_pause = new Intent();
            intent_play_pause.setAction(Constant.ACTION_PLAY);
            pending_intent_play = PendingIntent.getBroadcast(this, 5, intent_play_pause, PendingIntent.FLAG_UPDATE_CURRENT);

        }
        remoteViews.setOnClickPendingIntent(R.id.widget_play, pending_intent_play);
        // 下一曲
        Intent intent_next = new Intent();
        intent_next.setAction(Constant.ACTION_NEXT);
        PendingIntent pending_intent_next = PendingIntent.getBroadcast(this, 6, intent_next, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_next, pending_intent_next);

        mBuilder.setSmallIcon(R.mipmap.ic_launcher); // 设置顶部图标（状态栏）
        mBuilder.setContent(remoteViews);
        mBuilder.setOngoing(true);

    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mpv.setOnClickListener(this);
        mPrevious.setOnClickListener(this);
        mPlayMode.setOnClickListener(this);
        mNext.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mpv://自定义播放控件，点击播放或暂停
                if (mIsPlaying) {
                    sendBroadcast(Constant.ACTION_PAUSE);

                } else {
                    sendBroadcast(Constant.ACTION_PLAY);

                }
                break;
            case R.id.previous://上一首
                sendBroadcast(Constant.ACTION_PRV);

                break;
            case R.id.play_mode://切换播放模式
                MusicService.playMode++;
                switch (MusicService.playMode % 3) {
                    case 0://随机播放
                        mPlayMode.setImageResource(R.drawable.player_btn_mode_shuffle_normal);

                        break;
                    case 1://单曲循环
                        mPlayMode.setImageResource(R.drawable.player_btn_mode_loopsingle_normal);

                        break;
                    case 2://列表播放
                        mPlayMode.setImageResource(R.drawable.player_btn_mode_playall_normal);

                        break;
                }
                break;
            case R.id.next://下一首
                sendBroadcast(Constant.ACTION_NEXT);

                break;
        }
    }

    /**
     * 播放状态广播
     *
     * @param action
     */
    private void sendBroadcast(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        sendBroadcast(intent);

    }

    /**
     * 选择播放
     *
     * @param action
     * @param position
     */
    private void sendBroadcast(String action, int position) {
        Intent intent = new Intent();
        intent.putExtra("position", position);
        intent.setAction(action);
        sendBroadcast(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SpUtils.setInt("music_current_position", mPosition);

    }
}
