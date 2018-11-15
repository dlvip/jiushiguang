package com.pic.lib.activitys;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.pic.lib.activitys.BaseLibActivity;
import com.pic.lib.utils.ActivityUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.pic.lib.R;
import com.pic.lib.adapters.AlbumListAdapter;
import com.pic.lib.models.AlbumModel;
import com.pic.lib.utils.MyLinearLayoutManager;
import com.pic.lib.utils.PhotoSelectorHelper;

import java.util.ArrayList;
import java.util.List;

public class PhotoAlbumActivity extends BaseLibActivity implements PhotoSelectorHelper.OnLoadAlbumListener {

    private PhotoSelectorHelper mHelper;
    private List<AlbumModel> albumModels;
    private RecyclerView mRecyclerView;
    private AlbumListAdapter mAdapter;
    public static final String ALBUM_NAME = "album_name";

    @Override
    protected void initEvent() {
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra(ALBUM_NAME, mAdapter.getItem(position).getName());
                setResult(RESULT_OK, intent);
                ActivityUtils.finishActivity(mContext);

            }
        });
    }

    @Override
    protected void initView() {
        TextView textView = findViewById(R.id.top_title);
        textView.setText(R.string.select_pic);
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);

        mRecyclerView = findViewById(R.id.lv_show_album);
        mRecyclerView.setLayoutManager(new MyLinearLayoutManager(mContext));
        albumModels = new ArrayList<>();
        mAdapter = new AlbumListAdapter(albumModels);
        mRecyclerView.setAdapter(mAdapter);

        mHelper = new PhotoSelectorHelper(this);
        mHelper.getAlbumList(this);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.piclib_activity_photo_album;
    }

    @Override
    public void onAlbumLoaded(List<AlbumModel> albums) {
        albumModels.clear();
        albumModels.addAll(albums);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ActivityUtils.finishActivity(mContext);

                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
