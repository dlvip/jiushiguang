package com.old.time.fragments;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.activitys.CircleActivity;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.RecyclerItemDecoration;

/**
 * Created by NING on 2018/3/5.
 */

public class FindFragment extends CBaseFragment {

    private BaseQuickAdapter<String, BaseViewHolder> mAdapter;

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        mAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.adapter_find_type_pic, strings) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                ImageView img_find_pic = helper.getView(R.id.img_find_pic);
                GlideUtils.getInstance().setImageView(mContext, item, img_find_pic);

            }
        };
        View headerView = View.inflate(mContext, R.layout.header_find, null);
        mAdapter.addHeaderView(headerView);
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        mRecyclerView.setAdapter(mAdapter);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CircleActivity.class);
                ActivityUtils.startActivity((Activity) mContext, intent);

            }
        });

    }

    @Override
    public void getDataFromNet(boolean isRefresh) {
        mSwipeRefreshLayout.setRefreshing(false);

    }
}
