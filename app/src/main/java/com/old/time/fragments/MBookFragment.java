package com.old.time.fragments;

import com.old.time.beans.PerfecTypeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wcliang on 2019/7/18.
 */

public class MBookFragment extends BaseTabFragment {

    public static MBookFragment getInstance(){

        return new MBookFragment();
    }

    @Override
    public List<PerfecTypeBean> initTabTitle() {
        List<PerfecTypeBean> typeBeans = new ArrayList<>();
        typeBeans.add(PerfecTypeBean.getInstance("0", "书签"));
        typeBeans.add(PerfecTypeBean.getInstance("1", "推荐"));
        typeBeans.add(PerfecTypeBean.getInstance("2", "分类"));
        return typeBeans;
    }

    @Override
    public List<BaseFragment> initFragments() {
        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(SignBookFragment.getInstance());
        fragments.add(HomeFragment.getInstance());
        fragments.add(BookMallFragment.getInstance());
        return fragments;
    }
}
