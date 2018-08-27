package com.old.time.fragments;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.activitys.CoursesActivity;
import com.old.time.activitys.MusicsActivity;
import com.old.time.adapters.CourseAdapter;
import com.old.time.adapters.HCourseAdapter;
import com.old.time.adapters.HMusicAdapter;
import com.old.time.adapters.HomeAdapter;
import com.old.time.adapters.IconTabAdapter;
import com.old.time.beans.ArticleBean;
import com.old.time.beans.BannerBean;
import com.old.time.beans.CourseBean;
import com.old.time.beans.IconBean;
import com.old.time.beans.ResultBean;
import com.old.time.beans.TeacherBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.MyGridLayoutManager;
import com.old.time.utils.MyLinearLayoutManager;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;
import com.old.time.views.banner.BannerLayout;
import com.old.time.views.banner.adapter.MzBannerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NING on 2018/3/5.
 */

public class HomeFragment extends CBaseFragment {

    private List<BannerBean> bannerBeans;
    private BannerLayout recycler_banner;
    private MzBannerAdapter mzBannerAdapter;

    private List<ArticleBean> articleBeans;
    private HomeAdapter mAdapter;

    private List<IconBean> iconBeans;
    private RecyclerView recycler_icons;
    private IconTabAdapter iconAdapter;

    private List<CourseBean> courseBeans;
    private RecyclerView recycler_course;
    private HCourseAdapter hCourseAdapter;

    private List<TeacherBean> teacherBeans;
    private RecyclerView recycler_music;
    private HMusicAdapter hMusicAdapter;

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        View headerView = View.inflate(mContext, R.layout.header_fragment_home, null);
        recycler_banner = headerView.findViewById(R.id.recycler_banner);
        bannerBeans = new ArrayList<>();
        mzBannerAdapter = new MzBannerAdapter(mContext, bannerBeans);
        recycler_banner.setmBannerAdapter(mzBannerAdapter);

        //icon导航
        recycler_icons = headerView.findViewById(R.id.recycler_icons);
        recycler_icons.setLayoutManager(new MyGridLayoutManager(mContext, 5));
        iconBeans = new ArrayList<>();
        iconAdapter = new IconTabAdapter(iconBeans);
        recycler_icons.setAdapter(iconAdapter);

        //精品课堂
        View include_course = headerView.findViewById(R.id.include_course);
        TextView tv_course_title = include_course.findViewById(R.id.tv_recycler_title);
        tv_course_title.setText("精品课堂");
        recycler_course = include_course.findViewById(R.id.recycler_content);
        recycler_course.setLayoutManager(new MyLinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        recycler_course.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.HORIZONTAL_LIST, 10, R.color.color_fff));
        courseBeans = new ArrayList<>();
        hCourseAdapter = new HCourseAdapter(courseBeans);
        recycler_course.setAdapter(hCourseAdapter);
        SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);
        snapHelperStart.attachToRecyclerView(recycler_course);
        include_course.findViewById(R.id.linear_layout_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoursesActivity.startCoursesActivity(mContext);

            }
        });

        //名师优讲
        View include_music = headerView.findViewById(R.id.include_music);
        TextView tv_music_title = include_music.findViewById(R.id.tv_recycler_title);
        tv_music_title.setText("名师优讲");
        recycler_music = include_music.findViewById(R.id.recycler_content);
        recycler_music.setLayoutManager(new MyLinearLayoutManager(mContext));
        teacherBeans = new ArrayList<>();
        hMusicAdapter = new HMusicAdapter(teacherBeans);
        recycler_music.setAdapter(hMusicAdapter);
        include_music.findViewById(R.id.linear_layout_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicsActivity.startMusicsActivity(mContext);

            }
        });

        //家长专栏
        articleBeans = new ArrayList<>();
        mAdapter = new HomeAdapter(articleBeans);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.removeAllHeaderView();
        mAdapter.addHeaderView(headerView);
        mAdapter.setHeaderAndEmpty(true);

    }

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        getHomeBanners();
        getHomeIcons(isRefresh);
        getHomeCourses(isRefresh);
        getHomeTeachers(isRefresh);
        getHomeArticles(isRefresh);

    }

    /**
     * 获取文章列表
     *
     * @param isRefresh
     */
    private void getHomeArticles(final boolean isRefresh) {
        OkGoUtils.getInstance().postNetForData(null, Constant.GET_ARTICLE_LIST, new JsonCallBack<ResultBean<List<ArticleBean>>>() {

            @Override
            public void onSuccess(ResultBean<List<ArticleBean>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (isRefresh) {
                    articleBeans.clear();
                    mAdapter.setNewData(articleBeans);

                }
                if (mResultBean.status == Constant.STATUS_FRIEND_00) {
                    articleBeans.addAll(mResultBean.data);
                    mAdapter.setNewData(articleBeans);

                } else {
                    UIHelper.ToastMessage(mContext, mResultBean.msg);

                }
            }

            @Override
            public void onError(ResultBean<List<ArticleBean>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                UIHelper.ToastMessage(mContext, mResultBean.msg);

            }
        });
    }

    /**
     * 获取轮播图列表
     */
    private void getHomeBanners() {
        OkGoUtils.getInstance().postNetForData(Constant.GET_HOME_BANNERS, new JsonCallBack<ResultBean<List<BannerBean>>>() {

            @Override
            public void onSuccess(ResultBean<List<BannerBean>> resultBean) {
                if (resultBean == null || resultBean.data == null) {

                    return;
                }
                bannerBeans.clear();
                bannerBeans.addAll(resultBean.data);
                recycler_banner.initBannerImageView(bannerBeans);

            }

            @Override
            public void onError(ResultBean<List<BannerBean>> resultBean) {
                if (resultBean == null) {

                    return;
                }
                UIHelper.ToastMessage(mContext, resultBean.msg);
            }
        });
    }

    /**
     * 获取icon列表
     *
     * @param isRefresh
     */
    private void getHomeIcons(final boolean isRefresh) {
        OkGoUtils.getInstance().postNetForData(Constant.GET_HOME_ICONS, new JsonCallBack<ResultBean<List<IconBean>>>() {
            @Override
            public void onSuccess(ResultBean<List<IconBean>> mResultBean) {
                if (isRefresh) {
                    iconBeans.clear();
                    iconAdapter.setNewData(iconBeans);

                }
                if (mResultBean.status == Constant.STATUS_FRIEND_00) {
                    iconBeans.addAll(mResultBean.data);
                    iconAdapter.setNewData(iconBeans);

                } else {
                    UIHelper.ToastMessage(mContext, mResultBean.msg);

                }
            }

            @Override
            public void onError(ResultBean<List<IconBean>> mResultBean) {
                UIHelper.ToastMessage(mContext, mResultBean.msg);

            }
        });
    }

    /**
     * 精品课堂
     */
    private void getHomeCourses(final boolean isRefresh) {
        OkGoUtils.getInstance().postNetForData(Constant.GET_HOME_COURSES, new JsonCallBack<ResultBean<List<CourseBean>>>() {
            @Override
            public void onSuccess(ResultBean<List<CourseBean>> mResultBean) {
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
                UIHelper.ToastMessage(mContext, mResultBean.msg);

            }
        });
    }

    /**
     * 获取名师优讲
     */
    private void getHomeTeachers(final boolean isRefresh) {
        OkGoUtils.getInstance().postNetForData(Constant.GET_HONE_TEACHERS, new JsonCallBack<ResultBean<List<TeacherBean>>>() {
            @Override
            public void onSuccess(ResultBean<List<TeacherBean>> mResultBean) {
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
                UIHelper.ToastMessage(mContext, mResultBean.msg);

            }
        });
    }
}
