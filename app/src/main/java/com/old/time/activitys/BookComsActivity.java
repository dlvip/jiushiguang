package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.model.HttpParams;
import com.old.time.adapters.BookComAapter;
import com.old.time.beans.BookComEntity;
import com.old.time.beans.ResultBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UserLocalInfoUtils;
import com.old.time.views.CustomNetView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class BookComsActivity extends BaseCActivity {

    /**
     * 书评列表
     *
     * @param context
     */
    public static void startBookComsActivity(Context context) {
        Intent intent = new Intent(context, BookComsActivity.class);
        ActivityUtils.startActivity((Activity) context, intent);

    }

    private List<BookComEntity> bookComEntities = new ArrayList<>();
    private BookComAapter mAdapter;

    @Override
    protected void initView() {
        super.initView();
        setTitleText("书评");
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        mAdapter = new BookComAapter(bookComEntities);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getDataFromNet(false);

            }
        }, mRecyclerView);

        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                showSuspensionPopupWindow();

            }
        });

        EventBus.getDefault().register(this);
    }

    private int startNum;

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        if (isRefresh) {
            startNum = 0;

        }
        HttpParams params = new HttpParams();
        params.put("userId", UserLocalInfoUtils.instance().getUserId());
        params.put("pageNum", startNum);
        params.put("pageSize", Constant.PageSize);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_BOOK_COMMENTS, new JsonCallBack<ResultBean<List<BookComEntity>>>() {

            @Override
            public void onSuccess(ResultBean<List<BookComEntity>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                startNum++;
                if (isRefresh) {
                    bookComEntities.clear();
                    mAdapter.setNewData(bookComEntities);

                }
                if (mResultBean.data.size() < Constant.PageSize) {
                    mAdapter.loadMoreEnd();

                } else {
                    mAdapter.loadMoreComplete();

                }
                mAdapter.addData(mResultBean.data);
                if (mAdapter.getData().size() == 0) {
                    mCustomNetView.setDataForView(CustomNetView.NO_DATA);
                    mAdapter.setEmptyView(mCustomNetView);

                }
            }

            @Override
            public void onError(ResultBean<List<BookComEntity>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (mAdapter.getData().size() == 0) {
                    mCustomNetView.setDataForView(CustomNetView.NET_ERREY);
                    mAdapter.setEmptyView(mCustomNetView);

                } else {
                    mAdapter.loadMoreFail();

                }
            }
        });
    }

    @Override
    public void setSuspensionPopupWindowClick() {
        super.setSuspensionPopupWindowClick();
        SelectBookActivity.startSelectBookActivity(mContext);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void createBookCom(BookComEntity mBookComEntity) {
        if (mBookComEntity == null || mAdapter == null) {

            return;
        }
        mAdapter.addData(0, mBookComEntity);
        seleteToPosition(0);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }
}
