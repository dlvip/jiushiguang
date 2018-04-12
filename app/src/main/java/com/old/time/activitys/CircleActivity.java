package com.old.time.activitys;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.old.time.R;
import com.old.time.adapters.CircleAdapter;
import com.old.time.beans.LoginBean;
import com.old.time.constants.Code;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.okhttps.Http;
import com.old.time.okhttps.exception.ApiException;
import com.old.time.okhttps.subscriber.CommonSubscriber;
import com.old.time.okhttps.transformer.CommonTransformer;
import com.old.time.utils.MapParams;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.ScreenTools;
import com.old.time.utils.UIHelper;
import com.old.time.views.SuspensionPopupWindow;

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
        params.putParams("userid",Constant.USER_ID);
        params.putParams("current_userid",Constant.USER_ID);
        Http.getHttpService().login(Constant.GET_LIST_CONTENT, params.getParamString()).compose(new CommonTransformer<LoginBean>()).subscribe(new CommonSubscriber<LoginBean>(mContext) {
            @Override
            public void onNext(LoginBean loginBean) {
                mSwipeRefreshLayout.setRefreshing(false);

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
    protected void onDestroy() {
        super.onDestroy();
        if (mSuspensionPopupWindow != null) {
            mSuspensionPopupWindow.dismiss();

        }
    }
}
