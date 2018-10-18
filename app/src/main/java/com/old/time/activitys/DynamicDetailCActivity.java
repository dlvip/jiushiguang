package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.adapters.CommentAdapter;
import com.old.time.beans.CommentBean;
import com.old.time.beans.DynamicBean;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.views.NineImageView;

import java.util.ArrayList;
import java.util.List;

public class DynamicDetailCActivity extends BaseCActivity {

    public static final String DYNAMIC_BEAN = "mDynamicBean";

    /**
     * 动态详情页面
     *
     * @param mContext
     */
    public static void startDynamicDetailActivity(Activity mContext, DynamicBean mDynamicBean) {
        Intent intent = new Intent(mContext, DynamicDetailCActivity.class);
        intent.putExtra(DYNAMIC_BEAN, mDynamicBean);
        ActivityUtils.startActivity(mContext, intent);

    }

    private List<CommentBean> commentBeans = new ArrayList<>();
    private CommentAdapter mAdapter;
    private DynamicBean mDynamicBean;

    @Override
    protected void initView() {
        mDynamicBean = (DynamicBean) getIntent().getSerializableExtra("mDynamicBean");
        super.initView();
        View headerView = View.inflate(mContext, R.layout.dynamic_detail_header, null);
        TextView tv_content_time = headerView.findViewById(R.id.tv_content_time);
        tv_content_time.setText(mDynamicBean.createTimeStr.substring(0, 10));

        TextView expand_text_view = headerView.findViewById(R.id.expand_text_view);
        if (TextUtils.isEmpty(mDynamicBean.conetent)) {
            expand_text_view.setVisibility(View.GONE);

        } else {
            expand_text_view.setVisibility(View.VISIBLE);
            expand_text_view.setText(mDynamicBean.conetent);

        }
        NineImageView mNineImageView = headerView.findViewById(R.id.nineImageView);
        if (mDynamicBean.conetentImages == null || mDynamicBean.conetentImages.size() == 0) {
            mNineImageView.setVisibility(View.GONE);

        } else {
            mNineImageView.setVisibility(View.VISIBLE);
            mNineImageView.setDataForView(mDynamicBean.conetentImages);

        }
        mAdapter = new CommentAdapter(commentBeans);
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.addHeaderView(headerView);

    }

    @Override
    public void getDataFromNet(boolean isRefresh) {
        mSwipeRefreshLayout.setRefreshing(false);

    }
}
