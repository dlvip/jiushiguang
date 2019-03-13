package com.pic.lib.activitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.pic.lib.PicCode;
import com.pic.lib.activitys.BaseLibActivity;
import com.pic.lib.utils.ActivityUtils;
import com.pic.lib.utils.ScreenTools;
import com.pic.lib.R;
import com.pic.lib.adapters.DisplayImgAdapter;
import com.pic.lib.adapters.PhotoGalleyAdapter;
import com.pic.lib.glideUtils.GlideUtils;
import com.pic.lib.models.ImageSize;
import com.pic.lib.permission.PermissionUtil;
import com.pic.lib.utils.AlbumController;
import com.pic.lib.utils.PhotoSelectorHelper;
import com.pic.lib.utils.PictureUtil;
import com.pic.lib.utils.UriUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class PhotoPickActivity extends BaseLibActivity implements PhotoSelectorHelper.OnLoadPhotoListener, AdapterView.OnItemClickListener {
    private PhotoSelectorHelper mHelper;
    private GridView mGridView;
    private PhotoGalleyAdapter mGalleyAdapter;
    private View mPickAlbumView;
    private TextView mCountText;
    private static final int TO_PICK_ALBUM = 1;
    private static final int TO_PRIVIEW_PHOTO = 2;
    private static final int TO_TAKE_PHOTO = 3;
    private static final int TO_CAMERA_PHOTO = 4;
    private boolean isShowCamera;
    private int maxPickCount;
    private String mLastAlbumName;

    private int width;
    private ImageSize imageSize;

    @Override
    protected void initEvent() {
        mPickAlbumView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotoPickActivity.this, PhotoAlbumActivity.class);
                ActivityUtils.startActivityForResult(mContext, intent, TO_PICK_ALBUM);

            }
        });

        mGalleyAdapter.setOnDisplayImageAdapter(new DisplayImgAdapter<String>() {

            @Override
            public void onDisplayImage(Context context, ImageView imageView, String path) {
                GlideUtils.getInstance().setImageViewWH(mContext, path, imageView, imageSize.getWidth());

            }

            @Override
            public void onItemImageClick(Context context, int index, List<String> list) {
                PhotoPreviewActivity.startPhotoPreviewActivity(mContext, index, maxPickCount, mLastAlbumName, TO_PRIVIEW_PHOTO);

            }

            @Override
            public void onImageCheckL(String path, boolean isChecked) {
                updateCountView();

            }
        });

        mCountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDone();

            }
        });

    }

    @Override
    protected void initView() {
        width = (ScreenTools.getScreenWidth(mContext) - ScreenTools.dip2px(mContext, 6)) / 3;
        imageSize = new ImageSize(width, width);
        TextView tv_title = findViewById(R.id.top_title);
        tv_title.setText(getString(R.string.recent_pic));
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.right_layout_send).setVisibility(View.VISIBLE);

        List<String> list = getIntent().getStringArrayListExtra(PicCode.SELECT_PHOTO_LIST);
        if (list != null) {
            for (String s : list) {
                if (!TextUtils.isEmpty(s)) {
                    PhotoGalleyAdapter.mSelectedImage.add(s);

                }
            }
        }
        maxPickCount = getIntent().getIntExtra(PicCode.MAX_PICK_COUNT, 1);
        isShowCamera = getIntent().getBooleanExtra(PicCode.IS_SHOW_CAMERA, false);
        mGridView = findViewById(R.id.mp_galley_gridView);
        mGridView.setOnItemClickListener(this);
        mCountText = findViewById(R.id.tv_to_confirm);
        mPickAlbumView = findViewById(R.id.right_layout_send);
        mLastAlbumName = AlbumController.RECENT_PHOTO;
        mHelper = new PhotoSelectorHelper(this);
        mHelper.getReccentPhotoList(this);
        mGalleyAdapter = new PhotoGalleyAdapter(this, null, isShowCamera, maxPickCount);
        mGridView.setAdapter(mGalleyAdapter);

        if (maxPickCount > 1) {
            mCountText.setVisibility(View.VISIBLE);

        } else {
            mCountText.setVisibility(View.GONE);

        }
        updateCountView();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.piclib_activity_photo_pick;
    }

    /**
     * 图片加载完成
     *
     * @param photos
     */
    @Override
    public void onPhotoLoaded(List<String> photos) {
        mGalleyAdapter.notifyDataSetChanged(photos, true);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TO_PRIVIEW_PHOTO) {
            updateCountView();
            mGalleyAdapter.notifyDataSetChanged();

        }
        if (resultCode != RESULT_OK) {

            return;
        }

        switch (requestCode) {
            case TO_PICK_ALBUM:
                String name = data.getStringExtra(PicCode.ALBUM_NAME);
                if (mLastAlbumName.equals(name)) {

                    return;
                }
                if (getActionBar() != null) {
                    getActionBar().setTitle(name);

                }
                mLastAlbumName = name;
                if (name.equals(AlbumController.RECENT_PHOTO)) {
                    mHelper.getReccentPhotoList(this);

                } else {
                    mHelper.getAlbumPhotoList(name, this);

                }
                break;
            case TO_PRIVIEW_PHOTO:
                selectDone();

                break;
            case TO_TAKE_PHOTO:
                String url = UriUtil.getAbsolutePathFromUri(this, mUri);
                if (!TextUtils.isEmpty(url)) {
                    PictureUtil.notifyGallery(this, url);
                    PhotoGalleyAdapter.mSelectedImage.add(url);
                    selectDone();

                }
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Uri mUri;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String path = mGalleyAdapter.getItem(position);
        if (PhotoGalleyAdapter.mSelectedImage.size() >= maxPickCount) {
            ScreenTools.ToastMessage(mContext, "已经选满" + maxPickCount + "张");

            return;
        }
        if (TextUtils.isEmpty(path)) {
            mUri = PictureUtil.takePhoto(this, TO_TAKE_PHOTO);

        } else {
            PhotoGalleyAdapter.mSelectedImage.add(path);
            selectDone();

        }
    }

    private void selectDone() {
        ArrayList<String> list = new ArrayList<>();
        for (String s : PhotoGalleyAdapter.mSelectedImage) {
            list.add(s);

        }
        PhotoGalleyAdapter.mSelectedImage.clear();
        Intent intent = new Intent();
        intent.putStringArrayListExtra(PicCode.SELECT_PHOTO_LIST, list);
        setResult(RESULT_OK, intent);
        ActivityUtils.finishActivity(this);
    }


    @SuppressLint("SetTextI18n")
    private void updateCountView() {
        if (PhotoGalleyAdapter.mSelectedImage.size() == 0) {
            mCountText.setEnabled(false);

        } else {
            mCountText.setEnabled(true);

        }
        mCountText.setText("(" + PhotoGalleyAdapter.mSelectedImage.size() + "/" + maxPickCount + ")");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PhotoGalleyAdapter.mSelectedImage.clear();
    }
}
