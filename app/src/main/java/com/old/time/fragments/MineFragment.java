package com.old.time.fragments;

import android.widget.TextView;

import com.old.time.R;

/**
 * Created by NING on 2018/3/5.
 */

public class MineFragment extends BaseFragment {

    @Override
    public void getDataFromNet(boolean isRefresh) {

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void lazyLoad() {
        TextView top_title = findViewById(R.id.top_title);
        top_title.setText(getString(R.string.main_tab_mine));

    }
}
