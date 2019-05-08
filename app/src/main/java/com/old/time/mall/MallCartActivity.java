package com.old.time.mall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.old.time.R;
import com.old.time.activitys.BaseCActivity;
import com.old.time.beans.BookEntity;
import com.old.time.utils.ActivityUtils;

import java.io.Serializable;
import java.util.List;

public class MallCartActivity extends BaseCActivity {

    /**
     * 购物车
     *
     * @param context
     * @param bookEntities
     */
    public static void startMallCartActivity(Context context, List<BookEntity> bookEntities) {
        Intent intent = new Intent(context, MallCartActivity.class);
        intent.putExtra("bookEntities", (Serializable) bookEntities);
        ActivityUtils.startActivity((Activity) context, intent);

    }
    private int[] nums = new int[]{1, 2, 5, 10};
    private int indext = 0;
    private int num = 1;
    private BaseQuickAdapter<BookEntity, BaseViewHolder> mAdapter;

    @Override
    protected void initView() {
        super.initView();
        mAdapter = new BaseQuickAdapter<BookEntity, BaseViewHolder>(R.layout.adapter_cart_dialog) {
            @Override
            protected void convert(BaseViewHolder helper, BookEntity item) {
                helper.setText(R.id.tv_book_name, item.getTitle())//
                        .setText(R.id.tv_book_price, item.getPriceStr())//
                        .setText(R.id.tv_book_count, String.valueOf(item.getCount()))//
                        .addOnClickListener(R.id.img_count_reduce)//
                        .addOnClickListener(R.id.img_count_plus);


            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                BookEntity bookEntity = mAdapter.getItem(position);
                if (bookEntity == null) {

                    return;
                }
                switch (view.getId()) {
                    case R.id.img_count_reduce:
                        bookEntity.setCount(bookEntity.getCount() - num);

                        break;
                    case R.id.img_count_plus:
                        bookEntity.setCount(bookEntity.getCount() + num);

                        break;
                }
                if (bookEntity.getCount() < 1) {
                    mAdapter.remove(position);

                } else {
                    mAdapter.setData(position, bookEntity);

                }
            }
        });
    }

    @Override
    public void getDataFromNet(boolean isRefresh) {


    }
}
