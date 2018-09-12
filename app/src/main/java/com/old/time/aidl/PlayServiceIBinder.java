package com.old.time.aidl;

import android.content.Context;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.old.time.service.MediaPlayManager;
import com.old.time.service.PlayMusicService;
import com.old.time.utils.DebugLog;
import com.old.time.utils.SpUtils;

import java.util.List;

/**
 * Created by NING on 2018/9/12.
 */

public class PlayServiceIBinder extends com.old.time.aidl.IPlayControlAidlInterface.Stub implements MediaPlayManager.OnMediaPlayCallBackListener {

    private static final String TAG = "PlayServiceIBinder";

    private MediaPlayManager mMediaPlayManager;
    protected RemoteCallbackList<IOnModelChangedListener> mIOnModelChangedListeners;

    private List<ChapterBean> mChapterBeans;        //播放列表
    private int position;                           //播放索引

    private float speed = 1;                        //播放速率
    private int mode = 0;                           //0:顺序、1：单曲、2：随机

    public PlayServiceIBinder(Context mContext) {
        this.mMediaPlayManager = new MediaPlayManager(mContext, this);
        this.mIOnModelChangedListeners = new RemoteCallbackList<>();
        this.mChapterBeans = SpUtils.getObject(PlayMusicService.SERVICE_MODEL_LIST);
        this.position = SpUtils.getInt(PlayMusicService.SERVICE_PLAY_INDEX, 0);

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

            return mMediaPlayManager.getSpeed();
        }
        return speed;
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
        if (mMediaPlayManager != null && mChapterBeans != null && mChapterBeans.size() > 0) {
            mMediaPlayManager.setPlayUrl(mChapterBeans.get(position).getUrl());
            updatePlayModel(mChapterBeans.get(position));

        }
    }

    @Override
    public void play() throws RemoteException {
        DebugLog.d(TAG, "play");
        if (mChapterBeans == null || mChapterBeans.size() == 0) {

            return;
        }
        if (mMediaPlayManager != null) {
            mMediaPlayManager.play();

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

    @Override
    public void speed(float speed) throws RemoteException {
        DebugLog.d(TAG, "speed");
        this.speed = speed;
        if (mMediaPlayManager != null) {
            mMediaPlayManager.setSpeed(speed);

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
        if (mMediaPlayManager != null) {
            mMediaPlayManager.stop();
            mMediaPlayManager = null;

        }
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

    /**
     * 播放错误回调
     */
    public void updateError() {
        DebugLog.d(TAG, "onError");
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
        DebugLog.d(TAG, "updatePlayModel");
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
        DebugLog.d(TAG, "updateProgress");
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
