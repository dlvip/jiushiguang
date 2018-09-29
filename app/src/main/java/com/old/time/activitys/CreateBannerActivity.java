package com.old.time.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.beans.BannerBean;
import com.old.time.beans.PhotoInfoBean;
import com.old.time.beans.ResultBean;
import com.old.time.constants.Code;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.interfaces.UploadImagesCallBack;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.AliyPostUtil;
import com.old.time.utils.FileUtils;
import com.old.time.utils.PictureUtil;
import com.old.time.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;

public class CreateBannerActivity extends BaseActivity {

    private ImageView img_select_pic;
    private TextView edt_banner_title, edt_banner_url;

    @Override
    protected void initView() {
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.right_layout_send).setVisibility(View.VISIBLE);
        img_select_pic = findViewById(R.id.img_select_pic);
        findViewById(R.id.relative_layout_select_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPickActivity.startPhotoPickActivity(mContext, true, 1, Code.REQUEST_CODE_30);

            }
        });
        edt_banner_title = findViewById(R.id.edt_banner_title);
        edt_banner_url = findViewById(R.id.edt_banner_url);
    }

    private List<String> picPaths = new ArrayList<>();

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
                PictureUtil.cropPhotoRetrunBitmap3(this, pathStrs.get(0), uri, new int[]{25, 12}, Code.REQUEST_CODE_40);

                break;
            case Code.REQUEST_CODE_40:
                String outputPath = data.getStringExtra("outputPath");
                if (TextUtils.isEmpty(outputPath)) {

                    return;
                }
                GlideUtils.getInstance().setImageView(mContext, outputPath, img_select_pic);
                picPaths.clear();
                picPaths.add(outputPath);
                sendAliyunPic(picPaths);

                break;
        }
    }

    private ProgressDialog pd;
    private String picKey;

    /**
     * 上传图片到阿里云
     *
     * @param picPaths
     */
    private void sendAliyunPic(List<String> picPaths) {
        pd = UIHelper.showProgressMessageDialog(mContext, getString(R.string.please_wait));
        AliyPostUtil.getInstance(mContext).uploadCompresImgsToAliyun(picPaths, new UploadImagesCallBack() {
            @Override
            public void getImagesPath(List<PhotoInfoBean> onlineFileName) {
                UIHelper.dissmissProgressDialog(pd);
                if (onlineFileName == null || onlineFileName.size() == 0) {
                    UIHelper.ToastMessage(mContext, "上传图片失败");

                    return;
                }
                picKey = onlineFileName.get(0).picKey;

            }
        });
    }

    @Override
    public void save(View view) {
        super.save(view);
        String titleStr = edt_banner_title.getText().toString().trim();
        if (TextUtils.isEmpty(titleStr)) {

            return;
        }
        String urlStr = edt_banner_url.getText().toString().trim();
        if (TextUtils.isEmpty(urlStr)) {

            return;
        }
        HttpParams params = new HttpParams();
        params.put("picUrl", picKey);
        params.put("title", titleStr);
        params.put("detailUrl", urlStr);
        OkGoUtils.getInstance().postNetForData(params, Constant.INSERT_HOME_BANNERS, new JsonCallBack<ResultBean<BannerBean>>() {
            @Override
            public void onSuccess(ResultBean<BannerBean> mResultBean) {
                UIHelper.ToastMessage(mContext, mResultBean.msg);

            }

            @Override
            public void onError(ResultBean<BannerBean> mResultBean) {
                UIHelper.ToastMessage(mContext, mResultBean.msg);

            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_create_banner;
    }
}
