package com.old.time.activitys;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.adapters.CourseAdapter;
import com.old.time.beans.CourseBean;
import com.old.time.beans.ResultBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;
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

    private View play_music_view;
    private ImageView img_music_pic;
    private ValueAnimator rotateAnim;

    @Override
    protected void initView() {
        super.initView();
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
        linear_layout_more.setVisibility(View.VISIBLE);
        linear_layout_more.removeAllViews();
        play_music_view = View.inflate(mContext, R.layout.play_music_bottom, null);
        img_music_pic = play_music_view.findViewById(R.id.img_music_pic);
        linear_layout_more.addView(play_music_view);
        rotateAnim = ObjectAnimator.ofFloat(0, 360);
        rotateAnim.setDuration(15 * 1000);
        rotateAnim.setRepeatMode(ValueAnimator.RESTART);
        rotateAnim.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnim.setInterpolator(new LinearInterpolator());
        rotateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                img_music_pic.setRotation(value);

            }
        });
        startSpin();
    }

    public void startSpin() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (rotateAnim.isPaused()) {
                rotateAnim.resume();
            } else {
                rotateAnim.start();
            }
        } else {
            rotateAnim.start();
        }
    }

    public void stopSpin() {
        if (rotateAnim.isRunning()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                rotateAnim.pause();

            } else {
                rotateAnim.cancel();

            }
        }
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

    @Override
    protected void onDestroy() {
        stopSpin();
        super.onDestroy();
    }
}
