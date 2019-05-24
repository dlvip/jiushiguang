package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.beans.VideoBean;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.BitmapUtils;

public class ShareVDetailActivity extends BaseActivity {

    private static final String VIDEO_BEAN = "VideoBean";

    /**
     * 分享视频详情
     *
     * @param context
     * @param videoBean
     */
    public static void startShareVDetailActivity(Context context, VideoBean videoBean) {
        if (videoBean == null) {

            return;
        }
        Intent intent = new Intent(context, ShareVDetailActivity.class);
        intent.putExtra(VIDEO_BEAN, videoBean);
        ActivityUtils.startActivity((Activity) context, intent);

    }

    private ImageView img_video_pic;
    private TextView tv_video_name;
    private TextView tv_share_title;
    private CardView card_view_parent;

    private VideoBean videoBean;

    @Override
    protected void initView() {
        videoBean = (VideoBean) getIntent().getSerializableExtra(VIDEO_BEAN);
        card_view_parent = findViewById(R.id.card_view_parent);
        img_video_pic = findViewById(R.id.img_video_pic);
        tv_video_name = findViewById(R.id.tv_video_name);
        tv_share_title = findViewById(R.id.tv_share_title);

        setRightMoreImg(R.mipmap.icon_share);
        tv_video_name.setText(videoBean.getName());
        tv_share_title.setText(String.valueOf("【觅邮】邀你一起来看\"" + videoBean.getName() + "\""));
        GlideUtils.getInstance().setImageView(mContext, videoBean.getPic(), img_video_pic);

    }

    @Override
    public void more(View view) {
        super.more(view);
        Uri uri = BitmapUtils.saveBitmap(card_view_parent);
        Intent imageIntent = new Intent(Intent.ACTION_SEND);
        imageIntent.setType("image/*");
        imageIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(imageIntent, "分享"));

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_video_d_share;
    }
}
