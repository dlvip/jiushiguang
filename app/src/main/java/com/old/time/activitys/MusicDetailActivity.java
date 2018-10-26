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
import com.old.time.R;
import com.old.time.adapters.ChapterAdapter;
import com.old.time.aidl.ChapterBean;
import com.old.time.aidl.PlayServiceIBinder;
import com.old.time.beans.CourseBean;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.interfaces.ImageDownLoadCallBack;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.DataUtils;
import com.old.time.utils.SpUtils;

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
    private List<ChapterBean> chapterList = new ArrayList<>();

    @Override
    protected void initView() {
        mCourseBean = (CourseBean) getIntent().getSerializableExtra(COURSE_BEAN);
        super.initView();
        albumId = SpUtils.getString(mContext, PlayServiceIBinder.SP_PLAY_ALBUM_ID, PlayServiceIBinder.DEFAULT_ALBUM_ID);
        if (albumId.equals(mCourseBean.albumId)) {
            position = SpUtils.getInt(PlayServiceIBinder.SP_PLAY_POSITION, -1);

        }
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
    }

    @Override
    public void getDataFromNet(boolean isRefresh) {
        if (isRefresh) {
            chapterList.clear();
            mAdapter.setNewData(chapterList);

        }
        mSwipeRefreshLayout.setRefreshing(false);
        List<ChapterBean> chapterBeans = DataUtils.getModelBeans(mCourseBean.albumId, mContext);
        if (chapterBeans != null && chapterBeans.size() > 0) {
            chapterList.addAll(chapterBeans);

        }
        mAdapter.setNewData(chapterList);
        mAdapter.setcPosition(position);
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
