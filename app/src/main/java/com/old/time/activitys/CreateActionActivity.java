package com.old.time.activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.beans.PhotoInfoBean;
import com.old.time.constants.Code;
import com.old.time.dialogs.DialogChoseTime;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.interfaces.UploadImagesCallBack;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.AliyPostUtil;
import com.old.time.utils.DebugLog;
import com.old.time.utils.FileUtils;
import com.old.time.utils.PictureUtil;
import com.old.time.utils.TimeUtil;
import com.old.time.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;

public class CreateActionActivity extends BaseActivity {

    /**
     * 创建活动
     *
     * @param activity
     */
    public static void startCreateActionActivity(Activity activity) {
        Intent intent = new Intent(activity, CreateActionActivity.class);
        ActivityUtils.startActivity(activity, intent);

    }

    private int PIC_COUNT_SIZE = 1;
    private ImageView img_select_pic;
    private TextView tv_start_time, tv_end_time, tv_action_address;


    @Override
    protected void initView() {
        img_select_pic = findViewById(R.id.img_select_pic);
        tv_start_time = findViewById(R.id.tv_start_time);
        tv_end_time = findViewById(R.id.tv_end_time);
        tv_action_address = findViewById(R.id.tv_action_address);

    }

    @Override
    protected void initEvent() {
        super.initEvent();
        findViewById(R.id.relative_layout_select_pic).setOnClickListener(this);
        tv_start_time.setOnClickListener(this);
        tv_end_time.setOnClickListener(this);
        tv_action_address.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.relative_layout_select_pic:
                PhotoPickActivity.startPhotoPickActivity(mContext, true, PIC_COUNT_SIZE, Code.REQUEST_CODE_30);

                break;
            case R.id.tv_start_time:
                showTimeDialog("开始时间", 0);

                break;
            case R.id.tv_end_time:
                showTimeDialog("结束时间", 0);

                break;
            case R.id.tv_action_address:
                LocationMapActivity.startLocationMapActivity(mContext);

                break;
        }
    }

    private DialogChoseTime mDialogChoseTime;

    /**
     * 选择时间
     *
     * @param dialogTitle
     * @param type        0:开始时间  1：结束时间
     */
    private void showTimeDialog(String dialogTitle, int type) {
        if (mDialogChoseTime == null) {
            mDialogChoseTime = new DialogChoseTime(mContext, new DialogChoseTime.OnChooseTimeCallBack() {
                @Override
                public void timeCallBack(int type, String timeStr) {

                }
            });
        }
        String startTime = tv_start_time.getText().toString().trim();
        if (type == 0) {
            mDialogChoseTime.showChooseTimeDialog(type, dialogTitle, System.currentTimeMillis(), 30);

        } else if (TextUtils.isEmpty(startTime)) {
            UIHelper.ToastMessage(mContext, "请先选择开始时间");

        } else {
            long startTimeL = TimeUtil.dataToLong(startTime.replace(".", "-") + ":00");
            mDialogChoseTime.showChooseTimeDialog(type, dialogTitle, startTimeL, 1);

        }
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
                PictureUtil.cropPhotoRetrunBitmap3(this, pathStrs.get(0), uri, new int[]{2190, 1277}, Code.REQUEST_CODE_40);

                break;
            case Code.REQUEST_CODE_40:
                String outputPath = data.getStringExtra("outputPath");
                if (TextUtils.isEmpty(outputPath)) {

                    return;
                }
                GlideUtils.getInstance().setRoundImageView(mContext, outputPath, img_select_pic);
                picPaths.clear();
                picPaths.add(outputPath);
                sendAliyunPic(picPaths);

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
        pd = UIHelper.showProgressMessageDialog(mContext, getString(R.string.please_wait));
        AliyPostUtil.getInstance(mContext).uploadCompresImgsToAliyun(picPaths, new UploadImagesCallBack() {
            @Override
            public void getImagesPath(List<PhotoInfoBean> onlineFileName) {
                UIHelper.dissmissProgressDialog(pd);
                if (onlineFileName == null || onlineFileName.size() == 0) {

                    UIHelper.ToastMessage(mContext, "上传图片失败");
                    return;
                }
                String picKey = onlineFileName.get(0).picKey;
                DebugLog.d(TAG, picKey);
            }
        });
    }

    private List<String> picPaths = new ArrayList<>();

    @Override
    protected int getLayoutID() {
        return R.layout.activity_creat_action;
    }
}
