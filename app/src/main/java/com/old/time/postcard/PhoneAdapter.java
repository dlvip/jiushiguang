package com.old.time.postcard;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.PostCartBean;
import com.old.time.utils.MyLinearLayoutManager;

import java.util.List;

public class PhoneAdapter extends BaseQuickAdapter<PostCartBean, BaseViewHolder> {


    public PhoneAdapter(@Nullable List<PostCartBean> data) {
        super(R.layout.adapter_phone, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, PostCartBean item) {
        helper.setText(R.id.tv_name_letter, item.getCodeKey());
        PostCardAdapter adapter = new PostCardAdapter(item.getPhoneBeans());
        RecyclerView recyclerView = helper.getView(R.id.phone_recycler_view);
        recyclerView.setLayoutManager(new MyLinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);

    }
}
