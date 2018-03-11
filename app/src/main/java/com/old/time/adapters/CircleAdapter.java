package com.old.time.adapters;

import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.constants.Constant;
import com.old.time.utils.MyGridLayoutManager;
import com.old.time.views.ExpandableTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wcl on 2018/3/9.
 */

public class CircleAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private List<String> picUrls = new ArrayList<>();
    private MyGridLayoutManager myGridLayoutManager;
    private CirclePicAdapter mPicAdapter;

    public CircleAdapter(List<String> data) {
        super(R.layout.adapter_circle, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ExpandableTextView expand_text_view = helper.getView(R.id.expand_text_view);
        expand_text_view.setText(mContext.getString(R.string.circle_content));
        picUrls.clear();
        for (int i = 0; i < (helper.getLayoutPosition() - getHeaderLayoutCount() > 9 ? 9 : helper.getLayoutPosition() - getHeaderLayoutCount()); i++) {
            picUrls.add(Constant.PHOTO_PIC_URL);

        }
        RecyclerView recycler_view_pics = helper.getView(R.id.recycler_view_pics);
        myGridLayoutManager = new MyGridLayoutManager(mContext, 2);
        mPicAdapter = new CirclePicAdapter(picUrls);
        if (picUrls.size() > 2 && picUrls.size() != 4) {
            myGridLayoutManager.setSpanCount(3);

        } else {
            myGridLayoutManager.setSpanCount(2);

        }
        myGridLayoutManager.setAutoMeasureEnabled(true);
        recycler_view_pics.setLayoutManager(myGridLayoutManager);

        recycler_view_pics.setAdapter(mPicAdapter);
    }
}
