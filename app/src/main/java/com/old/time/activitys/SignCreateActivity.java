package com.old.time.activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.activity.CaptureActivity;
import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.beans.BookEntity;
import com.old.time.beans.JHBaseBean;
import com.old.time.beans.PhotoInfoBean;
import com.old.time.beans.ResultBean;
import com.old.time.beans.SignNameEntity;
import com.old.time.beans.UserInfoBean;
import com.old.time.constants.Code;
import com.old.time.constants.Constant;
import com.old.time.dialogs.DialogPromptCentre;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.interfaces.OnClickViewCallBack;
import com.old.time.interfaces.UploadImagesCallBack;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.permission.PermissionUtil;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.AliyPostUtil;
import com.old.time.utils.DataUtils;
import com.old.time.utils.FileUtils;
import com.old.time.utils.PictureUtil;
import com.old.time.utils.UIHelper;
import com.old.time.utils.UserLocalInfoUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;
import java.util.Random;

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
    private TextView tv_book_name;
    private String outputPath;
    private String signStr, bookId = "-1";

    @Override
    protected void initView() {
        setTitleText("创建书签");
        findViewById(R.id.relative_layout_more).setVisibility(View.GONE);
        relative_layout_right = findViewById(R.id.relative_layout_right);
        relative_layout_right.setVisibility(View.VISIBLE);

        relative_layout_select_pic = findViewById(R.id.relative_layout_select_pic);
        img_select_pic = findViewById(R.id.img_select_pic);
        tv_book_name = findViewById(R.id.tv_book_name);
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
        findViewById(R.id.img_open_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureUtil.captureCode(mContext);

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
            UIHelper.ToastMessage(mContext, "选择封面");

            return;
        }
        if (TextUtils.isEmpty(signStr)) {
            UIHelper.ToastMessage(mContext, "书签内容");

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
     * 平台获取图书信息
     */
    private void getJSGBookInfo(final String isbn) {
        pd = UIHelper.showProgressMessageDialog(mContext, getString(R.string.please_wait));
        HttpParams params = new HttpParams();
        params.put("isbn", isbn);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_BOOK_INFO, new JsonCallBack<ResultBean<BookEntity>>() {
            @Override
            public void onSuccess(ResultBean<BookEntity> mResultBean) {
                UIHelper.dissmissProgressDialog(pd);
                if (mResultBean == null || mResultBean.data == null) {
                    UIHelper.ToastMessage(mContext, "内容不存在");

                    return;
                }
                tv_book_name.setText(mResultBean.data.getTitle());
                bookId = mResultBean.data.getId();

            }

            @Override
            public void onError(ResultBean<BookEntity> mResultBean) {
                getBookInfo(isbn);

            }
        });
    }

    /**
     * 聚合查询图书信息
     */
    private void getBookInfo(String mIBSNCode) {
        HttpParams params = new HttpParams();
        params.put("key", Constant.BOOK_INFO_KEY);
        params.put("sub", mIBSNCode);
        OkGoUtils.getInstance().getNetForData(params, Constant.GET_JIHE_BOOK_INFO, new JsonCallBack<JHBaseBean<BookEntity>>() {
            @Override
            public void onSuccess(JHBaseBean<BookEntity> mResultBean) {
                if (mResultBean == null || mResultBean.result == null) {
                    UIHelper.dissmissProgressDialog(pd);
                    UIHelper.ToastMessage(mContext, "内容不存在");

                    return;
                }
                createBookInfo(mResultBean.result);


            }

            @Override
            public void onError(JHBaseBean<BookEntity> mResultBean) {
                UIHelper.dissmissProgressDialog(pd);
                UIHelper.ToastMessage(mContext, mResultBean.reason);

            }
        });
    }

    /**
     * 保存图书信息到自己服务器
     */
    private void createBookInfo(BookEntity mBookEntity) {
        HttpParams params = new HttpParams();
        params.put("levelNum", mBookEntity.getLevelNum());
        params.put("subtitle", mBookEntity.getSubtitle());
        params.put("author", mBookEntity.getAuthor());
        params.put("pubdate", mBookEntity.getPubdate());
        params.put("origin_title", mBookEntity.getOrigin_title());
        params.put("binding", mBookEntity.getBinding());
        params.put("pages", mBookEntity.getPages());
        params.put("images_medium", mBookEntity.getImages_medium());
        params.put("images_large", mBookEntity.getImages_large());
        params.put("publisher", mBookEntity.getPublisher());
        params.put("isbn10", mBookEntity.getIsbn10());
        params.put("isbn13", mBookEntity.getIsbn13());
        params.put("title", mBookEntity.getTitle());
        params.put("summary", mBookEntity.getSummary());
        params.put("price", mBookEntity.getPrice());
        params.put("url", "");
        OkGoUtils.getInstance().postNetForData(params, Constant.CREATE_BOOK_INFO, new JsonCallBack<ResultBean<BookEntity>>() {

            @Override
            public void onSuccess(ResultBean<BookEntity> mResultBean) {
                UIHelper.dissmissProgressDialog(pd);
                if (mResultBean == null || mResultBean.data == null) {
                    UIHelper.ToastMessage(mContext, "内容不存在");

                    return;
                }
                tv_book_name.setText(mResultBean.data.getTitle());
                bookId = mResultBean.data.getId();

            }

            @Override
            public void onError(ResultBean<BookEntity> mResultBean) {
                UIHelper.dissmissProgressDialog(pd);
                UIHelper.ToastMessage(mContext, mResultBean.msg);

            }
        });
    }

    private Random mRandom = new Random();

    /**
     * 创建书签
     */
    private void uploadDateForLine(String signPic) {
        UserLocalInfoUtils infoUtils = UserLocalInfoUtils.instance();
        HttpParams params = new HttpParams();
        if ("15093073252".equals(infoUtils.getMobile()) || "17600075773".equals(infoUtils.getMobile())) {
            params.put("userId", String.valueOf("01" + mRandom.nextInt(56)));

        } else {
            params.put("userId", UserLocalInfoUtils.instance().getUserId());

        }
        params.put("picUrl", signPic);
        params.put("content", signStr);
        params.put("bookId", bookId);
        OkGoUtils.getInstance().postNetForData(params, Constant.CREATE_SIGN_NAME, new JsonCallBack<ResultBean<SignNameEntity>>() {
            @Override
            public void onSuccess(ResultBean<SignNameEntity> mResultBean) {
                UIHelper.dissmissProgressDialog(pd);
                EventBus.getDefault().post(mResultBean.data);
                ActivityUtils.finishActivity(mContext);

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
            case CaptureActivity.REQ_CODE:
                String str = data.getStringExtra(CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN);
                UserLocalInfoUtils infoUtils = UserLocalInfoUtils.instance();
                if ("15093073252".equals(infoUtils.getMobile()) || "17600075773".equals(infoUtils.getMobile())) {
                    str = DataUtils.getSystemBookId(mRandom.nextInt(24));

                }
                if (TextUtils.isEmpty(str) || (str.length() != 10 && str.length() != 13)) {
                    UIHelper.ToastMessage(mContext, "内容不匹配");

                    return;
                }
                getJSGBookInfo(str);

                break;
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_sign_create;
    }

    @Override
    public void onBackPressed() {
        if (!TextUtils.isEmpty(outputPath) || !TextUtils.isEmpty(signStr) || !TextUtils.isEmpty(bookId)) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onPermissionResult(this, permissions, grantResults, new PermissionUtil.PermissionCallBack() {
            @Override
            public void onSuccess() {
                PictureUtil.captureCode(mContext);

            }

            @Override
            public void onShouldShow() {

            }

            @Override
            public void onFailed() {
                showDialogPermission();

            }
        });
    }
}
