package com.old.time.fragments;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.widget.LinearLayout;

import com.old.time.R;
import com.old.time.constants.Code;
import com.old.time.utils.DebugLog;
import com.old.time.utils.MyLinearLayoutManager;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;
import com.old.time.views.CustomNetView;

/**
 * Created by diliang on 2017/1/16.
 */
public abstract class CBaseFragment extends BaseFragment {

    public SwipeRefreshLayout.OnRefreshListener onRefreshListener;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public RecyclerView mRecyclerView;
    public CustomNetView mCustomNetView;

    public LinearLayoutManager mLayoutManager;
    public LinearLayout linear_layout_more;
    public LinearLayout.LayoutParams layoutParams;
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

    @Override
    protected int setContentView() {

        return R.layout.fragment_c_base;
    }

    @Override
    protected void lazyLoad() {
        mSwipeRefreshLayout = findViewById(R.id.swipeLayout);
        mRecyclerView = findViewById(R.id.c_recycler_view);
        mLayoutManager = new MyLinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        linear_layout_more = findViewById(R.id.linear_layout_more);
        layoutParams = (LinearLayout.LayoutParams) linear_layout_more.getLayoutParams();
        layoutParams.height = UIHelper.dip2px(50);

        //去除recyclerView 默认动画
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecyclerView.getItemAnimator().setChangeDuration(0);
        mRecyclerView.setLayoutManager(new MyLinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext));
        mSwipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);

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
        mRecyclerView.getAdapter();
        mCustomNetView = new CustomNetView(mContext, CustomNetView.NO_DATA);
    }

    /**
     * 滑动到哪一条
     *
     * @param position
     */
    public void seleteToPosition(int position) {
        if (mRecyclerView == null) {

            return;
        }
        mRecyclerView.stopScroll();
        LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        manager.scrollToPositionWithOffset(position, 0);
    }
}
