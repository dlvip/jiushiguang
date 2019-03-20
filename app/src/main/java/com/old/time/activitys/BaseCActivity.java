package com.old.time.activitys;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.old.time.R;
import com.old.time.constants.Code;
import com.old.time.utils.MyLinearLayoutManager;
import com.old.time.utils.ScreenTools;
import com.old.time.utils.UIHelper;
import com.old.time.views.SuspensionPopupWindow;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseCActivity extends BaseActivity {

    public BaseActivity mContext;

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
        for (int i = 0; i < 10; i++) {
            strings.add(TAG);

        }
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        linear_layout_more = findViewById(R.id.linear_layout_more);
        layoutParams = (LinearLayout.LayoutParams) linear_layout_more.getLayoutParams();
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;

        mSwipeRefreshLayout = findViewById(R.id.swipeLayout);
        mRecyclerView = findViewById(R.id.c_recycler_view);
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecyclerView.setLayoutManager(new MyLinearLayoutManager(mContext));
        mSwipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light, R.color.holo_orange_light, R.color.holo_red_light);
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

    /**
     * 定位显示哪一个item
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

    @Override
    protected void initEvent() {

    }

    private int W, H;
    private int showX, showY;
    private SuspensionPopupWindow mSuspensionPopupWindow;

    /**
     * 发送内容入口
     */
    public void showSuspensionPopupWindow() {
        if (mSuspensionPopupWindow == null) {
            ScreenTools mScreenTools = ScreenTools.instance(this);
            W = mScreenTools.getScreenWidth();
            H = mScreenTools.getScreenHeight();
            showX = W / 2 - UIHelper.dip2px(40);
            showY = H - UIHelper.dip2px(80);
            mSuspensionPopupWindow = new SuspensionPopupWindow(this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setmSuspensionPopupWindowClick();

                }
            });
        }
        mSuspensionPopupWindow.showAtLocationXY(getWindow().getDecorView(), Gravity.TOP, showX, showY);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSuspensionPopupWindow != null) {
            mSuspensionPopupWindow.dismiss();
            mSuspensionPopupWindow = null;

        }
    }

    /**
     * 按钮点击事件
     */
    public void setmSuspensionPopupWindowClick() {


    }


    public abstract void getDataFromNet(boolean isRefresh);

}
