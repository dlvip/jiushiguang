package com.old.time.activitys;

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

import com.old.time.R;
import com.old.time.adapters.DisplayImgAdapter;
import com.old.time.adapters.PhotoGalleyAdapter;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.models.ImageSize;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.AlbumController;
import com.old.time.utils.PhotoSelectorHelper;
import com.old.time.utils.PictureUtil;
import com.old.time.utils.ScreenTools;
import com.old.time.utils.UIHelper;
import com.old.time.utils.UriUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class PhotoPickActivity extends BaseActivity implements PhotoSelectorHelper.OnLoadPhotoListener, AdapterView.OnItemClickListener {
    private PhotoSelectorHelper mHelper;
    private GridView mGridView;
    private PhotoGalleyAdapter mGalleyAdapter;
    private View mPickAlbumView;
    private TextView mCountText;
    public static final String MAX_PICK_COUNT = "max_pick_count";
    public static final String IS_SHOW_CAMERA = "is_show_camera";
    public static final String SELECT_PHOTO_LIST = "select_photo_list";
    public static final String IS_OPEN_CAMERA = "is_open_camera";
    private static final int TO_PICK_ALBUM = 1;
    private static final int TO_PRIVIEW_PHOTO = 2;
    private static final int TO_TAKE_PHOTO = 3;
    private static final int TO_CAMERA_PHOTO = 4;
    private boolean isShowCamera;
    private int maxPickCount;
    private String mLastAlbumName;

    private int width;
    private ImageSize imageSize;

    public static void startPhotoPickActivity(Activity mContext, boolean isShowCamera, int maxPickCount//
            , Serializable picPaths, int requestCode) {
        Intent intent = new Intent(mContext, PhotoPickActivity.class);
        intent.putExtra(PhotoPickActivity.IS_SHOW_CAMERA, isShowCamera);
        intent.putExtra(PhotoPickActivity.MAX_PICK_COUNT, maxPickCount);
        intent.putExtra(PhotoPickActivity.SELECT_PHOTO_LIST, picPaths);
        ActivityUtils.startActivityForResult(mContext, intent, requestCode);

    }

    public static void startPhotoPickActivity(Activity mContext, boolean isShowCamera, int maxPickCount//
            , int requestCode) {
        Intent intent = new Intent(mContext, PhotoPickActivity.class);
        intent.putExtra(PhotoPickActivity.IS_SHOW_CAMERA, isShowCamera);
        intent.putExtra(PhotoPickActivity.MAX_PICK_COUNT, maxPickCount);
        ActivityUtils.startActivityForResult(mContext, intent, requestCode);

    }

    public static void startPhotoPickActivity(Activity mContext, boolean isShowCamera, int requestCode) {
        Intent intent = new Intent(mContext, PhotoPickActivity.class);
        intent.putExtra(PhotoPickActivity.IS_SHOW_CAMERA, isShowCamera);
        ActivityUtils.startActivityForResult(mContext, intent, requestCode);

    }

    public static void startPhotoPickActivity(Activity mContext, int requestCode) {
        Intent intent = new Intent(mContext, PhotoPickActivity.class);
        ActivityUtils.startActivityForResult(mContext, intent, requestCode);

    }

    @Override
    protected String[] getNeedPermissions() {

        return new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE};
    }

    @Override
    protected void initEvent() {
        mPickAlbumView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotoPickActivity.this, PhotoAlbumActivity.class);
                startActivityForResult(intent, TO_PICK_ALBUM);

            }
        });

        mGalleyAdapter.setOnDisplayImageAdapter(new DisplayImgAdapter<String>() {
            @Override
            public void onDisplayImage(Context context, ImageView imageView, String path) {
                GlideUtils.getInstance().setImageViewWH(mContext, path, imageView, imageSize.getWidth());

            }

            @Override
            public void onItemImageClick(Context context, int index, List<String> list) {
                Intent intent = new Intent(PhotoPickActivity.this, PhotoPreviewActivity.class);
                intent.putExtra(PhotoPreviewActivity.PHOTO_INDEX_IN_ALBUM, index);
                intent.putExtra(MAX_PICK_COUNT, maxPickCount);
                intent.putExtra(PhotoAlbumActivity.ALBUM_NAME, mLastAlbumName);
                startActivityForResult(intent, TO_PRIVIEW_PHOTO);

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
        width = (ScreenTools.instance(this).getScreenWidth() - UIHelper.dip2px(6)) / 3;
        imageSize = new ImageSize(width, width);
        setTitleText("最近照片");
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.right_layout_send).setVisibility(View.VISIBLE);
        TextView tv_send = findViewById(R.id.tv_send);
        tv_send.setText("相册");

        List<String> list = getIntent().getStringArrayListExtra(SELECT_PHOTO_LIST);
        if (list != null) {
            for (String s : list) {
                if (!TextUtils.isEmpty(s)) {
                    PhotoGalleyAdapter.mSelectedImage.add(s);

                }
            }
        }
        maxPickCount = getIntent().getIntExtra(MAX_PICK_COUNT, 1);
        isShowCamera = getIntent().getBooleanExtra(IS_SHOW_CAMERA, false);
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
        return R.layout.activity_photo_pick;
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
                String name = data.getStringExtra(PhotoAlbumActivity.ALBUM_NAME);
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
        if (id == android.R.id.home) {
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
            UIHelper.ToastMessage("已经选满" + maxPickCount + "张");

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
        intent.putStringArrayListExtra(SELECT_PHOTO_LIST, list);
        setResult(RESULT_OK, intent);
        ActivityUtils.finishActivity(this);
    }


    private void updateCountView() {
        if (PhotoGalleyAdapter.mSelectedImage.size() == 0) {
            mCountText.setEnabled(false);

        } else {
            mCountText.setEnabled(true);

        }
        mCountText.setText("确定(" + PhotoGalleyAdapter.mSelectedImage.size() + "/" + maxPickCount + ")");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PhotoGalleyAdapter.mSelectedImage.clear();
    }
}
