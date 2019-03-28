package com.old.time.activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.beans.BookComEntity;
import com.old.time.beans.BookEntity;
import com.old.time.beans.ResultBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.UIHelper;
import com.old.time.utils.UserLocalInfoUtils;

import org.greenrobot.eventbus.EventBus;

public class CreateComActivity extends BaseActivity {

    private static final String BOOK_ENTITY = "BookEntity";

    /**
     * 创建图书评论
     *
     * @param context
     * @param bookEntity
     */
    public static void startCreateComActivity(Context context, BookEntity bookEntity) {
        Intent intent = new Intent(context, CreateComActivity.class);
        intent.putExtra(BOOK_ENTITY, bookEntity);
        ActivityUtils.startActivity((Activity) context, intent);

    }

    private BookEntity bookEntity;
    private EditText edtContent;

    @Override
    protected void initView() {
        setTitleText("添加评论");
        setSendText("保存");
        bookEntity = (BookEntity) getIntent().getSerializableExtra(BOOK_ENTITY);
        TextView tv_book_name = findViewById(R.id.tv_book_name);
        edtContent = findViewById(R.id.edt_comment_content);
        tv_book_name.setText(bookEntity.getTitle());

    }

    private ProgressDialog pd;

    @Override
    public void save(View view) {
        super.save(view);
        String edtContentStr = edtContent.getText().toString().trim();
        if (TextUtils.isEmpty(edtContentStr)) {
            UIHelper.ToastMessage(mContext, "说点什么");

            return;
        }
        pd = UIHelper.showProgressMessageDialog(mContext, getString(R.string.please_wait));
        HttpParams params = new HttpParams();
        params.put("userId", UserLocalInfoUtils.instance().getUserId());
        params.put("bookId", bookEntity.getId());
        params.put("comment", edtContentStr);
        OkGoUtils.getInstance().postNetForData(params, Constant.CREATE_BOOK_COMMENT, new JsonCallBack<ResultBean<BookComEntity>>() {
            @Override
            public void onSuccess(ResultBean<BookComEntity> mResultBean) {
                UIHelper.dissmissProgressDialog(pd);
                EventBus.getDefault().post(mResultBean.data);
                ActivityUtils.finishActivity(mContext);

            }

            @Override
            public void onError(ResultBean<BookComEntity> mResultBean) {
                UIHelper.dissmissProgressDialog(pd);
                UIHelper.ToastMessage(mContext, mResultBean.msg);

            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_create_com;
    }
}
