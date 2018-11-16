package com.pic.lib.activitys;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pic.lib.PicCode;
import com.pic.lib.utils.ActivityUtils;
import com.pic.lib.utils.ScreenTools;
import com.pic.lib.R;
import com.pic.lib.adapters.ImagePagerAdapter;
import com.pic.lib.adapters.PhotoGalleyAdapter;
import com.pic.lib.utils.AlbumController;
import com.pic.lib.utils.PhotoSelectorHelper;

import java.util.ArrayList;
import java.util.List;

public class PhotoPreviewActivity extends BaseLibActivity implements PhotoSelectorHelper.OnLoadPhotoListener, ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private ImageView mCheckBox;
    private TextView mCountText;
    private TextView mPreviewNum;

    private int index, maxPickCount;
    private String albumName;
    private List<String> mList;
    private ImagePagerAdapter mPhotoAdapter;

    /**
     * 图片浏览
     *
     * @param index
     * @param maxPickCount
     * @param albumName
     */
    public static void startPhotoPreviewActivity(Context mContext, int index, int maxPickCount, String albumName, int requestCode) {
        Intent intent = new Intent(mContext, PhotoPreviewActivity.class);
        intent.putExtra(PicCode.EXTRA_IMAGE_INDEX, index);
        intent.putExtra(PicCode.MAX_PICK_COUNT, maxPickCount);
        intent.putExtra(PicCode.ALBUM_NAME, albumName);
        ActivityUtils.startActivityForResult((Activity) mContext, intent, requestCode);

    }

    @Override
    protected void initEvent() {
        mCheckBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int selCount = PhotoGalleyAdapter.mSelectedImage.size();
                if (selCount >= maxPickCount) {
                    ScreenTools.ToastMessage(PhotoPreviewActivity.this, "已经选满" + selCount + "张");

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
        Intent intent = getIntent();
        this.maxPickCount = intent.getIntExtra(PicCode.MAX_PICK_COUNT, 1);
        this.albumName = intent.getStringExtra(PicCode.ALBUM_NAME);
        this.index = intent.getIntExtra(PicCode.EXTRA_IMAGE_INDEX, 0);
        ActionBar mActionBar = getActionBar();
        if (mActionBar != null) {
            mActionBar.setTitle(albumName);
            mActionBar.setDisplayHomeAsUpEnabled(true);

        }
        mViewPager = this.findViewById(R.id.viewpager_preview_photo);
        mCheckBox = this.findViewById(R.id.checkbox_sel_flag);
        mPreviewNum = this.findViewById(R.id.tv_preview_num);
        mCountText = this.findViewById(R.id.tv_to_confirm);
        mViewPager.addOnPageChangeListener(this);
        mList = new ArrayList<>();
        mPhotoAdapter = new ImagePagerAdapter(getSupportFragmentManager(), mList, false);
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

        return R.layout.piclib_activity_photo_preview;
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
        if (id == R.id.home) {
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
