package com.old.time.postcard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.zxing.activity.CaptureActivity;
import com.old.time.R;
import com.old.time.activitys.BaseActivity;
import com.old.time.activitys.LocationMapActivity;
import com.old.time.activitys.TouchSettingActivity;
import com.old.time.activitys.WebViewActivity;
import com.old.time.adapters.LetterAdapter;
import com.old.time.beans.PhoneBean;
import com.old.time.beans.PhoneInfo;
import com.old.time.permission.PermissionUtil;
import com.old.time.pops.PostCartPop;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.MyGridLayoutManager;
import com.old.time.utils.PhoneUtils;
import com.old.time.utils.PictureUtil;
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

    private PhoneAdapter adapter;
    private RecyclerView mRecyclerView;
    private TextView tv_center_key;
    private ImageView img_more;


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
        adapter = new PhoneAdapter(phoneBeans);
        View headerView = View.inflate(mContext, R.layout.header_post_cart, null);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FastMailActivity.startFastMailActivity(mContext);

            }
        });
        adapter.removeAllHeaderView();
        adapter.addHeaderView(headerView);
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

        img_more = findViewById(R.id.img_more);
        img_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow();

            }
        });
    }

    private PostCartPop mPostCartPop;

    private void showPopWindow() {
        if (mPostCartPop == null) {
            mPostCartPop = new PostCartPop(mContext, new OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                    if (mPostCartPop != null && mPostCartPop.isShowing()) {
                        mPostCartPop.dismiss();

                    }
                    String string = (String) adapter.getItem(position);
                    switch (string) {
                        case "扫一扫":
                            PictureUtil.captureCode(mContext);

                            break;
                        case "附近":
                            LocationMapActivity.startLocationMapActivity(mContext);

                            break;
                        case "设置":
                            TouchSettingActivity.startSettingTouchActivity(mContext);

                            break;
                        default:

                            break;
                    }
                }
            });
        }
        mPostCartPop.showPopWindow(img_more, new String[]{"扫一扫", "附近", "设置"});

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
        if (position != 0) {
            position = position + adapter.getHeaderLayoutCount();

        }
        LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        manager.scrollToPositionWithOffset(position, 0);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onPermissionResult(this, permissions, grantResults, new PermissionUtil.PermissionCallBack() {
            @Override
            public void onSuccess() {
                PictureUtil.captureCode(mContext);

            }

            @Override
            public void onShouldShow() {

            }

            @Override
            public void onFailed() {
                showDialogPermission();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {

            return;
        }
        switch (requestCode) {
            case CaptureActivity.REQ_CODE:
                String str = data.getStringExtra(CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN);
                WebViewActivity.startWebViewActivity(mContext, str);

                break;
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_post_card;
    }
}
