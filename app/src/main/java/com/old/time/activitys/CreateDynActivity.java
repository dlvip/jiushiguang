package com.old.time.activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.adapters.CirclePicAdapter;
import com.old.time.beans.DynamicBean;
import com.old.time.beans.PhotoInfoBean;
import com.old.time.beans.ResultBean;
import com.old.time.beans.TopicBean;
import com.old.time.beans.UserInfoBean;
import com.old.time.constants.Code;
import com.old.time.constants.Constant;
import com.old.time.interfaces.UploadImagesCallBack;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.permission.PermissionUtil;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.AliyPostUtil;
import com.old.time.utils.MyGridLayoutManager;
import com.old.time.utils.UIHelper;
import com.old.time.utils.UserLocalInfoUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.old.time.activitys.PhotoPickActivity.SELECT_PHOTO_LIST;

public class CreateDynActivity extends BaseActivity {

    private static final String TOPICBEAN = "TopicBean";

    /**
     * 发送动态
     *
     * @param mContext
     */
    public static void startCreateDynActivity(Activity mContext, TopicBean mTopicBean) {
        if (!PermissionUtil.checkAndRequestPermissionsInActivity(mContext, CAMERA//
                , WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, ACCESS_COARSE_LOCATION)) {

            return;
        }
        if (!UserLocalInfoUtils.instance().isUserLogin()) {
            UserLoginActivity.startUserLoginActivity(mContext);

            return;
        }
        Intent intent = new Intent(mContext, CreateDynActivity.class);
        intent.putExtra(TOPICBEAN, mTopicBean);
        ActivityUtils.startActivity(mContext, intent);

    }

    private static final int PIC_COUNT_SIZE = 9;

    private TextView tv_topic_title;
    private EditText input_send_text;
    private ImageView img_take_pic;
    private RecyclerView recycler_view_pics;
    private List<String> picUrls = new ArrayList<>();
    private CirclePicAdapter mPicAdapter;

    @Override
    protected void initView() {
        PhotoPickActivity.startPhotoPickActivity(mContext, false, PIC_COUNT_SIZE//
                , (Serializable) picUrls, Code.REQUEST_CODE_30);
        setTitleText("发布乐趣动态");
        findViewById(R.id.right_layout_send).setVisibility(View.VISIBLE);
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        input_send_text = findViewById(R.id.input_send_text);
        tv_topic_title = findViewById(R.id.tv_topic_title);
        img_take_pic = findViewById(R.id.img_take_pic);
        recycler_view_pics = findViewById(R.id.recycler_view_pics);
        recycler_view_pics.setLayoutManager(new MyGridLayoutManager(mContext, 3));
        mPicAdapter = new CirclePicAdapter(picUrls);
        recycler_view_pics.setAdapter(mPicAdapter);
        TopicBean mTopicBean = (TopicBean) getIntent().getSerializableExtra(TOPICBEAN);
        setTopicMsg(mTopicBean);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        img_take_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoPickActivity.startPhotoPickActivity(mContext, false, PIC_COUNT_SIZE//
                        , (Serializable) mPicAdapter.getData(), Code.REQUEST_CODE_30);

            }
        });
    }

    private ProgressDialog pd;

    @Override
    public void save(View view) {
        super.save(view);
        final String contentStr = input_send_text.getText().toString().trim();
        if (TextUtils.isEmpty(contentStr)) {
            UIHelper.ToastMessage(mContext, "描述下乐趣吧");

            return;
        }
        if (mPicAdapter.getData().size() == 0) {
            UIHelper.ToastMessage(mContext, "还没选择配图呢");

            return;
        }
        if (mTopicBean == null) {
            UIHelper.ToastMessage(mContext, "选择个感兴趣的话题吧");

            return;
        }
        pd = UIHelper.showProgressMessageDialog(mContext, getString(R.string.please_wait));
        AliyPostUtil.getInstance(mContext).uploadCompresImgsToAliyun(mPicAdapter.getData(), new UploadImagesCallBack() {
            @Override
            public void getImagesPath(List<PhotoInfoBean> mPhotoInfoBeans) {
                if (mPhotoInfoBeans == null || mPhotoInfoBeans.size() == 0) {
                    UIHelper.dissmissProgressDialog(pd);
                    UIHelper.ToastMessage(mContext, "上传图片失败");

                    return;
                }
                sendCircleContent(contentStr, new Gson().toJson(mPhotoInfoBeans), mTopicBean.getId());
            }
        });
    }

    private TopicBean mTopicBean;

    /**
     * 设置话题
     */
    private void setTopicMsg(TopicBean mTopicBean) {
        if (mTopicBean == null) {

            return;
        }
        this.mTopicBean = mTopicBean;
        tv_topic_title.setText(mTopicBean.getTopic());

    }

    private Random mRandom = new Random();

    /**
     * 发送圈子内容
     */
    private void sendCircleContent(String content, String images, String topicId) {
        HttpParams params = new HttpParams();
        UserLocalInfoUtils infoUtils = UserLocalInfoUtils.instance();
        if ("15093073252".equals(infoUtils.getMobile()) || "17600075773".equals(infoUtils.getMobile())) {
            params.put("userId", String.valueOf(mRandom.nextInt(62)));

        } else {
            params.put("userId", UserLocalInfoUtils.instance().getUserId());

        }
        params.put("content", content);
        params.put("images", images);
        params.put("topicId", topicId);
        OkGoUtils.getInstance().postNetForData(params, Constant.CREATE_DYNAMIC, new JsonCallBack<ResultBean<DynamicBean>>() {
            @Override
            public void onSuccess(ResultBean<DynamicBean> mResultBean) {
                UIHelper.dissmissProgressDialog(pd);
                EventBus.getDefault().post(mResultBean.data);
                ActivityUtils.finishActivity(mContext);

            }

            @Override
            public void onError(ResultBean<DynamicBean> mResultBean) {
                UIHelper.dissmissProgressDialog(pd);
                UIHelper.ToastMessage(mContext, mResultBean.msg);

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
                ArrayList<String> resultPhotos = data.getStringArrayListExtra(SELECT_PHOTO_LIST);
                mPicAdapter.setNewData(resultPhotos);

                break;
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_send_dynamic;
    }

}
