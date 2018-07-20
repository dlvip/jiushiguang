package com.old.time.fragments;

import android.view.View;

import com.old.time.R;
import com.old.time.activitys.MusicActivity;
import com.old.time.adapters.HomeAdapter;
import com.old.time.beans.ArticleBean;
import com.old.time.beans.ResultBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.UIHelper;
import com.old.time.views.banner.BannerLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NING on 2018/3/5.
 */

public class HomeFragment extends CBaseFragment {

    private BannerLayout recycler_banner;
    private HomeAdapter mAdapter;

    private List<ArticleBean> articleBeans;
    private String cacheKey;

    @Override
    protected void lazyLoad() {
        cacheKey = HomeFragment.class.getName();
        super.lazyLoad();
        View headerView = View.inflate(mContext, R.layout.header_fragment_home, null);
        recycler_banner = headerView.findViewById(R.id.recycler_banner);
        recycler_banner.initBannerImageView(strings);

        articleBeans = new ArrayList<>();
        mAdapter = new HomeAdapter(articleBeans);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.removeAllHeaderView();
        mAdapter.addHeaderView(headerView);
        mAdapter.setHeaderAndEmpty(true);

        recycler_banner.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                WebViewActivity.startWebViewActivity(mContext);
                MusicActivity.startMusicActivity(mContext);
            }
        });
    }

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        OkGoUtils.postNetForData(Constant.GET_ARTICLE_LIST, cacheKey, new OkGoUtils.JsonObjCallBack<List<ArticleBean>>() {

            @Override
            public void onSuccess(ResultBean<List<ArticleBean>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (isRefresh) {
                    articleBeans.clear();
                    mAdapter.setNewData(articleBeans);

                }
                if (mResultBean.status == Constant.STATUS_FRIEND_00) {
                    articleBeans.addAll(mResultBean.data);
                    mAdapter.setNewData(articleBeans);

                } else {
                    UIHelper.ToastMessage(mContext, mResultBean.msg);

                }
            }

            @Override
            public void onError(ResultBean<List<ArticleBean>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                UIHelper.ToastMessage(mContext, mResultBean.msg);

            }
        });
    }
}
