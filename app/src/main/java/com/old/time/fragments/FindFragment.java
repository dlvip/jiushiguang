package com.old.time.fragments;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dueeeke.videoplayer.demo.DataUtil;
import com.dueeeke.videoplayer.demo.VideoBean;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.old.time.R;
import com.old.time.activitys.TalksActivity;
import com.old.time.activitys.TopicsActivity;
import com.old.time.activitys.VideoPagerActivity;
import com.old.time.activitys.VideosActivity;
import com.old.time.adapters.TalkAdapter;
import com.old.time.adapters.VideoFindAdapter;
import com.old.time.beans.EventBean;
import com.old.time.beans.ResultBean;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.MyGridLayoutManager;
import com.old.time.utils.MyLinearLayoutManager;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NING on 2018/3/5.
 */

public class FindFragment extends CBaseFragment {

    private List<VideoBean> videoBeans;
    private VideoFindAdapter vFAdapter;
    private RecyclerView recycler_view_video;

    private BaseQuickAdapter<EventBean, BaseViewHolder> mAdapter;
    private List<EventBean> eventBeans;

    private TextView tv_talk_title;
    private RecyclerView talkRecycler;
    private TalkAdapter talkAdapter;

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        eventBeans = new ArrayList<>();
        mAdapter = new BaseQuickAdapter<EventBean, BaseViewHolder>(R.layout.adapter_find_type_pic, eventBeans) {

            @Override
            protected void convert(BaseViewHolder helper, EventBean item) {
                helper.setText(R.id.tv_event_title, item.getTitle()).setText(R.id.tv_event_price, "￥ " + item.getPrice()).setText(R.id.tv_join_count, item.getJoinCount() + " 人参与");
                ImageView img_event_pic = helper.getView(R.id.img_event_pic);
                GlideUtils.getInstance().setImageView(mContext, item.getPicUrl(), img_event_pic);

            }
        };
        View headerView = View.inflate(mContext, R.layout.header_find, null);
        recycler_view_video = headerView.findViewById(R.id.recycler_view_video);
        recycler_view_video.setLayoutManager(new MyGridLayoutManager(mContext, 5));
        recycler_view_video.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.HORIZONTAL_LIST, 10));
        videoBeans = DataUtil.getVideoPagersList();
        vFAdapter = new VideoFindAdapter(videoBeans);
        recycler_view_video.setAdapter(vFAdapter);

        View talkView = headerView.findViewById(R.id.include_talk);
        tv_talk_title = talkView.findViewById(R.id.tv_recycler_title);
        tv_talk_title.setText("热议话题");
        talkRecycler = talkView.findViewById(R.id.recycler_content);
        talkRecycler.setLayoutManager(new MyLinearLayoutManager(mContext, LinearLayout.HORIZONTAL, false));
        talkAdapter = new TalkAdapter(R.layout.adapter_find_talk, strings);
        talkRecycler.setAdapter(talkAdapter);
        SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);
        snapHelperStart.attachToRecyclerView(talkRecycler);
        headerView.findViewById(R.id.tv_topic_more).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TopicsActivity.startTopicsActivity(mContext);

            }
        });

        headerView.findViewById(R.id.linear_layout_more).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TalksActivity.startTalksActivity(mContext);

            }
        });
        headerView.findViewById(R.id.linear_layout_find_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideosActivity.startVideosActivity(mContext);

            }
        });

        mAdapter.addHeaderView(headerView);
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, VideoPagerActivity.class);
                ActivityUtils.startActivity(mContext, intent);

            }
        });
    }

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        OkGoUtils.getInstance().postNetForData(Constant.GET_EVENT_LIST, new JsonCallBack<ResultBean<List<EventBean>>>() {
            @Override
            public void onSuccess(ResultBean<List<EventBean>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (isRefresh) {
                    eventBeans.clear();
                    mAdapter.setNewData(eventBeans);

                }
                if (mResultBean.status == Constant.STATUS_FRIEND_00) {
                    eventBeans.addAll(mResultBean.data);
                    mAdapter.setNewData(eventBeans);

                } else {
                    UIHelper.ToastMessage(mContext, mResultBean.msg);

                }
            }

            @Override
            public void onError(ResultBean<List<EventBean>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                UIHelper.ToastMessage(mContext, mResultBean.msg);

            }
        });
    }
}
