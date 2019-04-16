package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.dialogs.DialogPromptCentre;
import com.old.time.interfaces.OnClickViewCallBack;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.ScreenTools;
import com.old.time.utils.UIHelper;
import com.old.time.views.SuspensionPopupWindow;

/**
 * Created by NING on 2018/2/23.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

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

    private TextView tv_title, tv_send;

    /**
     * 设置title
     *
     * @param text
     */
    public void setTitleText(int text) {
        if (tv_title == null) {
            tv_title = findViewById(R.id.top_title);

        }
        if (tv_title != null) tv_title.setText(text);
    }

    /**
     * 设置title
     *
     * @param text
     */
    public void setTitleText(String text) {
        if (tv_title == null) {
            tv_title = findViewById(R.id.top_title);

        }
        if (tv_title != null) tv_title.setText(text);

    }

    public void setSendText(String text) {
        if (tv_send == null) {
            tv_send = findViewById(R.id.tv_send);
            findViewById(R.id.right_layout_send).setVisibility(View.VISIBLE);

        }
        tv_send.setText(text);

    }

    private ImageView img_more;

    public void setRightMoreImg(@DrawableRes int imgRes) {
        if (img_more == null) {
            img_more = findViewById(R.id.img_more);
            findViewById(R.id.relative_layout_more).setVisibility(View.VISIBLE);

        }
        img_more.setImageResource(imgRes);

    }

    public void setSendTextColor(int textColor) {
        if (tv_send == null) {
            tv_send = findViewById(R.id.tv_send);
            findViewById(R.id.right_layout_send).setVisibility(View.VISIBLE);

        }
        tv_send.setTextColor(getResources().getColor(textColor));

    }

    private DialogPromptCentre mDialogPromptCentre;

    /**
     * 申请权限的弹框
     */
    protected void showDialogPermission() {
        if (mDialogPromptCentre == null) {
            mDialogPromptCentre = new DialogPromptCentre(mContext, new OnClickViewCallBack() {
                @Override
                public void onClickTrueView() {
                    Intent localIntent = new Intent();
                    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (Build.VERSION.SDK_INT >= 9) {
                        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        localIntent.setData(Uri.fromParts("package", getPackageName(), null));

                    } else if (Build.VERSION.SDK_INT <= 8) {
                        localIntent.setAction(Intent.ACTION_VIEW);
                        localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                        localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());

                    }
                    ActivityUtils.startActivity(mContext, localIntent);

                }

                @Override
                public void onClickCancelView() {

                }
            });
        }
        mDialogPromptCentre.showDialog("申请权限失败，请手动开启相关权限");
    }

    /**
     * 获得状态栏高度
     */
    public int getStatusBarHeight() {
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");

        return this.getResources().getDimensionPixelSize(resourceId);
    }

    @Override
    public void onClick(View view) {


    }

    protected void initEvent() {


    }

    public void back(View view) {
        ActivityUtils.finishActivity(this);

    }

    public void save(View view) {

    }

    public void more(View view) {

    }

    public void setOrientation(int orientation) {
        setRequestedOrientation(orientation);

    }

    protected abstract void initView();

    protected abstract @LayoutRes
    int getLayoutID();

    private int W, H;
    private int showX, showY;
    private SuspensionPopupWindow mSuspensionPopupWindow;

    /**
     * 发送内容入口
     */
    public void showSuspensionPopupWindow() {
        if (mSuspensionPopupWindow == null) {
            ScreenTools mScreenTools = ScreenTools.instance(this);
            W = mScreenTools.getScreenWidth();
            H = mScreenTools.getScreenHeight();
            showX = W / 2 - UIHelper.dip2px(40);
            showY = H - UIHelper.dip2px(80);
            mSuspensionPopupWindow = new SuspensionPopupWindow(this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSuspensionPopupWindowClick();

                }
            });
        }
        mSuspensionPopupWindow.showAtLocationXY(getWindow().getDecorView(), Gravity.TOP, showX, showY);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSuspensionPopupWindow != null) {
            mSuspensionPopupWindow.dismiss();
            mSuspensionPopupWindow = null;

        }
    }

    /**
     * 按钮点击事件
     */
    public void setSuspensionPopupWindowClick() {


    }

}
