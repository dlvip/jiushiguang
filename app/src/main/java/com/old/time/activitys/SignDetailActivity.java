package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.old.time.BuildConfig;
import com.old.time.R;
import com.old.time.beans.SignNameEntity;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.BitmapUtils;

public class SignDetailActivity extends BaseActivity {

    private static final String SIGN_NAME_ENTITY = "mSignNameEntity";

    public static void startSignDetailActivity(Context context, SignNameEntity mSignNameEntity) {
        if (mSignNameEntity == null) {

            return;
        }
        Intent intent = new Intent(context, SignDetailActivity.class);
        intent.putExtra(SIGN_NAME_ENTITY, mSignNameEntity);
        ActivityUtils.startPicActivity((Activity) context, intent);

    }

    private TextView tv_book_name, tv_sign_content, tv_app_name;
    private ConstraintLayout constraint_layout_parent;
    private ImageView img_card_pic;

    private SignNameEntity mSignNameEntity;

    @Override
    protected void initView() {
        mSignNameEntity = (SignNameEntity) getIntent().getSerializableExtra(SIGN_NAME_ENTITY);
        constraint_layout_parent = findViewById(R.id.constraint_layout_parent);
        img_card_pic = findViewById(R.id.img_card_pic);
        tv_book_name = findViewById(R.id.tv_book_name);
        tv_sign_content = findViewById(R.id.tv_sign_content);
        tv_app_name = findViewById(R.id.tv_app_name);
        if (mSignNameEntity.getBookEntity() != null) {
            tv_book_name.setText(mSignNameEntity.getBookEntity().getTitle());

        }
        tv_sign_content.setText(mSignNameEntity.getContent());
        GlideUtils.getInstance().setImageView(mContext, mSignNameEntity.getPicUrl(), img_card_pic);
        tv_app_name.setText(String.valueOf(getString(R.string.app_name) + "：" + BuildConfig.VERSION_NAME));

        findViewById(R.id.tv_save_pic).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Uri uri = BitmapUtils.saveBitmap(constraint_layout_parent);
                Intent imageIntent = new Intent(Intent.ACTION_SEND);
                imageIntent.setType("image/*");
                imageIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(imageIntent, "分享"));

            }
        });
        constraint_layout_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.finishActivity(mContext);

            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_sign_detail;
    }
}
