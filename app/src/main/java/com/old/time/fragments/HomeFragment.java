package com.old.time.fragments;

import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;
import com.old.time.views.banner.BannerLayout;
import com.old.time.views.banner.layoutmanager.BannerLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NING on 2018/3/5.
 */

public class HomeFragment extends CBaseFragment implements BannerLayout.OnBannerItemClickListener, BannerLayoutManager.OnPageChangeListener {

    private ImageView img_gallery_bg;
    private BannerLayout recycler_banner;
    private int width;

    private BaseQuickAdapter<String, BaseViewHolder> mAdapter;

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        width = getWindowWidth();
        View headerView = View.inflate(mContext, R.layout.header_fragment_home, null);
        recycler_banner = headerView.findViewById(R.id.recycler_banner);
        img_gallery_bg = headerView.findViewById(R.id.img_gallery_bg);

        //解决recyclerView嵌套问题
        strings.clear();
        strings.add(Constant.PHOTO_PIC_URL);
        strings.add(Constant.PHOTO_PIC_URL);
        strings.add(Constant.PHOTO_PIC_URL);
        strings.add(Constant.PHOTO_PIC_URL);
        strings.add(Constant.PHOTO_PIC_URL);
        strings.add(Constant.PHOTO_PIC_URL);
        strings.add(Constant.PHOTO_PIC_URL);
        strings.add(Constant.PHOTO_PIC_URL);
        strings.add(Constant.PHOTO_PIC_URL);
        strings.add(Constant.PHOTO_PIC_URL);
        strings.add(Constant.PHOTO_PIC_URL);
        strings.add(Constant.PHOTO_PIC_URL);
        strings.add(Constant.PHOTO_PIC_URL);

        recycler_banner.initBannerImageView(strings);
        recycler_banner.setOnBannerItemClickListener(this);
        recycler_banner.setOnPageChangeListener(this);

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

    /***
     * 测试数据
     * @return
     */
    public List<Integer> getDatas() {
        TypedArray ar = getResources().obtainTypedArray(R.array.test_arr);
        final int[] resIds = new int[ar.length()];
        for (int i = 0; i < ar.length(); i++) {
            resIds[i] = ar.getResourceId(i, 0);
        }
        ar.recycle();
        List<Integer> tDatas = new ArrayList<>();
        for (int resId : resIds) {
            tDatas.add(resId);
        }
        return tDatas;
    }

    @Override
    public void getDataFromNet(boolean isRefresh) {
        mSwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onItemClick(int position) {
        UIHelper.ToastMessage(mContext, TAG + position);

    }

    @Override
    public void onPageSelected(int position) {
        if (strings != null && strings.size() != 0 && position < strings.size() && img_gallery_bg != null) {
            GlideUtils.getInstance().setImageViewTransRadius(mContext, strings.get(position), img_gallery_bg, getWindowWidth(), UIHelper.dip2px(200), 20);


        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
