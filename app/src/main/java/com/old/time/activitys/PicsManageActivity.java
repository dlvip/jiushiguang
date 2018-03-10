package com.old.time.activitys;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.old.time.R;
import com.old.time.adapters.PicsManageAdapter;
import com.old.time.beans.PicsManageBean;
import com.old.time.utils.MyGridLayoutManager;
import com.old.time.utils.UIHelper;

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

        View view = View.inflate(this, R.layout.bottom_creat_dress, null);
        view.findViewById(R.id.relative_layout_creat).setBackgroundResource(R.drawable.shape_radius0_bgfff);
        TextView tv_crest_address = (TextView) view.findViewById(R.id.tv_crest_address);
        tv_crest_address.setText("删除");
        tv_crest_address.setTextColor(getResources().getColor(R.color.color_ff4444));
        linear_layout_more.removeAllViews();
        linear_layout_more.addView(view);

    }

    @Override
    public void getDataFromNet(boolean isRefresh) {
        mSwipeRefreshLayout.setRefreshing(false);

    }
}
