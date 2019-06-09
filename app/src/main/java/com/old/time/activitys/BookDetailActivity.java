package com.old.time.activitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.old.time.R;
import com.old.time.beans.BookEntity;
import com.old.time.beans.ResultBean;
import com.old.time.beans.UMShareBean;
import com.old.time.constants.Constant;
import com.old.time.dialogs.DialogShareContent;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.readLib.ReadActivity;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.DebugLog;
import com.old.time.utils.UIHelper;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class BookDetailActivity extends BaseActivity {

    private static final String BOOK_ENTITY = "bookEntity";
    public static final String BOOK_ISBN = "isbn";

    /**
     * 图书详情
     *
     * @param context
     */
    public static void startBookDetailActivity(Context context, BookEntity bookEntity) {
        if (bookEntity == null) {

            return;
        }
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra(BOOK_ENTITY, bookEntity);
        ActivityUtils.startActivity((Activity) context, intent);

    }

    @Override
    protected String[] getNeedPermissions() {
        return super.getNeedPermissions();
    }

    private BookEntity bookEntity;

    private RelativeLayout relative_layout_parent;
    private ImageView img_book_pic;
    private TextView tv_book_name, tv_book_leve, tv_book_author, tv_book_public_sher, tv_book_price, tv_book_describe;

    private TextView tv_read_book;


    @Override
    protected void initView() {
        Intent intent = getIntent();
        isbn = intent.getStringExtra("isbn");
        if (TextUtils.isEmpty(isbn)) {
            bookEntity = (BookEntity) getIntent().getSerializableExtra(BOOK_ENTITY);

        }
        img_book_pic = findViewById(R.id.img_book_pic);
        tv_book_name = findViewById(R.id.tv_book_name);
        tv_book_leve = findViewById(R.id.tv_book_leve);
        tv_book_author = findViewById(R.id.tv_book_author);
        tv_book_public_sher = findViewById(R.id.tv_book_public_sher);
        tv_book_price = findViewById(R.id.tv_book_price);
        tv_book_describe = findViewById(R.id.tv_book_describe);

        relative_layout_parent = findViewById(R.id.relative_layout_parent);

        tv_read_book = findViewById(R.id.tv_read_book);

        setBookForView(bookEntity);
    }

    /**
     * 设置view
     */
    private void setBookForView(BookEntity bookEntity) {
        if (bookEntity == null) {
            getBookDetailInfo();

            return;
        }
        this.bookEntity = bookEntity;
        if (TextUtils.isEmpty(bookEntity.getFilePath())) {
            tv_read_book.setBackgroundResource(R.color.color_aaa);

        } else {
            tv_read_book.setBackgroundResource(R.color.color_bd9029);

        }

        GlideUtils.getInstance().setImageView(mContext, bookEntity.getImages_large(), img_book_pic);
        setTitleText(bookEntity.getTitle());
        tv_book_name.setText(bookEntity.getTitle());
        tv_book_author.setText(String.valueOf(bookEntity.getAuthor() + " / " + bookEntity.getBinding()));
        tv_book_leve.setText(bookEntity.getLevelNum());
        tv_book_public_sher.setText(String.valueOf("出版：" + bookEntity.getPublisher() + " " + bookEntity.getPubdate()));
        tv_book_price.setText(bookEntity.getPriceStr());
        tv_book_describe.setText(bookEntity.getSummary());

    }

    private String isbn;

    private void getBookDetailInfo() {
        HttpParams params = new HttpParams();
        params.put("isbn", isbn);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_BOOK_INFO, new JsonCallBack<ResultBean<BookEntity>>() {
            @Override
            public void onSuccess(ResultBean<BookEntity> mResultBean) {
                if (mResultBean == null || mResultBean.data == null) {
                    ActivityUtils.finishActivity(mContext);

                    return;
                }
                setBookForView(mResultBean.data);

            }

            @Override
            public void onError(ResultBean<BookEntity> mResultBean) {
                UIHelper.ToastMessage(mContext, mResultBean.msg);
                ActivityUtils.finishActivity(mContext);

            }
        });
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        tv_read_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downLoadFile(bookEntity);

            }
        });
        findViewById(R.id.frame_layout_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareBookDetail();

            }
        });
        findViewById(R.id.frame_layout_sign).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignListActivity.startSignListActivity(mContext, bookEntity);

            }
        });
    }

    private DialogShareContent shareContent;
    private UMShareBean umShareBean;

    /**
     * 分享
     */
    private void shareBookDetail() {
        if (relative_layout_parent == null || bookEntity == null) {

            return;
        }
        if (shareContent == null) {
            shareContent = new DialogShareContent(mContext, new DialogShareContent.ShareModelCallBackListener() {
                @Override
                public UMShareBean getShareModel() {

                    return umShareBean;
                }
            });
        }
        if (umShareBean == null) {
            umShareBean = new UMShareBean();

        }
        umShareBean.setTitle(bookEntity.getTitle());
        umShareBean.setImgUrl(bookEntity.getImages_large());
        umShareBean.setDescription(bookEntity.getSummary());
        umShareBean.setShareUrl(Constant.PU_GONG_YING_URL);

        shareContent.showShareContentDialog(relative_layout_parent);

    }

    /**
     * 下载图书
     */
    private void downLoadFile(final BookEntity bookEntity) {
        if (bookEntity == null || TextUtils.isEmpty(bookEntity.getFilePath())) {

            return;
        }
        BookEntity book = DataSupport.find(BookEntity.class, bookEntity.getId());
        if (book != null) {
            ReadActivity.openBook(mContext, book);

            return;
        }
        OkGoUtils.getInstance().downLoadFile(bookEntity.getFilePath(), new FileCallback() {

            @Override
            public void downloadProgress(Progress progress) {
                super.downloadProgress(progress);
                if (progress != null) DebugLog.d(TAG, progress.toString());

            }

            @Override
            public void onSuccess(Response<File> response) {
                if (response == null || response.body() == null || TextUtils.isEmpty(response.body().getPath())) {

                    return;
                }
                String filePath = response.body().getPath();
                bookEntity.setFilePath(filePath);

                SaveBookToSqlLiteTask mSaveBookToSqlLiteTask = new SaveBookToSqlLiteTask();
                mSaveBookToSqlLiteTask.execute(bookEntity);
            }

            @Override
            public void onError(Response<File> response) {
                super.onError(response);
                UIHelper.ToastMessage(mContext, "加载图书失败");

            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class SaveBookToSqlLiteTask extends AsyncTask<BookEntity, Void, Integer> {

        private static final int FAIL = 0;
        private static final int SUCCESS = 1;
        private BookEntity repeatBookEntity;

        @Override
        protected Integer doInBackground(BookEntity... params) {
            BookEntity bookEntity = params[0];
            List<BookEntity> books = DataSupport.where("filePath = ?", bookEntity.getFilePath()).find(BookEntity.class);
            if (books.size() > 0) {
                repeatBookEntity = books.get(0);

                return SUCCESS;
            }
            try {
                DataSupport.saveAll(Collections.singletonList(bookEntity));

            } catch (Exception e) {
                e.printStackTrace();

                return FAIL;
            }
            repeatBookEntity = bookEntity;
            return SUCCESS;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case FAIL:
                    UIHelper.ToastMessage(mContext, "打开图书失败");

                    break;
                case SUCCESS:
                    ReadActivity.openBook(mContext, repeatBookEntity);

                    break;
            }
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_book_detail;
    }
}
