package com.old.time.fragments;

import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.VideoUtils.NiceVideoPlayer;
import com.old.time.VideoUtils.NiceVideoPlayerManager;
import com.old.time.beans.VideoBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.Http;
import com.old.time.okhttps.exception.ApiException;
import com.old.time.okhttps.subscriber.CommonSubscriber;
import com.old.time.okhttps.transformer.CommonTransformer;
import com.old.time.utils.DebugLog;
import com.old.time.utils.MapParams;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.ScreenTools;
import com.old.time.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NING on 2018/6/27.
 */
public class VideoListFragment extends CBaseFragment {

    private static final String TAG = "VideoListFragment";
    private List<VideoBean> videoBeans = new ArrayList<>();

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        MapParams params = new MapParams();
        Http.getHttpService().getVideoList(Constant.GET_VIDEO_LIST, params.getParamString()).compose(new CommonTransformer<List<VideoBean>>()).subscribe(new CommonSubscriber<List<VideoBean>>(mContext) {
            @Override
            public void onNext(List<VideoBean> videoList) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (isRefresh) {
                    videoBeans.clear();
                    mAdapter.setNewData(videoBeans);

                }
                mAdapter.addData(videoList);
            }

            @Override
            protected void onError(ApiException e) {
                super.onError(e);
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    private BaseQuickAdapter<VideoBean, BaseViewHolder> mAdapter;

    private int firstItemPosition = 0;
    private int windowHeight;

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        mAdapter = new BaseQuickAdapter<VideoBean, BaseViewHolder>(R.layout.adapter_video_list, videoBeans) {

            @Override
            protected void convert(BaseViewHolder helper, VideoBean item) {
                if (windowHeight == 0) {
                    windowHeight = ScreenTools.instance(mContext).getScreenHeight();
                    DebugLog.d(TAG, "windowHeight=" + windowHeight);

                }
                NiceVideoPlayer mNiceVideoPlayer = helper.getView(R.id.nineImageView);
                mNiceVideoPlayer.setDataForView(item);
                if (firstItemPosition == helper.getLayoutPosition()) {
                    mNiceVideoPlayer.start();

                } else if (mNiceVideoPlayer.isPlaying()) {
                    mNiceVideoPlayer.release();

                }
            }
        };
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        firstItemPosition = mLayoutManager.findFirstVisibleItemPosition();
                        if (firstItemPosition + 1 < mAdapter.getData().size()) {
                            firstItemPosition = firstItemPosition + 1;

                        }
                        mAdapter.notifyItemChanged(firstItemPosition);

                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                DebugLog.d(TAG, "dy=" + dy);
                if (dy > UIHelper.dip2px(200)) {


                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();

    }
}
