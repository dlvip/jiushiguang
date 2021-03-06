package com.old.time.pops;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.utils.MyLinearLayoutManager;
import com.old.time.utils.RecyclerItemDecoration;

import java.util.Arrays;

public class PostCartPop extends BasePopWindow {

    public PostCartPop(Context context, RecyclerView.OnItemTouchListener listener) {
        super(context);
        mRecyclerView.addOnItemTouchListener(listener);

    }

    private RecyclerView mRecyclerView;
    private BaseQuickAdapter<String, BaseViewHolder> adapter;

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new MyLinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(context, RecyclerItemDecoration.VERTICAL_LIST, 1));
        adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_text) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_item_text, item);

            }
        };
        mRecyclerView.setAdapter(adapter);
        findViewById(R.id.view_null).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });

    }

    public void showPopWindow(View view, String[] strings) {
        adapter.setNewData(Arrays.asList(strings));
        showAtLocation(view);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.pop_post_cart;
    }
}
