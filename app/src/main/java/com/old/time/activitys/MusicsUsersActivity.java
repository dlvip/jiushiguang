package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;

import com.old.time.adapters.HMusicAdapter;
import com.old.time.beans.ResultBean;
import com.old.time.beans.TeacherBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;

public class MusicsUsersActivity extends BaseCActivity {

    public static void startMusicsActivity(Activity mContext) {
        Intent intent = new Intent(mContext, MusicsUsersActivity.class);
        ActivityUtils.startActivity(mContext, intent);

    }

    private List<TeacherBean> teacherBeans;
    private HMusicAdapter hMusicAdapter;

    @Override
    protected void initView() {
        super.initView();
        teacherBeans = new ArrayList<>();
        hMusicAdapter = new HMusicAdapter(teacherBeans);
        mRecyclerView.setAdapter(hMusicAdapter);

    }

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        OkGoUtils.getInstance().postNetForData(Constant.GET_HONE_TEACHERS, new JsonCallBack<ResultBean<List<TeacherBean>>>() {
            @Override
            public void onSuccess(ResultBean<List<TeacherBean>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (isRefresh) {
                    teacherBeans.clear();
                    hMusicAdapter.setNewData(teacherBeans);

                }
                if (mResultBean.status == Constant.STATUS_FRIEND_00) {
                    teacherBeans.addAll(mResultBean.data);
                    hMusicAdapter.setNewData(teacherBeans);

                } else {
                    UIHelper.ToastMessage(mContext, mResultBean.msg);

                }
            }

            @Override
            public void onError(ResultBean<List<TeacherBean>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                UIHelper.ToastMessage(mContext, mResultBean.msg);

            }
        });
    }
}
