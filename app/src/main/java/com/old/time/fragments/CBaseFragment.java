package com.old.time.fragments;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;

import com.old.time.R;
import com.old.time.constants.Code;
import com.old.time.utils.DebugLog;
import com.old.time.utils.MyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diliang on 2017/1/16.
 */
public abstract class CBaseFragment extends BaseFragment {

    public SwipeRefreshLayout.OnRefreshListener onRefreshListener;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public RecyclerView mRecyclerView;
    public LinearLayoutManager mLayoutManager;
    public List<String> strings = new ArrayList<>();
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

    public static String picUrl = "http://www.baidu.com/baidu.php?url=K00000jjAivt4-iQGnRI8d9h-XjsEBEhIA1yk9Ixhn6KLcQkSpcbjDqHNUPBzVQXRoYSFvrD8pAJLkShkWQ_2QkQBpAZTsAndhtJQUBK20sbbqqbhaLw2rEcKKpXI29VaO1vZjPik3IpTINDr87KVh392jx0iaIPe75PtH9E_tWz5L9DZf.7Y_ig4x2qd7YHlmrA9Kb_ovyTTMuKLWI6h9ikgdWI6h9HbmOxOeZlZOm4T5M33xU43I-hZ1tT5oeT5VlT5M_sSLW9vU_5Mvmxgl3xU4JglyIiUr2s1f_UVh1IyC.U1Yz0ZDqE5QRE0KY5U1zdQ1g_PjX0A-V5HnvPsKM5g93nfKdpHY0TA-b5HD0mv-b5Hn3nsKVIjYznj640AVG5H00TMfqrHTk0ANGujY0mhbqn0KVm1Ys0Z7spyfqn0Kkmv-b5H00ThIYmyTqn0KEIhsq0AdGujYs0A-kIjYs0A7B5HKxn0K-ThTqn0KsTjYs0A4vTjYsQW0snj0snj0s0AdYTjYs0AwbUL0qn0KzpWYs0AuY5H00TA6qn0KET1Ys0AFL5H00UMfqn0K1XWY0ThNkIjYkPHcsn10Ln1m1rHbY0ZPGujdWPj0vnjIbryc3m179uH--0A7W5HD0XMfqn0KVmdqhThqV5H00mLFW5HRzPWfL";

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

        //去除recyclerView 默认动画
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecyclerView.getItemAnimator().setChangeDuration(0);

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
    }

    /**
     * 互动到哪一条
     *
     * @param position
     */
    public void seleteToPosition(int position) {
        if (mRecyclerView == null) {

            return;
        }
        LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        manager.scrollToPositionWithOffset(position, 0);
    }
}
