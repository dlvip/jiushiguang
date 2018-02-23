package com.old.time.task;


import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class ReadClient {
	
	private final static String TAG = "ReadClient";
	
	private TaskManager 	mTaskMgr;
	private Handler mHandlerCallBack;
	private Handler mHandlerProxy;
	private Object mObjLock1, mObjLock2;
    
	public ReadClient() {
		if(!init()) {
			Log.d(TAG, "ReadClient is not init");
		}
	}
	
	private boolean init() {
		mTaskMgr = new TaskManager();
		mTaskMgr.init(0);

		mObjLock1 = new Object();
		mObjLock2 = new Object();

		return true;
	}
	
	public TaskManager getTaskManager() {
		return mTaskMgr;
	}
	

	public void setCallBackHander(Handler handler) {
		synchronized (mObjLock1) {
			mHandlerCallBack = handler;

		}
	}
	
	public void setNullCallBackHander(Handler handler) {
		if(null == handler)
			return ;
		
		synchronized (mObjLock1) {
			if(handler == mHandlerCallBack) {
				mHandlerCallBack.removeCallbacksAndMessages(null);
				mHandlerCallBack = null;
			}
		}
	}
	
	public boolean sendCallBackMsg(int msgID, int arg1, int arg2, Object obj) {
		synchronized (mObjLock1) {
			if(null == mHandlerCallBack) {
				return false;
			}
			
			Message msg = Message.obtain();
			msg.what = msgID;
			msg.arg1 = arg1;
			msg.arg2 = arg2;
			msg.obj = obj;

			return mHandlerCallBack.sendMessage(msg);
		}

	}
	
	public boolean sendCallBackMsg(int msgID, int arg1, int arg2) {
		synchronized (mObjLock1) {
			if(null == mHandlerCallBack) {
				return false;
			}
			
			Message msg = Message.obtain();
			msg.what = msgID;
			msg.arg1 = arg1;
			msg.arg2 = arg2;

			return mHandlerCallBack.sendMessage(msg);
		}

	}
	
	public boolean sendCallBackMsg(int msgID, Object obj) {
		synchronized (mObjLock1) {
			if(null == mHandlerCallBack) {
				return false;
			}
			
			Message msg = Message.obtain();
			msg.what = msgID;
			msg.obj = obj;

			return mHandlerCallBack.sendMessage(msg);
		}

	}
	
	public boolean sendCallBackMsg(int msgID) {
		synchronized (mObjLock1) {
			if(null == mHandlerCallBack) {
				return false;
			}
			Message msg = Message.obtain();
			msg.what= msgID;
			return mHandlerCallBack.sendMessage(msg);
			
//			return mHandlerCallBack.sendEmptyMessage(msgID);
		}
	}
	public boolean sendBackMsg(Message msg) {
		synchronized (mObjLock1) {
			if(null == mHandlerCallBack) {
				return false;
			}
			
			return mHandlerCallBack.sendMessage(msg);
		}
	}

	/*********************************************************************************************************/
    //发视频 个人空间使用
	public void setProxyHandler(Handler handler) {  // 服务通过该方法传过来自己的handler，用于控制服务
		synchronized (mObjLock2) {
			mHandlerProxy = handler;
		}
	}
	
	public void setNullProxyHander(Handler handler) {  // 服务销毁时，调用该方法，用于销毁自己的handler
		if(null == handler)
			return ;
		
		synchronized (mObjLock2) {
			if(handler == mHandlerProxy) {
				mHandlerProxy.removeCallbacksAndMessages(null);
				mHandlerProxy = null;
			}
		}
	}
	
	public boolean sendProxyMsg(int msgID, int arg1, int arg2, Object obj) {
		synchronized (mObjLock2) {
			if(null == mHandlerProxy) { // 用服务的handler来给服务发送消息，用于控制服务让服务做相应的下载操作
				return false;
			}
			
			Message msg = Message.obtain();
			msg.what = msgID;
			msg.arg1 = arg1;
			msg.arg2 = arg2;
			msg.obj = obj;

			return mHandlerProxy.sendMessage(msg);
		}

	}
	
	public boolean sendProxyMsg(int msgID, int arg1, int arg2) {
		synchronized (mObjLock2) {
			if(null == mHandlerProxy) {
				return false;
			}
			
			Message msg = Message.obtain();
			msg.what = msgID;
			msg.arg1 = arg1;
			msg.arg2 = arg2;

			return mHandlerProxy.sendMessage(msg);
		}

	}
	
	public boolean sendProxyMsg(int msgID, Object obj) {
		synchronized (mObjLock2) {
			if(null == mHandlerProxy) {
				return false;
			}
			
			Message msg = Message.obtain();
			msg.what = msgID;
			msg.obj = obj;

			return mHandlerProxy.sendMessage(msg);
		}

	}
	
	public boolean sendProxyMsg(int msgID) {
		synchronized (mObjLock2) {
			if(null == mHandlerProxy) {
				return false;
			}
			
			return mHandlerProxy.sendEmptyMessage(msgID); 
		}
	}
}

