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
import com.google.zxing.utils.ImageFindQrUtils;
import com.old.time.R;
import com.old.time.activitys.BaseActivity;
import com.old.time.activitys.UserLoginActivity;
import com.old.time.activitys.WebViewActivity;
import com.old.time.adapters.LetterAdapter;
import com.old.time.beans.PhoneBean;
import com.old.time.beans.PostCartBean;
import com.old.time.beans.RQCodeBean;
import com.old.time.permission.PermissionUtil;
import com.old.time.pops.PostCartPop;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.Base64Utils;
import com.old.time.utils.DebugLog;
import com.old.time.utils.MyGridLayoutManager;
import com.old.time.utils.PhoneUtils;
import com.old.time.utils.PictureUtil;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.ScreenTools;
import com.old.time.utils.UIHelper;
import com.old.time.utils.UserLocalInfoUtils;

import java.util.ArrayList;
import java.util.List;

public class PostCardActivity extends BaseActivity {

    /**
     * 通讯录
     *
     * @param mContext
     */
    public static void startPostCardActivity(Context mContext) {
        if (!UserLocalInfoUtils.instance().isUserLogin()) {
            UserLoginActivity.startUserLoginActivity((Activity) mContext);

            return;
        }

        Intent intent = new Intent(mContext, PostCardActivity.class);
        ActivityUtils.startActivity((Activity) mContext, intent);
        ActivityUtils.finishActivity((Activity) mContext);

    }

    /**
     * 联系人集合
     */
    private List<PostCartBean> postCartBeans = new ArrayList<>();
    /**
     * 字母集合
     */
    private List<String> chars = new ArrayList<>();
    private LetterAdapter mLetterAdapter;
    private RecyclerView mRView;

    private PhoneAdapter adapter;
    private RecyclerView mRecyclerView;
    private TextView tv_center_key;
    private ImageView img_more;

    @Override
    protected void initView() {
        findViewById(R.id.frame_layout_left).setVisibility(View.GONE);

        tv_center_key = findViewById(R.id.tv_center_key);
        mRecyclerView = findViewById(R.id.c_recycler_view);
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        adapter = new PhoneAdapter(postCartBeans);
        mRecyclerView.setAdapter(adapter);

        mRView = findViewById(R.id.recycler_view_bottom);
        mLetterAdapter = new LetterAdapter(postCartBeans);
        mRView.setAdapter(mLetterAdapter);
        mRView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent event) {
                int position = (int) (event.getX() * postCartBeans.size() / ScreenTools.instance(mContext).getScreenWidth());
                selectToPosition(position);
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
                showMoreBtnPopWindow();

            }
        });

        PhoneUtils.getPhoneNumberFromMobile(mContext, new PhoneUtils.OnPhoneBeanResultListener() {
            @Override
            public void onPhoneBeanList(List<PhoneBean> phoneBeans) {
                if (phoneBeans == null || phoneBeans.size() == 0) {

                    return;
                }
                postCartBeans.clear();
                chars.clear();
                for (int i = 0; i < phoneBeans.size(); i++) {
                    PhoneBean phoneBean = phoneBeans.get(i);
                    if (!chars.contains(phoneBean.getSortKey())) {
                        chars.add(phoneBean.getSortKey());
                        List<PhoneBean> infos = new ArrayList<>();
                        infos.add(phoneBean);
                        postCartBeans.add(PostCartBean.getInstance(phoneBean.getSortKey(), infos));

                    } else {
                        postCartBeans.get(chars.size() - 1).getPhoneBeans().add(phoneBean);

                    }
                }
                adapter.setNewData(postCartBeans);
                mLetterAdapter.setNewData(postCartBeans);
                mRView.setLayoutManager(new MyGridLayoutManager(mContext, postCartBeans.size()));
            }
        });
    }

    private PostCartPop mPostCartPop;

    private void showMoreBtnPopWindow() {
        if (mPostCartPop == null) {
            mPostCartPop = new PostCartPop(mContext, new OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                    if (mPostCartPop != null && mPostCartPop.isShowing()) {
                        mPostCartPop.dismiss();

                    }
                    String string = (String) adapter.getItem(position);
                    assert string != null;
                    switch (string) {
                        case "扫一扫":
                            PictureUtil.captureCode(mContext);

                            break;
                        case "服务号":
                            FastMailActivity.startFastMailActivity(mContext);

                            break;
                        case "个人中心":
                            UserCardActivity.startUserCardActivity(mContext, UserLocalInfoUtils.instance().getUserId());

                            break;
                        default:

                            break;
                    }
                }
            });
        }
        mPostCartPop.showPopWindow(img_more, new String[]{"扫一扫", "服务号", "个人中心"});

    }

    /**
     * 定位显示哪一个item
     *
     * @param position
     */
    public void selectToPosition(int position) {
        if (mRecyclerView == null) {

            return;
        }
        if (position >= postCartBeans.size()) {
            tv_center_key.setVisibility(View.GONE);

        } else {
            tv_center_key.setVisibility(View.VISIBLE);
            tv_center_key.setText(postCartBeans.get(position).getCodeKey());

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
                String[] dateS = str.split(ImageFindQrUtils.SPLIT_KEY);
                if (dateS.length > 1) {
                    try {
                        String encodeStr = Base64Utils.decode(dateS[1]);
                        RQCodeBean.mRQCodeNext(mContext, encodeStr);

                    } catch (Exception e) {
                        DebugLog.d(TAG, e.getMessage());
                        UIHelper.ToastMessage(mContext, "内容不存在");

                    }
                } else {
                    WebViewActivity.startWebViewActivity(mContext, str);

                }
                break;
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_post_card;
    }
}
