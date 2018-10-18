package com.old.time.activitys;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.adapters.SearchUserAdapter;
import com.old.time.beans.ResultBean;
import com.old.time.beans.UserInfoBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.StringUtils;
import com.old.time.utils.UIHelper;
import com.old.time.utils.UserLocalInfoUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseSearchActivity {

    private List<UserInfoBean> userInfoBeans = new ArrayList<>();
    private SearchUserAdapter adapter;
    private EditText edt_search_name;
    private String mobile;

    @Override
    protected void initView() {
        super.initView();
        edt_search_name = findViewById(R.id.edt_search_name);
        findViewById(R.id.tv_search_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchName = edt_search_name.getText().toString().trim();
                if (TextUtils.isEmpty(searchName) && StringUtils.isMobileNO(searchName)) {
                    UIHelper.ToastMessage(mContext, "请输入手机号");

                    return;
                }
                SearchActivity.this.mobile = searchName;
                getDataFromNet(true);

            }
        });
        adapter = new SearchUserAdapter(userInfoBeans);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void getDataFromNet(boolean isRefresh) {
        if (TextUtils.isEmpty(mobile) || adapter == null) {
            mSwipeRefreshLayout.setRefreshing(false);

            return;
        }
        userInfoBeans.clear();
        adapter.setNewData(userInfoBeans);
        HttpParams params = new HttpParams();
        params.put("userId", UserLocalInfoUtils.instance().getUserId());
        params.put("mobile", mobile);
        OkGoUtils.getInstance().postNetForData(params, Constant.INSERT_GOODS_USER, new JsonCallBack<ResultBean<UserInfoBean>>() {
            @Override
            public void onSuccess(ResultBean<UserInfoBean> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                userInfoBeans.add(mResultBean.data);
                adapter.setNewData(userInfoBeans);

            }

            @Override
            public void onError(ResultBean<UserInfoBean> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });
    }
}
