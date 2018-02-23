package com.old.time.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.old.time.fragments.ImageDetailFragment;

import java.util.List;

/**
 * Created by NING on 2018/2/23.
 */

public class ImagePagerAdapter extends FragmentStatePagerAdapter {

    public List<String> fileList;

    public ImagePagerAdapter(FragmentManager fm, List<String> fileList) {
        super(fm);
        this.fileList = fileList;
    }

    @Override
    public int getCount() {
        return fileList == null ? 0 : fileList.size();
    }

    @Override
    public Fragment getItem(int position) {
        String url = fileList.get(position);
        return ImageDetailFragment.newInstance(url);
    }
}
