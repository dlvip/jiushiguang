package com.old.time.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.adapters.EmptyAdapter;
import com.old.time.beans.BannerBean;
import com.old.time.beans.BookEntity;
import com.old.time.beans.ResultBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.UIHelper;
import com.old.time.views.banner.BannerLayout;
import com.old.time.views.banner.adapter.MzBannerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NING on 2018/3/5.
 */

public class HomeFragment extends CBaseFragment {

    private List<BannerBean> bannerBeans;
    private BannerLayout recycler_banner;
    private MzBannerAdapter mzBannerAdapter;


    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        View headerView = View.inflate(mContext, R.layout.header_fragment_home, null);
        LinearLayout linear_layout_content = headerView.findViewById(R.id.linear_layout_content);
        linear_layout_content.removeAllViews();
        recycler_banner = headerView.findViewById(R.id.recycler_banner);
        bannerBeans = new ArrayList<>();
        mzBannerAdapter = new MzBannerAdapter(mContext, bannerBeans);
        recycler_banner.setmBannerAdapter(mzBannerAdapter);

        EmptyAdapter emptyAdapter = new EmptyAdapter();
        emptyAdapter.removeAllHeaderView();
        emptyAdapter.addHeaderView(headerView);
        emptyAdapter.setHeaderAndEmpty(true);
        mRecyclerView.setAdapter(emptyAdapter);

    }

    private boolean isInit;

    /**
     * 创建类型
     */
    private void createItemView(BookEntity bookEntity) {
        if (!isInit) {
            View itemView = View.inflate(mContext, R.layout.title_recycler_view, null);
            TextView tvTitle = itemView.findViewById(R.id.tv_recycler_title);
            RecyclerView rvContent = itemView.findViewById(R.id.recycler_content);
            switch (bookEntity.getFilePath()) {
                case "0":
                    tvTitle.setText("重磅推荐");

                    break;
                case "1":
                    tvTitle.setText("近期热搜");

                    break;
                case "2":
                    tvTitle.setText("读者精品");

                    break;
                case "3":
                    tvTitle.setText("读者精品");

                    break;
            }
        }
    }

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        HttpParams params = new HttpParams();
        params.put("pageNum", 0);
        params.put("pageSize", 5);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_HOME_BANNERS, new JsonCallBack<ResultBean<List<BannerBean>>>() {

            @Override
            public void onSuccess(ResultBean<List<BannerBean>> resultBean) {
                if (resultBean == null || resultBean.data == null) {

                    return;
                }
                bannerBeans.clear();
                bannerBeans.addAll(resultBean.data);
                recycler_banner.initBannerImageView(bannerBeans);

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
