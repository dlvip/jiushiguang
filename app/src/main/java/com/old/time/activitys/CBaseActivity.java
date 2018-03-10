package com.old.time.activitys;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.old.time.R;
import com.old.time.constants.Code;
import com.old.time.utils.MyLinearLayoutManager;
import com.old.time.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;

public abstract class CBaseActivity extends BaseActivity {

    public BaseActivity mContext;

    public MyLinearLayoutManager mLayoutManager;
    public SwipeRefreshLayout.OnRefreshListener onRefreshListener;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public RecyclerView mRecyclerView;
    public LinearLayout linear_layout_more;
    public LinearLayout.LayoutParams layoutParams;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Code.HIDE_LOADING:
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

    public List<String> strings = new ArrayList<>();

    @Override
    protected int getLayoutID() {
        return R.layout.activity_cbase;
    }

    @Override
    protected void initView() {
        mContext = this;

        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        linear_layout_more = (LinearLayout) findViewById(R.id.linear_layout_more);
        layoutParams = (LinearLayout.LayoutParams) linear_layout_more.getLayoutParams();
        layoutParams.height = UIHelper.dip2px(50);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.c_recycler_view);
        mLayoutManager = new MyLinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromNet(true);

            }
        };
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        //初始化的时候自动加载
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                onRefreshListener.onRefresh();
            }
        });
    }

    public void seleteToPosition(int position) {
        if (mRecyclerView == null) {

            return;
        }
        LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        manager.scrollToPositionWithOffset(position, 0);
    }

    @Override
    protected void initEvent() {

    }


    public abstract void getDataFromNet(boolean isRefresh);

}
