package com.old.time.activitys;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.old.time.adapters.DownLoadAdapter;
import com.old.time.dialogs.DialogInputBottom;
import com.old.time.downloads.DownLoadListener;
import com.old.time.downloads.DownLoadManager;
import com.old.time.downloads.DownLoadService;
import com.old.time.downloads.TaskInfo;
import com.old.time.downloads.dbcontrol.FileHelper;
import com.old.time.downloads.dbcontrol.bean.SQLDownLoadInfo;
import com.old.time.interfaces.OnClickManagerCallBack;
import com.old.time.permission.PermissionUtil;
import com.old.time.utils.ActivityUtils;

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

    private DownLoadAdapter mAdapter;

    private List<TaskInfo> mTaskInfos = new ArrayList<>();

    @Override
    protected void initView() {
        super.initView();
        manager = DownLoadService.getDownLoadManager();
        manager.setSupportBreakpoint(true);
        manager.deleteAllTask();
        mTaskInfos.clear();
        mAdapter = new DownLoadAdapter(mTaskInfos);
        mRecyclerView.setAdapter(mAdapter);
        linear_layout_more.setVisibility(View.VISIBLE);
        TextView textView = new TextView(mContext);
        textView.setText("添加视频");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        textView.setLayoutParams(params);
        linear_layout_more.addView(textView);
        linear_layout_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEdtDialog();

            }
        });
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
            if(position < mAdapter.getItemCount()){
                mAdapter.setData(position, mTaskInfo);

            }

        }

        @Override
        public void onProgress(SQLDownLoadInfo sqlDownLoadInfo, boolean isSupportBreakpoint) {
            if (!mTaskInfos.contains(mTaskInfo)) {

                return;
            }
            mTaskInfo.setDownFileSize(sqlDownLoadInfo.getDownloadSize());
            mTaskInfo.setFileSize(sqlDownLoadInfo.getFileSize());
            int position = mTaskInfos.indexOf(mTaskInfo);
            if(position < mAdapter.getItemCount()){
                mAdapter.setData(position, mTaskInfo);

            }
        }

        @Override
        public void onStop(SQLDownLoadInfo sqlDownLoadInfo, boolean isSupportBreakpoint) {
            if (!mTaskInfos.contains(mTaskInfo)) {

                return;
            }
            mTaskInfo.setOnDownloading(false);
            int position = mTaskInfos.indexOf(mTaskInfo);
            if(position < mAdapter.getItemCount()){
                mAdapter.setData(position, mTaskInfo);

            }
        }

        @Override
        public void onSuccess(SQLDownLoadInfo sqlDownLoadInfo) {
            if (!mTaskInfos.contains(mTaskInfo)) {

                return;
            }
            mTaskInfo.setOnDownloading(false);
            int position = mTaskInfos.indexOf(mTaskInfo);
            if(position < mAdapter.getItemCount()){
                mAdapter.remove(position);

            }
        }

        @Override
        public void onError(SQLDownLoadInfo sqlDownLoadInfo) {
            if (!mTaskInfos.contains(mTaskInfo)) {

                return;
            }
            mTaskInfo.setOnDownloading(false);
            int position = mTaskInfos.indexOf(mTaskInfo);
            if(position < mAdapter.getItemCount()){
                mAdapter.setData(position, mTaskInfo);

            }
        }
    }

    private DialogInputBottom mInputBottomDialog;


    private void showEdtDialog(){
        if(mInputBottomDialog == null){
            mInputBottomDialog = new DialogInputBottom(mContext, new OnClickManagerCallBack() {
                @Override
                public void onClickRankManagerCallBack(int typeId, String downLoadUrl) {
                    TaskInfo mTaskInfo = new TaskInfo();
                    String fileName = "";
                    if (!TextUtils.isEmpty(downLoadUrl)) {
                        String[] filepaths = downLoadUrl.split("/");
                        if (filepaths != null && filepaths.length > 0) {
                            fileName = filepaths[filepaths.length - 1];

                        }
                    }
                    mTaskInfo.setFileName(downLoadUrl);
                    mTaskInfo.setTaskID(FileHelper.getUserID());
                    manager.addTask(FileHelper.getUserID(), downLoadUrl, fileName, new DownloadManagerListener(mTaskInfo));
                    mAdapter.addData(mTaskInfo);
                }
            });
        }
        mInputBottomDialog.show();

    }
}
