package com.old.time.postcard;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.PhoneInfo;
import com.old.time.utils.DataUtils;

import java.util.List;

public class PCardAdapter  extends BaseQuickAdapter<PhoneInfo, BaseViewHolder>{

    public PCardAdapter(@Nullable List<PhoneInfo> data) {
        super(R.layout.adapter_phone_detail, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PhoneInfo item) {
        helper.setText(R.id.tv_phone_num, item.getPhone())//
                .setText(R.id.tv_phone_dress, item.getPhoneDress())//
                .setVisible(R.id.view_line, helper.getLayoutPosition() != 0);
        if (TextUtils.isEmpty(item.getPhoneDress())) {
            DataUtils.getPhoneMsg(item.getPhone());

        }
    }
}
