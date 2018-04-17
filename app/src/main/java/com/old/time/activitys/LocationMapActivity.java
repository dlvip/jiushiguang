package com.old.time.activitys;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.cloud.CloudSearch;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.old.time.R;
import com.old.time.adapters.LocationAdapter;
import com.old.time.permission.PermissionUtil;
import com.old.time.utils.AMapLocationUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.DebugLog;
import com.old.time.utils.MyLinearLayoutManager;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;

public class LocationMapActivity extends BaseActivity {


    public static void startLocationMapActivity(Activity mContext) {
        if (!PermissionUtil.checkAndRequestPermissionsInActivity(mContext, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION})) {

            return;
        }
        Intent intent = new Intent(mContext, LocationMapActivity.class);
        ActivityUtils.startActivity(mContext, intent);
    }

    private MapView mMapView;
    private AMap aMap;

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

        aMap = mMapView.getMap();
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
        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {

            }
        });


    }

    private RecyclerView mRecyclerView;
    private LocationAdapter mAdapter;
    private List<PoiItem> poiItems = new ArrayList<>();

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.recycler_view_map);
        mRecyclerView.setLayoutManager(new MyLinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext));
        mAdapter = new LocationAdapter(poiItems);
        mRecyclerView.setAdapter(mAdapter);

        findViewById(R.id.tv_btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtils.finishActivity(mContext);

            }
        });
        Location location = aMap.getMyLocation();
        doSearchQuery(location.getLatitude(), location.getLongitude());
    }


    private PoiSearch.Query query;
    private String keyWord = "上地";
    private PoiSearch poiSearch;

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery(double latitude, double longitude) {
        query = new PoiSearch.Query(keyWord, "");
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(0);// 设置查第一页

        //构造 PoiSearch 对象，并设置监听
        poiSearch = new PoiSearch(this, query);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latitude, longitude), 1000));//设置周边搜索的中心点以及半径
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                poiItems.clear();
                if (poiResult == null) {

                    return;
                }
                List<PoiItem> poiItems = poiResult.getPois();
                if (poiItems == null) {

                    return;
                }
                mAdapter.setNewData(poiItems);
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
        //发送请求。
        poiSearch.searchPOIAsyn();

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
