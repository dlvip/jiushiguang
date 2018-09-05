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
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.CourseBean;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.interfaces.ImageDownLoadCallBack;
import com.old.time.mp3Utils.MediaUtil;
import com.old.time.mp3Utils.Mp3Info;
import com.old.time.mp3Utils.MusicPlayerView;
import com.old.time.mp3Utils.MusicService;
import com.old.time.permission.PermissionUtil;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.MyLinearLayoutManager;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.SpUtils;
import com.old.time.utils.StringUtils;
import com.old.time.utils.UIHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MusicActivity extends BaseActivity {

    public static void startMusicActivity(Context mContext, CourseBean mCourseBean) {
        if (!PermissionUtil.checkAndRequestPermissionsInActivity((Activity) mContext, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)) {

            return;
        }
        Intent intent = new Intent(mContext, MusicActivity.class);
        intent.putExtra("mCourseBean", mCourseBean);
        ActivityUtils.startLoginActivity((Activity) mContext, intent);

    }

    private static final String TAG = "MusicActivity";
    private MusicPlayerView mpv;
    private View mainView;
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

    private CourseBean mCourseBean;
    private RecyclerView recycler_view_music;
    private BaseQuickAdapter<Mp3Info, BaseViewHolder> mp3Adapter;

    public void initView() {
        mCourseBean = (CourseBean) getIntent().getSerializableExtra("mCourseBean");
        mainView = findViewById(R.id.music_bg);
        mSong = findViewById(R.id.textViewSong);//歌名
        mSinger = findViewById(R.id.textViewSinger);//歌手
        mpv = findViewById(R.id.mpv);//自定义播放控件
        mPrevious = findViewById(R.id.previous);//上一首
        mPlayMode = findViewById(R.id.play_mode);//播放模式
        mNext = findViewById(R.id.next);//下一首
        recycler_view_music = findViewById(R.id.recycler_view_music);
        remoteViews = new RemoteViews(getPackageName(), R.layout.customnotice);//通知栏布局
        //消息管理
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        createNotification();//创建通知栏

        mMusicList.clear();
        //音乐列表
        mMusicList.addAll(MediaUtil.getMp3Infos(this));
        //启动音乐服务
//        startMusicService();

        //初始化控件UI，默认显示历史播放歌曲
        mPosition = SpUtils.getInt("music_current_position", 0);
        switchSongUI(mPosition, mIsPlaying);
        recycler_view_music.setLayoutManager(new MyLinearLayoutManager(mContext));
        recycler_view_music.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST));
        mp3Adapter = new BaseQuickAdapter<Mp3Info, BaseViewHolder>(R.layout.adapter_music, mMusicList) {
            @Override
            protected void convert(BaseViewHolder helper, Mp3Info item) {
                int position = helper.getLayoutPosition() + 1;
                helper.setText(R.id.tv_music_index, position + "")//
                        .setText(R.id.tv_music_title, item.getTitle())//
                        .setText(R.id.tv_music_time, String.valueOf(item.getSize()));

            }
        };
        recycler_view_music.setAdapter(mp3Adapter);

//        getMusics();
        addMusic("289105");

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
                    remoteViews.setImageViewBitmap(R.id.widget_album, resource);
                    mpv.setCoverBitmap(resource);
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
        Intent intent_main = new Intent(mContext, MusicActivity.class);
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
            notification = new Notification.Builder(mContext, DEFAULT_CHANNEL_ID)//
                    .setSmallIcon(R.mipmap.ic_launcher)//
                    .setCustomContentView(remoteViews)//
                    .setChannelId(MY_CHANNEL_ID)//
                    .setOngoing(true).build();

        } else {
            notification = new NotificationCompat.Builder(mContext, DEFAULT_CHANNEL_ID)//
                    .setSmallIcon(R.mipmap.ic_launcher)//
                    .setContent(remoteViews)//
                    .setChannelId(MY_CHANNEL_ID)//
                    .setOngoing(true).build();

        }
    }

    private static final String DEFAULT_CHANNEL_ID = "1234567";
    private static final String MY_CHANNEL_ID = "my_channel_01";

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
                mp3Info.setDuration(Long.parseLong(musicObj.getString("playtimes")));
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
    public void onBackPressed() {
        super.onBackPressed();
        ActivityUtils.finishLoginActivity(mContext);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SpUtils.setInt("music_current_position", mPosition);

    }
}
