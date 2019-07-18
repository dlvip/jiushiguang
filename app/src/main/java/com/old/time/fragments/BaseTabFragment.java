package com.old.time.fragments;

import android.os.Handler;
import android.support.v4.view.ViewPager;

import com.old.time.R;
import com.old.time.adapters.FStatePagerAdapter;
import com.old.time.beans.PerfecTypeBean;
import com.old.time.interfaces.OnTabSelectListener;
import com.old.time.views.SlidingTabLayout;

import java.util.List;

public abstract class BaseTabFragment extends BaseFragment implements ViewPager.OnPageChangeListener, OnTabSelectListener {

    private ViewPager mViewPager;
    protected SlidingTabLayout mTabLayout;
    private List<BaseFragment> mFragments;
    private FStatePagerAdapter mAdapter;
    public Handler handler = new Handler();

    @Override
    protected int setContentView() {
        return R.layout.fragment_tab_base;
    }

    //初始化tab 名称
    public abstract List<PerfecTypeBean> initTabTitle();

    //初始化fragment
    public abstract List<BaseFragment> initFragments();

    @Override
    protected void lazyLoad() {
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.viewPager);
        List<PerfecTypeBean> mTitles = initTabTitle();
        mFragments = initFragments();
        mAdapter = new FStatePagerAdapter(getChildFragmentManager(), mTitles, mFragments);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(mTitles.size());
        mViewPager.addOnPageChangeListener(this);
        mTabLayout.setOnTabSelectListener(this);
        selectDefFragment(getDefPosition());

    }

    /**
     * 重新设置fragment
     */
    protected void resetLoginState() {
        if (mAdapter != null) mAdapter.notifyDataSetChanged();

    }

    /**
     * 默认选中
     *
     * @return
     */
    protected int getDefPosition() {

        return 0;
    }

    /**
     * 再次点击返回到顶部
     *
     * @param position
     */
    protected void scrollViewToTop(int position) {
        if (mViewPager == null || mFragments == null || mFragments.size() == 0) {

            return;
        }
        if (mFragments.get(position) instanceof CBaseFragment) {
            CBaseFragment cBaseFragment = (CBaseFragment) mFragments.get(position);
            cBaseFragment.seleteToPosition(0);

        }
    }

    /**
     * 默认选中
     *
     * @param defPosition
     */
    public void selectDefFragment(int defPosition) {
        if (mFragments == null || mViewPager == null) {

            return;
        }
        if (defPosition >= mFragments.size()) {
            defPosition = mFragments.size() - 1;

        }
        final int finalDefPosition = defPosition;
        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(finalDefPosition);

            }
        });
    }

    @Override
    public void getDataFromNet(boolean isRefresh) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabSelect(int position) {


    }

    @Override
    public void onTabReselect(int position) {
        scrollViewToTop(position);

    }
}