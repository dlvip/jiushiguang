package com.old.time.fragments;

import android.content.Context;

import com.old.time.utils.ScreenTools;

/**
 * Created by NING on 2018/2/23.
 */

public abstract class BaseFragment extends LazyLoadFragment {

    public String TAG;
    public Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = getActivity();
        TAG = getClass().getName();
    }

    /**
     * 网络请求数据
     *
     * @param isRefresh
     */
    public abstract void getDataFromNet(boolean isRefresh);

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public int getWindowWidth() {

        return ScreenTools.instance(mContext).getScreenWidth();
    }
}
