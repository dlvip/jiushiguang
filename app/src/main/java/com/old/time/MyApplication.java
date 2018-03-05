package com.old.time;

import android.app.Application;
import android.content.Context;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.old.time.constants.Key;
import com.old.time.loadsirs.core.LoadSir;
import com.old.time.loadsirs.customs.EmptyCallback;
import com.old.time.loadsirs.customs.ErrorCallback;
import com.old.time.loadsirs.customs.LoadingCallback;
import com.old.time.task.ReadClient;
import com.old.time.task.TaskManager;
import com.old.time.utils.ASRUtil;

/**
 * Created by NING on 2018/2/23.
 */

public class MyApplication extends Application {

    public static MyApplication application;

    public Context mContext;

    @Override
    public void onCreate() {
        ASRUtil.setAppID(this);
        super.onCreate();
        application = this;
        mContext = this;

        init();
    }

    private void init() {
        mTaskMgr = new TaskManager();
        mTaskMgr.init(0);
        client = new ReadClient();//初始化客户端配置信息管理者
        initLoadSirs();

    }

    private TaskManager mTaskMgr;

    public TaskManager getTaskManager() {
        return mTaskMgr;
    }

    public static ReadClient client;

    public static ReadClient getClient() {
        return client;

    }

    /**
     * 初始化挡板
     */
    private void initLoadSirs() {
        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())//错误挡板
                .addCallback(new EmptyCallback())//为空挡板
                .addCallback(new LoadingCallback())//加载中挡板
                .setDefaultCallback(LoadingCallback.class)//默认显示挡板
                .commit();
    }

    public static MyApplication getInstance() {
        return application;
    }
}
