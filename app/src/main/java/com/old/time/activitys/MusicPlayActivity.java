package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.aidl.OnModelChangedListener;
import com.old.time.aidl.PlayServiceIBinder;
import com.old.time.beans.CourseBean;
import com.old.time.dialogs.DialogChapterList;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.interfaces.ImageDownLoadCallBack;
import com.old.time.interfaces.OnClickManagerCallBack;
import com.old.time.aidl.ChapterBean;
import com.old.time.permission.PermissionUtil;
import com.old.time.service.PlayServiceConnection;
import com.old.time.service.manager.PlayServiceManager;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.DataUtils;
import com.old.time.utils.SpUtils;
import com.old.time.utils.StringUtils;
import com.old.time.utils.UIHelper;

import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MusicPlayActivity extends BaseActivity {

    public static final String PLAY_COURSE_BEAN = "mCourseBean";

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
        intent.putExtra(PLAY_COURSE_BEAN, mCourseBean);
        ActivityUtils.startLoginActivity((Activity) mContext, intent);

    }

    private static final String TAG = "MusicPlayActivity";
    private LinearLayout linear_layout_down;
    private ProgressBar mProgressBar;
    private View mainView;
    private ImageView img_book_pic;
    private ImageView img_more, img_previous, img_next, img_play;

    private TextView mSong;
    private TextView mSinger;
    private boolean mIsPlaying = false;
    private TextView tv_speed, tv_progress_time, tv_title_time;
    private OnModelChangedListener onModelChangedListener;
    private PlayServiceManager mPlayServiceManager;
    private PlayServiceConnection mPlayServiceConnection;

    private CourseBean mCourseBean;
    private String albumId;

    public void initView() {
        mCourseBean = (CourseBean) getIntent().getSerializableExtra(PLAY_COURSE_BEAN);
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

        onModelChangedListener = new OnModelChangedListener() {
            @Override
            public void updatePlayModel(final ChapterBean mChapterBean, final boolean isPlaying) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MusicPlayActivity.this.switchSongUI(mChapterBean, isPlaying);

                    }
                });
            }

            @Override
            public void updateProgress(final int progress, final int total) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MusicPlayActivity.this.updateProgress(progress, total);

                    }
                });
            }

            @Override
            public void updateError() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UIHelper.ToastMessage(mContext, "播放出错");

                    }
                });
            }

            @Override
            public void close() {

            }
        };

        mPlayServiceManager = new PlayServiceManager(mContext);
        mPlayServiceConnection = new PlayServiceConnection(new PlayServiceConnection.OnServiceConnectedListener() {
            @Override
            public void onServiceConnected() {
                mPlayServiceConnection.registerIOnModelChangedListener(onModelChangedListener);
                albumId = SpUtils.getString(mContext, PlayServiceIBinder.SP_PLAY_ALBUM_ID, PlayServiceIBinder.DEFAULT_ALBUM_ID);
                if ((TextUtils.isEmpty(albumId) && mCourseBean != null) || (mCourseBean != null && !albumId.equals(mCourseBean.albumId))) {
                    SpUtils.put(PlayServiceIBinder.SP_PLAY_ALBUM_ID, mCourseBean.albumId);
                    List<ChapterBean> chapterBeans = DataUtils.getModelBeans(mCourseBean.albumId, mContext);
                    mPlayServiceConnection.setStartList(chapterBeans, 0);

                } else if (!mPlayServiceConnection.isPlaying() && mCourseBean != null) {
                    List<ChapterBean> chapterBeans = DataUtils.getModelBeans(mCourseBean.albumId, mContext);
                    int position = SpUtils.getInt(PlayServiceIBinder.SP_PLAY_POSITION, 0);
                    mPlayServiceConnection.setStartList(chapterBeans, position);

                } else if (!mPlayServiceConnection.isPlaying() && mCourseBean == null) {
                    List<ChapterBean> chapterBeans = DataUtils.getModelBeans(albumId, mContext);
                    int position = SpUtils.getInt(PlayServiceIBinder.SP_PLAY_POSITION, 0);
                    mPlayServiceConnection.setStartList(chapterBeans, position);

                }
                ChapterBean chapterBean = mPlayServiceConnection.getPlayModel();
                switchSongUI(chapterBean, mPlayServiceConnection.isPlaying());
            }

            @Override
            public void onServiceDisconnected() {
                mPlayServiceConnection.unregisterIOnModelChangedListener(onModelChangedListener);

            }
        });
        mPlayServiceManager.bindService(mPlayServiceConnection);
    }

    @Override
    protected int getLayoutID() {

        return R.layout.activity_music_play;
    }

    /**
     * 设置进度
     *
     * @param current
     * @param total
     */
    private void updateProgress(int current, int total) {
        mProgressBar.setProgress(current);
        mProgressBar.setMax(total);
        tv_progress_time.setText(StringUtils.getDurationStr(current / 1000));
        tv_title_time.setText(StringUtils.getDurationStr(total / 1000));

    }

    /**
     * 刷新播放控件的歌名，歌手，图片，按钮的形状
     */
    private void switchSongUI(ChapterBean mChapterBean, final boolean isPlaying) {
        if (mChapterBean == null) {

            return;
        }
        mMusicList = mPlayServiceConnection.getPlayList();
        mPosition = mPlayServiceConnection.getPlayIndex();
        String mSongTitle = mChapterBean.getTitle();
        String mSingerArtist = mChapterBean.getArtist();
        mSong.setText(mSongTitle);
        mSinger.setText(mSingerArtist);
        tv_speed.setText(mPlayServiceConnection.getSpeed());
        GlideUtils.getInstance().downLoadBitmap(mContext, mChapterBean.getPicUrl(), 0, new ImageDownLoadCallBack() {

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
            dialogChapterList.notifyItemChanged(mMusicList, mPosition);

        }
    }

    /**
     * 更新播放控件
     */
    private void updateMpv(boolean isPlaying) {
        this.mIsPlaying = isPlaying;
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
                mPlayServiceConnection.previous();

                break;
            case R.id.img_play:
                mPlayServiceConnection.play(mIsPlaying);

                break;
            case R.id.img_next://下一首
                mPlayServiceConnection.next();

                break;
            case R.id.tv_speed://切换播放速率
                tv_speed.setText(mPlayServiceConnection.speed());

                break;
        }
    }

    private int mPosition;
    private List<ChapterBean> mMusicList;
    private DialogChapterList dialogChapterList;

    /**
     * 显示列表弹框
     */
    private void showListDialog() {
        if (dialogChapterList == null) {
            dialogChapterList = new DialogChapterList(mContext, new OnClickManagerCallBack() {
                @Override
                public void onClickRankManagerCallBack(int position, String typeName) {
                    mPlayServiceConnection.setStartList(mMusicList, position);

                }
            });
        }
        dialogChapterList.showChapterListDialog(mMusicList, mPosition);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityUtils.finishLoginActivity(mContext);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayServiceConnection != null) {
            mPlayServiceConnection.unregisterIOnModelChangedListener(onModelChangedListener);
            unbindService(mPlayServiceConnection);

        }
    }
}
