package com.old.time.activitys;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.constants.Code;
import com.old.time.utils.MyLinearLayoutManager;
import com.old.time.utils.ScreenTools;
import com.old.time.utils.UIHelper;
import com.old.time.views.CustomNetView;

/**
 * Created by NING on 2018/3/22.
 */

public abstract class BaseSActivity extends BaseActivity {

    public BaseActivity mContext;

    public MyLinearLayoutManager mLayoutManager;
    public SwipeRefreshLayout.OnRefreshListener onRefreshListener;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public RecyclerView mRecyclerView;
    public CustomNetView mCustomNetView;

    public LinearLayout linear_layout_more;
    public RelativeLayout.LayoutParams layoutParams;
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

    public int height;
    private View header_main, view_line;
    private TextView tv_title;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_sbase;
    }

    @Override
    protected void initView() {
        mContext = this;
        height = ScreenTools.instance(mContext).getScreenWidth();
        header_main = findViewById(R.id.header_main);
        view_line = findViewById(R.id.view_line);
        tv_title = findViewById(R.id.top_title);
        tv_title.setBackgroundResource(R.color.transparent);
        view_line.setBackgroundResource(R.color.transparent);
        header_main.setBackgroundResource(R.color.transparent);
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        linear_layout_more = findViewById(R.id.linear_layout_more);
        layoutParams = (RelativeLayout.LayoutParams) linear_layout_more.getLayoutParams();
        layoutParams.height = UIHelper.dip2px(50);
        linear_layout_more.setLayoutParams(layoutParams);

        mSwipeRefreshLayout = findViewById(R.id.swipeLayout);
        mRecyclerView = findViewById(R.id.c_recycler_view);
        mLayoutManager = new MyLinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
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

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int y = getScrollYDistance();
                if (y <= 10) {   //设置标题的背景颜色
                    header_main.setBackgroundResource(R.color.transparent);
                    view_line.setBackgroundResource(R.color.transparent);
                    tv_title.setTextColor(getResources().getColor(R.color.transparent));

                } else if (y > 10 && y <= height) { //滑动距离小于图的高度时，设置背景和字体颜色颜色透明度渐变
                    float scale = (float) y / height;
                    float alpha = (255 * scale);
                    tv_title.setTextColor(Color.argb((int) alpha, 0, 0, 0));
                    header_main.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                    view_line.setBackgroundColor(Color.argb((int) alpha, 239, 239, 239));

                } else {    //滑动到图下面设置普通颜色
                    header_main.setBackgroundResource(R.color.color_fff);
                    view_line.setBackgroundResource(R.color.line_bg);
                    tv_title.setTextColor(getResources().getColor(R.color.color_000));

                }
            }
        });

        mCustomNetView = new CustomNetView(mContext, CustomNetView.NO_DATA);
    }

    //监听recyclerView 的高度变化
    public int getScrollYDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisibleChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisibleChildView.getHeight();

        return (position) * itemHeight - firstVisibleChildView.getTop();
    }


    public void selectToPosition(int position) {
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
