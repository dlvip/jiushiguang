package com.old.time.dialogs;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.beans.AddressBean;
import com.old.time.databases.AddressDao;
import com.old.time.views.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NING on 2018/3/20.
 */

public class DialogChoseAddress extends BaseDialog {

    private List<String> provinces, citys, districts;

    private AddressDao addressDao;
    private List<AddressBean> provinceDatas;
    private List<AddressBean> cityDatas;
    private List<AddressBean> districtDatas;

    private TextView tvTitle;
    private WheelView wvProvinces;
    private WheelView wvCitys;
    private WheelView wvDistricts;
    private TextView btCancel;
    private TextView btOk;

    public DialogChoseAddress(@NonNull Activity context) {
        super(context, R.style.transparentFrameWindowStyle);
        addressDao = new AddressDao(getContext());


    }

    @Override
    protected void initDialogView() {
        tvTitle = findViewbyId(R.id.tv_title);
        wvProvinces = findViewbyId(R.id.wv_provinces);
        wvCitys = findViewbyId(R.id.wv_citys);
        wvDistricts = findViewbyId(R.id.wv_districts);

        btCancel = findViewbyId(R.id.bt_cancel);
        btOk = findViewbyId(R.id.bt_ok);

        /**
         * 设置显示的内容信息
         */
        provinceDatas = addressDao.getAddressesByFId("0");
        cityDatas = addressDao.getAddressesByFId(String.valueOf(provinceDatas.get(0).zoneId));
        districtDatas = addressDao.getAddressesByFId(String.valueOf(cityDatas.get(0).zoneId));

        provinces = getAddressDatas(provinceDatas);
        citys = getAddressDatas(cityDatas);
        districts = getAddressDatas(districtDatas);

        wvProvinces.setData(provinces);
        wvCitys.setData(citys);
        wvDistricts.setData(districts);

        /**
         * 默认选择第一个
         */
        wvProvinces.setDefault(0);
        wvCitys.setDefault(0);
        wvDistricts.setDefault(0);

        tvTitle.setText(getContext().getString(R.string.choose_citys));

    }

    /**
     * 将数据库中的数据转为界面显示的数据
     *
     * @param addresses
     * @return
     */
    private List<String> getAddressDatas(List<AddressBean> addresses) {

        List<String> datas = new ArrayList<>();
        for (AddressBean bean : addresses) {
            datas.add(bean.name);
        }
        return datas;
    }

    public class CityInfo {
        public AddressBean province;
        public AddressBean city;
        public AddressBean district;

        public CityInfo(AddressBean province, AddressBean city, AddressBean district) {
            this.province = province;
            this.city = city;
            this.district = district;
        }
    }


    @Override
    protected int setContentView() {
        return R.layout.dialog_choose_city;
    }
}
