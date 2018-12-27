package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.adapters.ChapterAdapter;
import com.old.time.aidl.ChapterBean;
import com.old.time.aidl.PlayServiceIBinder;
import com.old.time.beans.CourseBean;
import com.old.time.beans.ResultBean;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.interfaces.ImageDownLoadCallBack;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.SpUtils;
import com.old.time.views.CustomNetView;

import java.util.ArrayList;
import java.util.List;

public class MusicDetailActivity extends BaseCActivity {

    public static final String COURSE_BEAN = "mCourseBean";

    public static void startMusicDetailActivity(Activity mContext, CourseBean mCourseBean) {
        Intent intent = new Intent(mContext, MusicDetailActivity.class);
        intent.putExtra(COURSE_BEAN, mCourseBean);
        ActivityUtils.startActivity(mContext, intent);

    }

    private int position = -1;
    private String albumId;
    private View headerView;
    private TextView tv_music_title;
    private ImageView img_music_pic;
    private CourseBean mCourseBean;
    private ChapterAdapter mAdapter;
    private CustomNetView mCustomNetView;
    private List<ChapterBean> chapterList = new ArrayList<>();

    @Override
    protected void initView() {
        mCourseBean = (CourseBean) getIntent().getSerializableExtra(COURSE_BEAN);
        super.initView();
        albumId = SpUtils.getString(mContext, PlayServiceIBinder.SP_PLAY_ALBUM_ID, PlayServiceIBinder.DEFAULT_ALBUM_ID);
        if (albumId.equals(mCourseBean.albumId)) {
            position = SpUtils.getInt(PlayServiceIBinder.SP_PLAY_POSITION, -1);

        }
        mCustomNetView = new CustomNetView(mContext, CustomNetView.NO_DATA);

        headerView = View.inflate(mContext, R.layout.header_music_detail, null);
        img_music_pic = headerView.findViewById(R.id.img_music_pic);
        tv_music_title = headerView.findViewById(R.id.tv_music_title);
        mAdapter = new ChapterAdapter(chapterList);
        mAdapter.removeAllHeaderView();
        mAdapter.addHeaderView(headerView);
        mRecyclerView.setAdapter(mAdapter);
        tv_music_title.setText(mCourseBean.title);
        GlideUtils.getInstance().downLoadBitmap(mContext, mCourseBean.coursePic, 5, new ImageDownLoadCallBack() {

            @Override
            public void onDownLoadSuccess(Bitmap resource) {
                // 4.更换音乐背景
                assert resource != null;
                Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette p) {
                        int mutedColor = p.getMutedColor(Color.BLACK);
                        Palette.Swatch darkMutedSwatch = p.getDarkMutedSwatch();//获取柔和的黑
                        headerView.setBackgroundColor(darkMutedSwatch != null ? darkMutedSwatch.getRgb() : mutedColor);

                    }
                });
                img_music_pic.setImageBitmap(resource);
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MusicPlayActivity.startMusicPlayActivity(mContext, mCourseBean);
                mAdapter.setcPosition(position - mAdapter.getHeaderLayoutCount());

            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getDataFromNet(false);

            }
        }, mRecyclerView);
    }


    private int pageNum;

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        if (isRefresh) {
            pageNum = 0;

        }
        HttpParams params = new HttpParams();
        params.put("albumId", mCourseBean.albumId);
        params.put("pageNum", pageNum);
        params.put("pageSize", Constant.PageSize);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_MUSIC_LIST, new JsonCallBack<ResultBean<List<ChapterBean>>>() {
            @Override
            public void onSuccess(ResultBean<List<ChapterBean>> mResultBean) {
                pageNum++;
                mSwipeRefreshLayout.setRefreshing(false);
                if (isRefresh) {
                    chapterList.clear();
                    mAdapter.setNewData(chapterList);

                }
                if (mResultBean.data.size() < Constant.PageSize) {
                    mAdapter.loadMoreEnd();

                } else {
                    mAdapter.loadMoreComplete();

                }
                mAdapter.addData(mResultBean.data);
                if (mAdapter.getItemCount() - mAdapter.getHeaderLayoutCount() == 0) {
                    mCustomNetView.setDataForView(CustomNetView.NO_DATA);
                    mAdapter.setEmptyView(mCustomNetView);

                }
            }

            @Override
            public void onError(ResultBean<List<ChapterBean>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapter.loadMoreFail();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (albumId.equals(mCourseBean.albumId)) {
            position = SpUtils.getInt(PlayServiceIBinder.SP_PLAY_POSITION, -1);
            mAdapter.setcPosition(position);

        }
    }
}
