package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dueeeke.videoplayer.controller.StandardVideoController;
import com.dueeeke.videoplayer.player.IjkPlayer;
import com.dueeeke.videoplayer.player.IjkVideoView;
import com.dueeeke.videoplayer.player.PlayerConfig;
import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.beans.EpisodeEntity;
import com.old.time.beans.ResultBean;
import com.old.time.beans.VideoBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.MyLinearLayoutManager;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;

public class VideoDetailActivity extends BaseActivity {

    public static String VIDEO_ID = "videoId";

    /**
     * 视频详情页
     *
     * @param mContext
     */
    public static void startVideoDetailActivity(Context mContext, String videoId) {
        Intent intent = new Intent(mContext, VideoDetailActivity.class);
        intent.putExtra(VIDEO_ID, videoId);
        ActivityUtils.startLoginActivity((Activity) mContext, intent);

    }

    private String videoId;

    private IjkVideoView mMNVideoPlayer;

    private BaseQuickAdapter<EpisodeEntity, BaseViewHolder> pAdapter;

    private TextView tv_video_name, tv_video_type;

    @Override
    protected void initView() {
        videoId = getIntent().getStringExtra(VIDEO_ID);
        setTitleText("");
        findViewById(R.id.header_main).setBackgroundResource(R.color.transparent);
        findViewById(R.id.view_line).setBackgroundResource(R.color.transparent);
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);

        tv_video_name = findViewById(R.id.tv_video_name);
        tv_video_type = findViewById(R.id.tv_video_type);


        mMNVideoPlayer = findViewById(R.id.video_player);
        StandardVideoController controller = new StandardVideoController(this);
        mMNVideoPlayer.setPlayerConfig(new PlayerConfig.Builder().autoRotate()//
                .savingProgress().setCustomMediaPlayer(new IjkPlayer(this)).build());
        mMNVideoPlayer.setVideoController(controller);

        //选集
        RecyclerView pRecyclerView = findViewById(R.id.recycler_view);
        pRecyclerView.setLayoutManager(new MyLinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        pRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.HORIZONTAL_LIST, 10));
        pAdapter = new BaseQuickAdapter<EpisodeEntity, BaseViewHolder>(R.layout.adapter_video_item) {
            @Override
            protected void convert(BaseViewHolder helper, EpisodeEntity item) {
                int position = helper.getLayoutPosition() - pAdapter.getHeaderLayoutCount() + 1;
                if (position > 9) {
                    helper.setText(R.id.tv_video_position, String.valueOf(helper.getLayoutPosition()));

                } else {
                    helper.setText(R.id.tv_video_position, "0" + String.valueOf(helper.getLayoutPosition()));

                }
            }
        };
        pRecyclerView.setAdapter(pAdapter);

        getVideoDetail();
    }

    /**
     * 获取视频信息
     */
    private void getVideoDetail() {
        HttpParams params = new HttpParams();
        params.put("videoId", videoId);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_VIDEO_DETAIL, new JsonCallBack<ResultBean<VideoBean>>() {
            @Override
            public void onSuccess(ResultBean<VideoBean> mResultBean) {
                setDataForView(mResultBean.data);

            }

            @Override
            public void onError(ResultBean<VideoBean> mResultBean) {
                UIHelper.ToastMessage(mContext, mResultBean.msg);
                ActivityUtils.finishActivity(mContext);

            }
        });
    }

    /**
     * 设置数据
     */
    private void setDataForView(VideoBean mVideoBean) {
        if (mVideoBean == null) {

            return;
        }
        tv_video_name.setText(mVideoBean.getName());
        tv_video_type.setText(mVideoBean.getVideoTypeStr());
        pAdapter.setNewData(mVideoBean.getEpisodeEntities());
        if (pAdapter.getData().size() > 0) {
            mMNVideoPlayer.setUrl(pAdapter.getData().get(0).getUrl());
            mMNVideoPlayer.start();

        }
    }

    @Override
    public void back(View view) {
        ActivityUtils.finishLoginActivity(mContext);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMNVideoPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMNVideoPlayer.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMNVideoPlayer.release();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_video_detail;
    }
}
