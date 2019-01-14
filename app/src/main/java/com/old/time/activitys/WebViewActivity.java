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

    public static void startWebViewActivity(Activity mContext, String url, int tag) {
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra(WebViewFragment.WEB_VIEW_URL, url);
        intent.putExtra(WebViewFragment.IS_SHOW_BOTTOM, tag);
        ActivityUtils.startActivity(mContext, intent);

    }

    @Override
    protected void initView() {
        String url = getIntent().getStringExtra(WebViewFragment.WEB_VIEW_URL);
        url = "https://xesapi.speiyou.cn/h5/lighthouse/middlePage?targetUrl=https%3A%2F%2Fstatic-xesapi.speiyou.cn%2Frichmedia%2Farticle%2F154734392148754.html&from=";
        int tag = getIntent().getIntExtra(WebViewFragment.IS_SHOW_BOTTOM, -1);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        WebViewFragment mWebFragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        if (TextUtils.isEmpty(url)) {
            url = Constant.mHomeUrl;

        }
        bundle.putString(WebViewFragment.WEB_VIEW_URL, url);
        bundle.putInt(WebViewFragment.IS_SHOW_BOTTOM, tag);
        mWebFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.web_layout, mWebFragment);
        fragmentTransaction.commit();


    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_web_view;
    }
}
