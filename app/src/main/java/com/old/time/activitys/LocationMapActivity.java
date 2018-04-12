package com.old.time.activitys;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.MyLocationStyle;
import com.old.time.R;
import com.old.time.permission.PermissionUtil;
import com.old.time.utils.ActivityUtils;

public class LocationMapActivity extends BaseActivity {


    public static void startLocationMapActivity(Activity mContext) {
        if (!PermissionUtil.checkAndRequestPermissionsInActivity(mContext, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION})) {

            return;
        }
        Intent intent = new Intent(mContext, LocationMapActivity.class);
        ActivityUtils.startActivity(mContext, intent);
    }

    private MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView = findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写

        //连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。
        //（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.interval(2000);
        myLocationStyle.showMyLocation(true);

        AMap aMap = mMapView.getMap();
        //设置默认定位按钮是否显示，非必需设置。
        UiSettings mUiSettings = aMap.getUiSettings();
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setZoomInByScreenCenter(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        //设置为true表示启动显示定位蓝点
        aMap.setMyLocationEnabled(true);
        //设置定位蓝点的Style
        aMap.setMyLocationStyle(myLocationStyle);

//        PoiSearch.Query query = new PoiSearch.Query("建筑", "", "");
//        query.setPageSize(10);// 设置每页最多返回多少条poiitem
//        query.setPageNum(currentPage);//设置查询页码


    }

    @Override
    protected void initView() {
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_map_location;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }
}
