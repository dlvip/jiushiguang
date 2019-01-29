package com.old.time.postcard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.old.time.R;
import com.old.time.activitys.BaseCActivity;
import com.old.time.beans.PhoneInfo;
import com.old.time.permission.PermissionUtil;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.PhoneUtils;
import com.old.time.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;

public class PostCardActivity extends BaseCActivity {

    /**
     * 通讯录
     *
     * @param mContext
     */
    public static void startPostCardActivity(Context mContext) {
        Intent intent = new Intent(mContext, PostCardActivity.class);
        ActivityUtils.startActivity((Activity) mContext, intent);
        ActivityUtils.finishActivity((Activity) mContext);

    }

    private List<String> mLetters = new ArrayList<>();

    @Override
    protected void initView() {
        super.initView();
        findViewById(R.id.left_layout).setVisibility(View.GONE);
        phoneInfos.clear();
        mLetters.clear();
        phoneInfos.addAll(PhoneUtils.getPhoneNumberFromMobile(mContext));
        for (int i = 0; i < phoneInfos.size(); i++) {
            PhoneInfo phoneInfo = phoneInfos.get(i);
            if (!mLetters.contains(phoneInfo.getSortKey())) {
                mLetters.add(phoneInfo.getSortKey());
                phoneInfos.get(i).setShow(true);

            } else {
                phoneInfos.get(i).setShow(false);

            }
        }
        adapter = new PostCardAdapter(phoneInfos);
        mRecyclerView.setAdapter(adapter);

    }

    private List<PhoneInfo> phoneInfos = new ArrayList<>();
    private PostCardAdapter adapter;

    @Override
    public void getDataFromNet(boolean isRefresh) {
        mSwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onPermissionResult(this, permissions, grantResults, new PermissionUtil.PermissionCallBack() {
            @Override
            public void onSuccess() {
                UIHelper.ToastMessage(mContext, getString(R.string.permissions_apply_success));

            }

            @Override
            public void onShouldShow() {

            }

            @Override
            public void onFailed() {
                UIHelper.ToastMessage(mContext, getString(R.string.permissions_apply_fail));

            }
        });
    }
}
