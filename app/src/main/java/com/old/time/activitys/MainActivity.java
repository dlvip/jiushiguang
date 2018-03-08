package com.old.time.activitys;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.old.time.R;
import com.old.time.fragments.FindFragment;
import com.old.time.fragments.HomeFragment;
import com.old.time.fragments.MineFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void initView() {
        selectFragment(0);

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
                selectFragment(0);

                break;
            case R.id.relative_layout_find:
                selectFragment(1);

                break;
            case R.id.relative_layout_mine:
                selectFragment(2);

                break;
        }
    }

    /**
     * 选中那个
     */
    private void selectFragment(int position) {
        switch (position) {
            case 0:
                if (mHomeFragment == null) {
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    mHomeFragment = new HomeFragment();
                    fragmentTransaction.add(R.id.fl_content, mHomeFragment);
                    fragmentTransaction.commit();

                }
                switchConent(mHomeFragment);
                break;
            case 1:
                if (mFindFragment == null) {
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    mFindFragment = new FindFragment();
                    fragmentTransaction.add(R.id.fl_content, mFindFragment);
                    fragmentTransaction.commit();

                }
                switchConent(mFindFragment);

                break;
            case 2:
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
    private FindFragment mFindFragment;
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
        if (mFindFragment != null) {
            fragmentTransaction.hide(mFindFragment);

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
