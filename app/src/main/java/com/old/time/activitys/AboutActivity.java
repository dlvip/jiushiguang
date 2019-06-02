package com.old.time.activitys;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.old.time.R;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.UIHelper;

public class AboutActivity extends BaseActivity {

    public static void startAboutActivity(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        ActivityUtils.startActivity((Activity) context, intent);

    }

    @Override
    protected void initView() {
        setTitleText("联系我们");

    }

    @Override
    protected void initEvent() {
        super.initEvent();
        findViewById(R.id.tv_cop_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                assert cm != null;
                cm.setPrimaryClip(ClipData.newPlainText("simple text", "1371985810"));

                UIHelper.ToastMessage(mContext, "复制成功");
            }
        });
        findViewById(R.id.tv_cop_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                assert cm != null;
                cm.setPrimaryClip(ClipData.newPlainText("simple text", "704134967"));

                UIHelper.ToastMessage(mContext, "复制成功");
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_about;
    }
}
