package com.old.time.task;

import com.old.time.utils.DebugLog;

public class ReadClient {

    private final static String TAG = "ReadClient";

    private TaskManager mTaskMgr;

    public ReadClient() {
        if (!init()) {
            DebugLog.d(TAG, "ReadClient is not init");

        }
    }

    private boolean init() {
        mTaskMgr = new TaskManager();
        mTaskMgr.init(0);

        return true;
    }

    public TaskManager getTaskManager() {
        return mTaskMgr;
    }
}

