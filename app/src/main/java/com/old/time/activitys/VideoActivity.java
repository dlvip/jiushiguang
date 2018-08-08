package com.old.time.activitys;

import com.old.time.R;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

public class VideoActivity extends BaseActivity {


    private VerticalViewPager vertical_view_pager;

    @Override
    protected void initView() {
        vertical_view_pager = findViewById(R.id.vertical_view_pager);


    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_video;
    }
}
