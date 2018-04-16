package com.old.time.activitys;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.old.time.R;
import com.old.time.adapters.CircleAdapter;
import com.old.time.beans.LoginBean;
import com.old.time.beans.PhotoInfoBean;
import com.old.time.beans.UserInfoBean;
import com.old.time.constants.Code;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.interfaces.UploadImagesCallBack;
import com.old.time.okhttps.Http;
import com.old.time.okhttps.exception.ApiException;
import com.old.time.okhttps.subscriber.CommonSubscriber;
import com.old.time.okhttps.transformer.CommonTransformer;
import com.old.time.utils.AliyPostUtil;
import com.old.time.utils.DebugLog;
import com.old.time.utils.MapParams;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.ScreenTools;
import com.old.time.utils.UIHelper;
import com.old.time.utils.UserLocalInfoUtils;
import com.old.time.views.SuspensionPopupWindow;

import java.util.ArrayList;
import java.util.List;

public class CircleActivity extends SBaseActivity {

    private CircleAdapter mAdapter;

    @Override
    protected void initView() {
        super.initView();
        ScreenTools mScreenTools = ScreenTools.instance(this);
        W = mScreenTools.getScreenWidth();
        H = mScreenTools.getScreenHeight();
        mAdapter = new CircleAdapter(strings);
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
    public void getDataFromNet(boolean isRefresh) {
        MapParams params = new MapParams();
        params.putParams("userid", UserLocalInfoUtils.instance().getUserId());
        params.putParams("current_userid", UserLocalInfoUtils.instance().getUserId());
        Http.getHttpService().getListContent(Constant.GET_LIST_CONTENT, params.getParamString()).compose(new CommonTransformer<List<LoginBean>>()).subscribe(new CommonSubscriber<List<LoginBean>>(mContext) {
            @Override
            public void onNext(List<LoginBean> loginBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                sendCircleContent();

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
                    SendCircleActivity.startSendCircleActivity(mContext, Code.REQUEST_CODE_30);

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


                break;
        }
    }

    private List<PhotoInfoBean> photoInfoBeans = new ArrayList<>();

    /**
     * 上传图片到阿里云
     *
     * @param picUrls
     */
    private void sendPicToAliYun(List<String> picUrls) {
        AliyPostUtil.getInstance(mContext).uploadCompresImgsToAliyun(picUrls, new UploadImagesCallBack() {
            @Override
            public void getImagesPath(List<String> onlineFileName) {
                DebugLog.e("onlineFileName:::", onlineFileName.toString());

            }
        });
    }


    /**
     * 发送圈子内容
     */
    private void sendCircleContent() {
        photoInfoBeans.clear();
        for (int i = 0; i < 9; i++) {
            PhotoInfoBean photoInfoBean = new PhotoInfoBean();
            photoInfoBean.picKey = Constant.PHOTO_PIC_URL;
            photoInfoBean.with = 500;
            photoInfoBean.height = 500;
            photoInfoBeans.add(photoInfoBean);

        }
        Gson gson = new Gson();
        String ssonStr = gson.toJson(photoInfoBeans);
        DebugLog.e("photoInfoBeansJson:::", ssonStr);
        MapParams params = new MapParams();
        params.putParams("userid", UserLocalInfoUtils.instance().getUserId());
        params.putParams("content", getString(R.string.circle_content2));
        params.putParams("conetentimages", ssonStr);
        Http.getHttpService().sendContent(Constant.SEND_CONTENT, params.getParamString()).compose(new CommonTransformer<UserInfoBean>()).subscribe(new CommonSubscriber<UserInfoBean>(mContext) {
            @Override
            public void onNext(UserInfoBean mUserInfoBean) {
                UserLocalInfoUtils.instance().setmUserInfoBean(mUserInfoBean);
                MainActivity.startMainActivity(mContext);

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
