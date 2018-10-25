package com.old.time.activitys;

import com.old.time.adapters.ArticleAdapter;
import com.old.time.beans.ArticleBean;

import java.util.ArrayList;
import java.util.List;

public class ArticleActivity extends BaseCActivity {

    private List<ArticleBean> articleBeans = new ArrayList<>();
    private ArticleAdapter adapter;

    @Override
    protected void initView() {
        super.initView();
        adapter = new ArticleAdapter(articleBeans);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void getDataFromNet(boolean isRefresh) {
        mSwipeRefreshLayout.setRefreshing(false);

    }
}
