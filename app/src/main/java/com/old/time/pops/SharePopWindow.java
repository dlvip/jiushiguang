package com.old.time.pops;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.utils.DataUtils;
import com.old.time.utils.MyGridLayoutManager;
import com.old.time.utils.RecyclerItemDecoration;

public class SharePopWindow extends BasePopWindow {

    public SharePopWindow(Context context) {
        super(context);
    }

    private TextView tv_dialog_title, tv_cancel;
    private RecyclerView recycler_view;
    private BaseQuickAdapter<String, BaseViewHolder> adapter;
    private String[] strings = new String[]{"微信", "朋友圈", "qq", "空间", "微博", "更多"};
    private int[] src = new int[]{R.drawable.umeng_socialize_wechat//
            , R.drawable.umeng_socialize_wxcircle//
            , R.drawable.umeng_socialize_qq//
            , R.drawable.umeng_socialize_qzone//
            , R.drawable.umeng_socialize_sina//
            , R.mipmap.btn_more};

    @Override
    protected void initView() {
        tv_dialog_title = findViewById(R.id.tv_dialog_title);
        tv_cancel = findViewById(R.id.tv_cancel);

        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new MyGridLayoutManager(context, 3));
        recycler_view.addItemDecoration(new RecyclerItemDecoration(context, RecyclerItemDecoration.HORIZONTAL_LIST));
        adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.share_menu_item) {

            @Override
            protected void convert(BaseViewHolder helper, String item) {
                int position = helper.getLayoutPosition();
                helper.setText(R.id.socialize_text_view, strings[position])//
                        .setImageResource(R.id.socialize_image_view, src[position]);

            }
        };
        adapter.setNewData(DataUtils.getDateStrings(6));
        recycler_view.setAdapter(adapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_manager_list;
    }
}
