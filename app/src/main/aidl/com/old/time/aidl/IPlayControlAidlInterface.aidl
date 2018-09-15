// IPlayControlAidlInterface.aidl
package com.old.time.aidl;
import com.old.time.aidl.ChapterBean;
import com.old.time.aidl.IOnModelChangedListener;

// Declare any non-default types here with import statements

interface IPlayControlAidlInterface {

        //########################获取当前播放属性##############

        /**
         * 获取播放列表
         *
         * @return
         */
        List<ChapterBean> getPlayList();

        /**
         * 获取当前播放model
         *
         * @return
         */
        ChapterBean getPlayModel();

        /**
         * 当前播放索引
         *
         * @return
         */
        int getPlayIndex();

        /**
         * 获取当前播放状态
         *
         * @return
         */
        boolean getIsPlaying();

        /**
         * 获取当前进度
         *
         * @return
         */
        int getProgress();

        /**
         * 获取总进度
         *
         * @return
         */
        int getTotalProgress();

        /**
         * 获取播放速率
         *
         * @return
         */
        float getSpeed();

        /**
         * 获取播放模式 0：顺序、1：单曲、2：随机
         *
         * @return
         */
        int getMode();

        //########################设置播放属性##############

        /**
         * 设置播放列表并开启播放
         *
         * @param mChapterBeans
         * @param position
         */
        void setStartList(in List<ChapterBean> mChapterBeans, int position);

        /**
         * 设置播放列表
         *
         * @param mChapterBeans
         * @param position
         */
        void setPlayList(in List<ChapterBean> mChapterBeans, int position);

        /**
         * 播放
         */
        void play();

        /**
         * 暂停
         */
        void pause();

        /**
         * 播放速率
         *
         */
        void speed();

        /**
         * 设置播放模式
         */
        void mode();

        /**
         * 上一首
         */
        void previous();

        /**
         * 下一首
         */
        void next();

        /**
         * 进度
         *
         * @param current 当前进度
         * @param total   总进度
         */
        void progress(int current, int total);

        /**
         * 关闭
         */
        void close();


        //注册播放曲目改变时回调
        void registerIOnModelChangedListener(IOnModelChangedListener li);

        //取消注册播放曲目改变时回调
        void unregisterIOnModelChangedListener(IOnModelChangedListener li);

}
