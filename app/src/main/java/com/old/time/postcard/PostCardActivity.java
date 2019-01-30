package com.old.time.postcard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.old.time.R;
import com.old.time.activitys.BaseCActivity;
import com.old.time.adapters.LetterAdapter;
import com.old.time.beans.PhoneBean;
import com.old.time.beans.PhoneInfo;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.MyGridLayoutManager;
import com.old.time.utils.PhoneUtils;
import com.old.time.utils.RecyclerItemDecoration;
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

    /**
     * 联系人集合
     */
    private List<PhoneBean> phoneBeans = new ArrayList<>();
    /**
     * 字母集合
     */
    private List<String> chars = new ArrayList<>();

    @Override
    protected void initView() {
        super.initView();
        findViewById(R.id.left_layout).setVisibility(View.GONE);
        linear_layout_more.removeAllViews();
        layoutParams.height = UIHelper.dip2px(45);
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.CENTER;
        linear_layout_more.setLayoutParams(layoutParams);
        linear_layout_more.setVisibility(View.VISIBLE);
        RecyclerView mRView = new RecyclerView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mRView.setLayoutParams(params);
        linear_layout_more.addView(mRView);

        phoneBeans.clear();
        chars.clear();
        List<PhoneInfo> phoneInfos = PhoneUtils.getPhoneNumberFromMobile(mContext);
        for (int i = 0; i < phoneInfos.size(); i++) {
            PhoneInfo phoneInfo = phoneInfos.get(i);
            if (!chars.contains(phoneInfo.getSortKey())) {
                chars.add(phoneInfo.getSortKey());
                List<PhoneInfo> infos = new ArrayList<>();
                infos.add(phoneInfo);
                phoneBeans.add(PhoneBean.getInstance(phoneInfo.getSortKey(), infos));

            } else {
                phoneBeans.get(chars.size() - 1).getPhoneInfos().add(phoneInfo);

            }
        }
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        PhoneAdapter adapter = new PhoneAdapter(phoneBeans);
        mRecyclerView.setAdapter(adapter);

        LetterAdapter mLetterAdapter = new LetterAdapter(phoneBeans);
        mRView.setLayoutManager(new MyGridLayoutManager(mContext, phoneBeans.size()));
        mRView.setAdapter(mLetterAdapter);
        mRView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                seleteToPosition(position);

            }
        });
    }

    @Override
    public void getDataFromNet(boolean isRefresh) {
        mSwipeRefreshLayout.setRefreshing(false);

    }
}
