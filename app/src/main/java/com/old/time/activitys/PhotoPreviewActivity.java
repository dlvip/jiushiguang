package com.old.time.activitys;

import android.app.ActionBar;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.adapters.ImagePagerAdapter;
import com.old.time.adapters.PhotoGalleyAdapter;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.AlbumController;
import com.old.time.utils.PhotoSelectorHelper;
import com.old.time.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;

public class PhotoPreviewActivity extends BaseActivity implements PhotoSelectorHelper.OnLoadPhotoListener, ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private ImageView mCheckBox;
    private TextView mCountText;
    private TextView mPreviewNum;
    public static final String PHOTO_INDEX_IN_ALBUM = "photo_index_in_album";
    private int index, maxPickCount;
    private String albumName;
    private List<String> mList;
    private ImagePagerAdapter mPhotoAdapter;

    @Override
    protected void initEvent() {
        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selCount = PhotoGalleyAdapter.mSelectedImage.size();
                if (selCount >= maxPickCount) {
                    UIHelper.ToastMessage(PhotoPreviewActivity.this, "已经选满" + selCount + "张");

                    return;
                }
                int index = mViewPager.getCurrentItem();
                boolean selFlag = PhotoGalleyAdapter.mSelectedImage.contains(mList.get(index));
                mCheckBox.setSelected(!selFlag);
                if (selFlag) {
                    PhotoGalleyAdapter.mSelectedImage.remove(mList.get(index));

                } else {
                    PhotoGalleyAdapter.mSelectedImage.add(mList.get(index));

                }
                updateCountView();
            }
        });

        mCountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.finishActivity(PhotoPreviewActivity.this);

            }
        });
    }

    @Override
    protected void initView() {
        index = getIntent().getIntExtra(PHOTO_INDEX_IN_ALBUM, 0);
        maxPickCount = getIntent().getIntExtra(PhotoPickActivity.MAX_PICK_COUNT, 1);
        albumName = getIntent().getStringExtra(PhotoAlbumActivity.ALBUM_NAME);
        ActionBar mActionBar = getActionBar();
        if (mActionBar != null) {
            mActionBar.setTitle(albumName);
            mActionBar.setDisplayHomeAsUpEnabled(true);

        }
        mViewPager = (ViewPager) this.findViewById(R.id.viewpager_preview_photo);
        mCheckBox = (ImageView) this.findViewById(R.id.checkbox_sel_flag);
        mPreviewNum = (TextView) this.findViewById(R.id.tv_preview_num);
        mCountText = (TextView) this.findViewById(R.id.tv_to_confirm);
        mViewPager.addOnPageChangeListener(this);
        mList = new ArrayList<>();
        mPhotoAdapter = new ImagePagerAdapter(getSupportFragmentManager(), mList);
        mViewPager.setAdapter(mPhotoAdapter);

        if (albumName != null && !albumName.equals(AlbumController.RECENT_PHOTO)) {
            new PhotoSelectorHelper(this).getAlbumPhotoList(albumName, this);

        } else {
            new PhotoSelectorHelper(this).getReccentPhotoList(this);

        }

        updateCountView();
    }

    @Override
    protected int getLayoutID() {

        return R.layout.activity_photo_preview;
    }

    @Override
    public void onPhotoLoaded(List<String> photos) {
        mList.clear();
        mList.addAll(photos);
        mPhotoAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(index, false);
        mPreviewNum.setText(index + 1 + "/" + mList.size());
        mCheckBox.setSelected(PhotoGalleyAdapter.mSelectedImage.contains(mList.get(index)));

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCheckBox.setSelected(PhotoGalleyAdapter.mSelectedImage.contains(mList.get(position)));
        mPreviewNum.setText(position + 1 + "/" + mList.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
