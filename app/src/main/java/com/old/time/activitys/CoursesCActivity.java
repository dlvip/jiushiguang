package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.model.HttpParams;
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

public class CoursesCActivity extends BaseCActivity {

    public static void startCoursesActivity(Activity mContext) {
        Intent intent = new Intent(mContext, CoursesCActivity.class);
        ActivityUtils.startActivity(mContext, intent);

    }

    private CourseAdapter courseAdapter;
    private List<CourseBean> courseBeans;

    @Override
    protected void initView() {
        super.initView();
        courseBeans = new ArrayList<>();
        courseAdapter = new CourseAdapter(courseBeans);
        mRecyclerView.setAdapter(courseAdapter);
        courseAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getDataFromNet(false);

            }
        }, mRecyclerView);
        linear_layout_more.setVisibility(View.VISIBLE);
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        linear_layout_more.setLayoutParams(layoutParams);
        linear_layout_more.removeAllViews();

    }

    private int pageNum = 0;

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        if (isRefresh) {
            pageNum = 0;

        } else {
            pageNum++;

        }
        HttpParams params = new HttpParams();
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
                if (courseAdapter.getItemCount() - courseAdapter.getHeaderLayoutCount() == 0) {
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
