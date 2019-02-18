package com.old.time.postcard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.activitys.BaseActivity;
import com.old.time.beans.PhoneInfo;
import com.old.time.dialogs.DialogListManager;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.interfaces.OnClickManagerCallBack;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.MyLinearLayoutManager;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;

import java.util.Arrays;

public class PCardDetailActivity extends BaseActivity {

    /**
     * 联系人详情页
     *
     * @param context
     */
    public static void startPCardDetailActivity(Context context, PhoneInfo phoneInfo) {
        Intent intent = new Intent(context, PCardDetailActivity.class);
        intent.putExtra(PHONE_INFO, phoneInfo);
        ActivityUtils.startActivity((Activity) context, intent);

    }

    public static final String PHONE_INFO = "phoneInfo";

    private BaseQuickAdapter<String, BaseViewHolder> adapter;

    private ImageView img_header_bg, img_user_pic;
    private RecyclerView recycler_view_call;
    private TextView tv_user_name;

    @Override
    protected void initView() {
        mPhoneInfo = (PhoneInfo) getIntent().getSerializableExtra(PHONE_INFO);
        img_header_bg = findViewById(R.id.img_header_bg);
        img_user_pic = findViewById(R.id.img_user_pic);
        tv_user_name = findViewById(R.id.tv_user_name);
        recycler_view_call = findViewById(R.id.recycler_view_call);

        findViewById(R.id.tv_call_phone).setOnClickListener(this);
        findViewById(R.id.relative_layout_title).setBackgroundResource(R.color.transparent);

        setTitleText(mPhoneInfo.getName());
        tv_user_name.setText(mPhoneInfo.getName());
        GlideUtils.getInstance().setImgTransRes(mContext, mPhoneInfo.getPhoto(), img_header_bg, 0, 0);
        GlideUtils.getInstance().setRadiusImageView(mContext, mPhoneInfo.getPhoto(), img_user_pic, 10);
        recycler_view_call.setLayoutManager(new MyLinearLayoutManager(mContext));
        recycler_view_call.addItemDecoration(new RecyclerItemDecoration(mContext));
        adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.adapter_phone_detail) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_phone_num, item).setVisible(R.id.view_line, helper.getLayoutPosition() != 0);

            }
        };
        recycler_view_call.setAdapter(adapter);
        adapter.setNewData(Arrays.asList(mPhoneInfo.getNumber().split(",")));

    }

    private DialogListManager dialogListManager;
    private PhoneInfo mPhoneInfo;

    private void showCallPhoneDialog() {
        if (dialogListManager == null) {
            dialogListManager = new DialogListManager(mContext, new OnClickManagerCallBack() {
                @Override
                public void onClickRankManagerCallBack(int position, String typeName) {
                    callPhone(mPhoneInfo.getNumber().split(",")[0]);

                }
            });
        }
        dialogListManager.setDialogViewData("拨号", new String[]{mPhoneInfo.getName()});

    }

    /**
     * 拨打电话（直接拨打电话）
     *
     * @param phoneNum 电话号码
     */
    private void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        ActivityUtils.startActivity(mContext, intent);

    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void callPhoneBySelf(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        ActivityUtils.startActivity(mContext, intent);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_call_phone:
                showCallPhoneDialog();

                break;
            case R.id.tv_call_video:
                UIHelper.ToastMessage(mContext, "敬请期待");

                break;
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_pcard_detail;
    }
}
