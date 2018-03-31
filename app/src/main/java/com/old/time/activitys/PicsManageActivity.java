package com.old.time.activitys;

import android.support.v7.widget.GridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.old.time.R;
import com.old.time.adapters.PicsManageAdapter;
import com.old.time.beans.PicsManageBean;
import com.old.time.constants.Code;
import com.old.time.utils.MyGridLayoutManager;
import com.old.time.utils.ScreenTools;
import com.old.time.utils.UIHelper;
import com.old.time.views.SuspensionPopupWindow;

import java.util.ArrayList;
import java.util.List;

public class PicsManageActivity extends CBaseActivity {

    private PicsManageAdapter mAdapter;
    private List<PicsManageBean> mPicsManageBeans = new ArrayList<>();

    @Override
    protected void initView() {
        super.initView();
        setTitleText("照片管理");
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        ScreenTools mScreenTools = ScreenTools.instance(this);
        W = mScreenTools.getScreenWidth();
        H = mScreenTools.getScreenHeight();
        mRecyclerView.setPadding(UIHelper.dip2px(5), 0, UIHelper.dip2px(5), 0);
        mRecyclerView.setBackgroundResource(R.color.color_fff);
        mAdapter = new PicsManageAdapter(mPicsManageBeans);
        MyGridLayoutManager mGridLayoutManager = new MyGridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                int itemType = mPicsManageBeans.get(position).getItemType();
                int spanSize;
                if (itemType == PicsManageBean.TEXT) {
                    spanSize = 3;

                } else {
                    spanSize = 1;

                }
                return spanSize;
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        View view = View.inflate(this, R.layout.bottom_layout_view, null);
        view.findViewById(R.id.relative_layout_creat).setBackgroundResource(R.drawable.shape_radius0_stroke_line_bgfff);
        TextView tv_crest_address = (TextView) view.findViewById(R.id.tv_crest_address);
        tv_crest_address.setText("删除");
        tv_crest_address.setTextColor(getResources().getColor(R.color.color_ff4444));
        linear_layout_more.removeAllViews();
        linear_layout_more.addView(view);

        linear_layout_more.post(new Runnable() {
            @Override
            public void run() {
                showSuspensionPopupWindow();

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
                    CameraTakeActivity.startCameraActivity(mContext, Code.REQUEST_CODE_30);

                }
            });
        }
        mSuspensionPopupWindow.showAtLocationXY(getWindow().getDecorView(), Gravity.TOP, showX, showY);

    }

    @Override
    public void getDataFromNet(boolean isRefresh) {
        mSwipeRefreshLayout.setRefreshing(false);

    }
}
