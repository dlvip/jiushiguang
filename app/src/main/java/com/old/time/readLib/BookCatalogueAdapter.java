package com.old.time.readLib;

import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.readLib.db.BookCatalogue;

import java.util.List;

/**
 * Created by wcliang on 2019/6/5.
 */

public class BookCatalogueAdapter extends BaseQuickAdapter<BookCatalogue, BaseViewHolder> {

    /**
     * 图书目录
     *
     * @param data
     * @return
     */
    public static BookCatalogueAdapter getInstance(@Nullable List<BookCatalogue> data) {

        return new BookCatalogueAdapter(data);
    }

    private Typeface typeface;
    private int currentCharter = 0;

    private BookCatalogueAdapter(@Nullable List<BookCatalogue> data) {
        super(R.layout.adapter_book_catalogue, data);
    }

    @Override
    public void setNewData(@Nullable List<BookCatalogue> data) {
        Config config = Config.getInstance();
        typeface = config.getTypeface();
        PageFactory pageFactory = PageFactory.getInstance();
        currentCharter = pageFactory.getCurrentCharter();
        super.setNewData(pageFactory.getDirectoryList());

        selectToPosition(currentCharter);
    }

    private RecyclerView mRecyclerView;

    /**
     * 定位显示哪一个item
     *
     * @param position
     */
    private void selectToPosition(int position) {
        if (mRecyclerView == null) {
            mRecyclerView = getRecyclerView();

        }
        LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        manager.scrollToPositionWithOffset(position, 0);

    }

    @Override
    protected void convert(BaseViewHolder helper, BookCatalogue item) {
        TextView tv_item_text = helper.getView(R.id.tv_item_text);
        tv_item_text.setMaxLines(1);
        tv_item_text.setTypeface(typeface);
        if (currentCharter == helper.getLayoutPosition()) {
            tv_item_text.setTextColor(mContext.getResources().getColor(R.color.colorAccent));

        } else {
            tv_item_text.setTextColor(mContext.getResources().getColor(R.color.color_000));

        }
        tv_item_text.setText(item.getBookCatalogue());

    }
}
