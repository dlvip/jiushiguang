package com.old.time.activitys;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v7.graphics.Palette;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.beans.CourseBean;
import com.old.time.constants.Constant;
import com.old.time.dialogs.DialogChapterList;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.interfaces.ImageDownLoadCallBack;
import com.old.time.interfaces.OnClickManagerCallBack;
import com.old.time.beans.ChapterBean;
import com.old.time.musicPlay.MusicService;
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

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MusicPlayActivity extends BaseActivity implements MusicBroadReceiver.MusicPlayCallBackListener {

    /**
     * 播放页面
     *
     * @param mContext
     * @param mCourseBean
     */
    public static void startMusicPlayActivity(Context mContext, CourseBean mCourseBean) {
        if (!PermissionUtil.checkAndRequestPermissionsInActivity((Activity) mContext//
                , WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)) {

            return;
        }
        Intent intent = new Intent(mContext, MusicPlayActivity.class);
        intent.putExtra("mCourseBean", mCourseBean);
        ActivityUtils.startLoginActivity((Activity) mContext, intent);

    }

    private static final String TAG = "MusicPlayActivity";
    private LinearLayout linear_layout_down;
    private ProgressBar mProgressBar;
    private View mainView;
    private ImageView img_book_pic;
    private ImageView img_more, img_previous, img_next, img_play;

    private CourseBean mCourseBean;
    private List<ChapterBean> mChapterBeans = new ArrayList<>();
    private TextView mSong;
    private TextView mSinger;
    private int mPosition;
    private boolean mIsPlaying = false;
    private TextView tv_speed, tv_progress_time, tv_title_time;

    private MusicBroadReceiver receiver;

    public void initView() {
        //创建广播接受者
        if (receiver == null) {
            receiver = new MusicBroadReceiver(this);

        }
        registerReceiver(receiver, MusicBroadReceiver.getIntentFilter());
        linear_layout_down = findViewById(R.id.linear_layout_down);
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

        mCourseBean = (CourseBean) getIntent().getSerializableExtra("mCourseBean");
        if (mCourseBean == null) {
            mCourseBean = SpUtils.getObject(SpUtils.MUSIC_CURRENT_COURSEBEAN);

        }
        if (mCourseBean != null) {
            getMusics(mCourseBean.albumId);

        }

        mIsPlaying = MusicService.isPlaying;
        //初始化控件UI，默认显示历史播放歌曲
        mPosition = SpUtils.getInt(SpUtils.MUSIC_CURRENT_POSITION, 0);
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
        musicService.putParcelableArrayListExtra("music_list", (ArrayList<? extends Parcelable>) mChapterBeans);
        startService(musicService);

    }

    /**
     * 刷新播放控件的歌名，歌手，图片，按钮的形状
     */
    private void switchSongUI(int position, final boolean isPlaying) {
        if (mChapterBeans == null || mChapterBeans.size() == 0 || position > mChapterBeans.size() - 1) {

            return;
        }
        if (tv_speed != null) {
            tv_speed.setText("x" + floats[MusicService.PLAY_SPEED % floats.length]);

        }
        // 1.获取播放数据
        ChapterBean mChapterBean = mChapterBeans.get(position);
        // 2.设置歌曲名，歌手
        String mSongTitle = mChapterBean.getTitle();
        String mSingerArtist = mChapterBean.getArtist();
        mSong.setText(mSongTitle);
        mSinger.setText(mSingerArtist);
        GlideUtils.getInstance().downLoadBitmap(mContext, mChapterBean.getPicUrl(), new ImageDownLoadCallBack() {
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
            }
        });
        updateMpv(isPlaying);
        if (dialogChapterList != null) {
            dialogChapterList.showChapterListDialog(mChapterBeans, mPosition);

        }
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

    @Override
    protected void initEvent() {
        super.initEvent();
        linear_layout_down.setOnClickListener(this);
        img_more.setOnClickListener(this);
        img_previous.setOnClickListener(this);
        img_play.setOnClickListener(this);
        img_next.setOnClickListener(this);
        tv_speed.setOnClickListener(this);
        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float y1 = 0, y2 = 0;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_POINTER_DOWN:
                        y1 = event.getY();

                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        y2 = event.getY();

                        break;
                }
                if (y2 - y1 > UIHelper.dip2px(30)) {
                    onBackPressed();

                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_layout_down:
                onBackPressed();

                break;
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
            case R.id.tv_speed://切换播放速率
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

    /**
     * 显示列表弹框
     */
    private void showListDialog() {
        if (dialogChapterList == null) {
            dialogChapterList = new DialogChapterList(mContext, new OnClickManagerCallBack() {
                @Override
                public void onClickRankManagerCallBack(int position, String typeName) {
                    sendBroadcast(position);

                }
            });
        }
        dialogChapterList.showChapterListDialog(mChapterBeans, mPosition);

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
     * 获取章节列表
     */
    private void getMusics(String fileName) {
        String string = StringUtils.getJson(fileName + ".json", mContext);
        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("list");
            mChapterBeans.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject musicObj = jsonArray.getJSONObject(i);
                ChapterBean chapterBean = new ChapterBean();
                chapterBean.setAlbum(musicObj.getString("coverLarge"));
                chapterBean.setAlbumId(Long.parseLong(musicObj.getString("albumId")));
                chapterBean.setAudio(musicObj.getString("playUrl64"));
                chapterBean.setDuration(Long.parseLong(musicObj.getString("duration")));
                chapterBean.setPicUrl(musicObj.getString("coverLarge"));
                chapterBean.setTitle(musicObj.getString("title"));
                chapterBean.setUrl(musicObj.getString("playUrl64"));
                mChapterBeans.add(chapterBean);

            }
            startMusicService();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送选择播放广播
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
        SpUtils.setInt(SpUtils.MUSIC_CURRENT_POSITION, mPosition);
        SpUtils.setObject(SpUtils.MUSIC_CURRENT_COURSEBEAN, mCourseBean);

    }

    @Override
    public void start(int position) {
        DebugLog.d(TAG, "start");
        this.mPosition = position;
        if (img_play != null) {
            img_play.setImageResource(R.mipmap.ic_pause_white_36dp);

        }
        mIsPlaying = true;
        switchSongUI(mPosition, mIsPlaying);
    }

    @Override
    public void play() {
        DebugLog.d(TAG, "play");
        if (img_play != null) {
            img_play.setImageResource(R.mipmap.ic_pause_white_36dp);

        }
        mIsPlaying = true;
        updateMpv(mIsPlaying);
    }

    @Override
    public void pause() {
        DebugLog.d(TAG, "pause");
        if (img_play != null) {
            img_play.setImageResource(R.mipmap.ic_play_white_36dp);

        }
        mIsPlaying = false;
        updateMpv(mIsPlaying);
    }

    @Override
    public void previous() {
        DebugLog.d(TAG, "previous");
        if (mPosition - 1 > 0) {
            mPosition--;

        } else {
            mPosition = mChapterBeans.size() - 1;

        }
        mIsPlaying = true;
        switchSongUI(mPosition, mIsPlaying);
    }

    @Override
    public void next() {
        DebugLog.d(TAG, "next");
        if (mPosition + 1 < mChapterBeans.size()) {
            mPosition++;

        } else {
            mPosition = 0;

        }
        mIsPlaying = true;
        switchSongUI(mPosition, mIsPlaying);
    }

    @Override
    public void progress(int current, int total) {
        DebugLog.d(TAG, "progress");
        if (mProgressBar != null && tv_progress_time != null && tv_title_time != null) {
            mProgressBar.setProgress(current);
            mProgressBar.setMax(total);
            tv_progress_time.setText(StringUtils.getDurationStr(current / 1000));
            tv_title_time.setText(StringUtils.getDurationStr(total / 1000));

        }
    }

    @Override
    public void close() {
        DebugLog.d(TAG, "close");

    }

    private float[] floats = {0.7f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f};

    @Override
    public void speed(float speed) {
        DebugLog.d(TAG, "speed");
        if (tv_speed != null) {
            tv_speed.setText("x" + String.valueOf(speed));

        }
    }
}
