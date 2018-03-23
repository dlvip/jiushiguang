package com.old.time.activitys;

import android.view.View;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.dialogs.DialogChoseAddress;

public class CreateDressActivity extends BaseActivity {

    private TextView tv_select_address;

    @Override
    protected void initView() {
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        setTitleText("添加地址");
        tv_select_address = findViewById(R.id.tv_select_address);


    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_select_address:
                showAddressDialog();

                break;
        }
    }

    private DialogChoseAddress mDialogChoseAddress;

    /**
     * 选择地址弹框
     */
    private void showAddressDialog() {
        if (mDialogChoseAddress == null) {
            mDialogChoseAddress = new DialogChoseAddress(mContext, new DialogChoseAddress.OnChooseListener() {
                @Override
                public void endSelect(DialogChoseAddress.CityInfo cityInfo) {
                    if (cityInfo != null) {
                        tv_select_address.setText(cityInfo.province.name + "  " + cityInfo.district.name + "  " + cityInfo.city.name);

                    }
                }
            });
        }
        mDialogChoseAddress.showDialog();
    }


    @Override
    protected void initEvent() {
        super.initEvent();
        tv_select_address.setOnClickListener(this);

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_creat_dress;
    }
}
