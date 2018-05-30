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
import com.old.time.utils.RecyclerItemDecoration;

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

        }
        mAdapter = new DownLoadAdapter(manager);
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext,RecyclerItemDecoration.VERTICAL_LIST,10));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void getDataFromNet(boolean isRefresh) {
        mSwipeRefreshLayout.setRefreshing(false);

    }
}
