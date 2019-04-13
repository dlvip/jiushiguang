package com.old.time.rongIM;

import android.net.Uri;
import android.view.View;

import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.activitys.BaseActivity;
import com.old.time.beans.BookEntity;
import com.old.time.beans.ResultBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.UIHelper;

public class ConversationActivity extends BaseActivity {

    private String targetId;

    @Override
    protected void initView() {
        Uri uri = getIntent().getData();
        if (uri != null) {
            targetId = uri.getQueryParameter("targetId");

        }
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        getCartRoomInfo();
    }

    /**
     * 获取聊天室
     */
    private void getCartRoomInfo() {
        HttpParams params = new HttpParams();
        params.put("isbn", targetId);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_BOOK_INFO, new JsonCallBack<ResultBean<BookEntity>>() {
            @Override
            public void onSuccess(final ResultBean<BookEntity> mResultBean) {
                if (mResultBean == null || mResultBean.data == null) {

                    return;
                }
                setDataForView(mResultBean.data);

            }

            @Override
            public void onError(ResultBean<BookEntity> mResultBean) {
                UIHelper.ToastMessage(mContext, mResultBean.msg);

            }
        });
    }

    /**
     * 设置聊天信息
     *
     * @param bookEntity
     */
    private void setDataForView(BookEntity bookEntity) {
        if (bookEntity == null) {

            return;
        }
        setTitleText(bookEntity.getTitle());

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_conversation;
    }
}
