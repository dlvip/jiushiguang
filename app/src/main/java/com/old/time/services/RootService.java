package com.old.time.services;

import android.app.Service;

import com.old.time.db.DBMusicocoController;
import com.old.time.manager.MediaManager;
import com.old.time.preference.AppPreference;
import com.old.time.preference.PlayPreference;
import com.old.time.preference.SettingPreference;

/**
 * Created by DuanJiaNing on 2017/6/24.
 */

public abstract class RootService extends Service {

    protected DBMusicocoController dbController;
    protected MediaManager mediaManager;

    protected PlayPreference playPreference;
    protected AppPreference appPreference;
    protected SettingPreference settingPreference;

    public RootService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.playPreference = new PlayPreference(this);
        this.appPreference = new AppPreference(this);
        this.settingPreference = new SettingPreference(this);
        this.dbController = new DBMusicocoController(this, false);
        this.mediaManager = MediaManager.getInstance();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (dbController != null) {
            dbController.close();
        }
    }
}
