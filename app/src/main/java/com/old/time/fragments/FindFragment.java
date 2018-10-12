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
import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.activitys.TopicsActivity;
import com.old.time.activitys.VideoPagerActivity;
import com.old.time.activitys.VideosActivity;
import com.old.time.activitys.WebViewActivity;
import com.old.time.adapters.TopicAdapter;
import com.old.time.adapters.VideoFindAdapter;
import com.old.time.beans.ActionBean;
import com.old.time.beans.ResultBean;
import com.old.time.beans.TopicBean;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.MyGridLayoutManager;
import com.old.time.utils.MyLinearLayoutManager;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;
import com.old.time.utils.UserLocalInfoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NING on 2018/3/5.
 */
public class FindFragment extends CBaseFragment {

    private List<VideoBean> videoBeans;
    private VideoFindAdapter vFAdapter;
    private RecyclerView recycler_view_video;

    private BaseQuickAdapter<ActionBean, BaseViewHolder> mAdapter;
    private List<ActionBean> actionBeans;

    private TextView tv_talk_title;
    private RecyclerView talkRecycler;
    private TopicAdapter topicAdapter;
    private List<TopicBean> topicBeans = new ArrayList<>();

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        actionBeans = new ArrayList<>();
        mAdapter = new BaseQuickAdapter<ActionBean, BaseViewHolder>(R.layout.adapter_find_type_pic, actionBeans) {

            @Override
            protected void convert(BaseViewHolder helper, ActionBean item) {
                helper.setText(R.id.tv_event_title, item.getTitle())//
//                        .setText(R.id.tv_event_price, item.getPriceStr())//
                        .setText(R.id.tv_join_count, "0 人参与");
                ImageView img_event_pic = helper.getView(R.id.img_event_pic);
                GlideUtils.getInstance().setImageView(mContext, item.getPic(), img_event_pic);

            }
        };
        View headerView = View.inflate(mContext, R.layout.header_find, null);
        recycler_view_video = headerView.findViewById(R.id.recycler_view_video);
        recycler_view_video.setLayoutManager(new MyGridLayoutManager(mContext, 5));
        recycler_view_video.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.HORIZONTAL_LIST, 10));
        videoBeans = DataUtil.getVideoPagersList();
        vFAdapter = new VideoFindAdapter(videoBeans);
        recycler_view_video.setAdapter(vFAdapter);

        View linear_layout_item = headerView.findViewById(R.id.linear_layout_item);
        tv_talk_title = linear_layout_item.findViewById(R.id.tv_recycler_title);
        tv_talk_title.setText("热议话题");
        talkRecycler = linear_layout_item.findViewById(R.id.recycler_content);
        talkRecycler.setLayoutManager(new MyLinearLayoutManager(mContext, LinearLayout.VERTICAL, false));
        topicAdapter = new TopicAdapter(topicBeans);
        talkRecycler.setAdapter(topicAdapter);
        SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);
        snapHelperStart.attachToRecyclerView(talkRecycler);

        linear_layout_item.findViewById(R.id.linear_layout_more).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TopicsActivity.startTopicsActivity(mContext);

            }
        });
        headerView.findViewById(R.id.linear_layout_find_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideosActivity.startVideosActivity(mContext);

            }
        });

        mAdapter.addHeaderView(headerView);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setHeaderAndEmpty(true);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                WebViewActivity.startWebViewActivity(mContext, Constant.mHomeUrl, 0);

            }
        });
    }

    private int pageNum = 0;

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        getTopicList();
        HttpParams params = new HttpParams();
        params.put("userId", UserLocalInfoUtils.instance().getUserId());
        params.put("pageNum", pageNum);
        params.put("pageSize", Constant.PageSize);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_ACTION_LIST, new JsonCallBack<ResultBean<List<ActionBean>>>() {
            @Override
            public void onSuccess(ResultBean<List<ActionBean>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (isRefresh) {
                    actionBeans.clear();
                    mAdapter.setNewData(actionBeans);

                }
                if (mResultBean.status == Constant.STATUS_FRIEND_00) {
                    actionBeans.addAll(mResultBean.data);
                    mAdapter.setNewData(actionBeans);

                } else {
                    UIHelper.ToastMessage(mContext, mResultBean.msg);

                }
            }

            @Override
            public void onError(ResultBean<List<ActionBean>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                UIHelper.ToastMessage(mContext, mResultBean.msg);

            }
        });
    }

    /**
     * 获取话题列表
     */
    private void getTopicList() {
        HttpParams params = new HttpParams();
        params.put("userId", UserLocalInfoUtils.instance().getUserId());
        params.put("pageNum", "0");
        params.put("pageSize", "5");
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_TOPIC_LIST, new JsonCallBack<ResultBean<List<TopicBean>>>() {
            @Override
            public void onSuccess(ResultBean<List<TopicBean>> mResultBean) {
                if (mResultBean != null && mResultBean.data != null && mResultBean.data.size() > 0) {
                    topicAdapter.setNewData(mResultBean.data);

                }
            }

            @Override
            public void onError(ResultBean<List<TopicBean>> mResultBean) {

            }
        });
    }
}
