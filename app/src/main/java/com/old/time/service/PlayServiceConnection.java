package com.old.time.service;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.old.time.aidl.ChapterBean;
import com.old.time.aidl.IOnModelChangedListener;
import com.old.time.aidl.IPlayControlAidlInterface;
import com.old.time.aidl.OnModelChangedListener;
import com.old.time.service.manager.PlayNotifyManager;

import java.util.List;

/**
 * Created by wcl on 2018/9/12.
 */

public class PlayServiceConnection implements ServiceConnection {

    /**
     * 链接回调
     */
    public interface OnServiceConnectedListener {

        void onServiceConnected();

        void onServiceDisconnected();


    }

    private static final String TAG = "PlayServiceConnection";

    private Activity mContext;


    private OnServiceConnectedListener onServiceConnectedListener;
    private IPlayControlAidlInterface iPlayControlAidlInterface;
    private PlayNotifyManager playNotifyManager;

    public PlayServiceConnection(Activity mContext, OnServiceConnectedListener onServiceConnectedListener) {
        this.mContext = mContext;
        this.onServiceConnectedListener = onServiceConnectedListener;
        this.playNotifyManager = PlayNotifyManager.getInstance(mContext);

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        iPlayControlAidlInterface = IPlayControlAidlInterface.Stub.asInterface(service);
        if (onServiceConnectedListener != null) {
            onServiceConnectedListener.onServiceConnected();

        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        if (onServiceConnectedListener != null) {
            onServiceConnectedListener.onServiceDisconnected();

        }
    }

    /**
     * 设置播放列表并开启播放
     *
     * @param mChapterBeans
     * @param position
     */
    public void setStartList(List<ChapterBean> mChapterBeans, int position) {
        if (iPlayControlAidlInterface != null) {
            try {
                iPlayControlAidlInterface.setStartList(mChapterBeans, position);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 播放
     */
    public void play(boolean isPlaying) {
        if (iPlayControlAidlInterface != null) {
            try {
                if (isPlaying) {
                    iPlayControlAidlInterface.pause();

                } else {
                    iPlayControlAidlInterface.play();

                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 上一首
     */
    public void previous() {
        if (iPlayControlAidlInterface != null) {
            try {
                iPlayControlAidlInterface.previous();

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 下一首
     */
    public void next() {
        if (iPlayControlAidlInterface != null) {
            try {
                iPlayControlAidlInterface.next();

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 播放速率
     */
    public String speed() {
        if (iPlayControlAidlInterface != null) {
            try {
                iPlayControlAidlInterface.speed();

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        if (iPlayControlAidlInterface != null) {
            try {
                return "x" + String.valueOf(iPlayControlAidlInterface.getSpeed());

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        return "x1.0";
    }

    /**
     * 获取播放速率
     *
     * @return
     */
    public String getSpeed() {
        if (iPlayControlAidlInterface != null) {
            try {
                return "x" + String.valueOf(iPlayControlAidlInterface.getSpeed());

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        return "x1.0";
    }

    /**
     * 获取播放列表
     *
     * @return
     */
    public List<ChapterBean> getPlayList() {
        List<ChapterBean> chapterBeans = null;
        if (iPlayControlAidlInterface != null) {
            try {
                chapterBeans = iPlayControlAidlInterface.getPlayList();

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return chapterBeans;
    }

    /**
     * 获取播放索引
     *
     * @return
     */
    public int getPlayIndex() {
        int position = 0;
        if (iPlayControlAidlInterface != null) {
            try {
                position = iPlayControlAidlInterface.getPlayIndex();

            } catch (RemoteException e) {
                e.printStackTrace();

            }
        }
        return position;
    }

    /**
     * 获取播放状态
     *
     * @return
     */
    public boolean isPlaying() {
        if (iPlayControlAidlInterface != null) {
            try {

                return iPlayControlAidlInterface.getIsPlaying();
            } catch (RemoteException e) {
                e.printStackTrace();

            }
        }

        return false;
    }

    /**
     * 注册监听
     *
     * @param listener
     */
    public void registerIOnModelChangedListener(IOnModelChangedListener listener) {
        if (iPlayControlAidlInterface != null) {
            try {
                iPlayControlAidlInterface.registerIOnModelChangedListener(listener);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 取消监听
     *
     * @param listener
     */
    public void unregisterIOnModelChangedListener(IOnModelChangedListener listener) {
        if (iPlayControlAidlInterface != null) {
            try {
                iPlayControlAidlInterface.unregisterIOnModelChangedListener(listener);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

}
