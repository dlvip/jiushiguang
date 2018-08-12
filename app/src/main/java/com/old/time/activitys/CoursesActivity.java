package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;

import com.old.time.R;
import com.old.time.adapters.HCourseAdapter;
import com.old.time.beans.CourseBean;
import com.old.time.beans.ResultBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;

public class CoursesActivity extends CBaseActivity {

    public static void startCoursesActivity(Activity mContext) {
        Intent intent = new Intent(mContext, CoursesActivity.class);
        ActivityUtils.startActivity(mContext, intent);

    }

    private HCourseAdapter hCourseAdapter;
    private List<CourseBean> courseBeans;

    @Override
    protected void initView() {
        super.initView();
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        courseBeans = new ArrayList<>();
        hCourseAdapter = new HCourseAdapter(R.layout.adapter_course, courseBeans);
        mRecyclerView.setAdapter(hCourseAdapter);

    }

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        OkGoUtils.getInstance().postNetForData(Constant.GET_HOME_COURSES, new JsonCallBack<ResultBean<List<CourseBean>>>() {
            @Override
            public void onSuccess(ResultBean<List<CourseBean>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (isRefresh) {
                    courseBeans.clear();
                    hCourseAdapter.setNewData(courseBeans);

                }
                if (mResultBean.status == Constant.STATUS_FRIEND_00) {
                    courseBeans.addAll(mResultBean.data);
                    hCourseAdapter.setNewData(courseBeans);

                } else {
                    UIHelper.ToastMessage(mContext, mResultBean.msg);

                }
            }

            @Override
            public void onError(ResultBean<List<CourseBean>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                UIHelper.ToastMessage(mContext, mResultBean.msg);

            }
        });
    }
}
