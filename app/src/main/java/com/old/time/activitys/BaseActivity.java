package com.old.time.activitys;

import android.content.Intent;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.permission.PermissionUtil;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.ScreenTools;
import com.old.time.utils.UIHelper;
import com.old.time.views.SuspensionPopupWindow;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by NING on 2018/2/23.
 */

public abstract class BaseActivity extends PermissionActivity implements View.OnClickListener {

    @Override
    protected void loadView() {
        String[] mPermissions = getNeedPermissions();
        if (mPermissions != null && mPermissions.length > 0 //
                && !PermissionUtil.checkAndRequestPermissionsInActivity(mContext, mPermissions)) {

            return;
        }
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

    public void setTitleColor(@ColorRes int color) {
        if (tv_title == null) {
            tv_title = findViewById(R.id.top_title);

        }
        if (tv_title != null) tv_title.setTextColor(getResources().getColor(color));

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }
}
