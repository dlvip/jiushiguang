package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.adapters.SignNameAdapter;
import com.old.time.beans.ResultBean;
import com.old.time.beans.SignNameEntity;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UserLocalInfoUtils;

import java.util.ArrayList;
import java.util.List;

public class SignListActivity extends BaseCActivity {

    public static final String FRIEND_ID = "friendId";

    /**
     * 打卡列表
     *
     * @param context
     */
    public static void startSignListActivity(Context context, String friendId) {
        Intent intent = new Intent(context, SignListActivity.class);
        intent.putExtra(FRIEND_ID, friendId);
        ActivityUtils.startActivity((Activity) context, intent);

    }

    private List<SignNameEntity> signNameEntities = new ArrayList<>();
    private SignNameAdapter adapter;
    private String friendId;

    @Override
    protected void initView() {
        friendId = getIntent().getStringExtra(FRIEND_ID);
        super.initView();
        setTitleText(TextUtils.isEmpty(friendId) ? "每日书签" : "个性书签");
        setSendText("创建");
        findViewById(R.id.right_layout_send).setVisibility(View.VISIBLE);
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        adapter = new SignNameAdapter(signNameEntities);
        mRecyclerView.setAdapter(adapter);

    }

    private int startNum;

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        if (isRefresh) {
            startNum = 0;

        }
        HttpParams params = new HttpParams();
        params.put("userId", UserLocalInfoUtils.instance().getUserId());
        params.put("friendId", friendId);
        params.put("pageNum", startNum);
        params.put("pageSize", Constant.PageSize);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_SIGN_NAME_LIST, new JsonCallBack<ResultBean<List<SignNameEntity>>>() {
            @Override
            public void onSuccess(ResultBean<List<SignNameEntity>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                startNum++;
                if (isRefresh) {
                    signNameEntities.clear();
                    adapter.setNewData(signNameEntities);

                }
                adapter.addData(mResultBean.data);
            }

            @Override
            public void onError(ResultBean<List<SignNameEntity>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    @Override
    public void save(View view) {
        super.save(view);
        SignCreateActivity.startSignCreateActivity(mContext);

    }
}
