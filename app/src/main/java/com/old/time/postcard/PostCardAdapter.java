package com.old.time.postcard;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.PhoneInfo;
import com.old.time.dialogs.DialogListManager;
import com.old.time.interfaces.OnClickManagerCallBack;
import com.old.time.utils.ActivityUtils;

import java.util.List;

public class PostCardAdapter extends BaseQuickAdapter<PhoneInfo, BaseViewHolder> {


    PostCardAdapter(@Nullable List<PhoneInfo> data) {
        super(R.layout.adapter_post_card, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, final PhoneInfo item) {
        helper.setText(R.id.tv_user_pic, item.getNamePic())//
                .setText(R.id.tv_user_name, item.getName())//
                .setGone(R.id.view_line_1, helper.getLayoutPosition() != 0);

        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCallPhoneDialog(item);

            }
        });
    }

    private DialogListManager dialogListManager;
    private PhoneInfo mPhoneInfo;

    private void showCallPhoneDialog(PhoneInfo phoneInfo) {
        if (phoneInfo == null) {

            return;
        }
        this.mPhoneInfo = phoneInfo;
        if (dialogListManager == null) {
            dialogListManager = new DialogListManager(mContext, new OnClickManagerCallBack() {
                @Override
                public void onClickRankManagerCallBack(int position, String typeName) {
                    callPhone(mPhoneInfo.getNumber());

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
        ActivityUtils.startActivity((Activity) mContext, intent);

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
        ActivityUtils.startActivity((Activity) mContext, intent);

    }
}
