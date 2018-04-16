package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.old.time.R;
import com.old.time.adapters.DynamicAdapter;
import com.old.time.beans.DynamicBean;
import com.old.time.beans.PhotoInfoBean;
import com.old.time.constants.Code;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.interfaces.UploadImagesCallBack;
import com.old.time.okhttps.Http;
import com.old.time.okhttps.exception.ApiException;
import com.old.time.okhttps.subscriber.CommonSubscriber;
import com.old.time.okhttps.transformer.CommonTransformer;
import com.old.time.utils.AliyPostUtil;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.DebugLog;
import com.old.time.utils.EasyPhotos;
import com.old.time.utils.MapParams;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.ScreenTools;
import com.old.time.utils.UIHelper;
import com.old.time.utils.UserLocalInfoUtils;
import com.old.time.views.SuspensionPopupWindow;

import java.util.ArrayList;
import java.util.List;

public class DynamicActivity extends SBaseActivity {

    private List<DynamicBean> mDynamicBeans = new ArrayList<>();
    private DynamicAdapter mAdapter;
    private String userid;

    public static String USER_ID = "userId";

    /**
     * 旧时光圈子
     *
     * @param mContext
     * @param userId
     */
    public static void startDynamicActivity(Activity mContext, String userId) {
        if (!UserLocalInfoUtils.instance().isUserLogin()) {
            UserLoginActivity.startUserLoginActivity(mContext);

            return;
        }
        if (TextUtils.isEmpty(userId)) {
            userId = UserLocalInfoUtils.instance().getUserId();

        }
        Intent intent = new Intent(mContext, DynamicActivity.class);
        intent.putExtra(USER_ID, userId);
        ActivityUtils.startActivity(mContext, intent);

    }

    @Override
    protected void initView() {
        userid = getIntent().getStringExtra(USER_ID);
        super.initView();
        ScreenTools mScreenTools = ScreenTools.instance(this);
        W = mScreenTools.getScreenWidth();
        H = mScreenTools.getScreenHeight();
        mDynamicBeans.clear();
        mAdapter = new DynamicAdapter(mDynamicBeans);
        View headerView = View.inflate(mContext, R.layout.header_circle, null);
        ImageView img_circle_header_pic = headerView.findViewById(R.id.img_circle_header_pic);
        GlideUtils.getInstance().setImageView(mContext, Constant.PHOTO_PIC_URL, img_circle_header_pic);
        mAdapter.addHeaderView(headerView);
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext));
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                showSuspensionPopupWindow();

            }
        });
    }

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        MapParams params = new MapParams();
        params.putParams("userid", userid);
        params.putParams("current_userid", UserLocalInfoUtils.instance().getUserId());
        Http.getHttpService().getListContent(Constant.GET_LIST_CONTENT, params.getParamString()).compose(new CommonTransformer<List<DynamicBean>>()).subscribe(new CommonSubscriber<List<DynamicBean>>(mContext) {
            @Override
            public void onNext(List<DynamicBean> dynamicBeans) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (isRefresh) {
                    mDynamicBeans.clear();
                    mAdapter.setNewData(mDynamicBeans);

                }
                mAdapter.addData(dynamicBeans);
            }

            @Override
            protected void onError(ApiException e) {
                super.onError(e);
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    private int W, H;
    private int showX, showY;
    private SuspensionPopupWindow mSuspensionPopupWindow;

    /**
     * 发送内容入口
     */
    private void showSuspensionPopupWindow() {
        if (mSuspensionPopupWindow == null) {
            showX = W / 2 - UIHelper.dip2px(40);
            showY = H - UIHelper.dip2px(80);
            mSuspensionPopupWindow = new SuspensionPopupWindow(this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SendDynamiceActivity.startSendCircleActivity(mContext, Code.REQUEST_CODE_30);

                }
            });
        }
        mSuspensionPopupWindow.showAtLocation("+", getWindow().getDecorView(), Gravity.TOP, showX, showY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {

            return;
        }
        switch (requestCode) {
            case Code.REQUEST_CODE_30:
                List<String> picUrls = data.getStringArrayListExtra(EasyPhotos.RESULT_PHOTOS);
                String contentStr = data.getStringExtra(SendDynamiceActivity.CONTENT_STR);
                sendPicToAliYun(contentStr, picUrls);

                break;
        }
    }

    /**
     * 上传图片到阿里云
     *
     * @param picUrls
     */
    private void sendPicToAliYun(final String conStr, List<String> picUrls) {
        if (picUrls == null || picUrls.size() == 0) {
            sendCircleContent(conStr, "");

            return;
        }
        AliyPostUtil.getInstance(mContext).uploadCompresImgsToAliyun(picUrls, new UploadImagesCallBack() {
            @Override
            public void getImagesPath(List<PhotoInfoBean> mPhotoInfoBeans) {
                if (mPhotoInfoBeans == null || mPhotoInfoBeans.size() == 0) {

                    return;
                }
                Gson gson = new Gson();
                String ssonStr = gson.toJson(mPhotoInfoBeans);
                sendCircleContent(conStr, ssonStr);

                DebugLog.e("onlineFileName:::", mPhotoInfoBeans.toString());
            }
        });
    }


    /**
     * 发送圈子内容
     */
    private void sendCircleContent(String content, String mPhotoInfoBeanStr) {
        MapParams params = new MapParams();
        params.putParams("userid", UserLocalInfoUtils.instance().getUserId());
        if (!TextUtils.isEmpty(content)) {
            params.putParams("content", content);

        }
        if (!TextUtils.isEmpty(mPhotoInfoBeanStr)) {
            params.putParams("conetentimages", mPhotoInfoBeanStr);

        }
        Http.getHttpService().sendContent(Constant.SEND_CONTENT, params.getParamString()).compose(new CommonTransformer<String>()).subscribe(new CommonSubscriber<String>(mContext) {
            @Override
            public void onNext(String string) {

            }

            @Override
            protected void onError(ApiException e) {
                super.onError(e);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSuspensionPopupWindow != null) {
            mSuspensionPopupWindow.dismiss();

        }
    }
}
