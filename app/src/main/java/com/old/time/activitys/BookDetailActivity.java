package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bifan.txtreaderlib.ui.HwTxtPlayActivity;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.old.time.R;
import com.old.time.beans.BookEntity;
import com.old.time.beans.ResultBean;
import com.old.time.beans.UMShareBean;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.pops.SharePopWindow;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.DebugLog;
import com.old.time.utils.SpUtils;
import com.old.time.utils.UIHelper;

import java.io.File;

public class BookDetailActivity extends BaseActivity {

    private static final String BOOK_ENTITY = "bookEntity";
    private static final String BOOK_ISBN = "isbn";

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

    /**
     * 图书详情
     *
     * @param context
     */
    public static void startBookDetailActivity(Context context, String isbn) {
        if (TextUtils.isEmpty(isbn)) {

            return;
        }
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra(BOOK_ISBN, isbn);
        ActivityUtils.startActivity((Activity) context, intent);

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

    private SharePopWindow sharePopWindow;
    private UMShareBean umShareBean;

    /**
     * 分享
     */
    private void shareBookDetail() {
        if (relative_layout_parent == null || bookEntity == null) {

            return;
        }
        if (sharePopWindow == null) {
            sharePopWindow = new SharePopWindow(mContext, new SharePopWindow.ShareModelCallBackListener() {
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

        sharePopWindow.showBottomAtLocation(relative_layout_parent);

    }

    /**
     * 下载图书
     */
    private void downLoadFile(final BookEntity bookEntity) {
        if (bookEntity == null || TextUtils.isEmpty(bookEntity.getFilePath())) {

            return;
        }
        String filePath = (String) SpUtils.get(bookEntity.getIsbn13(), "");
        if (!TextUtils.isEmpty(filePath)) {
            HwTxtPlayActivity.loadTxtFile(mContext, filePath);

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
                SpUtils.put(bookEntity.getIsbn13(), filePath);
                HwTxtPlayActivity.loadTxtFile(mContext, filePath);
            }

            @Override
            public void onError(Response<File> response) {
                super.onError(response);

            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_book_detail;
    }
}
