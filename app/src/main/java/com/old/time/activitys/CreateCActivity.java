package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.old.time.R;
import com.old.time.beans.CreateBean;
import com.old.time.utils.ActivityUtils;
import java.util.ArrayList;
import java.util.List;

public class CreateCActivity extends BaseCActivity {

    public static void startCreateActivity(Activity activity) {
        Intent intent = new Intent(activity, CreateCActivity.class);
        ActivityUtils.startActivity(activity, intent);

    }

    private List<CreateBean> createBeans = new ArrayList<>();
    private BaseQuickAdapter<CreateBean, BaseViewHolder> adapter;

    @Override
    protected void initView() {
        super.initView();
        createBeans.clear();
        createBeans.add(CreateBean.getInstance("添加活动", CreateActionActivity.class));
        createBeans.add(CreateBean.getInstance("添加轮播", CreateBannerActivity.class));
        createBeans.add(CreateBean.getInstance("添加宝贝", CreateGoodsActivity.class));
        createBeans.add(CreateBean.getInstance("显示宝贝", GoodsCActivity.class));
        createBeans.add(CreateBean.getInstance("搜索用户", SearchActivity.class));

        adapter = new BaseQuickAdapter<CreateBean, BaseViewHolder>(R.layout.dialog_manager_item, createBeans) {
            @Override
            protected void convert(BaseViewHolder helper, CreateBean item) {
                helper.setText(R.id.tv_text_name, item.getTitle());

            }
        };
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, createBeans.get(position).getaClass());
                ActivityUtils.startActivity(mContext, intent);

            }
        });
    }

    @Override
    public void getDataFromNet(boolean isRefresh) {
        mSwipeRefreshLayout.setRefreshing(false);

    }
}
