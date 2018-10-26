package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.iflytek.voiceads.AdError;
import com.iflytek.voiceads.IFLYAdListener;
import com.iflytek.voiceads.IFLYAdSize;
import com.iflytek.voiceads.IFLYBannerAd;
import com.old.time.R;
import com.old.time.constants.Constant;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.DebugLog;
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

    private int tag;
    private String url;
    private WebViewFragment mWebFragment;
    private FragmentTransaction fragmentTransaction;

    private LinearLayout layout_ads;
    private IFLYBannerAd bannerView;


    @Override
    protected void initView() {
        url = getIntent().getStringExtra(WebViewFragment.WEB_VIEW_URL);
        tag = getIntent().getIntExtra(WebViewFragment.IS_SHOW_BOTTOM, -1);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mWebFragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        if (TextUtils.isEmpty(url)) {
            url = Constant.mHomeUrl;

        }
        bundle.putString(WebViewFragment.WEB_VIEW_URL, url);
        bundle.putInt(WebViewFragment.IS_SHOW_BOTTOM, tag);
        mWebFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.web_layout, mWebFragment);
        fragmentTransaction.commit();


        //此广告位为Demo专用，广告的展示不产生费用
        String adUnitId = "7A5B165A3BC8FD55BE34C33110365A8D";
        //创建旗帜广告，传入广告位ID
        bannerView = IFLYBannerAd.createBannerAd(this, adUnitId);
        //设置请求的广告尺寸
        bannerView.setAdSize(IFLYAdSize.BANNER);
        //设置下载广告前，弹窗提示
        //bannerView.setParameter(AdKeys.DOWNLOAD_ALERT, "true");
        //请求广告，添加监听器
        bannerView.loadAd(mAdListener);
        //将广告添加到布局
        layout_ads = findViewById(R.id.linear_layout_waiting);
        layout_ads.removeAllViews();
        layout_ads.addView(bannerView);

    }

    IFLYAdListener mAdListener = new IFLYAdListener() {

        /**
         * 广告请求成功
         */
        @Override
        public void onAdReceive() {
            //展示广告
            bannerView.showAd();
            DebugLog.d(TAG,"onAdReceive");
        }

        /**
         * 广告请求失败
         */
        @Override
        public void onAdFailed(AdError error) {
            //注意如果请求广告失败，记录这里的errorcode进行反馈
            DebugLog.d(TAG,"onAdFailed");
        }

        /**
         * 广告被点击
         */
        @Override
        public void onAdClick() {
            DebugLog.d(TAG,"onAdClick");
        }

        /**
         * 广告被关闭
         */
        @Override
        public void onAdClose() {
            DebugLog.d(TAG,"onAdClose");
        }

        @Override
        public void onAdExposure() {
            // TODO Auto-generated method stub
            DebugLog.d(TAG,"onAdExposure");
        }

        @Override
        public void onCancel() {
            // TODO Auto-generated method stub
            DebugLog.d(TAG,"onCancel");
        }

        @Override
        public void onConfirm() {
            // TODO Auto-generated method stub
            DebugLog.d(TAG,"onConfirm");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bannerView.destroy();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_web_view;
    }
}
