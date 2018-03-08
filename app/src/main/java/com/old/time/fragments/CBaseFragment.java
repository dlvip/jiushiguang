package com.old.time.fragments;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;

import com.old.time.R;
import com.old.time.constants.Code;
import com.old.time.constants.Constant;
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

    @Override
    protected int setContentView() {

        return R.layout.fragment_c_base;
    }

    @Override
    protected void lazyLoad() {
        strings.clear();
        strings.add(Constant.PHOTO_PIC_URL);
        strings.add(Constant.PHOTO_PIC_URL);
        strings.add(Constant.PHOTO_PIC_URL);
        strings.add(Constant.PHOTO_PIC_URL);
        strings.add(Constant.PHOTO_PIC_URL);
        strings.add(Constant.PHOTO_PIC_URL);
        strings.add(Constant.PHOTO_PIC_URL);
        strings.add(Constant.PHOTO_PIC_URL);
        strings.add(Constant.PHOTO_PIC_URL);
        strings.add(Constant.PHOTO_PIC_URL);
        strings.add(Constant.PHOTO_PIC_URL);
        strings.add(Constant.PHOTO_PIC_URL);
        strings.add(Constant.PHOTO_PIC_URL);
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
