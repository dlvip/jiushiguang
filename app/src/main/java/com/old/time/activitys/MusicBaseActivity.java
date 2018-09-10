package com.old.time.activitys;

import com.old.time.receivers.MusicBroadReceiver;

/**
 * Created by wcl on 2018/9/8.
 */

public abstract class MusicBaseActivity extends BaseActivity implements MusicBroadReceiver.MusicPlayCallBackListener {

    public MusicBroadReceiver receiver;

    @Override
    protected void initView() {
        //创建广播接受者
        if (receiver == null) {
            receiver = new MusicBroadReceiver(this);

        }
        registerReceiver(receiver, MusicBroadReceiver.getIntentFilter());

    }

    @Override
    public void start(int position) {

    }

    @Override
    public void play() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void previous() {

    }

    @Override
    public void next() {

    }

    @Override
    public void progress(int current, int total) {

    }

    @Override
    public void close() {

    }

    @Override
    public void speed(float speed) {

    }
}
