package com.old.time.postcard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.activitys.BaseActivity;
import com.old.time.adapters.LetterAdapter;
import com.old.time.beans.PhoneBean;
import com.old.time.beans.PhoneInfo;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.MyGridLayoutManager;
import com.old.time.utils.PhoneUtils;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.ScreenTools;

import java.util.ArrayList;
import java.util.List;

public class PostCardActivity extends BaseActivity {

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

    private RecyclerView mRecyclerView;
    private TextView tv_center_key;

    @Override
    protected void initView() {
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
        tv_center_key = findViewById(R.id.tv_center_key);
        mRecyclerView = findViewById(R.id.c_recycler_view);
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        PhoneAdapter adapter = new PhoneAdapter(phoneBeans);
        mRecyclerView.setAdapter(adapter);

        RecyclerView mRView = findViewById(R.id.recycler_view_bottom);
        mRView.setLayoutManager(new MyGridLayoutManager(mContext, phoneBeans.size()));
        LetterAdapter mLetterAdapter = new LetterAdapter(phoneBeans);
        mRView.setAdapter(mLetterAdapter);
        mRView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent event) {
                int position = (int) (event.getX() * phoneBeans.size() / ScreenTools.instance(mContext).getScreenWidth());
                seleteToPosition(position);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    tv_center_key.setVisibility(View.GONE);

                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent event) {


            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {


            }
        });
    }

    /**
     * 定位显示哪一个item
     *
     * @param position
     */
    public void seleteToPosition(int position) {
        if (mRecyclerView == null) {

            return;
        }
        if (position >= phoneBeans.size()) {
            tv_center_key.setVisibility(View.GONE);

        } else {
            tv_center_key.setVisibility(View.VISIBLE);
            tv_center_key.setText(phoneBeans.get(position).getCodeKey());

        }
        LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        manager.scrollToPositionWithOffset(position, 0);

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_post_card;
    }
}
