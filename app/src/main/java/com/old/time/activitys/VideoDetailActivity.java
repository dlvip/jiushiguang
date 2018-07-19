package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.old.time.R;
import com.old.time.videoUtils.NiceVideoPlayer;
import com.old.time.constants.Constant;
import com.old.time.utils.ActivityUtils;
import com.old.time.views.WebViewFragment;

public class VideoDetailActivity extends BaseActivity {

    /**
     * 视频详情页
     *
     * @param mContext
     */
    public static void startVideoDetailActivity(Context mContext) {
        Intent intent = new Intent(mContext, VideoDetailActivity.class);
        ActivityUtils.startLoginActivity((Activity) mContext, intent);

    }

    private NiceVideoPlayer mMNVideoPlayer;
    private WebViewFragment mWebFragment;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void initView() {
        mMNVideoPlayer = findViewById(R.id.nineImageView);
        mMNVideoPlayer.setUp(Constant.MP4_PATH_URL, null);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mWebFragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(WebViewFragment.WEB_VIEW_URL, Constant.mHomeUrl);
        mWebFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.web_layout, mWebFragment);
        fragmentTransaction.commit();

    }


    @Override
    public void back(View view) {
        ActivityUtils.finishLoginActivity(mContext);

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_video_detail;
    }
}
