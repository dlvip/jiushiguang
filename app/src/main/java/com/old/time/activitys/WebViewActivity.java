package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;

import com.old.time.R;
import com.old.time.constants.Constant;
import com.old.time.utils.ActivityUtils;
import com.old.time.views.WebViewFragment;

public class WebViewActivity extends BaseActivity {

    /**
     * H5页面
     *
     * @param mContext
     */
    public static void startWebViewActivity(Activity mContext) {
        Intent intent = new Intent(mContext, WebViewActivity.class);
        ActivityUtils.startActivity(mContext, intent);

    }

    public static void startWebViewActivity(Activity mContext, String url) {
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra(WebViewFragment.WEB_VIEW_URL, url);
        ActivityUtils.startActivity(mContext, intent);

    }

    private String url;
    private WebViewFragment mWebFragment;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void initView() {
        url = getIntent().getStringExtra(WebViewFragment.WEB_VIEW_URL);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mWebFragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        if (TextUtils.isEmpty(url)) {
            url = Constant.mHomeUrl;

        }
        bundle.putString(WebViewFragment.WEB_VIEW_URL, url);
        mWebFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.web_layout, mWebFragment);
        fragmentTransaction.commit();

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_web_view;
    }
}
