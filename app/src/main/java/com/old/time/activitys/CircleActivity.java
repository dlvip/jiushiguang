package com.old.time.activitys;

import android.view.View;
import android.widget.ImageView;

import com.old.time.R;
import com.old.time.adapters.CircleAdapter;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.utils.RecyclerItemDecoration;

public class CircleActivity extends SBaseActivity {

    private CircleAdapter mAdapter;

    @Override
    protected void initView() {
        super.initView();
        mAdapter = new CircleAdapter(strings);
        View headerView = View.inflate(mContext, R.layout.header_circle, null);
        ImageView img_circle_header_pic = headerView.findViewById(R.id.img_circle_header_pic);
        GlideUtils.getInstance().setImageView(mContext, Constant.PHOTO_PIC_URL, img_circle_header_pic);
        mAdapter.addHeaderView(headerView);
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext));
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    public void getDataFromNet(boolean isRefresh) {
        mSwipeRefreshLayout.setRefreshing(false);

    }
}
