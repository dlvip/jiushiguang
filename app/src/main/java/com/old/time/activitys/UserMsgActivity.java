package com.old.time.activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.beans.PhotoInfoBean;
import com.old.time.beans.ResultBean;
import com.old.time.beans.UserInfoBean;
import com.old.time.constants.Code;
import com.old.time.constants.Constant;
import com.old.time.dialogs.DialogChoseAddress;
import com.old.time.dialogs.DialogInputBottom;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.interfaces.OnClickManagerCallBack;
import com.old.time.interfaces.UploadImagesCallBack;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.AliyPostUtil;
import com.old.time.utils.FileUtils;
import com.old.time.utils.PictureUtil;
import com.old.time.utils.UIHelper;
import com.old.time.utils.UserLocalInfoUtils;

import java.util.ArrayList;
import java.util.List;

public class UserMsgActivity extends BaseActivity {

    private TextView tv_edt_phone, tv_edt_nick, tv_edt_address, tv_edt_brief;
    private ImageView img_user_header;
    private int PIC_COUNT_SIZE = 1;
    private UserInfoBean mUserInfoBean;

    private RelativeLayout right_layout_send;

    /**
     * 消息通知
     *
     * @param mContext
     */
    public static void startUserMsgActivity(Activity mContext) {
        if (!UserLocalInfoUtils.instance().isUserLogin()) {
            UserLoginActivity.startUserLoginActivity(mContext);

            return;
        }
        Intent intent = new Intent(mContext, UserMsgActivity.class);
        ActivityUtils.startActivity(mContext, intent);

    }

    @Override
    protected void initView() {
        mUserInfoBean = UserLocalInfoUtils.instance().getmUserInfoBean();
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        setTitleText("资料信息");
        img_user_header = findViewById(R.id.img_user_header);
        tv_edt_phone = findViewById(R.id.tv_edt_phone);
        tv_edt_nick = findViewById(R.id.tv_edt_nick);
        tv_edt_address = findViewById(R.id.tv_edt_address);
        tv_edt_brief = findViewById(R.id.tv_edt_brief);

        right_layout_send = findViewById(R.id.right_layout_send);
        right_layout_send.setVisibility(View.VISIBLE);
        right_layout_send.setOnClickListener(this);

        tv_edt_phone.setText(mUserInfoBean.getMobile());
        GlideUtils.getInstance().setRoundImageView(mContext, mUserInfoBean.getAvatar(), img_user_header);
        tv_edt_nick.setText(mUserInfoBean.getUserName());
        tv_edt_brief.setText(mUserInfoBean.getVocation());
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
                PhotoPickActivity.startPhotoPickActivity(mContext, true, PIC_COUNT_SIZE, Code.REQUEST_CODE_30);

                break;
            case R.id.linear_layout_phone:
                getmDialogInputBottom(0);
                mDialogInputBottom.showDialog(R.string.edt_nickname, R.string.dialog_true);

                break;
            case R.id.linear_layout_address:
                showAddressDialog();

                break;
            case R.id.linear_layout_brief:
                getmDialogInputBottom(1);
                mDialogInputBottom.showDialog(R.string.user_set_brief, R.string.dialog_true);

                break;
            case R.id.right_layout_send:
                updateUserMsg();

                break;
        }
    }

    private String userName, avatar, vocation, location, birthday, sex;
    private ProgressDialog pd;

    /**
     * 修改用户信息
     */
    private void updateUserMsg() {
        if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(avatar) //
                && TextUtils.isEmpty(vocation) && TextUtils.isEmpty(birthday) //
                && TextUtils.isEmpty(sex)) {

            UIHelper.ToastMessage(mContext, "没有要提交的内容");

            return;
        }
        pd = UIHelper.showProgressMessageDialog(mContext, getString(R.string.please_wait));
        HttpParams params = new HttpParams();
        params.put("userId", UserLocalInfoUtils.instance().getUserId());
        params.put("userName", userName);
        params.put("avatar", avatar);
//        params.put("birthday", birthday);
//        params.put("sex", sex);
        params.put("vocation", vocation);
        OkGoUtils.getInstance().postNetForData(params, Constant.UPDATE_USER_MSG, new JsonCallBack<ResultBean<UserInfoBean>>() {
            @Override
            public void onSuccess(ResultBean<UserInfoBean> mResultBean) {
                UIHelper.dissmissProgressDialog(pd);
                if (mResultBean == null || mResultBean.data == null) {
                    UIHelper.ToastMessage(mContext, "操作失败");

                    return;
                }
                UserLocalInfoUtils.instance().setmUserInfoBean(mResultBean.data);
                UIHelper.ToastMessage(mContext, mResultBean.msg);
                ActivityUtils.finishActivity(mContext);
            }

            @Override
            public void onError(ResultBean<UserInfoBean> mResultBean) {
                UIHelper.dissmissProgressDialog(pd);
                if (mResultBean == null || mResultBean.data == null) {
                    UIHelper.ToastMessage(mContext, "操作失败");

                    return;
                }
                UIHelper.ToastMessage(mContext, mResultBean.msg);

            }
        });
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
                        location = cityInfo.province.name + "  " + cityInfo.city.name + "  " + cityInfo.district.name;
                        tv_edt_address.setText(location);

                    }
                }
            });
        }
        mDialogChoseAddress.showDialog();
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
                            UserMsgActivity.this.userName = typeName;

                            break;
                        case 1:
                            if (tv_edt_brief == null) {

                                return;
                            }
                            tv_edt_brief.setText(typeName);
                            UserMsgActivity.this.vocation = typeName;

                            break;
                    }
                }
            });
        }
    }

    private List<String> picPaths = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {

            return;
        }
        switch (requestCode) {
            case Code.REQUEST_CODE_30:
                List<String> pathStrs = data.getStringArrayListExtra(PhotoPickActivity.SELECT_PHOTO_LIST);
                if (pathStrs == null || pathStrs.size() == 0 || TextUtils.isEmpty(pathStrs.get(0))) {

                    return;
                }
                Uri uri = Uri.fromFile(FileUtils.createPicturePath(System.currentTimeMillis() + ""));
                PictureUtil.cropPhotoRetrunBitmap3(this, pathStrs.get(0), uri, new int[]{1, 1}, Code.REQUEST_CODE_40);

                break;
            case Code.REQUEST_CODE_40:
                String outputPath = data.getStringExtra("outputPath");
                if (TextUtils.isEmpty(outputPath)) {

                    return;
                }
                GlideUtils.getInstance().setRoundImageView(mContext, outputPath, img_user_header);
                picPaths.clear();
                picPaths.add(outputPath);
                sendAliyunPic(picPaths);

                break;
        }
    }

    /**
     * 上传图片到阿里云
     *
     * @param picPaths
     */
    private void sendAliyunPic(List<String> picPaths) {
        pd = UIHelper.showProgressMessageDialog(mContext, getString(R.string.please_wait));
        AliyPostUtil.getInstance(mContext).uploadCompresImgsToAliyun(picPaths, new UploadImagesCallBack() {
            @Override
            public void getImagesPath(List<PhotoInfoBean> onlineFileName) {
                UIHelper.dissmissProgressDialog(pd);
                if (onlineFileName == null || onlineFileName.size() == 0) {

                    UIHelper.ToastMessage(mContext, "上传图片失败");
                    return;
                }
                UserMsgActivity.this.avatar = onlineFileName.get(0).picKey;
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_user_mesg;
    }
}
