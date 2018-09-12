package com.old.time.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

/**
 * Created by NING on 2018/9/12.
 */

public class PlayServiceManager {

    private Context context;

    public PlayServiceManager(Context context) {
        this.context = context;
    }

    //启动服务，需要关闭记得一定要使用 stopService 关闭，即使没有组件绑定到服务服务也会一直运行，因为此时他是以启动的方式启动的，而不是绑定。
    public static void startPlayService(Context context) {
        Intent intent = new Intent(context, PlayMusicService.class);
        context.startService(intent);

    }

    //绑定服务
    public void bindService(ServiceConnection connection, Bundle bundle) {
        Intent intent = new Intent(context, PlayMusicService.class);
        if (bundle != null) {
            intent.putExtra(PlayMusicService.SERVICE_BUNDLE_EXTRA, bundle);

        }
        context.bindService(intent, connection, Service.BIND_AUTO_CREATE);

    }
}
