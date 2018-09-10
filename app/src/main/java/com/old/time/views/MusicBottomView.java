package com.old.time.views;

import android.app.Activity;
import android.content.Context;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.aidl.IPlayControl;
import com.old.time.aidl.Song;
import com.old.time.db.DBMusicocoController;
import com.old.time.manager.MediaManager;
import com.old.time.manager.PlayNotifyManager;
import com.old.time.preference.PlayPreference;
import com.old.time.services.PlayServiceCallback;
import com.old.time.services.SongInfo;

/**
 * Created by wcl on 2018/9/10.
 */

public class MusicBottomView extends LinearLayout implements PlayServiceCallback {

    public MusicBottomView(Context context, int status) {
        super(context);
        initView();

    }

    public MusicBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();

    }

    public MusicBottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();

    }

    private ImageView img_music_pic;
    private TextView tv_music_title;
    private View frame_layout_play;
    private CompletedView tasks_view;
    private ImageView img_play_btn;

    private void initView() {
        activity = (Activity) getContext();
        View view = View.inflate(getContext(), R.layout.play_music_bottom, this);
        img_music_pic = view.findViewById(R.id.img_music_pic);
        tv_music_title = view.findViewById(R.id.tv_music_title);
        frame_layout_play = view.findViewById(R.id.frame_layout_play);
        tasks_view = view.findViewById(R.id.tasks_view);
        img_play_btn = view.findViewById(R.id.img_play_btn);
        img_play_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mControl.resume();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private SongInfo currentSong;
    private Activity activity;
    private final MediaManager mediaManager = MediaManager.getInstance();
    private PlayNotifyManager playNotifyManager;

    public void initData(IPlayControl control, DBMusicocoController dbController) {
        this.mControl = control;
        this.playNotifyManager = new PlayNotifyManager(activity, control, dbController);

        // songChanged 错过，手动赋值为 playNotifyManager # currentSong
        try {
            Song song = control.currentSong();
            if (song != null) {
                playNotifyManager.updateSong(mediaManager.getSongInfo(activity, song));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        playNotifyManager.show();
        playNotifyManager.initBroadcastReceivers();

        try {
            // songChanged & onPlayListChange 回调会对 currentSong 进行赋值，但应用第一次启动时的回调很有可能已经错过，
            // 这是因为 MainActivity 中的 bindService 方法比较耗时导致，该方法会对 MediaManager 进行数
            // 据初始化，这些数据时急需的，不能异步获取，只能阻塞
            Song song = control.currentSong();
            currentSong = this.mediaManager.getSongInfo(activity, song);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private IPlayControl mControl;

    @Override
    public void songChanged(Song song, int index, boolean isNext) {

    }

    @Override
    public void startPlay(Song song, int index, int status) {

    }

    @Override
    public void stopPlay(Song song, int index, int status) {

    }

    @Override
    public void onPlayListChange(Song current, int index, int id) {

    }

    @Override
    public void dataIsReady(IPlayControl mControl) {


    }
}
