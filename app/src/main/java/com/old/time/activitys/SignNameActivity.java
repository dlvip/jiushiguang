package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.old.time.R;
import com.old.time.beans.PhotoInfoBean;
import com.old.time.constants.Code;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.interfaces.UploadImagesCallBack;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.AliyPostUtil;
import com.old.time.utils.FileUtils;
import com.old.time.utils.PictureUtil;
import com.old.time.utils.UIHelper;

import java.util.Collections;
import java.util.List;

public class SignNameActivity extends BaseActivity {

    /**
     * 上传签名
     *
     * @param context
     */
    public static void startSignNameActivity(Context context) {
        Intent intent = new Intent(context, SignNameActivity.class);
        ActivityUtils.startActivity((Activity) context, intent);


    }

    private RelativeLayout relative_layout_select_pic, relative_layout_right;
    private ImageView img_select_pic;
    private EditText edt_sign_content;
    private String outputPath;

    @Override
    protected void initView() {
        setTitleText("上传打卡");
        findViewById(R.id.relative_layout_more).setVisibility(View.GONE);
        relative_layout_right = findViewById(R.id.relative_layout_right);
        relative_layout_right.setVisibility(View.VISIBLE);

        relative_layout_select_pic = findViewById(R.id.relative_layout_select_pic);
        img_select_pic = findViewById(R.id.img_select_pic);
        edt_sign_content = findViewById(R.id.edt_sign_content);
        relative_layout_select_pic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PhotoPickActivity.startPhotoPickActivity(mContext, true, Code.REQUEST_CODE_30);

            }
        });
        relative_layout_right.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                uploadContent();

            }
        });
    }

    /**
     * 上传内容
     */
    private void uploadContent() {
        if (TextUtils.isEmpty(outputPath)) {
            UIHelper.ToastMessage(mContext, "请选择封面");

            return;
        }
        String signStr = edt_sign_content.getText().toString().trim();
        if (TextUtils.isEmpty(signStr)) {
            UIHelper.ToastMessage(mContext, "你的一句动人话语，可以温暖到别人呢");

            return;
        }
        AliyPostUtil.getInstance(mContext).uploadCompresImgsToAliyun(Collections.singletonList(outputPath), new UploadImagesCallBack() {
            @Override
            public void getImagesPath(List<PhotoInfoBean> onlineFileName) {


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Code.REQUEST_CODE_30:
                List<String> pathStrs = data.getStringArrayListExtra(PhotoPickActivity.SELECT_PHOTO_LIST);
                if (pathStrs == null || pathStrs.size() == 0 || TextUtils.isEmpty(pathStrs.get(0))) {

                    return;
                }
                Uri uri = Uri.fromFile(FileUtils.createPicturePath(System.currentTimeMillis() + ""));
                PictureUtil.cropPhotoRetrunBitmap3(this, pathStrs.get(0), uri, new int[]{1000, 618}, Code.REQUEST_CODE_40);

                break;
            case Code.REQUEST_CODE_40:
                outputPath = data.getStringExtra("outputPath");
                if (TextUtils.isEmpty(outputPath)) {

                    return;
                }
                GlideUtils.getInstance().setImageView(mContext, outputPath, img_select_pic);

                break;
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_sign_name;
    }
}
