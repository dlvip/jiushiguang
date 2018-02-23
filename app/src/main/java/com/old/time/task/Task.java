package com.old.time.task;

public abstract class Task implements Runnable {
	
	protected TaskManager mTaskMgr;
	protected String mTaskName;
	protected boolean 		mbCancel;
	protected boolean 		mbRunning;
	
	public Task(String strTaskName) {
		mTaskMgr = null;
		mTaskName = strTaskName;
		mbCancel = false;
		mbRunning = false;
	}
	
	public TaskManager getTaskManager() {
		return mTaskMgr;
		
	}
	
	public void setTaskManager(TaskManager taskMgr) {
		mTaskMgr = taskMgr;
	}
	
	public String getTaskName() {
		return mTaskName;
	}
	
	public void setTaskName(String strTaskName) {
		mTaskName = strTaskName;
	}
	
	public void cancelTask() {
		mbCancel = true;
	}
	
	public boolean isRunning() {
		return mbRunning;
	}
	

	@Override
	public void run() {
		if(mbCancel) {
			return;
		}
		
		mbRunning = true;
		doTask();
		mbRunning = false;
		
		if(null != mTaskMgr) {
			mTaskMgr.delTask(mTaskName);
		}
		
		mbCancel = false;
	}

	abstract protected void doTask();
}
