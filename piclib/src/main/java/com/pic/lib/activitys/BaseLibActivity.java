package com.pic.lib.activitys;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pic.lib.utils.ActivityUtils;

/**
 * Created by NING on 2018/2/23.
 */

public abstract class BaseLibActivity extends AppCompatActivity implements View.OnClickListener {

    public Activity mContext;
    public String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.TAG = getClass().getName();
        this.mContext = this;
        setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getLayoutID());
        initView();

        initEvent();

    }

    public void back(View view) {
        ActivityUtils.finishActivity(this);

    }

    public void save(View view) {

    }

    @Override
    public void onClick(View view) {


    }

    protected void initEvent() {


    }

    public void setOrientation(int orientation) {
        setRequestedOrientation(orientation);

    }

    protected abstract void initView();

    protected abstract @LayoutRes
    int getLayoutID();

}
