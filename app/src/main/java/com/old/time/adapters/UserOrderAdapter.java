package com.old.time.adapters;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.dialogs.DialogListManager;
import com.old.time.interfaces.OnClickManagerCallBack;
import com.old.time.utils.UIHelper;

import java.util.List;

/**
 * Created by NING on 2018/3/28.
 */

public class UserOrderAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public UserOrderAdapter(List<String> data) {
        super(R.layout.adapter_user_order, data);


    }

    @Override
    protected void convert(final BaseViewHolder helper, String item) {
        helper.getView(R.id.order_status).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = helper.getLayoutPosition();
                showDeleteDialog(position);

            }
        });
    }

    private DialogListManager mDialog;

    private void showDeleteDialog(int position) {
        if (mDialog == null) {
            mDialog = new DialogListManager(mContext, new OnClickManagerCallBack() {
                @Override
                public void onClickRankManagerCallBack(int typeId, String typeName) {
                    UIHelper.ToastMessage(mContext, typeName);

                }
            });
        }
        mDialog.setDialogViewData("订单编号: " + position, new String[]{"删除订单"});
    }
}
