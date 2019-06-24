package com.old.time.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.adapters.BookMallAdapter;
import com.old.time.beans.BookMallEntity;
import com.old.time.beans.ResultBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.MyGridLayoutManager;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;

import java.util.List;


/**
 * Created by wcl on 2019/6/22.
 */

public class BookMallFragment extends BaseFragment {


    @Override
    public void getDataFromNet(boolean isRefresh) {
        OkGoUtils.getInstance().postNetForData(Constant.GET_BOOK_MALL_LIST, new JsonCallBack<ResultBean<List<BookMallEntity>>>() {
            @Override
            public void onSuccess(ResultBean<List<BookMallEntity>> mResultBean) {
                if (mResultBean != null && mResultBean.data != null && mResultBean.data.size() != 0) {
                    linear_layout_item.removeAllViews();
                    for (int i = 0; i < mResultBean.data.size(); i++) {
                        linear_layout_item.addView(createItemView(mResultBean.data.get(i)));

                    }
                }
            }

            @Override
            public void onError(ResultBean<List<BookMallEntity>> mResultBean) {

            }
        });
    }

    /**
     * 创建类型
     */
    private View createItemView(BookMallEntity itemBookEntity) {
        View itemView = View.inflate(mContext, R.layout.title_recycler_view, null);
        TextView tvTitle = itemView.findViewById(R.id.tv_recycler_title);
        RecyclerView rvContent = itemView.findViewById(R.id.recycler_content);
        rvContent.setLayoutManager(new MyGridLayoutManager(mContext, 2) {
            @Override
            public boolean canScrollVertically() {

                return false;
            }
        });
        rvContent.setPadding(UIHelper.dip2px(5), 0, UIHelper.dip2px(10), 0);
        tvTitle.setText(itemBookEntity.getTitle());
        BookMallAdapter adapter = new BookMallAdapter(itemBookEntity.getTabEntities());
        rvContent.setAdapter(adapter);
        rvContent.addItemDecoration(new RecyclerItemDecoration(mContext//
                , RecyclerItemDecoration.HORIZONTAL_LIST, 10, R.color.transparent));

        rvContent.addItemDecoration(new RecyclerItemDecoration(mContext//
                , RecyclerItemDecoration.VERTICAL_LIST, 10, R.color.transparent));

        return itemView;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_book_mall;
    }

    private LinearLayout linear_layout_item;

    @Override
    protected void lazyLoad() {
        findViewById(R.id.left_layout).setVisibility(View.GONE);
        linear_layout_item = findViewById(R.id.linear_layout_content);
        linear_layout_item.removeAllViews();

        getDataFromNet(true);
    }
}
