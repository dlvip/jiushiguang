package com.old.time.fragments;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.old.time.R;
import com.old.time.activitys.WebViewActivity;
import com.old.time.beans.AlbumBean;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.okhttps.Http;
import com.old.time.okhttps.exception.ApiException;
import com.old.time.okhttps.subscriber.CommonSubscriber;
import com.old.time.okhttps.transformer.CommonTransformer;
import com.old.time.utils.MapParams;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;
import com.old.time.views.banner.BannerLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NING on 2018/3/5.
 */

public class HomeFragment extends CBaseFragment {

    private BannerLayout recycler_banner;
    private int width;

    private BaseQuickAdapter<AlbumBean, BaseViewHolder> mAdapter;

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        width = getWindowWidth();
        View headerView = View.inflate(mContext, R.layout.header_fragment_home, null);
        recycler_banner = headerView.findViewById(R.id.recycler_banner);
        recycler_banner.initBannerImageView(strings);
        recycler_banner.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                WebViewActivity.startWebViewActivity(mContext);

            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 5));
        mAdapter = new BaseQuickAdapter<AlbumBean, BaseViewHolder>(R.layout.adapter_home, mAlbumBeans) {
            @Override
            protected void convert(BaseViewHolder helper, AlbumBean item) {
                if (helper.getLayoutPosition() % 2 != 0) {
                    helper.getConvertView().setPadding(0, 0, UIHelper.dip2px(5), 0);

                } else {
                    helper.getConvertView().setPadding(UIHelper.dip2px(5), 0, 0, 0);

                }
                helper.setText(R.id.tv_photo_name,item.getTitle()).setText(R.id.tv_photo_money,item.getPrice());
                ImageView img_phone_pic = helper.getView(R.id.img_phone_pic);
                GlideUtils.getInstance().setImageViewWH(mContext, item.getPicUrl(), img_phone_pic, width / 2);
            }
        };
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                WebViewActivity.startWebViewActivity(mContext);

            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.removeAllHeaderView();
        mAdapter.addHeaderView(headerView);
        mAdapter.setHeaderAndEmpty(true);
    }

    private List<AlbumBean> mAlbumBeans = new ArrayList<>();

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        MapParams params = new MapParams();
        Http.getHttpService().getAlbumList(Constant.GET_ALUMLIST, params.getParamString())
                .compose(new CommonTransformer<List<AlbumBean>>())
                .subscribe(new CommonSubscriber<List<AlbumBean>>(mContext) {
                    @Override
                    public void onNext(List<AlbumBean> albumBeans) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        if (isRefresh) {
                            mAlbumBeans.clear();
                            mAdapter.setNewData(mAlbumBeans);

                        }
                        mAdapter.addData(albumBeans);
                    }

                    @Override
                    protected void onError(ApiException e) {
                        super.onError(e);
                        mSwipeRefreshLayout.setRefreshing(false);

                    }
                });

    }
}
