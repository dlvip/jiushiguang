package com.old.time.activitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Parcelable;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.beans.CourseBean;
import com.old.time.constants.Constant;
import com.old.time.dialogs.DialogChapterList;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.interfaces.ImageDownLoadCallBack;
import com.old.time.interfaces.OnClickManagerCallBack;
import com.old.time.mp3Utils.Mp3Info;
import com.old.time.mp3Utils.MusicService;
import com.old.time.permission.PermissionUtil;
import com.old.time.receivers.MusicBroadReceiver;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.DebugLog;
import com.old.time.utils.SpUtils;
import com.old.time.utils.StringUtils;
import com.old.time.utils.UIHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MusicPlayActivity extends BaseActivity implements MusicBroadReceiver.MusicPlayCallBackListener {

    public static void startMusicPlayActivity(Context mContext, CourseBean mCourseBean) {
        if (!PermissionUtil.checkAndRequestPermissionsInActivity((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})) {

            return;
        }
        Intent intent = new Intent(mContext, MusicPlayActivity.class);
        intent.putExtra("mCourseBean", mCourseBean);
        ActivityUtils.startLoginActivity((Activity) mContext, intent);

    }

    private static final String TAG = "MusicPlayActivity";
    private ProgressBar mProgressBar;
    private View mainView;
    private ImageView img_book_pic;
    private ImageView img_more, img_previous, img_next, img_play;
    private List<Mp3Info> mMusicList = new ArrayList<>();
    private TextView mSong;
    private TextView mSinger;
    private int mPosition;
    private boolean mIsPlaying = false;
    private TextView tv_speed, tv_progress_time, tv_title_time;

    private RemoteViews remoteViews;
    private PendingIntent pending_intent_play;
    private NotificationManager mNotificationManager;
    private Mp3Info mMp3Info;

    private MusicBroadReceiver receiver;

    public void initView() {
        //创建广播接受者
        if (receiver == null) {
            receiver = new MusicBroadReceiver(this);

        }
        registerReceiver(receiver, MusicBroadReceiver.getIntentFilter());

        mainView = findViewById(R.id.music_bg);
        mSong = findViewById(R.id.textViewSong);//歌名
        mSinger = findViewById(R.id.textViewSinger);//歌手
        img_more = findViewById(R.id.img_more);
        img_previous = findViewById(R.id.img_previous);//上一首
        img_play = findViewById(R.id.img_play);//播放模式
        img_next = findViewById(R.id.img_next);//下一首
        tv_progress_time = findViewById(R.id.tv_progress_time);
        tv_title_time = findViewById(R.id.tv_title_time);
        mProgressBar = findViewById(R.id.pro_percent);
        tv_speed = findViewById(R.id.tv_speed);
        img_book_pic = findViewById(R.id.img_book_pic);
        remoteViews = new RemoteViews(getPackageName(), R.layout.customnotice);//通知栏布局
        //消息管理
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        createNotification();//创建通知栏

        addMusic("289105");

        mIsPlaying = MusicService.isPlaying;
        //初始化控件UI，默认显示历史播放歌曲
        mPosition = SpUtils.getInt("music_current_position", 0);
        switchSongUI(mPosition, mIsPlaying);

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_music_play;
    }

    /**
     * 开始音乐服务并传输数据
     */
    private void startMusicService() {
        Intent musicService = new Intent();
        musicService.setClass(getApplicationContext(), MusicService.class);
        musicService.putParcelableArrayListExtra("music_list", (ArrayList<? extends Parcelable>) mMusicList);
        startService(musicService);

    }

    /**
     * 刷新播放控件的歌名，歌手，图片，按钮的形状
     */
    private void switchSongUI(int position, final boolean isPlaying) {
        if (mMusicList.size() > 0 && position < mMusicList.size()) {
            // 1.获取播放数据
            mMp3Info = mMusicList.get(position);
            // 2.设置歌曲名，歌手
            String mSongTitle = mMp3Info.getTitle();
            String mSingerArtist = mMp3Info.getArtist();
            mSong.setText(mSongTitle);
            mSinger.setText(mSingerArtist);
            // 3.更新notification通知栏和播放控件UI
            GlideUtils.getInstance().downLoadBitmap(mContext, mMp3Info.getPicUrl(), new ImageDownLoadCallBack() {
                @Override
                public void onDownLoadSuccess(Bitmap resource) {
                    // 4.更换音乐背景
                    assert resource != null;
                    Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette p) {
                            int mutedColor = p.getMutedColor(Color.BLACK);
                            Palette.Swatch darkMutedSwatch = p.getDarkMutedSwatch();//获取柔和的黑
                            mainView.setBackgroundColor(darkMutedSwatch != null ? darkMutedSwatch.getRgb() : mutedColor);

                        }
                    });
                    img_book_pic.setImageBitmap(resource);
                    remoteViews.setImageViewBitmap(R.id.widget_album, resource);
                }
            });
            remoteViews.setTextViewText(R.id.widget_title, mMp3Info.getTitle());
            remoteViews.setTextViewText(R.id.widget_artist, mMp3Info.getArtist());
            refreshPlayStateUI(isPlaying);

        } else {
            UIHelper.ToastMessage(mContext, "没有播放内容");

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
            img_play.setImageResource(R.mipmap.ic_pause_white_36dp);

        } else {
            img_play.setImageResource(R.mipmap.ic_play_white_36dp);

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
            pending_intent_play = PendingIntent.getBroadcast(mContext, 4, intent_play_pause, PendingIntent.FLAG_UPDATE_CURRENT);

        } else {//如果暂停——》播放
            intent_play_pause = new Intent();
            intent_play_pause.setAction(Constant.ACTION_PLAY);
            pending_intent_play = PendingIntent.getBroadcast(mContext, 5, intent_play_pause, PendingIntent.FLAG_UPDATE_CURRENT);

        }
        remoteViews.setOnClickPendingIntent(R.id.widget_play, pending_intent_play);
        mNotificationManager.notify(Constant.NOTIFICATION_CEDE, notification);
    }

    private Notification notification = null;

    /**
     * 创建通知栏
     */
    @SuppressLint("NewApi")
    private void createNotification() {
        // 点击跳转到主界面
        Intent intent_main = new Intent(mContext, MusicPlayActivity.class);
        PendingIntent pending_intent_go = PendingIntent.getActivity(mContext, 1, intent_main, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notice, pending_intent_go);

        // 4个参数context, requestCode, intent, flags
        Intent intent_cancel = new Intent();
        intent_cancel.setAction(Constant.ACTION_CLOSE);
        PendingIntent pending_intent_close = PendingIntent.getBroadcast(mContext, 2, intent_cancel, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_close, pending_intent_close);

        // 上一曲
        Intent intent_prv = new Intent();
        intent_prv.setAction(Constant.ACTION_PRV);
        PendingIntent pending_intent_prev = PendingIntent.getBroadcast(mContext, 3, intent_prv, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_prev, pending_intent_prev);

        // 设置播放暂停
        Intent intent_play_pause;
        if (mIsPlaying) {//如果正在播放——》暂停
            intent_play_pause = new Intent();
            intent_play_pause.setAction(Constant.ACTION_PAUSE);
            pending_intent_play = PendingIntent.getBroadcast(mContext, 4, intent_play_pause, PendingIntent.FLAG_UPDATE_CURRENT);

        } else {//如果暂停——》播放
            intent_play_pause = new Intent();
            intent_play_pause.setAction(Constant.ACTION_PLAY);
            pending_intent_play = PendingIntent.getBroadcast(mContext, 5, intent_play_pause, PendingIntent.FLAG_UPDATE_CURRENT);

        }
        remoteViews.setOnClickPendingIntent(R.id.widget_play, pending_intent_play);
        // 下一曲
        Intent intent_next = new Intent();
        intent_next.setAction(Constant.ACTION_NEXT);
        PendingIntent pending_intent_next = PendingIntent.getBroadcast(mContext, 6, intent_next, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_next, pending_intent_next);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(MY_CHANNEL_ID, DEFAULT_CHANNEL_ID, NotificationManager.IMPORTANCE_LOW);
            mNotificationManager.createNotificationChannel(mChannel);

        }
        notification = new Notification.Builder(mContext, DEFAULT_CHANNEL_ID)//
                .setSmallIcon(R.mipmap.ic_launcher)//
                .setCustomContentView(remoteViews)//
                .setChannelId(MY_CHANNEL_ID)//
                .setOngoing(true).build();
    }

    private static final String DEFAULT_CHANNEL_ID = "1234567";
    private static final String MY_CHANNEL_ID = "my_channel_01";

    @Override
    protected void initEvent() {
        super.initEvent();
        img_more.setOnClickListener(this);
        img_previous.setOnClickListener(this);
        img_play.setOnClickListener(this);
        img_next.setOnClickListener(this);
        tv_speed.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_more:
                showListDialog();

                break;
            case R.id.img_previous://上一首
                sendBroadcast(Constant.ACTION_PRV);

                break;
            case R.id.img_play:
                if (mIsPlaying) {
                    sendBroadcast(Constant.ACTION_PAUSE);

                } else {
                    sendBroadcast(Constant.ACTION_PLAY);

                }

                break;
            case R.id.img_next://下一首
                sendBroadcast(Constant.ACTION_NEXT);

                break;
            case R.id.tv_speed://切换播放模式
                sendSpeedBroadcast();

                break;
        }
    }

    /**
     * 改变播放速率
     */
    private void sendSpeedBroadcast() {
        MusicService.PLAY_SPEED++;
        Intent intent = new Intent();
        intent.putExtra(MusicBroadReceiver.START_SPEED, floats[MusicService.PLAY_SPEED % floats.length]);
        intent.setAction(Constant.ACTION_SPEED);
        sendBroadcast(intent);

    }

    private DialogChapterList dialogChapterList;

    private void showListDialog() {
        if (dialogChapterList == null) {
            dialogChapterList = new DialogChapterList(mContext, new OnClickManagerCallBack() {
                @Override
                public void onClickRankManagerCallBack(int position, String typeName) {
                    sendBroadcast(position);

                }
            });
        }
        dialogChapterList.showChapterListDialog(mMusicList);

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

//    /**
//     * 获取章节列表
//     */
//    private void getMusics() {
//        HttpParams params = new HttpParams();
//        params.put("albumId", mCourseBean.albumId);
//        params.put("pageNum", "1");
//        params.put("pageSize", "15");
//        OkGoUtils.getInstance().postNetForData(params, Constant.GET_MUSIC_LIST, new JsonCallBack<ResultBean<List<MusicBean>>>() {
//
//            @Override
//            public void onSuccess(ResultBean<List<MusicBean>> mResultBean) {
//                mMusicList.clear();
//                for (MusicBean mMusicBean : mResultBean.data) {
//                    Mp3Info mp3Info = new Mp3Info();
//                    mp3Info.setAlbum(mMusicBean.getMusicPic());
//                    mp3Info.setAlbumId(Long.parseLong(mMusicBean.getAlbumId()));
//                    mp3Info.setAudio(mMusicBean.getMusicUrl());
//                    mp3Info.setDuration(mMusicBean.getMusicTime());
//                    mp3Info.setPicUrl(mMusicBean.getMusicPic());
//                    mp3Info.setTitle(mMusicBean.getMusicTitle());
//                    mp3Info.setUrl(mMusicBean.getMusicUrl());
//
//                    mMusicList.add(mp3Info);
//                }
//                startMusicService();
//
//            }
//
//            @Override
//            public void onError(ResultBean<List<MusicBean>> mResultBean) {
//
//            }
//        });
//    }

    private void addMusic(String jsonName) {
        if (TextUtils.isEmpty(jsonName)) {

            return;
        }
        String string = StringUtils.getJson(jsonName + ".json", mContext);
        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("list");
            mMusicList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject musicObj = jsonArray.getJSONObject(i);
                Mp3Info mp3Info = new Mp3Info();
                mp3Info.setAlbum(musicObj.getString("coverLarge"));
                mp3Info.setAlbumId(Long.parseLong(musicObj.getString("albumId")));
                mp3Info.setAudio(musicObj.getString("playUrl64"));
                mp3Info.setDuration(Long.parseLong(musicObj.getString("duration")));
                mp3Info.setPicUrl(musicObj.getString("coverLarge"));
                mp3Info.setTitle(musicObj.getString("title"));
                mp3Info.setUrl(musicObj.getString("playUrl64"));
                mMusicList.add(mp3Info);

            }
            startMusicService();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 选择播放
     *
     * @param position
     */
    private void sendBroadcast(int position) {
        Intent intent = new Intent();
        intent.putExtra(MusicBroadReceiver.START_POSITION, position);
        intent.setAction(Constant.ACTION_LIST_ITEM);
        sendBroadcast(intent);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityUtils.finishLoginActivity(mContext);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SpUtils.setInt("music_current_position", mPosition);
        unregisterReceiver(receiver);

    }

    @Override
    public void start(int position) {
        DebugLog.d(TAG,"start");
        this.mPosition = position;
        if (img_play != null) {
            img_play.setImageResource(R.mipmap.ic_pause_white_36dp);

        }
        mIsPlaying = true;
        refreshPlayStateUI(mIsPlaying);
    }

    @Override
    public void play() {
        DebugLog.d(TAG,"play");
        if (img_play != null) {
            img_play.setImageResource(R.mipmap.ic_pause_white_36dp);

        }
        mIsPlaying = true;
        refreshPlayStateUI(mIsPlaying);
    }

    @Override
    public void pause() {
        DebugLog.d(TAG,"pause");
        if (img_play != null) {
            img_play.setImageResource(R.mipmap.ic_play_white_36dp);

        }
        mIsPlaying = false;
        refreshPlayStateUI(mIsPlaying);
    }

    @Override
    public void previous() {
        DebugLog.d(TAG,"previous");
        if (mPosition - 1 > 0) {
            mPosition--;

        } else {
            mPosition = mMusicList.size() - 1;

        }
        mIsPlaying = true;
        refreshPlayStateUI(mIsPlaying);
    }

    @Override
    public void next() {
        DebugLog.d(TAG,"next");
        if (mPosition + 1 < mMusicList.size()) {
            mPosition++;

        } else {
            mPosition = 0;

        }
        mIsPlaying = true;
        refreshPlayStateUI(mIsPlaying);
    }

    @Override
    public void progress(int current, int total) {
        DebugLog.d(TAG,"progress");
        if (mProgressBar != null && tv_progress_time != null && tv_title_time != null) {
            mProgressBar.setProgress(current);
            mProgressBar.setMax(total);
            tv_progress_time.setText(StringUtils.getDurationStr(current / 1000));
            tv_title_time.setText(StringUtils.getDurationStr(total / 1000));

        }
    }

    @Override
    public void close() {
        DebugLog.d(TAG,"close");
        if (img_play != null) {
            img_play.setImageResource(R.mipmap.ic_play_white_36dp);

        }
        mIsPlaying = false;
        refreshPlayStateUI(mIsPlaying);
    }

    private float[] floats = {0.7f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f};

    @Override
    public void speed(float speed) {
        DebugLog.d(TAG,"speed");
        if (tv_speed != null) {
            tv_speed.setText("x" + String.valueOf(speed));

        }
    }
}
