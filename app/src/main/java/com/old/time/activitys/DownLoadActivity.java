package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.old.time.adapters.DownLoadAdapter;
import com.old.time.downloads.DownLoadListener;
import com.old.time.downloads.DownLoadManager;
import com.old.time.downloads.DownLoadService;
import com.old.time.downloads.TaskInfo;
import com.old.time.downloads.dbcontrol.bean.SQLDownLoadInfo;
import com.old.time.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

public class DownLoadActivity extends CBaseActivity {

    public static void startDownLoadActivity(Context mContext) {
        Intent intent = new Intent(mContext, DownLoadActivity.class);
        ActivityUtils.startActivity((Activity) mContext, intent);

    }

    /**
     * 下载管理器
     */
    private DownLoadManager manager;

    /**
     * 用户ID，客户端切换用户时可以显示相应用户的下载任务
     */
    private String userId = "123456";

    /**
     * 模拟下载地址
     */
    private String downLoadUrl = "http://test.o.longbeidata.com/filekey/95b0b81bd7f14e658b001928183294a0.mp4";

    private DownLoadAdapter mAdapter;

    private List<TaskInfo> mTaskInfos = new ArrayList<>();

    @Override
    protected void initView() {
        super.initView();
        manager = DownLoadService.getDownLoadManager();
        manager.changeUser(userId);
        manager.setSupportBreakpoint(true);
        manager.getAllTask();
        mTaskInfos.clear();
        for (int i = 0; i < 20; i++) {
            TaskInfo info = new TaskInfo();
            info.setFileName("下载管理Item" + i);
            info.setTaskID("下载管理Item" + i);
            info.setOnDownloading(true);
            manager.addTask("下载管理Item" + i, downLoadUrl, "下载管理Item" + i);
            mTaskInfos.add(info);

        }
        manager.setAllTaskListener(new DownloadManagerListener());
        mAdapter = new DownLoadAdapter(mTaskInfos, manager);
    }

    @Override
    public void getDataFromNet(boolean isRefresh) {
        mSwipeRefreshLayout.setRefreshing(false);

    }

    private class DownloadManagerListener implements DownLoadListener {

        @Override
        public void onStart(SQLDownLoadInfo sqlDownLoadInfo) {

        }

        @Override
        public void onProgress(SQLDownLoadInfo sqlDownLoadInfo, boolean isSupportBreakpoint) {
            for (int i = 0; i < mTaskInfos.size(); i++) {
                TaskInfo mTaskInfo = mTaskInfos.get(i);
                if (mTaskInfo.getTaskID().equals(sqlDownLoadInfo.getTaskID())) {
                    mTaskInfo.setDownFileSize(sqlDownLoadInfo.getDownloadSize());
                    mTaskInfo.setFileSize(sqlDownLoadInfo.getFileSize());
                    mAdapter.notifyItemChanged(i);

                    break;
                }
            }
        }

        @Override
        public void onStop(SQLDownLoadInfo sqlDownLoadInfo, boolean isSupportBreakpoint) {

        }

        @Override
        public void onSuccess(SQLDownLoadInfo sqlDownLoadInfo) {
            for (int i = 0; i < mTaskInfos.size(); i++) {
                TaskInfo mTaskInfo = mTaskInfos.get(i);
                if (mTaskInfo.getTaskID().equals(sqlDownLoadInfo.getTaskID())) {
                    mAdapter.remove(i);

                    break;
                }
            }
        }

        @Override
        public void onError(SQLDownLoadInfo sqlDownLoadInfo) {
            for (int i = 0; i < mTaskInfos.size(); i++) {
                TaskInfo mTaskInfo = mTaskInfos.get(i);
                if (mTaskInfo.getTaskID().equals(sqlDownLoadInfo.getTaskID())) {
                    mTaskInfo.setOnDownloading(false);
                    mAdapter.remove(i);

                    break;
                }
            }
        }
    }
}
