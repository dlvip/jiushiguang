package com.old.time.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.beans.BookEntity;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.BitmapUtils;

public class BookDetailActivity extends BaseActivity {

    private static final String BOOK_ENTITY = "bookEntity";

    /**
     * 图书详情
     *
     * @param context
     */
    public static void startBookDetailActivity(Context context, BookEntity bookEntity) {
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra(BOOK_ENTITY, bookEntity);
        ActivityUtils.startActivity((Activity) context, intent);

    }

    private BookEntity bookEntity;

    private View relative_layout_more;
    private RelativeLayout relative_layout_parent;
    private ImageView img_book_pic;
    private TextView tv_book_name, tv_book_leve, tv_book_author, tv_book_public_sher, tv_book_price, tv_book_describe;


    @Override
    protected void initView() {
        bookEntity = (BookEntity) getIntent().getSerializableExtra(BOOK_ENTITY);
        img_book_pic = findViewById(R.id.img_book_pic);
        tv_book_name = findViewById(R.id.tv_book_name);
        tv_book_leve = findViewById(R.id.tv_book_leve);
        tv_book_author = findViewById(R.id.tv_book_author);
        tv_book_public_sher = findViewById(R.id.tv_book_public_sher);
        tv_book_price = findViewById(R.id.tv_book_price);
        tv_book_describe = findViewById(R.id.tv_book_describe);

        relative_layout_more = findViewById(R.id.relative_layout_more);
        relative_layout_more.setVisibility(View.VISIBLE);
        relative_layout_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = BitmapUtils.saveBitmap(mContext, relative_layout_parent);
                Intent imageIntent = new Intent(Intent.ACTION_SEND);
                imageIntent.setType("image/*");
                imageIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(imageIntent, "分享"));

            }
        });

        relative_layout_parent = findViewById(R.id.relative_layout_parent);

        GlideUtils.getInstance().setImageView(mContext, bookEntity.getImages_large(), img_book_pic);
        setTitleText(bookEntity.getTitle());
        tv_book_name.setText(bookEntity.getTitle());
        tv_book_author.setText(bookEntity.getAuthor() + " / " + bookEntity.getBinding());
        tv_book_leve.setText(bookEntity.getLevelNum());
        tv_book_public_sher.setText("出版：" + bookEntity.getPublisher() + " " + bookEntity.getPubdate());
        tv_book_price.setText(bookEntity.getPriceStr());
        tv_book_describe.setText(bookEntity.getSummary());

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_book_detail;
    }
}
