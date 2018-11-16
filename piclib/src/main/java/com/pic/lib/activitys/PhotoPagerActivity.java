package com.pic.lib.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.pic.lib.PicCode;
import com.pic.lib.R;
import com.pic.lib.adapters.ImagePagerAdapter;
import com.pic.lib.views.HackyViewPager;

import java.util.List;

public class PhotoPagerActivity extends FragmentActivity {

    private HackyViewPager mPager;
    private int pagerPosition;
    private TextView indicator;
    private List<String> urls;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.piclib_activity_image_detail_pager);
        Intent intent = getIntent();
        pagerPosition = intent.getIntExtra(PicCode.EXTRA_IMAGE_INDEX, 0);
        urls = intent.getStringArrayListExtra(PicCode.EXTRA_IMAGE_URLS);
        mPager = findViewById(R.id.pager);
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls, true);
        mPager.setAdapter(mAdapter);
        indicator = findViewById(R.id.indicator);

        CharSequence text = getString(R.string.viewpager_indicator, 1, mPager.getAdapter().getCount());
        indicator.setText(text);
        // 更新下标
        mPager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                CharSequence text = getString(R.string.viewpager_indicator, position + 1, mPager.getAdapter().getCount());
                indicator.setText(text);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(PicCode.STATE_POSITION);

        }

        mPager.setCurrentItem(pagerPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PicCode.STATE_POSITION, mPager.getCurrentItem());

    }
}
