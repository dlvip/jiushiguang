package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;

import com.old.time.R;
import com.old.time.adapters.DynamicAdapter;
import com.old.time.beans.DynamicBean;
import com.old.time.beans.PhotoInfoBean;
import com.old.time.constants.Code;
import com.old.time.constants.Constant;
import com.old.time.interfaces.UploadImagesCallBack;
import com.old.time.utils.AliyPostUtil;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.EasyPhotos;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.ScreenTools;
import com.old.time.utils.UIHelper;
import com.old.time.views.SuspensionPopupWindow;

import java.util.ArrayList;
import java.util.List;

public class DynamicSActivity extends BaseSActivity {

    private List<DynamicBean> mDynamicBeans = new ArrayList<>();
    private DynamicAdapter mAdapter;

    /**
     * 旧时光圈子
     *
     * @param mContext
     */
    public static void startDynamicActivity(Activity mContext) {
        Intent intent = new Intent(mContext, DynamicSActivity.class);
        ActivityUtils.startActivity(mContext, intent);

    }

    @Override
    protected void initView() {
        super.initView();
        ScreenTools mScreenTools = ScreenTools.instance(this);
        W = mScreenTools.getScreenWidth();
        H = mScreenTools.getScreenHeight();
        mDynamicBeans.clear();
        mAdapter = new DynamicAdapter(mDynamicBeans);
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext));
        mRecyclerView.setAdapter(mAdapter);
        mDynamicBeans.clear();
        for (int i = 0; i < 20; i++) {
            DynamicBean mDynamicBean = new DynamicBean();
            mDynamicBean.setContent(getString(R.string.circle_content));
            mDynamicBean.setCreateTime("2016/12/21 12:32");
            List<PhotoInfoBean> photoInfoBeans = new ArrayList<>();
            photoInfoBeans.clear();
            for (int j = 0; j < (i > 9 ? 9 : i); j++) {
                PhotoInfoBean mPhotoInfoBean = new PhotoInfoBean();
                mPhotoInfoBean.picKey = Constant.PHOTO_PIC_URL;
                mPhotoInfoBean.height = 1080;
                mPhotoInfoBean.with = 1920;
                photoInfoBeans.add(mPhotoInfoBean);

            }

            mDynamicBean.setContentImages(photoInfoBeans);
            mDynamicBeans.add(mDynamicBean);
        }
        mAdapter.setNewData(mDynamicBeans);


        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                showSuspensionPopupWindow();

            }
        });
    }

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        mSwipeRefreshLayout.setRefreshing(false);

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
                    CreateDynActivity.startCreateDynActivity(mContext, Code.REQUEST_CODE_30);

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
                String contentStr = data.getStringExtra(CreateDynActivity.CONTENT_STR);
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


                }
            }
        });
    }


    /**
     * 发送圈子内容
     */
    private void sendCircleContent(String content, String mPhotoInfoBeanStr) {


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSuspensionPopupWindow != null) {
            mSuspensionPopupWindow.dismiss();

        }
    }
}
