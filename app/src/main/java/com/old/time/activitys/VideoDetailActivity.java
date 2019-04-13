package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dueeeke.videoplayer.controller.StandardVideoController;
import com.dueeeke.videoplayer.player.IjkPlayer;
import com.dueeeke.videoplayer.player.IjkVideoView;
import com.dueeeke.videoplayer.player.PlayerConfig;
import com.old.time.R;
import com.old.time.adapters.VideoFindAdapter;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.DataUtils;
import com.old.time.utils.MyGridLayoutManager;
import com.old.time.utils.MyLinearLayoutManager;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.StringUtils;

public class VideoDetailActivity extends BaseActivity {

    public static String PLAY_URL = "playUrl";

    /**
     * 视频详情页
     *
     * @param mContext
     */
    public static void startVideoDetailActivity(Context mContext, String playUrl) {
        Intent intent = new Intent(mContext, VideoDetailActivity.class);
        intent.putExtra(PLAY_URL, playUrl);
        ActivityUtils.startLoginActivity((Activity) mContext, intent);

    }

    private IjkVideoView mMNVideoPlayer;

    private RecyclerView dRecyclerView;
    private BaseQuickAdapter<String, BaseViewHolder> dAdapter;

    private RecyclerView pRecyclerView;
    private BaseQuickAdapter<String, BaseViewHolder> pAdapter;

    @Override
    protected void initView() {
        setTitleText("");
        findViewById(R.id.header_main).setBackgroundResource(R.color.transparent);
        findViewById(R.id.view_line).setBackgroundResource(R.color.transparent);
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        mMNVideoPlayer = findViewById(R.id.video_player);
        StandardVideoController controller = new StandardVideoController(this);
        mMNVideoPlayer.setPlayerConfig(new PlayerConfig.Builder().autoRotate()//
                .savingProgress().setCustomMediaPlayer(new IjkPlayer(this)).build());
        mMNVideoPlayer.setUrl(getIntent().getStringExtra(PLAY_URL));
        mMNVideoPlayer.setVideoController(controller);
        mMNVideoPlayer.start();

        //选集
        pRecyclerView = findViewById(R.id.recycler_view);
        pRecyclerView.setLayoutManager(new MyLinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        pRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.HORIZONTAL_LIST, 10));
        pAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.adapter_video_item, DataUtils.getDateStrings(30)) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                int position = helper.getLayoutPosition() - pAdapter.getHeaderLayoutCount() + 1;
                if (position > 9) {
                    helper.setText(R.id.tv_video_position, String.valueOf(helper.getLayoutPosition()));

                } else {
                    helper.setText(R.id.tv_video_position, "0" + String.valueOf(helper.getLayoutPosition()));

                }
            }
        };
        pRecyclerView.setAdapter(pAdapter);

        //推荐
        dRecyclerView = findViewById(R.id.recycler_view_detail);
        dRecyclerView.setLayoutManager(new MyLinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        dRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.HORIZONTAL_LIST, 10));
        dAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.adapter_video_detail, DataUtils.getDateStrings(20)) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                ImageView img_find_video = helper.getView(R.id.img_find_video);
                GlideUtils.getInstance().setImageView(mContext, Constant.PHOTO_PIC_URL, img_find_video);

            }
        };
        dRecyclerView.setAdapter(dAdapter);
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
