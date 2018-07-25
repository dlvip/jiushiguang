package com.old.time.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lzy.okgo.model.Response;
import com.old.time.R;
import com.old.time.adapters.HCourseAdapter;
import com.old.time.adapters.HMusicAdapter;
import com.old.time.adapters.HomeAdapter;
import com.old.time.adapters.IconTabAdapter;
import com.old.time.beans.ArticleBean;
import com.old.time.beans.BannerBean;
import com.old.time.beans.ResultBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.MyGridLayoutManager;
import com.old.time.utils.MyLinearLayoutManager;
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

    private HomeAdapter mAdapter;

    private List<ArticleBean> articleBeans;
    private String cacheKey;
    private RecyclerView recycler_icons;
    private IconTabAdapter adapter;

    private RecyclerView recycler_course;
    private HCourseAdapter hCourseAdapter;

    private RecyclerView recycler_music;
    private HMusicAdapter hMusicAdapter;

    @Override
    protected void lazyLoad() {
        cacheKey = HomeFragment.class.getName();
        super.lazyLoad();
        strings.clear();
        for (int i = 0; i < 5; i++) {
            strings.add(Constant.PHOTO_PIC_URL);

        }
        View headerView = View.inflate(mContext, R.layout.header_fragment_home, null);
        recycler_banner = headerView.findViewById(R.id.recycler_banner);

        recycler_icons = headerView.findViewById(R.id.recycler_icons);
        recycler_icons.setLayoutManager(new MyGridLayoutManager(mContext, 5));
        adapter = new IconTabAdapter(strings);
        recycler_icons.setAdapter(adapter);

        //精品课堂
        recycler_course = headerView.findViewById(R.id.recycler_course);
        recycler_course.setLayoutManager(new MyLinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        recycler_course.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.HORIZONTAL_LIST, 10, R.color.color_fff));
        hCourseAdapter = new HCourseAdapter(strings);
        recycler_course.setAdapter(hCourseAdapter);

        //名师优讲
        recycler_music = headerView.findViewById(R.id.recycler_music);
        recycler_music.setLayoutManager(new MyLinearLayoutManager(mContext));
        hMusicAdapter = new HMusicAdapter(strings);
        recycler_music.setAdapter(hMusicAdapter);

        articleBeans = new ArrayList<>();
        mAdapter = new HomeAdapter(articleBeans);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.removeAllHeaderView();
        mAdapter.addHeaderView(headerView);
        mAdapter.setHeaderAndEmpty(true);

    }

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        getHomeBanners();
        getHomeArticles(isRefresh);

    }

    /**
     * 获取文章列表
     *
     * @param isRefresh
     */
    private void getHomeArticles(final boolean isRefresh) {
        OkGoUtils.getInstance().postNetForData(Constant.GET_ARTICLE_LIST, cacheKey, new JsonCallBack<ResultBean<List<ArticleBean>>>() {

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

    /**
     * 获取轮播图列表
     */
    private void getHomeBanners() {
        OkGoUtils.getInstance().postNetForData(Constant.GET_HOME_BANNERS, new JsonCallBack<ResultBean<List<BannerBean>>>() {

            @Override
            public void onSuccess(ResultBean<List<BannerBean>> resultBean) {
                if (resultBean == null || resultBean.data == null) {

                    return;
                }
                recycler_banner.initBannerImageView(resultBean.data);

            }

            @Override
            public void onError(ResultBean<List<BannerBean>> resultBean) {
                if (resultBean == null) {

                    return;
                }
                UIHelper.ToastMessage(mContext, resultBean.msg);
            }
        });
    }
}
