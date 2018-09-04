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
import com.old.time.mp3Utils.Mp3Info;
import com.old.time.utils.MyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NING on 2018/9/4.
 */

public class DialogChapterList extends BaseDialog {

    private OnClickManagerCallBack onClickManagerCallBack;

    public DialogChapterList(@NonNull Context context, OnClickManagerCallBack onClickManagerCallBack) {
        super((Activity) context, R.style.transparentFrameWindowStyle);
        this.onClickManagerCallBack = onClickManagerCallBack;

    }

    private BaseQuickAdapter<Mp3Info, BaseViewHolder> mAdapter;
    private TextView tv_dialog_title;
    private RecyclerView recycler_view;

    @Override
    protected void initDialogView() {
        findViewbyId(R.id.view_line_bottom).setVisibility(View.GONE);
        findViewbyId(R.id.tv_cancel).setVisibility(View.GONE);
        tv_dialog_title = findViewbyId(R.id.tv_dialog_title);
        tv_dialog_title.setText("音频列表");
        recycler_view = findViewbyId(R.id.recycler_view);
        recycler_view.setLayoutManager(new MyLinearLayoutManager(mContext));
        mAdapter = new BaseQuickAdapter<Mp3Info, BaseViewHolder>(R.layout.adapter_music, new ArrayList<Mp3Info>()) {
            @Override
            protected void convert(BaseViewHolder helper, Mp3Info item) {
                int position = helper.getLayoutPosition() - getHeaderLayoutCount() + 1;
                helper.setText(R.id.tv_music_index, position + " .")//
                        .setText(R.id.tv_music_title, item.getTitle())//
                        .setText(R.id.tv_music_time, item.getDurationStr());

            }
        };
        recycler_view.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                onClickManagerCallBack.onClickRankManagerCallBack(position, "");

            }
        });
    }

    /**
     * 显示弹框
     *
     * @param mp3Infos
     */
    public void showChapterListDialog(List<Mp3Info> mp3Infos) {
        if (mp3Infos == null || mp3Infos.size() == 0) {

            return;
        }
        show();
        mAdapter.setNewData(mp3Infos);

    }

    @Override
    protected int setContentView() {
        return R.layout.dialog_manager_list;
    }
}