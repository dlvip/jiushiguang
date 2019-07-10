package com.old.time.fragments;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.adapters.RBookAdapter;
import com.old.time.adapters.RBookListAdapter;
import com.old.time.beans.BannerBean;
import com.old.time.beans.BookEntity;
import com.old.time.beans.RBookEntity;
import com.old.time.beans.RItemBookEntity;
import com.old.time.beans.ResultBean;
import com.old.time.constants.Code;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.DebugLog;
import com.old.time.utils.MyGridLayoutManager;
import com.old.time.utils.MyLinearLayoutManager;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;
import com.old.time.views.banner.BannerLayout;
import com.old.time.views.banner.adapter.MzBannerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NING on 2018/3/5.
 */

public class HomeFragment extends BaseFragment {

    public SwipeRefreshLayout.OnRefreshListener onRefreshListener;
    public SwipeRefreshLayout mSwipeRefreshLayout;

    public Handler loadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Code.HIDE_LOADING:
                    DebugLog.e("close－", "close_loading");
                    mSwipeRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            mSwipeRefreshLayout.setRefreshing(false);

                        }
                    });
                    break;
            }
        }
    };

    private List<BannerBean> bannerBeans;
    private BannerLayout recycler_banner;
    private MzBannerAdapter mzBannerAdapter;

    private LinearLayout linear_layout_item;

    @Override
    protected int setContentView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void lazyLoad() {
        findViewById(R.id.left_layout).setVisibility(View.GONE);
        mSwipeRefreshLayout = findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);

        linear_layout_item = findViewById(R.id.linear_layout_content);
        linear_layout_item.removeAllViews();
        recycler_banner = findViewById(R.id.recycler_banner);
        bannerBeans = new ArrayList<>();
        mzBannerAdapter = new MzBannerAdapter(mContext, bannerBeans);
        recycler_banner.setmBannerAdapter(mzBannerAdapter);

        onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromNet(true);

            }
        };
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        loadHandler.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true); //手动开启小圆圈loading，但是不会执行刷新数据的监听
                onRefreshListener.onRefresh();           //所有要手动调用回调，

            }
        });
    }

    /**
     * 创建类型
     */
    private View createItemView(int indext, RItemBookEntity itemBookEntity) {
        View itemView = View.inflate(mContext, R.layout.title_recycler_view, null);
        TextView tvTitle = itemView.findViewById(R.id.tv_recycler_title);
        RecyclerView rvContent = itemView.findViewById(R.id.recycler_content);
        rvContent.setLayoutManager(new MyGridLayoutManager(mContext, 3) {
            @Override
            public boolean canScrollVertically() {

                return false;
            }
        });
        rvContent.setPadding(UIHelper.dip2px(5), 0, UIHelper.dip2px(10), 0);
        tvTitle.setText(itemBookEntity.getTitle());
        BaseQuickAdapter<BookEntity, BaseViewHolder> adapter;
        switch (indext) {
            case 0:
                adapter = new RBookAdapter(new ArrayList<BookEntity>());
                List<BookEntity> bookEntities = itemBookEntity.getBookEntities();
                if (bookEntities != null && bookEntities.size() != 0) {
                    BookEntity bookEntity = bookEntities.get(0);
                    View viewHeader = View.inflate(mContext, R.layout.header_recycler_item, null);
                    ImageView img_book_pic = viewHeader.findViewById(R.id.img_book_pic);
                    TextView tv_book_name = viewHeader.findViewById(R.id.tv_book_name);
                    tv_book_name.setText(bookEntity.getTitle());
                    TextView tv_book_describe = viewHeader.findViewById(R.id.tv_book_describe);
                    tv_book_describe.setText(bookEntity.getSummary());
                    TextView tv_book_author = viewHeader.findViewById(R.id.tv_book_author);
                    tv_book_author.setText(bookEntity.getAuthor());
                    GlideUtils.getInstance().setImageView(mContext, bookEntity.getImages_large(), img_book_pic);
                    adapter.addHeaderView(viewHeader);
                    itemBookEntity.getBookEntities().remove(0);

                }
                rvContent.setAdapter(adapter);
                adapter.setNewData(itemBookEntity.getBookEntities());

                break;
            case 4:
                adapter = new RBookListAdapter(new ArrayList<BookEntity>());
                rvContent.setLayoutManager(new MyLinearLayoutManager(mContext) {
                    @Override
                    public boolean canScrollVertically() {

                        return false;
                    }
                });
                rvContent.addItemDecoration(new RecyclerItemDecoration(mContext));
                rvContent.setAdapter(adapter);
                adapter.setNewData(itemBookEntity.getBookEntities());

                break;
            default:
                adapter = new RBookAdapter(new ArrayList<BookEntity>());
                rvContent.addItemDecoration(new RecyclerItemDecoration(mContext//
                        , RecyclerItemDecoration.VERTICAL_LIST, 10, R.color.transparent));

                rvContent.setAdapter(adapter);
                adapter.setNewData(itemBookEntity.getBookEntities());

                break;
        }
        rvContent.addItemDecoration(new RecyclerItemDecoration(mContext//
                , RecyclerItemDecoration.HORIZONTAL_LIST, 10, R.color.transparent));


        return itemView;
    }

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        HttpParams params = new HttpParams();
        params.put("tabId", "0");
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_RECOMMENT_R_BOOK, new JsonCallBack<ResultBean<List<RItemBookEntity>>>() {
            @Override
            public void onSuccess(ResultBean<List<RItemBookEntity>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                List<RItemBookEntity> rItemBookEntities = mResultBean.data;
                if (rItemBookEntities == null || rItemBookEntities.size() == 0) {

                    return;
                }
                linear_layout_item.removeAllViews();
                for (int i = 0; i < rItemBookEntities.size(); i++) {
                    linear_layout_item.addView(createItemView(i, rItemBookEntities.get(i)));

                }
            }

            @Override
            public void onError(ResultBean<List<RItemBookEntity>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });
    }
}
