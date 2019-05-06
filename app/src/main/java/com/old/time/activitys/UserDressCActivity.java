package com.old.time.activitys;

import android.view.View;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.adapters.UserDressAdapter;
import com.old.time.utils.DataUtils;
import com.old.time.utils.RecyclerItemDecoration;

public class UserDressCActivity extends BaseCActivity {

    private UserDressAdapter mAdapter;

    @Override
    protected void initView() {
        super.initView();
        setTitleText("地址管理");
        linear_layout_more.setVisibility(View.VISIBLE);
        mAdapter = new UserDressAdapter(DataUtils.getDateStrings(20));
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        mRecyclerView.setAdapter(mAdapter);
        View view = View.inflate(this, R.layout.bottom_layout_view, null);
        view.findViewById(R.id.relative_layout_creat).setBackgroundResource(R.drawable.shape_radius0_stroke_line_bgfff);
        TextView tv_crest_address = view.findViewById(R.id.tv_crest_address);
        tv_crest_address.setText("添加地址");
        tv_crest_address.setTextColor(getResources().getColor(R.color.color_ff4444));
        linear_layout_more.removeAllViews();
        linear_layout_more.addView(view);

    }

    @Override
    protected void initEvent() {
        super.initEvent();
        linear_layout_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateDressActivity.startCreateDressActivity(mContext);

            }
        });
    }

    @Override
    public void getDataFromNet(boolean isRefresh) {
        mSwipeRefreshLayout.setRefreshing(false);

    }
}
