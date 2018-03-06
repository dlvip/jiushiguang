package com.old.time.activitys;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.old.time.R;
import com.old.time.fragments.CircleFragment;
import com.old.time.fragments.HomeFragment;
import com.old.time.fragments.MineFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {
        super.initEvent();
        findViewById(R.id.relative_layout_home).setOnClickListener(this);
        findViewById(R.id.relative_layout_find).setOnClickListener(this);
        findViewById(R.id.relative_layout_mine).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.relative_layout_home:
                if (mHomeFragment == null) {
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    mHomeFragment = new HomeFragment();
                    fragmentTransaction.add(R.id.fl_content, mHomeFragment);
                    fragmentTransaction.commit();

                }
                switchConent(mHomeFragment);

                break;
            case R.id.relative_layout_find:
                if (mCircleFragment == null) {
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    mCircleFragment = new CircleFragment();
                    fragmentTransaction.add(R.id.fl_content, mCircleFragment);
                    fragmentTransaction.commit();

                }
                switchConent(mCircleFragment);

                break;
            case R.id.relative_layout_mine:
                if (mMineFragment == null) {
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    mMineFragment = new MineFragment();
                    fragmentTransaction.add(R.id.fl_content, mMineFragment);
                    fragmentTransaction.commit();

                }
                switchConent(mMineFragment);

                break;
        }
    }


    private HomeFragment mHomeFragment;
    private MineFragment mMineFragment;
    private CircleFragment mCircleFragment;
    private FragmentTransaction fragmentTransaction;

    /**
     * 切换Fragment
     *
     * @param fragment
     */
    public void switchConent(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (mHomeFragment != null) {
            fragmentTransaction.hide(mHomeFragment);

        }
        if (mCircleFragment != null) {
            fragmentTransaction.hide(mCircleFragment);

        }
        if (mMineFragment != null) {
            fragmentTransaction.hide(mMineFragment);

        }

        fragmentTransaction.show(fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }
}
