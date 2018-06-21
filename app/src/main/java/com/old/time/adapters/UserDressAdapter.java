package com.old.time.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.activitys.CreateDressActivity;
import com.old.time.dialogs.DialogListManager;
import com.old.time.interfaces.OnClickManagerCallBack;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.UIHelper;

import java.util.List;

/**
 * Created by wcl on 2018/3/21.
 */

public class UserDressAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public UserDressAdapter(List<String> data) {
        super(R.layout.adapter_manager_address, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.getView(R.id.tv_address_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDressDialog();

            }
        });
        helper.getView(R.id.tv_address_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtDressMessage();

            }
        });
    }

    /**
     * 编辑地址
     */
    private void edtDressMessage() {
        Intent intent = new Intent(mContext, CreateDressActivity.class);
        ActivityUtils.startActivity((Activity) mContext, intent);

    }

    private DialogListManager mDialogListManager;

    /**
     * 删除地址
     */
    private void showDeleteDressDialog() {
        if (mDialogListManager == null) {
            mDialogListManager = new DialogListManager(mContext, new OnClickManagerCallBack() {
                @Override
                public void onClickRankManagerCallBack(int typeId, String typeName) {
                    UIHelper.ToastMessage(mContext, "删除地址");

                }
            });
        }
        mDialogListManager.setDialogViewData("删除地址", new String[]{"删除地址"});
    }
}
