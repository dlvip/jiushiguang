package com.pic.lib.task;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class TaskManager {

    //线程名称
    public static final String IMAGEDOWNLOAD_THREAD_NAME = "ImageDownLoad";

    private ScheduledExecutorService mThreadPool;
    private HashMap<String, Task> mMapTask;

    private static final int THREAD_COUNT = 20;
    private static TaskManager mTaskManager;

    synchronized public static TaskManager init(int nThreadNum) {
        if (mTaskManager == null) {
            mTaskManager = new TaskManager(nThreadNum);

        }
        return mTaskManager;
    }

    public static TaskManager getInstance(){
        if(mTaskManager == null){
            init(0);

        }
        return mTaskManager;
    }

    public TaskManager(int nThreadNum) {
        if (1 == nThreadNum) {
            mThreadPool = Executors.newSingleThreadScheduledExecutor(); // 只有单个后台线程

        } else if (0 == nThreadNum) {
            mThreadPool = Executors.newScheduledThreadPool(THREAD_COUNT); // 指定大小的线程池

        } else {
            mThreadPool = Executors.newScheduledThreadPool(nThreadNum);

        }
        mMapTask = new HashMap<>();

    }

    synchronized public void shutdown() {
        mThreadPool.shutdown();
        for (Task task : mMapTask.values()) {
            if (null != task) {
                task.cancelTask();

            }
        }
        mMapTask.clear();
    }

    synchronized public boolean addTask(Task task) {
        if (mThreadPool.isShutdown()) {
            return false;
        }

        if (null != mMapTask.get(task.getTaskName())) {
            return false;
        }

        task.setTaskManager(this);
        mMapTask.put(task.getTaskName(), task);
        mThreadPool.execute(task);

        return true;
    }

    synchronized public Task findTask(String strTaskName) {
        return mMapTask.get(strTaskName);
    }

    synchronized public void delTask(String strTaskName) {
        Task task = mMapTask.get(strTaskName);
        if (null != task) {
            task.cancelTask();
            mMapTask.remove(strTaskName);
        }
    }

    synchronized public void delAllTask() {
        for (Task task : mMapTask.values()) {
            if (null != task) {
                task.cancelTask();

            }
        }
        mMapTask.clear();
    }
}
