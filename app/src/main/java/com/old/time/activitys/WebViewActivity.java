package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.old.time.R;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.UIHelper;
import com.old.time.utils.webUtils.WebViewJavaScriptFunction;
import com.old.time.utils.webUtils.X5WebView;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class WebViewActivity extends BaseActivity {

    private static final String mHomeUrl = "http://yst.longbei.ren/html/yst/index.html";
    private ProgressBar progress_bar_web;
    private X5WebView mWebView;
    private LinearLayout linear_layout_more;
    private LinearLayout.LayoutParams layoutParams;

    public SwipeRefreshLayout.OnRefreshListener onRefreshListener;
    public SwipeRefreshLayout mSwipeRefreshLayout;

    /**
     * H5页面
     *
     * @param mContext
     */
    public static void startWebViewActivity(Activity mContext) {
        Intent intent = new Intent(mContext, WebViewActivity.class);
        ActivityUtils.startActivity(mContext, intent);

    }

    @Override
    protected void initView() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);

        linear_layout_more = findViewById(R.id.linear_layout_more);
        layoutParams = (LinearLayout.LayoutParams) linear_layout_more.getLayoutParams();
        layoutParams.height = UIHelper.dip2px(50);

        progress_bar_web = findViewById(R.id.progress_bar_web);
        mWebView = findViewById(R.id.x5_web_view);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.loadUrl(mHomeUrl);
        mWebView.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        mWebView.addJavascriptInterface(new WebViewJavaScriptFunction() {

            @Override
            public void onJsFunctionCalled(String tag) {

            }

            @JavascriptInterface
            public void onX5ButtonClicked() {
                enableX5FullscreenFunc();

            }

            @JavascriptInterface
            public void onCustomButtonClicked() {
                disableX5FullscreenFunc();

            }

            @JavascriptInterface
            public void onLiteWndButtonClicked() {
                enableLiteWndFunc();

            }

            @JavascriptInterface
            public void onPageVideoClicked() {
                enablePageVideoFunc();

            }
        }, "Android");

        mSwipeRefreshLayout = findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light, R.color.holo_orange_light, R.color.holo_red_light);
        onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWebView.reload();//重新刷新页面

            }
        };
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }

    // 向webview发出信息
    private void enableX5FullscreenFunc() {
        if (mWebView.getX5WebViewExtension() != null) {
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            mWebView.getX5WebViewExtension().invokeMiscMethod("setVideoParams", data);
        }
    }

    private void disableX5FullscreenFunc() {
        if (mWebView.getX5WebViewExtension() != null) {
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", true);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            mWebView.getX5WebViewExtension().invokeMiscMethod("setVideoParams", data);
        }
    }

    private void enableLiteWndFunc() {
        if (mWebView.getX5WebViewExtension() != null) {
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", true);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            mWebView.getX5WebViewExtension().invokeMiscMethod("setVideoParams", data);
        }
    }

    private void enablePageVideoFunc() {
        if (mWebView.getX5WebViewExtension() != null) {
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 1);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            mWebView.getX5WebViewExtension().invokeMiscMethod("setVideoParams", data);
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);//页面中有链接的 点击调用这里
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!mWebView.getSettings().getLoadsImagesAutomatically()) {
                mWebView.getSettings().setLoadsImagesAutomatically(true);

            }
        }
    }

    private class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView webView, int newProgress) {
            if (newProgress == 100) {
                mSwipeRefreshLayout.setRefreshing(false);
                progress_bar_web.setVisibility(View.GONE);//加载完网页进度条消失

            } else if (progress_bar_web.getVisibility() == View.VISIBLE) {
                progress_bar_web.setProgress(newProgress);//设置进度值

            } else {
                progress_bar_web.setVisibility(View.VISIBLE);

            }
            super.onProgressChanged(webView, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView webView, String title) {
            super.onReceivedTitle(webView, title);
            setTitleText(title);
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_web_view;
    }
}
