package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.old.time.R;
import com.old.time.beans.VideosBean;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.DataUtils;
import com.old.time.utils.MyGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class VideosActivity extends BaseCActivity {

    public static void startVideosActivity(Context mContext) {
        Intent intent = new Intent(mContext, VideosActivity.class);
        ActivityUtils.startActivity((Activity) mContext, intent);

    }

    private List<VideosBean> videosBeans = new ArrayList<>();
    private BaseQuickAdapter<VideosBean, BaseViewHolder> adapter;

    @Override
    protected void initView() {
        super.initView();
        findViewById(R.id.left_layout).setVisibility(View.GONE);
        mRecyclerView.setLayoutManager(new MyGridLayoutManager(mContext, 3));
        adapter = new BaseQuickAdapter<VideosBean, BaseViewHolder>(R.layout.adapter_videos, videosBeans) {
            @Override
            protected void convert(BaseViewHolder helper, VideosBean item) {
                ImageView img_video_pic = helper.getView(R.id.img_video_pic);
                GlideUtils.getInstance().setImageView(mContext, item.getD_pic(), img_video_pic);
                helper.setText(R.id.tv_video_name, item.getD_name());

            }
        };
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                VideoDetailActivity.startVideoDetailActivity(mContext);

            }
        });
    }

    @Override
    public void getDataFromNet(boolean isRefresh) {
        mSwipeRefreshLayout.setRefreshing(false);
        videosBeans.clear();
        videosBeans.addAll(DataUtils.getVideosBeans("mp4",mContext));
        adapter.setNewData(videosBeans);

    }
}
