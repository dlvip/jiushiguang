// IOnModelChangedListener.aidl
package com.old.time.aidl;
import com.old.time.aidl.ChapterBean;

// Declare any non-default types here with import statements

interface IOnModelChangedListener {

    /**
     * 更新播放model
     *
     * @param mChapterBean
     */
    void updatePlayModel(in ChapterBean mChapterBean, boolean isPlaying);

    /**
     * 更新进度
     *
     * @param progress 当前进度
     * @param total    总进度
     */
    void updateProgress(int progress, int total);

    /**
     * 播放有误
     *
     */
    void updateError();

    /**
     * 关闭
     *
     */
    void close();


}
