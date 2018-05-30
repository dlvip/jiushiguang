package com.old.time.activitys;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.old.time.adapters.DownLoadAdapter;
import com.old.time.downloads.DownLoadListener;
import com.old.time.downloads.DownLoadManager;
import com.old.time.downloads.DownLoadService;
import com.old.time.downloads.TaskInfo;
import com.old.time.downloads.dbcontrol.bean.SQLDownLoadInfo;
import com.old.time.permission.PermissionUtil;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class DownLoadActivity extends CBaseActivity {

    public static void startDownLoadActivity(Context mContext) {
        if (!PermissionUtil.checkAndRequestPermissionsInActivity((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE})) {

            return;
        }

        Intent intent = new Intent(mContext, DownLoadActivity.class);
        ActivityUtils.startActivity((Activity) mContext, intent);

    }

    /**
     * 下载管理器
     */
    private DownLoadManager manager;

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
        manager.setSupportBreakpoint(true);
        manager.deleteAllTask();
        mTaskInfos.clear();
        for (int i = 0; i < 20; i++) {
            TaskInfo mTaskInfo = new TaskInfo();
            String fileName = "";
            if (!TextUtils.isEmpty(downLoadUrl)) {
                String[] filepaths = downLoadUrl.split("/");
                if (filepaths != null && filepaths.length > 0) {
                    fileName = filepaths[filepaths.length - 1];
                    fileName = fileName.replace(".mp4","");

                }
            }
            mTaskInfo.setFileName(i + fileName);
            mTaskInfo.setTaskID(i + fileName);
            manager.addTask(i + fileName, downLoadUrl, i + fileName, new DownloadManagerListener(mTaskInfo));
            mTaskInfos.add(mTaskInfo);

        }
        mAdapter = new DownLoadAdapter(mTaskInfos);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void getDataFromNet(boolean isRefresh) {
        mSwipeRefreshLayout.setRefreshing(false);

    }

    private class DownloadManagerListener implements DownLoadListener {

        private TaskInfo mTaskInfo;

        public DownloadManagerListener(TaskInfo mTaskInfo) {
            this.mTaskInfo = mTaskInfo;

        }

        @Override
        public void onStart(SQLDownLoadInfo sqlDownLoadInfo) {
            if (!mTaskInfos.contains(mTaskInfo)) {

                return;
            }
            mTaskInfo.setOnDownloading(true);
            int position = mTaskInfos.indexOf(mTaskInfo);
            mAdapter.setData(position, mTaskInfo);

        }

        @Override
        public void onProgress(SQLDownLoadInfo sqlDownLoadInfo, boolean isSupportBreakpoint) {
            if (!mTaskInfos.contains(mTaskInfo)) {

                return;
            }
            mTaskInfo.setDownFileSize(sqlDownLoadInfo.getDownloadSize());
            mTaskInfo.setFileSize(sqlDownLoadInfo.getFileSize());
            int position = mTaskInfos.indexOf(mTaskInfo);
            mAdapter.setData(position, mTaskInfo);

        }

        @Override
        public void onStop(SQLDownLoadInfo sqlDownLoadInfo, boolean isSupportBreakpoint) {
            if (!mTaskInfos.contains(mTaskInfo)) {

                return;
            }
            mTaskInfo.setOnDownloading(false);
            int position = mTaskInfos.indexOf(mTaskInfo);
            mAdapter.setData(position, mTaskInfo);
        }

        @Override
        public void onSuccess(SQLDownLoadInfo sqlDownLoadInfo) {
            if (!mTaskInfos.contains(mTaskInfo)) {

                return;
            }
            mTaskInfo.setOnDownloading(false);
            int position = mTaskInfos.indexOf(mTaskInfo);
            mAdapter.remove(position);
        }

        @Override
        public void onError(SQLDownLoadInfo sqlDownLoadInfo) {
            if (!mTaskInfos.contains(mTaskInfo)) {

                return;
            }
            mTaskInfo.setOnDownloading(false);
            int position = mTaskInfos.indexOf(mTaskInfo);
            mAdapter.setData(position, mTaskInfo);
        }
    }
}
