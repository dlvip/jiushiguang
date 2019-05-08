package com.old.time.activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.zxing.activity.CaptureActivity;
import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.adapters.BooksAdapter;
import com.old.time.beans.BookEntity;
import com.old.time.beans.JHBaseBean;
import com.old.time.beans.ResultBean;
import com.old.time.constants.Constant;
import com.old.time.dialogs.DialogMallCart;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.permission.PermissionUtil;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.PictureUtil;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;
import com.old.time.views.CustomNetView;

import java.util.ArrayList;
import java.util.List;

public class BooksActivity extends BaseCActivity {

    /**
     * 图书列表
     *
     * @param context
     */
    public static void startBooksActivity(Context context) {
        Intent intent = new Intent(context, BooksActivity.class);
        ActivityUtils.startActivity((Activity) context, intent);

    }

    private BooksAdapter adapter;
    private List<BookEntity> bookEntities = new ArrayList<>();

    private View bottomView;
    private ImageView img_btn_more;
    private TextView tv_mall_count, tv_mall_money, tv_mall_commit;

    @Override
    protected void initView() {
        super.initView();
        setTitleText("图书列表");
        adapter = new BooksAdapter(bookEntities);
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        mRecyclerView.setAdapter(adapter);
        setSendText("添书");

        linear_layout_more.setVisibility(View.VISIBLE);
        layoutParams.height = UIHelper.dip2px(60);
        linear_layout_more.setLayoutParams(layoutParams);
        linear_layout_more.removeAllViews();
        bottomView = View.inflate(mContext, R.layout.mall_cart_bottom, null);
        img_btn_more = bottomView.findViewById(R.id.img_btn_more);
        tv_mall_count = bottomView.findViewById(R.id.tv_mall_count);
        tv_mall_money = bottomView.findViewById(R.id.tv_mall_money);
        tv_mall_commit = bottomView.findViewById(R.id.tv_mall_commit);
        linear_layout_more.addView(bottomView);

    }

    @Override
    public void save(View view) {
        super.save(view);
        PictureUtil.captureCode(mContext);

    }

    @Override
    protected void initEvent() {
        super.initEvent();
        img_btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMallCartDialog();

            }
        });
        tv_mall_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.ToastMessage(mContext, "去结算");

            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getDataFromNet(false);

            }
        }, mRecyclerView);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void onSimpleItemClick(BaseQuickAdapter mAdapter, View view, int position) {
                BookEntity bookEntity = adapter.getItem(position);
                if (bookEntity == null) {

                    return;
                }
                BookDetailActivity.startBookDetailActivity(mContext, bookEntity);

            }
        });
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {

            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter mAdapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.img_book_cart:
                        BookEntity bookEntity = adapter.getItem(position);
                        if (bookEntity == null) {

                            return;
                        }
                        bookEntity.setSelect(!bookEntity.isSelect());
                        adapter.setData(position, bookEntity);
                        setDataForBottomView(bookEntity);

                        break;
                }
            }
        });
    }

    private DialogMallCart dialogMallCart;

    /**
     * 购物车
     */
    private void showMallCartDialog() {
        if (dialogMallCart == null) {
            dialogMallCart = new DialogMallCart(mContext, new DialogMallCart.OnItemDateUpdateListener() {
                @Override
                public void onItemListener(BookEntity bookEntity) {
                    setDataForBottomView(bookEntity);

                }
            });
        }
        dialogMallCart.showMallCartDialog(linear_layout_more, books);

    }

    /**
     * 选择图书
     */
    private List<BookEntity> books = new ArrayList<>();

    /**
     * 修改底部显示
     */
    private void setDataForBottomView(BookEntity bookEntity) {
        if (bottomView == null || bookEntity == null) {

            return;
        }
        if (bookEntity.getCount() < 1) {
            books.remove(bookEntity);
            int position = bookEntities.indexOf(bookEntity);
            bookEntity.setCount(1);
            bookEntity.setSelect(false);
            adapter.setData(position, bookEntity);

        } else if (!books.contains(bookEntity)) {
            books.add(bookEntity);

        }
        double money = 0;//总价
        int count = 0;//总数（本）
        for (BookEntity b : books) {
            money += b.getDPrice() * b.getCount();
            count += b.getCount();

        }
        tv_mall_money.setText(String.valueOf("合计：￥" + money));
        tv_mall_count.setText(String.valueOf("已选择" + books.size() + "种 共" + count + "本"));
    }

    private int startNum;

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        if (isRefresh) {
            startNum = 0;

        }
        HttpParams params = new HttpParams();
        params.put("pageNum", startNum);
        params.put("pageSize", Constant.PageSize);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_BOOK_LIST, new JsonCallBack<ResultBean<List<BookEntity>>>() {
            @Override
            public void onSuccess(ResultBean<List<BookEntity>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                startNum++;
                if (isRefresh) {
                    mSwipeRefreshLayout.setEnabled(false);
                    bookEntities.clear();
                    adapter.setNewData(bookEntities);

                }
                if (mResultBean.data.size() < Constant.PageSize) {
                    adapter.loadMoreEnd();

                } else {
                    adapter.loadMoreComplete();

                }
                adapter.addData(mResultBean.data);
                if (adapter.getData().size() == 0) {
                    mCustomNetView.setDataForView(CustomNetView.NO_DATA);
                    adapter.setEmptyView(mCustomNetView);

                }

            }

            @Override
            public void onError(ResultBean<List<BookEntity>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (adapter.getData().size() == 0) {
                    mCustomNetView.setDataForView(CustomNetView.NET_ERREY);
                    adapter.setEmptyView(mCustomNetView);

                } else {
                    adapter.loadMoreFail();

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {

            return;
        }
        String str = data.getStringExtra(CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN);
        if (TextUtils.isEmpty(str) || (str.length() != 10 && str.length() != 13)) {
            UIHelper.ToastMessage(mContext, "内容不匹配");

            return;
        }
        getJSGBookInfo(str);
    }

    private ProgressDialog pd;

    /**
     * 平台获取图书信息
     */
    private void getJSGBookInfo(final String isbn) {
        pd = UIHelper.showProgressMessageDialog(mContext, getString(R.string.please_wait));
        HttpParams params = new HttpParams();
        params.put("isbn", isbn);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_BOOK_INFO, new JsonCallBack<ResultBean<BookEntity>>() {
            @Override
            public void onSuccess(ResultBean<BookEntity> mResultBean) {
                UIHelper.dissmissProgressDialog(pd);
                if (mResultBean == null || mResultBean.data == null) {
                    UIHelper.ToastMessage(mContext, "内容不存在");

                    return;
                }
                UIHelper.ToastMessage(mContext, "图书已存在");

            }

            @Override
            public void onError(ResultBean<BookEntity> mResultBean) {
                getBookInfo(isbn);

            }
        });
    }

    /**
     * 聚合查询图书信息
     */
    private void getBookInfo(String mIBSNCode) {
        HttpParams params = new HttpParams();
        params.put("key", Constant.BOOK_INFO_KEY);
        params.put("sub", mIBSNCode);
        OkGoUtils.getInstance().getNetForData(params, Constant.GET_JIHE_BOOK_INFO, new JsonCallBack<JHBaseBean<BookEntity>>() {
            @Override
            public void onSuccess(JHBaseBean<BookEntity> mResultBean) {
                if (mResultBean == null || mResultBean.result == null) {
                    UIHelper.dissmissProgressDialog(pd);
                    UIHelper.ToastMessage(mContext, "内容不存在");

                    return;
                }
                createBookInfo(mResultBean.result);

            }

            @Override
            public void onError(JHBaseBean<BookEntity> mResultBean) {
                UIHelper.dissmissProgressDialog(pd);
                UIHelper.ToastMessage(mContext, mResultBean.reason);

            }
        });
    }

    /**
     * 保存图书信息到自己服务器
     */
    private void createBookInfo(BookEntity mBookEntity) {
        HttpParams params = new HttpParams();
        params.put("levelNum", mBookEntity.getLevelNum());
        params.put("subtitle", mBookEntity.getSubtitle());
        params.put("author", mBookEntity.getAuthor());
        params.put("pubdate", mBookEntity.getPubdate());
        params.put("origin_title", mBookEntity.getOrigin_title());
        params.put("binding", mBookEntity.getBinding());
        params.put("pages", mBookEntity.getPages());
        params.put("images_medium", mBookEntity.getImages_medium());
        params.put("images_large", mBookEntity.getImages_large());
        params.put("publisher", mBookEntity.getPublisher());
        params.put("isbn10", mBookEntity.getIsbn10());
        params.put("isbn13", mBookEntity.getIsbn13());
        params.put("title", mBookEntity.getTitle());
        params.put("summary", mBookEntity.getSummary());
        params.put("price", mBookEntity.getPrice());
        params.put("url", "");
        OkGoUtils.getInstance().postNetForData(params, Constant.CREATE_BOOK_INFO, new JsonCallBack<ResultBean<BookEntity>>() {

            @Override
            public void onSuccess(ResultBean<BookEntity> mResultBean) {
                UIHelper.dissmissProgressDialog(pd);
                if (mResultBean == null || mResultBean.data == null) {
                    UIHelper.ToastMessage(mContext, "内容不存在");

                    return;
                }
                adapter.addData(0, mResultBean.data);
                seleteToPosition(0);

            }

            @Override
            public void onError(ResultBean<BookEntity> mResultBean) {
                UIHelper.dissmissProgressDialog(pd);
                UIHelper.ToastMessage(mContext, mResultBean.msg);

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onPermissionResult(this, permissions, grantResults, new PermissionUtil.PermissionCallBack() {
            @Override
            public void onSuccess() {
                PictureUtil.captureCode(mContext);

            }

            @Override
            public void onShouldShow() {

            }

            @Override
            public void onFailed() {
                showDialogPermission();

            }
        });
    }
}
