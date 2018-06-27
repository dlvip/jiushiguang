package com.old.time.fragments;

import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.VideoUtils.NiceVideoPlayer;
import com.old.time.VideoUtils.NiceVideoPlayerManager;
import com.old.time.VideoUtils.TxVideoPlayerController;
import com.old.time.constants.Constant;
import com.old.time.utils.DebugLog;
import com.old.time.utils.RecyclerItemDecoration;

/**
 * Created by NING on 2018/6/27.
 */

public class VideoListFragment extends CBaseFragment {

    private static final String TAG = "VideoListFragment";

    @Override
    public void getDataFromNet(boolean isRefresh) {
        mSwipeRefreshLayout.setRefreshing(false);

    }

    private BaseQuickAdapter<String, BaseViewHolder> mAdapter;

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        mAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.adapter_video_list, strings) {

            @Override
            protected void convert(BaseViewHolder helper, String item) {
                NiceVideoPlayer mNiceVideoPlayer = helper.getView(R.id.nineImageView);
                mNiceVideoPlayer.setDataForView(Constant.MP4_PATH_URL, item);

            }
        };
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                DebugLog.d(TAG, "newState=" + newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                DebugLog.d(TAG, "dx=" + dx + ":::dy=" + dy);

            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();

    }
}
