package com.old.time.aidl;

import android.content.Context;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.old.time.service.MediaSessionManager;
import com.old.time.service.PlayMusicService;
import com.old.time.utils.DebugLog;
import com.old.time.utils.SpUtils;

import java.util.List;

/**
 * Created by NING on 2018/9/12.
 */

public class PlayServiceIBinder extends com.old.time.aidl.IPlayControlAidlInterface.Stub implements MediaSessionManager.OnMediaPlayCallBackListener {

    private static final String TAG = "PlayServiceIBinder";

    private MediaSessionManager mMediaSessionManager;
    protected RemoteCallbackList<IOnModelChangedListener> mIOnModelChangedListeners;

    private List<ChapterBean> mChapterBeans;        //播放列表
    private int position;                           //播放索引

    private float speed = 1;                        //播放速率
    private int mode = 0;                           //0:顺序、1：单曲、2：随机

    public PlayServiceIBinder(Context mContext) {
        this.mMediaSessionManager = new MediaSessionManager(mContext, this);
        this.mIOnModelChangedListeners = new RemoteCallbackList<>();
        this.mChapterBeans = SpUtils.getObject(PlayMusicService.SERVICE_MODEL_LIST);
        this.position = SpUtils.getInt(PlayMusicService.SERVICE_PLAY_INDEX, 0);

    }

    @Override
    public List<ChapterBean> getPlayList() throws RemoteException {

        return mChapterBeans;
    }

    @Override
    public ChapterBean getPlayModel() throws RemoteException {
        if (mChapterBeans == null || mChapterBeans.size() == 0) {

            return null;
        }
        return mChapterBeans.get(position);
    }

    @Override
    public int getPlayIndex() throws RemoteException {

        return position;
    }

    @Override
    public boolean getIsPlaying() throws RemoteException {
        if (mMediaSessionManager != null) {

            return mMediaSessionManager.isPlaying();
        }
        return false;
    }

    @Override
    public int getProgress() throws RemoteException {
        if (mMediaSessionManager != null) {

            return mMediaSessionManager.getProgress();
        }
        return 0;
    }

    @Override
    public int getTotalProgress() throws RemoteException {
        if (mMediaSessionManager != null) {

            return mMediaSessionManager.getTotalProgress();
        }
        return 0;
    }

    @Override
    public float getSpeed() throws RemoteException {
        if (mMediaSessionManager != null) {

            return mMediaSessionManager.getSpeed();
        }
        return speed;
    }

    @Override
    public int getMode() throws RemoteException {

        return mode;
    }

    @Override
    public void setStartList(List<ChapterBean> mChapterBeans, int position) throws RemoteException {
        this.mChapterBeans = mChapterBeans;
        this.position = position;
        if (mMediaSessionManager != null && mChapterBeans != null && mChapterBeans.size() > 0) {
            mMediaSessionManager.play(mChapterBeans.get(position).getUrl());
            updatePlayModel(mChapterBeans.get(position));

        }
    }

    @Override
    public void setPlayList(List<ChapterBean> mChapterBeans, int position) throws RemoteException {
        this.mChapterBeans = mChapterBeans;
        this.position = position;
        if (mMediaSessionManager != null && mChapterBeans != null && mChapterBeans.size() > 0) {
            mMediaSessionManager.setPlayUrl(mChapterBeans.get(position).getUrl());
            updatePlayModel(mChapterBeans.get(position));

        }
    }

    @Override
    public void play() throws RemoteException {
        if (mChapterBeans == null || mChapterBeans.size() == 0) {

            return;
        }
        if (mMediaSessionManager != null) {
            mMediaSessionManager.play();

        }
        updatePlayModel(mChapterBeans.get(position));
    }

    @Override
    public void pause() throws RemoteException {
        if (mChapterBeans == null || mChapterBeans.size() == 0) {

            return;
        }
        if (mMediaSessionManager != null) {
            mMediaSessionManager.pause();

        }
        updatePlayModel(mChapterBeans.get(position));
    }

    @Override
    public void speed(float speed) throws RemoteException {
        this.speed = speed;
        if (mMediaSessionManager != null) {
            mMediaSessionManager.setSpeed(speed);

        }
    }

    @Override
    public void mode() throws RemoteException {
        this.mode++;

    }

    @Override
    public void previous() throws RemoteException {
        if (mChapterBeans == null || mChapterBeans.size() == 0) {

            return;
        }
        if (position <= 0) {
            position = mChapterBeans.size() - 1;

        } else {
            position--;

        }
        if (mMediaSessionManager != null) {
            mMediaSessionManager.play(mChapterBeans.get(position).getUrl());

        }
        updatePlayModel(mChapterBeans.get(position));
    }

    @Override
    public void next() throws RemoteException {
        if (mChapterBeans == null || mChapterBeans.size() == 0) {

            return;
        }
        if (position >= mChapterBeans.size() - 1) {
            position = 0;

        } else {
            position++;

        }

        if (mMediaSessionManager != null) {
            mMediaSessionManager.play(mChapterBeans.get(position).getUrl());

        }
        updatePlayModel(mChapterBeans.get(position));
    }

    @Override
    public void progress(int current, int total) throws RemoteException {
        updateProgress(current, total);

    }

    @Override
    public void close() throws RemoteException {
        if (mMediaSessionManager != null) {
            mMediaSessionManager.stop();
            mMediaSessionManager = null;

        }
    }

    @Override
    public void registerIOnModelChangedListener(IOnModelChangedListener li) throws RemoteException {
        mIOnModelChangedListeners.register(li);

    }

    @Override
    public void unregisterIOnModelChangedListener(IOnModelChangedListener li) throws RemoteException {
        mIOnModelChangedListeners.unregister(li);

    }

    @Override
    public void onError() {
        updateError();

    }

    @Override
    public void onCompletion() {
        try {
            next();

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放错误回调
     */
    public void updateError() {
        try {
            if (mIOnModelChangedListeners != null) {
                int count = mIOnModelChangedListeners.beginBroadcast();
                for (int i = 0; i < count; i++) {
                    IOnModelChangedListener mIOnModelChangedListener = mIOnModelChangedListeners.getBroadcastItem(0);
                    mIOnModelChangedListener.updateError();

                }
            }
        } catch (RemoteException e) {
            DebugLog.d(TAG, e.getMessage());

        }
    }

    /**
     * 更新model
     */
    public void updatePlayModel(ChapterBean mChapterBean) {
        if (mChapterBean == null) {

            return;
        }
        try {
            if (mIOnModelChangedListeners != null) {
                int count = mIOnModelChangedListeners.beginBroadcast();
                for (int i = 0; i < count; i++) {
                    IOnModelChangedListener mIOnModelChangedListener = mIOnModelChangedListeners.getBroadcastItem(0);
                    mIOnModelChangedListener.updatePlayModel(mChapterBean);
                    mIOnModelChangedListener.updateIsPlaying(getIsPlaying());

                }
            }
        } catch (RemoteException e) {
            DebugLog.d(TAG, e.getMessage());

        }
    }

    /**
     * 更新进度
     */
    private void updateProgress(int progress, int total) {
        try {
            if (mIOnModelChangedListeners != null) {
                int count = mIOnModelChangedListeners.beginBroadcast();
                for (int i = 0; i < count; i++) {
                    IOnModelChangedListener mIOnModelChangedListener = mIOnModelChangedListeners.getBroadcastItem(0);
                    mIOnModelChangedListener.updateProgress(progress, total);

                }
            }
        } catch (RemoteException e) {
            DebugLog.d(TAG, e.getMessage());

        }
    }
}
