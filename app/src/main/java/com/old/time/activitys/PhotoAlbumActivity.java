package com.old.time.activitys;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.old.time.R;
import com.old.time.adapters.AlbumListAdapter;
import com.old.time.models.AlbumModel;
import com.old.time.utils.PhotoSelectorHelper;

import java.util.List;

public class PhotoAlbumActivity extends BaseActivity implements PhotoSelectorHelper.OnLoadAlbumListener, AdapterView.OnItemClickListener {
    private PhotoSelectorHelper mHelper;
    private ListView mListView;
    private AlbumListAdapter mAdapter;
    public static final String ALBUM_NAME = "album_name";

    @Override
    protected void initEvent() {
        mListView.setOnItemClickListener(this);

    }

    @Override
    protected void initView() {
        setTitleText("选择相册");
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);

        mListView = (ListView) this.findViewById(R.id.lv_show_album);
        mListView.setAdapter(mAdapter = new AlbumListAdapter(this));

        mHelper = new PhotoSelectorHelper(this);
        mHelper.getAlbumList(this);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_photo_album;
    }

    @Override
    public void onAlbumLoaded(List<AlbumModel> albums) {
        mAdapter.notifyDataSetChanged(albums, true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra(ALBUM_NAME, mAdapter.getItem(position).getName());
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
