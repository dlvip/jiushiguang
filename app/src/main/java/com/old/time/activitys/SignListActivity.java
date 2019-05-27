package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.beans.BookEntity;
import com.old.time.constants.Constant;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;

public class SignListActivity extends BaseSignsActivity {

    /**
     * 打卡列表
     *
     * @param context
     */
    public static void startSignListActivity(Context context, BookEntity bookEntity) {
        Intent intent = new Intent(context, SignListActivity.class);
        intent.putExtra("bookEntity", bookEntity);
        ActivityUtils.startActivity((Activity) context, intent);

    }

    private BookEntity bookEntity;

    @Override
    protected void initView() {
        bookEntity = (BookEntity) getIntent().getSerializableExtra("bookEntity");
        super.initView();
        setTitleText("每日书签");
        setSendText("创建");
        findViewById(R.id.right_layout_send).setVisibility(View.VISIBLE);

    }

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        if (isRefresh) {
            startNum = 0;

        }
        this.isRefresh = isRefresh;
        HttpParams params = new HttpParams();
        params.put("bookId", bookEntity.getId());
        params.put("pageNum", startNum);
        params.put("pageSize", Constant.PageSize);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_SIGN_NAME_LIST, jsonCallBack);

    }

    @Override
    public void save(View view) {
        super.save(view);
        SignCreateActivity.startSignCreateActivity(mContext, bookEntity);

    }
}
