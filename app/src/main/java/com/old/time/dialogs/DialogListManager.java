package com.old.time.dialogs;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.interfaces.OnClickManagerCallBack;
import com.old.time.utils.MyLinearLayoutManager;
import com.old.time.utils.RecyclerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NING on 2018/1/10.
 */

public class DialogListManager extends BaseDialog {

    private OnClickManagerCallBack onClickManagerCallBack;

    public DialogListManager(@NonNull Context context, OnClickManagerCallBack onClickManagerCallBack) {
        super((Activity) context, R.style.transparentFrameWindowStyle);
        this.onClickManagerCallBack = onClickManagerCallBack;

    }

    /**
     * 初始化数据
     *
     * @param title
     * @param rank_types
     */
    public void setDialogViewData(String title, String[] rank_types) {
        show();
        myTypeItems.clear();
        for (int i = 0; i < rank_types.length; i++) {
            MyTypeItem myTypeItem = new MyTypeItem();
            myTypeItem.typeId = i;
            myTypeItem.typeName = rank_types[i];
            myTypeItems.add(myTypeItem);

        }
        mAdapter.setNewData(myTypeItems);
        setDialogTitle(title);
    }

    private BaseQuickAdapter<MyTypeItem, BaseViewHolder> mAdapter;
    private List<MyTypeItem> myTypeItems = new ArrayList<>();
    private TextView tv_dialog_title, tv_cancel;
    private RecyclerView recycler_view;

    @Override
    protected void initDialogView() {
        tv_cancel = findViewbyId(R.id.tv_cancel);
        tv_dialog_title = findViewbyId(R.id.tv_dialog_title);
        recycler_view = findViewbyId(R.id.recycler_view);
        recycler_view.setLayoutManager(new MyLinearLayoutManager(mContext));
        recycler_view.addItemDecoration(new RecyclerItemDecoration(mContext));
        mAdapter = new BaseQuickAdapter<MyTypeItem, BaseViewHolder>(R.layout.dialog_manager_item, myTypeItems) {
            @Override
            protected void convert(BaseViewHolder helper, MyTypeItem item) {
                helper.setText(R.id.tv_text_name, item.typeName);

            }
        };
        recycler_view.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MyTypeItem myTypeItem = (MyTypeItem) adapter.getItem(position);
                if (myTypeItem == null || onClickManagerCallBack == null) {

                    return;
                }
                onClickManagerCallBack.onClickRankManagerCallBack(myTypeItem.typeId, "");
                dismiss();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });
    }

    /**
     * 设置title
     *
     * @param title
     */
    private void setDialogTitle(String title) {
        if (tv_dialog_title == null) {

            return;
        }
        tv_dialog_title.setText(title);
    }


    @Override
    protected int setContentView() {

        return R.layout.dialog_manager_list;
    }

    public class MyTypeItem {
        public int typeId;
        public String typeName;

    }
}
