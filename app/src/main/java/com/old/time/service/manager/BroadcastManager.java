package com.old.time.service.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.old.time.constants.Constant;

/**
 * Created by wcl on 2018/9/13.
 */

public class BroadcastManager {

    // 耳机插入和拔出事件
    public static final String ACTION_PLUG = "android.intent.action.HEADSET_PLUG";

    //播放
    public static final String ACTION_PLAY = "com.old.time.play";

    //暂停
    public static final String ACTION_PAUSE = "com.old.time.pause";

    //上一曲
    public static final String ACTION_PRV = "com.old.time.prv";

    //下一曲
    public static final String ACTION_NEXT = "com.old.time.next";

    //关闭
    public static final String ACTION_CLOSE = "com.old.time.close";

    private static BroadcastManager mInstance;

    private BroadcastManager() {

    }

    public static BroadcastManager getInstance() {
        if (mInstance == null) {
            mInstance = new BroadcastManager();

        }
        return mInstance;
    }

    /**
     * 注册广播接收器
     */
    public void registerBroadReceiver(Context context, String identity, BroadcastReceiver receiver) {
        IntentFilter filter = new IntentFilter(identity);
        context.registerReceiver(receiver, filter);
    }

    /**
     * 注册通知栏广播接受者
     *
     * @param context
     * @param receiver
     */
    public void registerBroadReceiver(Context context, BroadcastReceiver receiver) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_PLUG);
        filter.addAction(ACTION_PLAY);
        filter.addAction(ACTION_PAUSE);
        filter.addAction(ACTION_PRV);
        filter.addAction(ACTION_NEXT);
        filter.addAction(ACTION_CLOSE);
        context.registerReceiver(receiver, filter);
    }

    /**
     * 发送广播
     */
    public void sendBroadcast(Context context, String identity, @Nullable Bundle extras) {
        Intent intent = new Intent();
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.setAction(identity);
        context.sendBroadcast(intent);
    }

    /**
     * 注销广播接收者
     */
    public void unregisterReceiver(Context context, BroadcastReceiver receiver) {
        context.unregisterReceiver(receiver);
    }
}
