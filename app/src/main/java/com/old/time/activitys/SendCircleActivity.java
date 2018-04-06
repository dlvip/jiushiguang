package com.old.time.activitys;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.old.time.R;
import com.old.time.adapters.CirclePicAdapter;
import com.old.time.constants.Code;
import com.old.time.permission.PermissionUtil;
import com.old.time.utils.AMapLocationUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.DebugLog;
import com.old.time.utils.EasyPhotos;
import com.old.time.utils.MyGridLayoutManager;
import com.old.time.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;

public class SendCircleActivity extends BaseActivity {

    /**
     * 发送圈子内容
     *
     * @param mContext
     * @param requestCode
     */
    public static void startSendCircleActivity(Activity mContext, int requestCode) {
        if (!PermissionUtil.checkAndRequestPermissionsInActivity(mContext, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION})) {

            return;
        }
        Intent intent = new Intent(mContext, SendCircleActivity.class);
        ActivityUtils.startActivityForResult(mContext, intent, requestCode);

    }

    private TextView tv_user_location;
    private EditText input_send_text;
    private ImageView img_take_pic, img_rich_location;
    private RecyclerView recycler_view_pics;
    private List<String> picUrls = new ArrayList<>();
    private CirclePicAdapter mPicAdapter;

    @Override
    protected void initView() {
        CameraTakeActivity.startCameraActivity(mContext, Code.REQUEST_CODE_30);
        setTitleText("");
        findViewById(R.id.right_layout_send).setVisibility(View.VISIBLE);
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        img_rich_location = findViewById(R.id.img_rich_location);
        tv_user_location = findViewById(R.id.tv_user_location);
        input_send_text = findViewById(R.id.input_send_text);
        img_take_pic = findViewById(R.id.img_take_pic);
        recycler_view_pics = findViewById(R.id.recycler_view_pics);
        MyGridLayoutManager myGridLayoutManager = new MyGridLayoutManager(mContext, 5);
        recycler_view_pics.setLayoutManager(myGridLayoutManager);
        mPicAdapter = new CirclePicAdapter(picUrls);
        recycler_view_pics.setAdapter(mPicAdapter);

    }

    @Override
    protected void initEvent() {
        super.initEvent();
        img_take_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraTakeActivity.startCameraActivity(mContext, mPicAdapter.getData(), Code.REQUEST_CODE_30);

            }
        });
        tv_user_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserAddress();

            }
        });
        img_rich_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationMapActivity.startLocationMapActivity(mContext);

            }
        });
    }

    /**
     * 获取用户信息
     */
    private void getUserAddress() {
        AMapLocationUtils mAMapLocationUtils = AMapLocationUtils.getmAMapLocationUtils();
        mAMapLocationUtils.startLocation(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation == null) {
                    UIHelper.ToastMessage(mContext, "定位失败");

                    return;
                }
                int ErrorCode = aMapLocation.getErrorCode();
                if (ErrorCode == 0) {
                    String Province = aMapLocation.getProvince();//省信息
                    String City = aMapLocation.getCity();//城市信息
                    String District = aMapLocation.getDistrict();//城区信息
                    String Street = aMapLocation.getStreet();//街道信息
                    String StreetNum = aMapLocation.getStreetNum();//街道门牌号信息
                    String addressStr = Province + City + District + Street;
                    DebugLog.e("addressStr::", addressStr);
                    if (TextUtils.isEmpty(addressStr)) {
                        tv_user_location.setVisibility(View.GONE);

                    } else {
                        tv_user_location.setVisibility(View.VISIBLE);
                        tv_user_location.setText(addressStr);

                    }

                } else {
                    String ErrorInfo = aMapLocation.getErrorInfo();
                    UIHelper.ToastMessage(mContext, "定位失败，ErrCode:::" + ErrorCode + ", errInfo:" + ErrorInfo);

                }
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
            case Code.REQUEST_CODE_30:
                ArrayList<String> resultPhotos = data.getStringArrayListExtra(EasyPhotos.RESULT_PHOTOS);
                mPicAdapter.setNewData(resultPhotos);

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AMapLocationUtils.getmAMapLocationUtils().stopLocation();
        AMapLocationUtils.getmAMapLocationUtils().onDestroyLocation();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_send_circle;
    }
}
