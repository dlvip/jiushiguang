package com.old.time.task;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TaskManager {
    private ScheduledExecutorService mThreadPool;
    private HashMap<String, Task> mMapTask;
    private HashMap<String, Task> examMapTask;//考试任务

    private static final int THREAD_COUNT = 20;

    synchronized public void init(int nThreadNum) {
        if (1 == nThreadNum) {
            mThreadPool = Executors.newSingleThreadScheduledExecutor(); // 只有单个后台线程
        } else if (0 == nThreadNum) {
            mThreadPool = Executors.newScheduledThreadPool(THREAD_COUNT); // 指定大小的线程池
        } else {
            mThreadPool = Executors.newScheduledThreadPool(nThreadNum);
        }
        mMapTask = new HashMap<String, Task>();
        examMapTask = new HashMap<String, Task>();

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
// ---------------------------------------------------
    synchronized public boolean addExamTask(Task task) {
        if (mThreadPool.isShutdown()) {
            return false;
        }

        if (null != examMapTask.get(task.getTaskName())) {
            return false;
        }
//        task.setTaskManager(this);
        examMapTask.put(task.getTaskName(), task);
        mThreadPool.execute(task);
        return true;
    }
    /**
     * 延迟任务
     * @param task
     * @param secondTime :几秒延时
     * @return
     */
    synchronized public boolean addExamTask(Task task,int secondTime) {
        if (mThreadPool.isShutdown()) {
            return false;
        }

        if (null != examMapTask.get(task.getTaskName())) {
            return false;
        }

//        task.setTaskManager(this);
        examMapTask.put(task.getTaskName(), task);
        mThreadPool.schedule(task,secondTime, TimeUnit.SECONDS);
        return true;
    }
    synchronized public void delExamTask(String strTaskName) {
        Task task = examMapTask.get(strTaskName);
        if (null != task) {
            task.cancelTask();
            examMapTask.remove(strTaskName);
        }
    }

    /**
     * 是否有考试的任务
     */
    synchronized public boolean hasExamTask() {
        if (examMapTask.size() > 0){
            return true;
        }
        return false;
    }

}
