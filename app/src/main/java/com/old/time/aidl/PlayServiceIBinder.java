package com.old.time.aidl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.old.time.service.ThreadPoolUtil;
import com.old.time.service.manager.BroadcastManager;
import com.old.time.service.manager.MediaPlayManager;
import com.old.time.service.PlayMusicService;
import com.old.time.utils.DataUtils;
import com.old.time.utils.DebugLog;
import com.old.time.utils.SpUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by NING on 2018/9/12.
 */

public class PlayServiceIBinder extends com.old.time.aidl.IPlayControlAidlInterface.Stub implements MediaPlayManager.OnMediaPlayCallBackListener {

    private static final String TAG = "PlayServiceIBinder";

    private MediaPlayManager mMediaPlayManager;
    protected RemoteCallbackList<IOnModelChangedListener> mIOnModelChangedListeners;

    private List<ChapterBean> mChapterBeans;        //播放列表
    private int position;                           //播放索引

    private int speed = 1;                          //播放速率 0.7、1.0、1.5、2.0、2.5、3.0
    private int mode = 0;                           //0:顺序、1：单曲、2：随机

    private BroadcastReceiver mBroadcastReceiver;
    private Context mContext;

    public PlayServiceIBinder(Context mContext) {
        this.mContext = mContext;
        this.mMediaPlayManager = new MediaPlayManager(mContext, this);
        this.mIOnModelChangedListeners = new RemoteCallbackList<>();
        this.mChapterBeans = SpUtils.getObject(PlayMusicService.SERVICE_MODEL_LIST);
        this.position = SpUtils.getInt(PlayMusicService.SERVICE_PLAY_INDEX, 0);

        sentPositionToMainByTimer();
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    switch (intent.getAction()) {
                        case BroadcastManager.ACTION_PLUG:
                            if (intent.hasExtra("state")) {
                                if (intent.getIntExtra("state", 0) == 0) { // 耳机拔出
                                    pause();

                                } else if (intent.getIntExtra("state", 0) == 1) { // 耳机插入
                                    play();

                                }
                            }

                            break;
                        case BroadcastManager.ACTION_PLAY:
                            play();

                            break;
                        case BroadcastManager.ACTION_PAUSE:
                            pause();

                            break;
                        case BroadcastManager.ACTION_PRV:
                            previous();

                            break;
                        case BroadcastManager.ACTION_NEXT:
                            next();

                            break;
                        case BroadcastManager.ACTION_CLOSE:
                            close();

                            break;
                    }
                } catch (RemoteException e) {
                    DebugLog.d(TAG, e.getMessage());

                }
            }
        };
        BroadcastManager.getInstance().registerBroadReceiver(mContext, mBroadcastReceiver);
    }

    /**
     * 播放进度
     */
    private void sentPositionToMainByTimer() {
        ThreadPoolUtil.getScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (mMediaPlayManager != null && mMediaPlayManager.isPlaying()) {
                    //1.准备好的时候.告诉activity,当前歌曲的总时长
                    final int currentPosition = mMediaPlayManager.getProgress();
                    final int totalDuration = mMediaPlayManager.getTotalProgress();
                    updateProgress(currentPosition, totalDuration);

                }
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public List<ChapterBean> getPlayList() throws RemoteException {
        DebugLog.d(TAG, "getPlayList");
        return mChapterBeans;
    }

    @Override
    public ChapterBean getPlayModel() throws RemoteException {
        DebugLog.d(TAG, "getPlayModel");
        if (mChapterBeans == null || mChapterBeans.size() == 0) {

            return null;
        }
        return mChapterBeans.get(position);
    }

    @Override
    public int getPlayIndex() throws RemoteException {
        DebugLog.d(TAG, "getPlayIndex");
        return position;
    }

    @Override
    public boolean getIsPlaying() throws RemoteException {
        DebugLog.d(TAG, "getIsPlaying");
        if (mMediaPlayManager != null) {

            return mMediaPlayManager.isPlaying();
        }
        return false;
    }

    @Override
    public int getProgress() throws RemoteException {
        DebugLog.d(TAG, "getProgress");
        if (mMediaPlayManager != null) {

            return mMediaPlayManager.getProgress();
        }
        return 0;
    }

    @Override
    public int getTotalProgress() throws RemoteException {
        DebugLog.d(TAG, "getTotalProgress");
        if (mMediaPlayManager != null) {

            return mMediaPlayManager.getTotalProgress();
        }
        return 0;
    }

    @Override
    public float getSpeed() throws RemoteException {
        DebugLog.d(TAG, "getSpeed");
        if (mMediaPlayManager != null) {

            return floats[speed % floats.length];
        }
        return 1.0f;
    }

    @Override
    public int getMode() throws RemoteException {
        DebugLog.d(TAG, "getMode");
        return mode;
    }

    @Override
    public void setStartList(List<ChapterBean> mChapterBeans, int position) throws RemoteException {
        DebugLog.d(TAG, "setStartList");
        this.mChapterBeans = mChapterBeans;
        this.position = position;
        if (mMediaPlayManager != null && mChapterBeans != null && mChapterBeans.size() > 0) {
            mMediaPlayManager.play(mChapterBeans.get(position).getUrl());
            updatePlayModel(mChapterBeans.get(position));

        }
    }

    @Override
    public void setPlayList(List<ChapterBean> mChapterBeans, int position) throws RemoteException {
        DebugLog.d(TAG, "setPlayList");
        this.mChapterBeans = mChapterBeans;
        this.position = position;

    }

    @Override
    public void play() throws RemoteException {
        DebugLog.d(TAG, "play");
        if (mChapterBeans == null || mChapterBeans.size() == 0) {
            String alumId = SpUtils.getString(mContext, PlayServiceIBinder.SP_PLAY_ALBUM_ID, PlayServiceIBinder.DEFAULT_ALBUM_ID);
            mChapterBeans = DataUtils.getModelBeans(alumId, mContext);
            if (mChapterBeans != null && mChapterBeans.size() > 0) {
                play();

            }

            return;
        }
        if (mMediaPlayManager != null) {
            if (mMediaPlayManager.isStarted()) {
                mMediaPlayManager.play();

            } else {
                mMediaPlayManager.play(mChapterBeans.get(position).getUrl());

            }
        }
        updatePlayModel(mChapterBeans.get(position));
    }

    @Override
    public void pause() throws RemoteException {
        DebugLog.d(TAG, "pause");
        if (mChapterBeans == null || mChapterBeans.size() == 0) {

            return;
        }
        if (mMediaPlayManager != null) {
            mMediaPlayManager.pause();

        }
        updatePlayModel(mChapterBeans.get(position));
    }

    private float[] floats = new float[]{0.7f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f};

    @Override
    public void speed() throws RemoteException {
        DebugLog.d(TAG, "speed");
        this.speed++;
        if (mMediaPlayManager != null) {
            mMediaPlayManager.setSpeed(floats[speed % floats.length]);

        }
    }

    @Override
    public void mode() throws RemoteException {
        DebugLog.d(TAG, "mode");
        this.mode++;

    }

    @Override
    public void previous() throws RemoteException {
        DebugLog.d(TAG, "previous");
        if (mChapterBeans == null || mChapterBeans.size() == 0) {

            return;
        }
        if (position <= 0) {
            position = mChapterBeans.size() - 1;

        } else {
            position--;

        }
        if (mMediaPlayManager != null) {
            mMediaPlayManager.play(mChapterBeans.get(position).getUrl());

        }
        updatePlayModel(mChapterBeans.get(position));
    }

    @Override
    public void next() throws RemoteException {
        DebugLog.d(TAG, "next");
        if (mChapterBeans == null || mChapterBeans.size() == 0) {

            return;
        }
        if (position >= mChapterBeans.size() - 1) {
            position = 0;

        } else {
            position++;

        }

        if (mMediaPlayManager != null) {
            mMediaPlayManager.play(mChapterBeans.get(position).getUrl());

        }
        updatePlayModel(mChapterBeans.get(position));
    }

    @Override
    public void progress(int current, int total) throws RemoteException {
        DebugLog.d(TAG, "progress");
        updateProgress(current, total);

    }

    @Override
    public void close() throws RemoteException {
        DebugLog.d(TAG, "close");
        closeNotify();

    }

    @Override
    public void registerIOnModelChangedListener(IOnModelChangedListener li) throws RemoteException {
        DebugLog.d(TAG, "registerIOnModelChangedListener");
        mIOnModelChangedListeners.register(li);

    }

    @Override
    public void unregisterIOnModelChangedListener(IOnModelChangedListener li) throws RemoteException {
        DebugLog.d(TAG, "unregisterIOnModelChangedListener");
        mIOnModelChangedListeners.unregister(li);

    }

    @Override
    public void onError() {
        updateError();

    }

    @Override
    public void onCompletion() {
        DebugLog.d(TAG, "onCompletion");
        try {
            next();

        } catch (RemoteException e) {
            e.printStackTrace();

        }
    }

    public void closeNotify() {
        DebugLog.d(TAG, "closeNotify");
        if (mIOnModelChangedListeners != null) {
            int count = mIOnModelChangedListeners.beginBroadcast();
            for (int i = 0; i < count; i++) {
                IOnModelChangedListener mIOnModelChangedListener = mIOnModelChangedListeners.getBroadcastItem(i);
                try {
                    mIOnModelChangedListener.close();

                } catch (RemoteException e) {
                    e.printStackTrace();

                }
            }
            mIOnModelChangedListeners.finishBroadcast();

        }
    }

    /**
     * 播放错误回调
     */
    public void updateError() {
        DebugLog.d(TAG, "onError");
        if (mIOnModelChangedListeners != null) {
            int count = mIOnModelChangedListeners.beginBroadcast();
            for (int i = 0; i < count; i++) {
                IOnModelChangedListener mIOnModelChangedListener = mIOnModelChangedListeners.getBroadcastItem(i);
                try {
                    mIOnModelChangedListener.updateError();

                } catch (RemoteException e) {
                    e.printStackTrace();

                }
            }
            mIOnModelChangedListeners.finishBroadcast();
        }
    }

    public static final String DEFAULT_ALBUM_ID = "289105";

    /**
     * 播放记录
     */
    public static final String SP_PLAY_ALBUM_ID = "sp_play_album_id";
    public static final String SP_PLAY_MODELS = "sp_play_models";
    public static final String SP_PLAY_POSITION = "sp_play_position";

    /**
     * 更新model
     */
    public void updatePlayModel(ChapterBean mChapterBean) {
        DebugLog.d(TAG, "updatePlayModel");
        if (mChapterBean == null) {

            return;
        }
        if (mIOnModelChangedListeners != null) {
            int count = mIOnModelChangedListeners.beginBroadcast();
            for (int i = 0; i < count; i++) {
                IOnModelChangedListener mIOnModelChangedListener = mIOnModelChangedListeners.getBroadcastItem(i);
                try {
                    mIOnModelChangedListener.updatePlayModel(mChapterBean, getIsPlaying());
                    SpUtils.setObject(SP_PLAY_MODELS, mChapterBeans);
                    SpUtils.setInt(SP_PLAY_POSITION, position);

                } catch (RemoteException e) {
                    DebugLog.d(TAG, e.getMessage());

                }
            }
            mIOnModelChangedListeners.finishBroadcast();
        }
    }

    /**
     * 更新进度
     */
    private void updateProgress(int progress, int total) {
        DebugLog.d(TAG, "updateProgress");
        if (mIOnModelChangedListeners != null) {
            int count = mIOnModelChangedListeners.beginBroadcast();
            for (int i = 0; i < count; i++) {
                IOnModelChangedListener mIOnModelChangedListener = mIOnModelChangedListeners.getBroadcastItem(i);
                try {
                    mIOnModelChangedListener.updateProgress(progress, total);

                } catch (RemoteException e) {
                    e.printStackTrace();

                }
            }
            mIOnModelChangedListeners.finishBroadcast();
        }
    }
}
