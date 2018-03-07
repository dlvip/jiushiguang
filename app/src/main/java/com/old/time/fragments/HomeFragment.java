package com.old.time.fragments;

import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.adapters.GalleyAdapter;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.utils.Gallerys.AnimManager;
import com.old.time.utils.Gallerys.GalleryRecyclerView;
import com.old.time.utils.MyLinearLayoutManager;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NING on 2018/3/5.
 */

public class HomeFragment extends CBaseFragment implements GalleryRecyclerView.OnItemClickListener {

    private GalleyAdapter mGalleyAdapter;
    private GalleryRecyclerView rv_galley_list;
    private int width;

    private BaseQuickAdapter<String, BaseViewHolder> mAdapter;

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        width = getWindowWidth();
        View headerView = View.inflate(mContext, R.layout.header_fragment_home, null);
        rv_galley_list = headerView.findViewById(R.id.rv_galley_list);

        mGalleyAdapter = new GalleyAdapter(mContext, getDatas());
        rv_galley_list.setLayoutManager(new MyLinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        rv_galley_list.setAdapter(mGalleyAdapter);
        rv_galley_list.initFlingSpeed(9000)                             // 设置滑动速度（像素/s）
                .initPageParams(0, 60)     // 设置页边距和左右图片的可见宽度，单位dp
                .setAnimFactor(0.15f)                                   // 设置切换动画的参数因子
                .setAnimType(AnimManager.ANIM_BOTTOM_TO_TOP)            // 设置切换动画类型，目前有AnimManager.ANIM_BOTTOM_TO_TOP和目前有AnimManager.ANIM_TOP_TO_BOTTOM
                .setOnItemClickListener(this);                          // 设置点击事件

        // 背景高斯模糊 & 淡入淡出
        rv_galley_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }
        });

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
    public void onItemClick(View view, int position) {
        UIHelper.ToastMessage(mContext, TAG + position);

    }
}
