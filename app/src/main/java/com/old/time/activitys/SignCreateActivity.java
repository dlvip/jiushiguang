package com.old.time.activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.beans.PhotoInfoBean;
import com.old.time.beans.ResultBean;
import com.old.time.beans.SignNameEntity;
import com.old.time.constants.Code;
import com.old.time.constants.Constant;
import com.old.time.dialogs.DialogPromptCentre;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.interfaces.OnClickViewCallBack;
import com.old.time.interfaces.UploadImagesCallBack;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.AliyPostUtil;
import com.old.time.utils.FileUtils;
import com.old.time.utils.PictureUtil;
import com.old.time.utils.UIHelper;
import com.old.time.utils.UserLocalInfoUtils;

import java.util.Collections;
import java.util.List;

public class SignCreateActivity extends BaseActivity {

    /**
     * 上传签名
     *
     * @param context
     */
    public static void startSignCreateActivity(Context context) {
        Intent intent = new Intent(context, SignCreateActivity.class);
        ActivityUtils.startActivity((Activity) context, intent);


    }

    private RelativeLayout relative_layout_select_pic, relative_layout_right;
    private ImageView img_select_pic;
    private EditText edt_sign_content;
    private String outputPath;
    private String signStr;

    @Override
    protected void initView() {
        setTitleText("创建书签");
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
        edt_sign_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    signStr = "";

                } else {
                    signStr = String.valueOf(s);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
    }

    private DialogPromptCentre dialogPromptCentre;

    private void showFinishDialog() {
        if (dialogPromptCentre == null) {
            dialogPromptCentre = new DialogPromptCentre(mContext, new OnClickViewCallBack() {
                @Override
                public void onClickTrueView() {
                    ActivityUtils.finishActivity(mContext);

                }

                @Override
                public void onClickCancelView() {
                    dialogPromptCentre.dismiss();

                }
            });
        }
        dialogPromptCentre.showDialog("内容排版很优美，确定要放弃吗？");

    }

    private ProgressDialog pd;

    /**
     * 上传内容
     */
    private void uploadContent() {
        if (TextUtils.isEmpty(outputPath)) {
            UIHelper.ToastMessage(mContext, "请选择封面");

            return;
        }
        if (TextUtils.isEmpty(signStr)) {
            UIHelper.ToastMessage(mContext, "你的一句动人话语，可以温暖到别人呢");

            return;
        }
        pd = UIHelper.showProgressMessageDialog(mContext, getString(R.string.please_wait));
        AliyPostUtil.getInstance(mContext).uploadCompresImgsToAliyun(Collections.singletonList(outputPath), new UploadImagesCallBack() {
            @Override
            public void getImagesPath(List<PhotoInfoBean> onlineFileName) {
                if (onlineFileName == null || onlineFileName.size() == 0) {
                    UIHelper.dissmissProgressDialog(pd);

                    return;
                }
                uploadDateForLine(onlineFileName.get(0).picKey);

            }
        });
    }

    /**
     * 创建
     */
    private void uploadDateForLine(String signPic) {
        HttpParams params = new HttpParams();
        params.put("userId", UserLocalInfoUtils.instance().getUserId());
        params.put("picUrl", signPic);
        params.put("content", signStr);
        OkGoUtils.getInstance().postNetForData(params, Constant.CREAT_SIGN_NAME, new JsonCallBack<ResultBean<SignNameEntity>>() {
            @Override
            public void onSuccess(ResultBean<SignNameEntity> mResultBean) {
                UIHelper.dissmissProgressDialog(pd);

            }

            @Override
            public void onError(ResultBean<SignNameEntity> mResultBean) {
                UIHelper.dissmissProgressDialog(pd);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {

            return;
        }
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
        return R.layout.activity_sign_create;
    }

    @Override
    public void onBackPressed() {
        if (!TextUtils.isEmpty(outputPath) || !TextUtils.isEmpty(signStr)) {
            showFinishDialog();

            return;
        }
        super.onBackPressed();
    }

    @Override
    public void back(View view) {
        if (!TextUtils.isEmpty(outputPath) || !TextUtils.isEmpty(signStr)) {
            showFinishDialog();

            return;
        }
        super.back(view);
    }
}
