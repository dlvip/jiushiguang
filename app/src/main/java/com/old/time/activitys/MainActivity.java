package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.fragments.BookMallFragment;
import com.old.time.fragments.DynamicsFragment;
import com.old.time.fragments.HomeFragment;
import com.old.time.fragments.MBookFragment;
import com.old.time.fragments.UserFragment;
import com.old.time.utils.ActivityUtils;

public class MainActivity extends BaseActivity {

    private ImageView main_img_home, main_img_find, main_img_mine;
    private TextView tv_main_home, tv_main_find, tv_main_mine;

    /**
     * mainActivity
     *
     * @param mContext
     */
    public static void startMainActivity(Activity mContext) {
        Intent intent = new Intent(mContext, MainActivity.class);
        ActivityUtils.startActivity(mContext, intent);
        ActivityUtils.finishActivity(mContext);

    }

    @Override
    protected void initView() {
        main_img_home = findViewById(R.id.main_img_home);
        main_img_find = findViewById(R.id.main_img_find);
        main_img_mine = findViewById(R.id.main_img_mine);
        tv_main_home = findViewById(R.id.tv_main_home);
        tv_main_find = findViewById(R.id.tv_main_find);
        tv_main_mine = findViewById(R.id.tv_main_mine);
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
        resetTabButton(position);
        switch (position) {
            case 0:
                if (mHomeFragment == null) {
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    mHomeFragment = MBookFragment.getInstance();
                    fragmentTransaction.add(R.id.fl_content, mHomeFragment);
                    fragmentTransaction.commit();

                }
                switchConent(mHomeFragment);

                break;
            case 1:
                if (mFindFragment == null) {
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    mFindFragment = DynamicsFragment.getInstance();
                    fragmentTransaction.add(R.id.fl_content, mFindFragment);
                    fragmentTransaction.commit();

                }
                switchConent(mFindFragment);

                break;
            case 2:
                if (mMineFragment == null) {
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    mMineFragment = UserFragment.getInstance();
                    fragmentTransaction.add(R.id.fl_content, mMineFragment);
                    fragmentTransaction.commit();

                }
                switchConent(mMineFragment);

                break;
        }
    }

    /**
     * 修改按钮状态
     */
    private void resetTabButton(int tabIndex) {
        main_img_home.setImageResource(R.mipmap.tab_home_normal);
        main_img_find.setImageResource(R.mipmap.tab_find_normal);
        main_img_mine.setImageResource(R.mipmap.tab_my_normal);
        tv_main_home.setTextColor(getResources().getColor(R.color.color_666));
        tv_main_find.setTextColor(getResources().getColor(R.color.color_666));
        tv_main_mine.setTextColor(getResources().getColor(R.color.color_666));
        switch (tabIndex) {
            case 0:
                main_img_home.setImageResource(R.mipmap.tab_home_active);
                tv_main_home.setTextColor(getResources().getColor(R.color.coloe_58ad2c));

                break;
            case 1:
                main_img_find.setImageResource(R.mipmap.tab_find_active);
                tv_main_find.setTextColor(getResources().getColor(R.color.coloe_58ad2c));

                break;
            case 2:
                main_img_mine.setImageResource(R.mipmap.tab_my_active);
                tv_main_mine.setTextColor(getResources().getColor(R.color.coloe_58ad2c));

                break;
        }
    }


    private MBookFragment mHomeFragment;
    private UserFragment mMineFragment;
    private DynamicsFragment mFindFragment;
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
    public void onBackPressed() {
        moveTaskToBack(true);//将此任务转向后台

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

}
