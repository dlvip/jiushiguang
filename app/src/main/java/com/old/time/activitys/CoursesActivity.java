package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.adapters.CourseAdapter;
import com.old.time.beans.CourseBean;
import com.old.time.beans.ResultBean;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.interfaces.ImageDownLoadCallBack;
import com.old.time.mp3Utils.MusicService;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.SpUtils;
import com.old.time.utils.UIHelper;
import com.old.time.views.CompletedView;
import com.old.time.views.CustomNetView;

import java.util.ArrayList;
import java.util.List;

public class CoursesActivity extends CBaseActivity {

    public static void startCoursesActivity(Activity mContext) {
        Intent intent = new Intent(mContext, CoursesActivity.class);
        ActivityUtils.startActivity(mContext, intent);

    }

    private CustomNetView mCustomNetView;
    private CourseAdapter courseAdapter;
    private List<CourseBean> courseBeans;

    private View play_music_bottom;
    private TextView tv_music_title;
    private CompletedView tasks_view;
    private FrameLayout frame_layout_play;
    private ImageView img_play_btn, img_music_pic;

    @Override
    protected void initView() {
        super.initView();
        linear_layout_more.setVisibility(View.VISIBLE);
        layoutParams.height = UIHelper.dip2px(60);
        linear_layout_more.setLayoutParams(layoutParams);
        linear_layout_more.removeAllViews();
        play_music_bottom = View.inflate(mContext, R.layout.play_music_bottom, null);
        img_music_pic = play_music_bottom.findViewById(R.id.img_music_pic);
        tv_music_title = play_music_bottom.findViewById(R.id.tv_music_title);
        tasks_view = play_music_bottom.findViewById(R.id.tasks_view);
        img_play_btn = play_music_bottom.findViewById(R.id.img_play_btn);
        frame_layout_play = play_music_bottom.findViewById(R.id.frame_layout_play);
        linear_layout_more.addView(play_music_bottom);
        tasks_view.setProgress(20);

        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        courseBeans = new ArrayList<>();
        courseAdapter = new CourseAdapter(courseBeans);
        mRecyclerView.setAdapter(courseAdapter);
        courseAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getDataFromNet(false);

            }
        }, mRecyclerView);
        mCustomNetView = new CustomNetView(mContext, CustomNetView.NO_DATA);

        setBottomPlayMusicView();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        linear_layout_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicPlayActivity.startMusicPlayActivity(mContext, mCourseBean);

            }
        });
        frame_layout_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_play_btn.setImageResource(!MusicService.isPlaying ? R.mipmap.ic_player_pause : R.mipmap.ic_player_start);

            }
        });
    }

    private CourseBean mCourseBean;

    /**
     * 设置底部View
     */
    private void setBottomPlayMusicView() {
        mCourseBean = SpUtils.getObject(SpUtils.MUSIC_CURRENT_COURSEBEAN);
        if (mCourseBean == null && courseBeans != null && courseBeans.size() > 0) {
            mCourseBean = courseBeans.get(0);

        }
        if (mCourseBean == null) {

            return;
        }
        tv_music_title.setText(mCourseBean.title);
        img_play_btn.setImageResource(MusicService.isPlaying ? R.mipmap.ic_player_pause : R.mipmap.ic_player_start);
        GlideUtils.getInstance().downLoadBitmap(mContext, mCourseBean.coursePic, new ImageDownLoadCallBack() {
            @Override
            public void onDownLoadSuccess(Bitmap resource) {
                // 4.更换音乐背景
                assert resource != null;
                Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette p) {
                        int mutedColor = p.getMutedColor(Color.BLACK);
                        Palette.Swatch darkMutedSwatch = p.getDarkMutedSwatch();//获取柔和的黑
                        play_music_bottom.setBackgroundColor(darkMutedSwatch != null ? darkMutedSwatch.getRgb() : mutedColor);

                    }
                });
                img_music_pic.setImageBitmap(resource);
            }
        });

    }

    private int pageNum = 1;

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        if (isRefresh) {
            pageNum = 1;

        } else {
            pageNum++;

        }
        HttpParams params = new HttpParams();
        params.put("userId", "06l6pkk0");
        params.put("pageNum", pageNum);
        params.put("pageSize", Constant.PageSize);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_HOME_COURSES, new JsonCallBack<ResultBean<List<CourseBean>>>() {
            @Override
            public void onSuccess(ResultBean<List<CourseBean>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (isRefresh) {
                    courseBeans.clear();
                    courseAdapter.setNewData(courseBeans);

                }
                if (mResultBean.data.size() < Constant.PageSize) {
                    courseAdapter.loadMoreEnd();

                } else {
                    courseAdapter.loadMoreComplete();

                }
                courseAdapter.addData(mResultBean.data);
                if (courseAdapter.getItemCount() == 0) {
                    mCustomNetView.setDataForView(CustomNetView.NO_DATA);
                    courseAdapter.setEmptyView(mCustomNetView);

                }
            }

            @Override
            public void onError(ResultBean<List<CourseBean>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                UIHelper.ToastMessage(mContext, mResultBean.msg);
                if (courseAdapter.getItemCount() == 0) {
                    mCustomNetView.setDataForView(CustomNetView.NET_ERREY);
                    courseAdapter.setEmptyView(mCustomNetView);

                } else {
                    courseAdapter.loadMoreFail();

                }
            }
        });
    }
}
