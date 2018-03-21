package com.old.time.activitys;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.constants.Code;
import com.old.time.dialogs.DialogInputBottom;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.interfaces.OnClickManagerCallBack;
import com.old.time.utils.ActivityUtils;

import java.io.Serializable;
import java.util.List;

public class UserMesgActivity extends BaseActivity {

    private TextView tv_edt_phone, tv_edt_nick, tv_edt_address, tv_edt_brief;
    private ImageView img_user_header;
    private int PIC_COUNT_SIZE = 1;

    @Override
    protected void initView() {
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        setTitleText("资料信息");
        img_user_header = findViewById(R.id.img_user_header);
        tv_edt_phone = findViewById(R.id.tv_edt_phone);
        tv_edt_nick = findViewById(R.id.tv_edt_nick);
        tv_edt_address = findViewById(R.id.tv_edt_address);
        tv_edt_brief = findViewById(R.id.tv_edt_brief);

    }

    @Override
    protected void initEvent() {
        super.initEvent();
        findViewById(R.id.linear_layout_phone).setOnClickListener(this);
        findViewById(R.id.linear_layout_address).setOnClickListener(this);
        findViewById(R.id.linear_layout_brief).setOnClickListener(this);
        findViewById(R.id.frame_layout_header).setOnClickListener(this);

    }

    private DialogInputBottom mDialogInputBottom;
    private int mDialogType;

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.frame_layout_header:
                selectHeaderPic();

                break;
            case R.id.linear_layout_phone:
                getmDialogInputBottom(0);
                mDialogInputBottom.showDialog(R.string.edt_nickname, R.string.dialog_true);

                break;
            case R.id.linear_layout_address:


                break;
            case R.id.linear_layout_brief:
                getmDialogInputBottom(1);
                mDialogInputBottom.showDialog(R.string.user_set_brief, R.string.dialog_true);

                break;
        }
    }

    /**
     * 选择头像照片
     */
    private void selectHeaderPic() {
        Intent intent = new Intent(mContext, PhotoPickActivity.class);
        intent.putExtra(PhotoPickActivity.IS_SHOW_CAMERA, true);
        intent.putExtra(PhotoPickActivity.MAX_PICK_COUNT, PIC_COUNT_SIZE);
        ActivityUtils.startActivityForResult(mContext, intent, Code.REQUEST_CODE_30);

    }

    /**
     * 获取输入框弹窗
     */
    private void getmDialogInputBottom(int DialogType) {
        this.mDialogType = DialogType;
        if (mDialogInputBottom == null) {
            mDialogInputBottom = new DialogInputBottom(mContext, new OnClickManagerCallBack() {
                @Override
                public void onClickRankManagerCallBack(int typeId, String typeName) {
                    switch (mDialogType) {
                        case 0:
                            if (tv_edt_nick == null) {

                                return;
                            }
                            tv_edt_nick.setText(typeName);

                            break;
                        case 1:
                            if (tv_edt_brief == null) {

                                return;
                            }
                            tv_edt_brief.setText(typeName);

                            break;
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {

            return;
        }
        switch (requestCode) {
            case Code.REQUEST_CODE_30:
                List<String> pathStrs = data.getStringArrayListExtra(PhotoPickActivity.SELECT_PHOTO_LIST);
                if (pathStrs == null || pathStrs.size() == 0 || TextUtils.isEmpty(pathStrs.get(0)))

                    return;
                GlideUtils.getInstance().setRoundImageView(mContext, pathStrs.get(0), img_user_header);

                break;
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_user_mesg;
    }
}
