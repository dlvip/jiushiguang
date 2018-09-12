package com.old.time.activitys;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.aidl.ChapterBean;
import com.old.time.aidl.IOnModelChangedListener;
import com.old.time.aidl.IPlayControlAidlInterface;
import com.old.time.aidl.PlayServiceIBinder;
import com.old.time.fragments.FindFragment;
import com.old.time.fragments.HomeFragment;
import com.old.time.fragments.MineFragment;
import com.old.time.permission.PermissionUtil;
import com.old.time.service.PlayServiceManager;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.DebugLog;
import com.old.time.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_PHONE_STATE;

public class MainActivity extends BaseActivity {

    private ImageView main_img_home, main_img_find, main_img_mine;
    private TextView tv_main_home, tv_main_find, tv_main_mine;

    /**
     * mainActivity
     *
     * @param mContext
     */
    public static void startMainActivity(Activity mContext) {
        if (!PermissionUtil.checkAndRequestPermissionsInActivity(mContext, READ_PHONE_STATE)) {

            return;
        }
        Intent intent = new Intent(mContext, MainActivity.class);
        ActivityUtils.startActivity(mContext, intent);
        ActivityUtils.finishActivity(mContext);

    }

    private PlayServiceManager mPlayServiceManager;
    private IPlayControlAidlInterface iPlayServiceIBinder;

    @Override
    protected void initView() {
        main_img_home = findViewById(R.id.main_img_home);
        main_img_find = findViewById(R.id.main_img_find);
        main_img_mine = findViewById(R.id.main_img_mine);
        tv_main_home = findViewById(R.id.tv_main_home);
        tv_main_find = findViewById(R.id.tv_main_find);
        tv_main_mine = findViewById(R.id.tv_main_mine);
        selectFragment(0);

        mPlayServiceManager = new PlayServiceManager(mContext);
        mPlayServiceManager.bindService(conn, null);
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iPlayServiceIBinder = IPlayControlAidlInterface.Stub.asInterface(service);
            try {
                List<ChapterBean> chapterBeans = getModelList("289105");
                iPlayServiceIBinder.setPlayList(chapterBeans, chapterBeans.size() - 1);
                iPlayServiceIBinder.registerIOnModelChangedListener(new OnModelChangedListener());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iPlayServiceIBinder = null;

        }
    };

    private class OnModelChangedListener extends IOnModelChangedListener.Stub {

        @Override
        public void updatePlayModel(ChapterBean mChapterBean) throws RemoteException {
            DebugLog.d(TAG, "updatePlayModel=" + mChapterBean.toString());

        }

        @Override
        public void updateProgress(int progress, int total) throws RemoteException {
            DebugLog.d(TAG, "progress=" + progress + ":::total=" + total);
        }

        @Override
        public void updateError() throws RemoteException {
            DebugLog.d(TAG, "updateError");
        }

        @Override
        public void updateIsPlaying(boolean isPlaying) throws RemoteException {
            DebugLog.d(TAG, "updateIsPlaying:::isPlaying=" + isPlaying);
        }

        @Override
        public IBinder asBinder() {
            return null;
        }
    }

    /**
     * 获取章节列表
     */
    private List<ChapterBean> getModelList(String fileName) {
        String string = StringUtils.getJson(fileName + ".json", mContext);
        List<ChapterBean> chapterBeans = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("list");
            chapterBeans.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject musicObj = jsonArray.getJSONObject(i);
                ChapterBean chapterBean = new ChapterBean();
                chapterBean.setAlbum(musicObj.getString("coverLarge"));
                chapterBean.setAlbumId(Long.parseLong(musicObj.getString("albumId")));
                chapterBean.setAudio(musicObj.getString("playUrl64"));
                chapterBean.setDuration(Long.parseLong(musicObj.getString("duration")));
                chapterBean.setPicUrl(musicObj.getString("coverLarge"));
                chapterBean.setTitle(musicObj.getString("title"));
                chapterBean.setUrl(musicObj.getString("playUrl64"));
                chapterBeans.add(chapterBean);

            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return chapterBeans;
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
    public void onBackPressed() {
        moveTaskToBack(true);//将此任务转向后台

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conn != null) {
            unbindService(conn);

        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }
}
