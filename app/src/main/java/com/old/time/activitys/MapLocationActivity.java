package com.old.time.activitys;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.old.time.R;
import com.old.time.permission.PermissionUtil;
import com.old.time.utils.AMapLocationUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.DebugLog;
import com.old.time.utils.UIHelper;

public class MapLocationActivity extends BaseActivity {


    public static void startMapLocationActivity(Activity mContext) {
        if (!PermissionUtil.checkAndRequestPermissionsInActivity(mContext, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION})) {

            return;
        }
        Intent intent = new Intent(mContext, MapLocationActivity.class);
        ActivityUtils.startActivity(mContext, intent);
    }

    private String addressStr;

    private AMapLocationUtils mAMapLocationUtils;

    @Override
    protected void initView() {
        findViewById(R.id.right_layout_send).setVisibility(View.VISIBLE);
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        mAMapLocationUtils = AMapLocationUtils.getmAMapLocationUtils();
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
                    addressStr = Province + City + District + Street;
                    DebugLog.e("addressStr::", addressStr);

                } else {
                    String ErrorInfo = aMapLocation.getErrorInfo();
                    UIHelper.ToastMessage(mContext, "定位失败，ErrCode:::" + ErrorCode + ", errInfo:" + ErrorInfo);

                }
            }
        });
    }

    @Override
    public void save(View view) {
        super.save(view);
        Intent intent = new Intent();
        intent.putExtra("addressStr", addressStr);
        setResult(Activity.RESULT_OK, intent);
        ActivityUtils.finishActivity(mContext);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_map_location;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAMapLocationUtils.stopLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAMapLocationUtils.onDestroyLocation();
    }
}
