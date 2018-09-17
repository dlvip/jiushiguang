package com.old.time.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.old.time.aidl.ChapterBean;
import com.old.time.aidl.IPlayControlAidlInterface;
import com.old.time.aidl.OnModelChangedListener;
import com.old.time.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wcl on 2018/9/12.
 */

public class PlayServiceConnection implements ServiceConnection {

    private static final String TAG = "PlayServiceConnection";

    private Activity mContext;
    private List<ChapterBean> chapterBeans;

    private IPlayControlAidlInterface iPlayControlAidlInterface;
    private OnModelChangedListener onModelChangedListener;

    public PlayServiceConnection(Activity mContext, OnModelChangedListener onModelChangedListener) {
        this.mContext = mContext;
        this.onModelChangedListener = onModelChangedListener;
        this.chapterBeans = getModelList("289105");

    }

    /**
     * 获取章节列表
     */
    private List<ChapterBean> getModelList(String fileName) {
        String string = StringUtils.getJson(fileName + ".json", mContext);
        List<ChapterBean> chapterBeans = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("list");
            chapterBeans.clear();
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
                chapterBeans.add(chapterBean);

            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return chapterBeans;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        iPlayControlAidlInterface = IPlayControlAidlInterface.Stub.asInterface(service);
        try {
            if (onModelChangedListener != null)
                iPlayControlAidlInterface.registerIOnModelChangedListener(onModelChangedListener);

            iPlayControlAidlInterface.setStartList(chapterBeans, chapterBeans.size() - 1);

        } catch (RemoteException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        try {
            if (onModelChangedListener != null)
                iPlayControlAidlInterface.unregisterIOnModelChangedListener(onModelChangedListener);

        } catch (RemoteException e) {
            e.printStackTrace();
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
}
