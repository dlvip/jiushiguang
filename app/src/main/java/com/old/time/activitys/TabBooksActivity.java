package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.lzy.okgo.model.HttpParams;
import com.old.time.adapters.RBookListAdapter;
import com.old.time.beans.BookEntity;
import com.old.time.beans.ResultBean;
import com.old.time.beans.TabEntity;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.views.CustomNetView;

import java.util.ArrayList;
import java.util.List;

public class TabBooksActivity extends BaseCActivity {

    /**
     * 分类下的图书列表
     *
     * @param context
     * @param tabEntity
     */
    public static void startTabBooksActivity(Context context, TabEntity tabEntity) {
        if (tabEntity == null) {

            return;
        }
        Intent intent = new Intent(context, TabBooksActivity.class);
        intent.putExtra("tabEntity", tabEntity);
        ActivityUtils.startActivity((Activity) context, intent);

    }

    private TabEntity tabEntity;
    private RBookListAdapter rBookAdapter;
    private List<BookEntity> bookEntities = new ArrayList<>();

    @Override
    protected void initView() {
        tabEntity = (TabEntity) getIntent().getSerializableExtra("tabEntity");
        super.initView();
        setTitleText(tabEntity.getName());
        rBookAdapter = new RBookListAdapter(bookEntities);
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext));
        mRecyclerView.setAdapter(rBookAdapter);

    }

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        int startNum;
        if (isRefresh) {
            startNum = 0;

        } else {
            startNum = bookEntities.size();

        }
        HttpParams params = new HttpParams();
        params.put("tabId", tabEntity.getTabId());
        params.put("startNum", String.valueOf(startNum));
        params.put("pageSize", String.valueOf(Constant.PageSize));
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_TAB_BOOK_LIST, new JsonCallBack<ResultBean<List<BookEntity>>>() {

            @Override
            public void onSuccess(ResultBean<List<BookEntity>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (isRefresh) {
                    bookEntities.clear();
                    rBookAdapter.setNewData(bookEntities);

                }
                if (mResultBean.data != null) {
                    rBookAdapter.addData(mResultBean.data);

                }
                if (rBookAdapter.getData().size() == 0) {
                    mCustomNetView.setDataForView(CustomNetView.NO_DATA);
                    rBookAdapter.setEmptyView(mCustomNetView);

                }
            }

            @Override
            public void onError(ResultBean<List<BookEntity>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });
    }
}
