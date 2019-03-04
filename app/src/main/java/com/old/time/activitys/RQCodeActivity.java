package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.zxing.utils.ImageFindQrUtils;
import com.old.time.R;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.BitmapUtils;
import com.old.time.utils.DebugLog;
import com.old.time.utils.UIHelper;

public class RQCodeActivity extends BaseActivity {

    /**
     * 个人二维码
     *
     * @param context
     */
    public static void startRQCodeActivity(Context context, String baseStr) {
        Intent intent = new Intent(context, RQCodeActivity.class);
        intent.putExtra("baseStr", baseStr);
        ActivityUtils.startLoginActivity((Activity) context, intent);

    }

    private LinearLayout linear_layout_parent;
    private ImageView img_code_pic;
    private String baseStr;

    @Override
    protected void initView() {
        baseStr = getIntent().getStringExtra("baseStr");
        setTitleText("");
        findViewById(R.id.relative_layout_more).setVisibility(View.GONE);
        findViewById(R.id.relative_layout_title).setBackgroundResource(R.color.transparent);
        ImageView img_left_btn = findViewById(R.id.img_left_btn);
        img_left_btn.setImageResource(R.mipmap.public_arrow_down);
        linear_layout_parent = findViewById(R.id.linear_layout_parent);
        img_code_pic = findViewById(R.id.img_code_pic);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) img_code_pic.getLayoutParams();
        params.width = UIHelper.dip2px(300);
        params.height = UIHelper.dip2px(300);
        img_code_pic.setLayoutParams(params);
        img_code_pic.setImageBitmap(ImageFindQrUtils.createQRCode(baseStr, UIHelper.dip2px(300)));
        findViewById(R.id.tv_save_pic).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Uri uri = BitmapUtils.saveBitmap(mContext, linear_layout_parent);
                Intent imageIntent = new Intent(Intent.ACTION_SEND);
                imageIntent.setType("image/*");
                imageIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(imageIntent, "分享"));

            }
        });
    }

    @Override
    public void onBackPressed() {
        ActivityUtils.finishLoginActivity(mContext);

    }

    @Override
    public void back(View view) {
        ActivityUtils.finishLoginActivity(mContext);

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_rqcode;
    }
}
