package com.old.time.fragments;

import android.view.View;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.adapter.Call;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.old.time.R;
import com.old.time.activitys.WebViewActivity;
import com.old.time.adapters.HomeAdapter;
import com.old.time.beans.ArticleBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.ObjCallBack;
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
                WebViewActivity.startWebViewActivity(mContext);

            }
        });
    }

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        OkGo.post(Constant.GET_ARTICLE_LIST).cacheKey(cacheKey).execute(new ObjCallBack<List<ArticleBean>>());

    }
}
