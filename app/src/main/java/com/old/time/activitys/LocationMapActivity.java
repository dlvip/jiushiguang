package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.old.time.R;
import com.old.time.adapters.LocationAdapter;
import com.old.time.beans.PoiItemBean;
import com.old.time.permission.PermissionUtil;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.MyLinearLayoutManager;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.views.SearchEditText;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class LocationMapActivity extends BaseActivity {


    public static void startLocationMapActivity(Activity mContext) {
        if (!PermissionUtil.checkAndRequestPermissionsInActivity(mContext, ACCESS_COARSE_LOCATION)) {

            return;
        }
        Intent intent = new Intent(mContext, LocationMapActivity.class);
        ActivityUtils.startActivity(mContext, intent);
    }

    private GeocodeSearch geocoderSearch;
    private LatLonPoint searchLatlonPoint;
    private MapView mMapView;
    private AMap aMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView = findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写

        aMap = mMapView.getMap();
        //设置默认定位按钮是否显示，非必需设置。
        UiSettings mUiSettings = aMap.getUiSettings();
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setZoomInByScreenCenter(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        //设置为true表示启动显示定位蓝点
        aMap.setMyLocationEnabled(true);
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {


            }

            @Override
            public void onCameraChangeFinish(CameraPosition position) {
                LatLng mLatLng = position.target;
                searchLatlonPoint = new LatLonPoint(mLatLng.latitude, mLatLng.longitude);
                doSearchQuery(mLatLng.latitude, mLatLng.longitude);

            }
        });
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
                if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (result != null && result.getRegeocodeAddress() != null
                            && result.getRegeocodeAddress().getFormatAddress() != null) {
                        String address = result.getRegeocodeAddress().getProvince() + result.getRegeocodeAddress().getCity() + result.getRegeocodeAddress().getDistrict() + result.getRegeocodeAddress().getTownship();
                        firstItem = new PoiItem("regeo", searchLatlonPoint, address, address);
                        doSearchQuery(firstItem.getLatLonPoint().getLatitude(), firstItem.getLatLonPoint().getLongitude());

                    }
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {


            }
        });
        searchAddress();
    }

    /**
     * 搜索周边
     */
    private void searchAddress() {
        if (searchLatlonPoint == null) {

            return;
        }
        RegeocodeQuery query = new RegeocodeQuery(searchLatlonPoint, 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }

    private SearchEditText txtSearch;
    private PoiItem firstItem;
    private RecyclerView mRecyclerView;
    private LocationAdapter mAdapter;
    private List<PoiItemBean> mPoiItemBeans = new ArrayList<>();

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.recycler_view_map);
        mRecyclerView.setLayoutManager(new MyLinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext));
        mAdapter = new LocationAdapter(mPoiItemBeans);
        mRecyclerView.setAdapter(mAdapter);

        findViewById(R.id.tv_btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtils.finishActivity(mContext);

            }
        });

        txtSearch = findViewById(R.id.edt_search_name);
        txtSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    keyWord = txtSearch.getText().toString().trim();
                    PoiItem firstItem = new PoiItem("tip", searchLatlonPoint, keyWord, keyWord);
                    doSearchQuery(firstItem.getLatLonPoint().getLatitude(), firstItem.getLatLonPoint().getLongitude());

                }
                return false;
            }
        });
    }


    private PoiSearch.Query query;
    private String keyWord;
    private PoiSearch poiSearch;

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery(double latitude, double longitude) {
        query = new PoiSearch.Query(keyWord, "楼宇");
        query.setCityLimit(true);
        query.setPageSize(20);
        query.setPageNum(1);
        poiSearch = new PoiSearch(this, query);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latitude, longitude), 1500, true));//设置周边搜索的中心点以及半径
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                if (poiResult == null) {

                    return;
                }
                List<PoiItem> poiItems = poiResult.getPois();
                if (poiItems == null) {

                    return;
                }
                mPoiItemBeans.clear();
                if (firstItem != null) {
                    poiItems.add(firstItem);

                }
                for (int j = 0; j < poiItems.size(); j++) {
                    PoiItem mPoiItem = poiItems.get(j);
                    PoiItemBean mPoiItemBean = new PoiItemBean();
                    mPoiItemBean.isSelect = j == 0;
                    mPoiItemBean.setBusinessArea(mPoiItem.getBusinessArea());
                    mPoiItemBean.setCityName(mPoiItem.getCityName());
                    mPoiItemBean.setProvinceName(mPoiItem.getProvinceName());
                    mPoiItemBean.setTitle(mPoiItem.getTitle());
                    mPoiItemBeans.add(mPoiItemBean);

                }
                mAdapter.setNewData(mPoiItemBeans);
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
