package com.old.time.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.old.time.beans.PerfecTypeBean;
import com.old.time.fragments.BaseFragment;

import java.util.List;

/**
 * Created by NING on 2018/4/3.
 */

public class FStatePagerAdapter extends FragmentStatePagerAdapter {

    private List<PerfecTypeBean> mTitles;
    private List<BaseFragment> mFragments;

    public FStatePagerAdapter(FragmentManager fm, List<PerfecTypeBean> mTitles, List<BaseFragment> mFragments) {
        super(fm);
        this.mTitles = mTitles;
        this.mFragments = mFragments;

    }

    public FStatePagerAdapter(FragmentManager fm, List<BaseFragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;

    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? mFragments.size() : mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles == null) {

            return "";
        }
        return mTitles.get(position).distitle;
    }
}
