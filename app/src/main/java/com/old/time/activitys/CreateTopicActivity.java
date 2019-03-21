package com.old.time.activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.beans.PhotoInfoBean;
import com.old.time.beans.ResultBean;
import com.old.time.beans.TopicBean;
import com.old.time.constants.Code;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.interfaces.UploadImagesCallBack;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.AliyPostUtil;
import com.old.time.utils.DebugLog;
import com.old.time.utils.FileUtils;
import com.old.time.utils.PictureUtil;
import com.old.time.utils.UIHelper;
import com.old.time.utils.UserLocalInfoUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;

public class CreateTopicActivity extends BaseActivity {

    /**
     * 创建话题
     *
     * @param activity
     */
    public static void startCreateTalkActivity(Activity activity) {
        if (!UserLocalInfoUtils.instance().isUserLogin()) {
            UserLoginActivity.startUserLoginActivity(activity);

            return;
        }
        Intent intent = new Intent(activity, CreateTopicActivity.class);
        ActivityUtils.startActivity(activity, intent);

    }

    private ImageView img_select_pic;
    private EditText edt_topic;

    @Override
    protected void initView() {
        setTitleText("创建话题");
        img_select_pic = findViewById(R.id.img_select_pic);
        edt_topic = findViewById(R.id.edt_topic);
        findViewById(R.id.right_layout_send).setVisibility(View.VISIBLE);
        findViewById(R.id.relative_layout_select_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPickActivity.startPhotoPickActivity(mContext, true, Code.REQUEST_CODE_30);

            }
        });
    }

    private String outputPath;

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
                PictureUtil.cropPhotoRetrunBitmap3(this, pathStrs.get(0), uri, new int[]{5, 3}, Code.REQUEST_CODE_40);

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
    public void save(View view) {
        super.save(view);
        if (TextUtils.isEmpty(outputPath)) {
            UIHelper.ToastMessage(mContext, "给话题配图");

            return;
        }
        final String topicDetail = edt_topic.getText().toString().trim();
        if (TextUtils.isEmpty(topicDetail)) {
            UIHelper.ToastMessage(mContext, "话题名称");

            return;
        }
        pd = UIHelper.showProgressMessageDialog(mContext, getString(R.string.please_wait));
        AliyPostUtil.getInstance(mContext).uploadCompresImgsToAliyun(Collections.singletonList(outputPath), new UploadImagesCallBack() {
            @Override
            public void getImagesPath(List<PhotoInfoBean> mPhotoInfoBeans) {
                if (mPhotoInfoBeans == null || mPhotoInfoBeans.size() == 0) {
                    UIHelper.dissmissProgressDialog(pd);
                    UIHelper.ToastMessage(mContext, "上传图片失败");

                    return;
                }
                createTopic(topicDetail, mPhotoInfoBeans.get(0).picKey);
            }
        });
    }

    private ProgressDialog pd;

    /**
     * 创建话题
     */
    private void createTopic(String topicStr, String pic) {
        HttpParams params = new HttpParams();
        params.put("userId", UserLocalInfoUtils.instance().getUserId());
        params.put("topic", topicStr);
        params.put("pic", pic);
        OkGoUtils.getInstance().postNetForData(params, Constant.INSERT_TOPIC, new JsonCallBack<ResultBean<TopicBean>>() {
            @Override
            public void onSuccess(ResultBean<TopicBean> mResultBean) {
                UIHelper.dissmissProgressDialog(pd);
                EventBus.getDefault().post(mResultBean.data);
                ActivityUtils.finishActivity(mContext);

            }

            @Override
            public void onError(ResultBean<TopicBean> mResultBean) {
                UIHelper.dissmissProgressDialog(pd);
                UIHelper.ToastMessage(mContext, mResultBean.msg);

            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_create_topic;
    }
}
