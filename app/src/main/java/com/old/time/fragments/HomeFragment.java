package com.old.time.fragments;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;
import com.old.time.views.banner.BannerLayout;

/**
 * Created by NING on 2018/3/5.
 */

public class  HomeFragment extends CBaseFragment implements BannerLayout.OnBannerItemClickListener {

    private BannerLayout recycler_banner;
    private int width;

    private BaseQuickAdapter<String, BaseViewHolder> mAdapter;

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        width = getWindowWidth();
        View headerView = View.inflate(mContext, R.layout.header_fragment_home, null);
        recycler_banner = headerView.findViewById(R.id.recycler_banner);

        recycler_banner.initBannerImageView(strings);
        recycler_banner.setOnBannerItemClickListener(this);

        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 5));
        mAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.adapter_home, strings) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                if (helper.getLayoutPosition() % 2 != 0) {
                    helper.getConvertView().setPadding(0, 0, UIHelper.dip2px(5), 0);

                } else {
                    helper.getConvertView().setPadding(UIHelper.dip2px(5), 0, 0, 0);

                }
                ImageView img_phone_pic = helper.getView(R.id.img_phone_pic);
                GlideUtils.getInstance().setImageViewWH(mContext, item, img_phone_pic, width / 2);

            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.removeAllHeaderView();
        mAdapter.addHeaderView(headerView);
        mAdapter.setHeaderAndEmpty(true);
    }

    @Override
    public void getDataFromNet(boolean isRefresh) {
        mSwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onItemClick(int position) {
        UIHelper.ToastMessage(mContext, TAG + position);

    }
}
