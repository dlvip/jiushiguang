package com.old.time.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.beans.GoodsBean;
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

public class CreateGoodsActivity extends BaseActivity {

    @Override
    protected void initView() {
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.right_layout_send).setVisibility(View.VISIBLE);
        img_goods_pic = findViewById(R.id.img_goods_pic);
        img_goods_pic.setOnClickListener(this);
        tv_goods_title = findViewById(R.id.tv_goods_title);
        tv_goods_price = findViewById(R.id.tv_goods_price);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.img_goods_pic:
                PhotoPickActivity.startPhotoPickActivity(mContext, true, PIC_COUNT_SIZE, Code.REQUEST_CODE_30);

                break;
        }
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
                PictureUtil.cropPhotoRetrunBitmap3(this, pathStrs.get(0), uri, new int[]{1, 1}, Code.REQUEST_CODE_40);

                break;
            case Code.REQUEST_CODE_40:
                String outputPath = data.getStringExtra("outputPath");
                if (TextUtils.isEmpty(outputPath)) {

                    return;
                }
                GlideUtils.getInstance().setRoundImageView(mContext, outputPath, img_goods_pic);
                picPaths.clear();
                picPaths.add(outputPath);

                break;
        }
    }

    private ProgressDialog pd;

    /**
     * 上传图片到阿里云
     *
     * @param picPaths
     */
    private void sendAliyunPic(List<String> picPaths) {
        if (picPaths == null || picPaths.size() == 0) {
            UIHelper.ToastMessage(mContext, "至少得选张图片吧");

            return;
        }
        pd = UIHelper.showProgressMessageDialog(mContext, getString(R.string.please_wait));
        AliyPostUtil.getInstance(mContext).uploadCompresImgsToAliyun(picPaths, new UploadImagesCallBack() {
            @Override
            public void getImagesPath(List<PhotoInfoBean> onlineFileName) {
                if (onlineFileName == null || onlineFileName.size() == 0) {
                    UIHelper.dissmissProgressDialog(pd);
                    UIHelper.ToastMessage(mContext, "上传失败，请重试。");

                    return;
                }
                setDataForLine(onlineFileName.get(0).picKey);
            }
        });
    }

    private int PIC_COUNT_SIZE = 1;
    private ImageView img_goods_pic;
    private EditText tv_goods_title, tv_goods_price;
    private String edtGoodsTitleStr, edtGoodsPriceStr;

    @Override
    public void save(View view) {
        super.save(view);
        sendAliyunPic(picPaths);

    }

    /**
     * 提交数据
     *
     * @param picKey
     */
    private void setDataForLine(String picKey) {
        edtGoodsTitleStr = tv_goods_title.getText().toString().trim();
        edtGoodsPriceStr = tv_goods_price.getText().toString().trim();
        HttpParams params = new HttpParams();
        params.put("picKey", picKey);
        params.put("title", edtGoodsTitleStr);
        params.put("price", edtGoodsPriceStr);
        OkGoUtils.getInstance().postNetForData(params, Constant.INSERT_GOODS_INFO, new JsonCallBack<ResultBean<GoodsBean>>() {
            @Override
            public void onSuccess(ResultBean<GoodsBean> mResultBean) {
                UIHelper.dissmissProgressDialog(pd);

            }

            @Override
            public void onError(ResultBean<GoodsBean> mResultBean) {
                UIHelper.dissmissProgressDialog(pd);
                UIHelper.ToastMessage(mContext, "上传失败，请重试。");

            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_create_goods;
    }
}
