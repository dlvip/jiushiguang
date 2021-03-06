package com.old.time.dialogs;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.interfaces.OnClickManagerCallBack;
import com.old.time.aidl.ChapterBean;
import com.old.time.utils.MyLinearLayoutManager;
import com.old.time.utils.UIHelper;

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

    private BaseQuickAdapter<ChapterBean, BaseViewHolder> mAdapter;
    private TextView tv_dialog_title;
    private RecyclerView recycler_view;

    @Override
    protected void initDialogView() {
        findViewbyId(R.id.view_line_bottom).setVisibility(View.GONE);
        findViewbyId(R.id.tv_cancel).setVisibility(View.GONE);
        tv_dialog_title = findViewbyId(R.id.tv_dialog_title);
        tv_dialog_title.setText("音频列表");
        recycler_view = findViewbyId(R.id.recycler_view);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) recycler_view.getLayoutParams();
        params.height = UIHelper.dip2px(350);
        recycler_view.setLayoutParams(params);
        recycler_view.setLayoutManager(new MyLinearLayoutManager(mContext));
        mAdapter = new BaseQuickAdapter<ChapterBean, BaseViewHolder>(R.layout.adapter_music, new ArrayList<ChapterBean>()) {
            @Override
            protected void convert(BaseViewHolder helper, ChapterBean item) {
                int position = helper.getLayoutPosition() - getHeaderLayoutCount() + 1;
                int colorSrc;
                if (position - 1 == cPosition) {
                    colorSrc = mContext.getResources().getColor(R.color.color_ff4444);

                } else {
                    colorSrc = mContext.getResources().getColor(R.color.color_000);

                }
                helper.setText(R.id.tv_music_index, position + " .")//
                        .setTextColor(R.id.tv_music_index, colorSrc)//
                        .setText(R.id.tv_music_title, item.getTitle())//
                        .setTextColor(R.id.tv_music_title, colorSrc)//
                        .setText(R.id.tv_music_time, item.getDurationStr())//
                        .setTextColor(R.id.tv_music_time, colorSrc);

            }
        };
        recycler_view.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                onClickManagerCallBack.onClickRankManagerCallBack(position, "");
                dismiss();

            }
        });
    }

    private int cPosition;


    /**
     * 更新播放
     *
     * @param chapterBeans
     * @param cPosition
     */
    public void notifyItemChanged(List<ChapterBean> chapterBeans, int cPosition) {
        if (chapterBeans == null || chapterBeans.size() == 0 || mAdapter == null) {

            return;
        }
        this.cPosition = cPosition;
        mAdapter.setNewData(chapterBeans);
        recycler_view.stopScroll();
        LinearLayoutManager manager = (LinearLayoutManager) recycler_view.getLayoutManager();
        if (cPosition - 2 > -1) {
            manager.scrollToPositionWithOffset(cPosition - 2, 0);

        } else {
            manager.scrollToPositionWithOffset(0, 0);

        }
    }

    /**
     * 显示弹框
     *
     * @param chapterBeans
     */
    public void showChapterListDialog(List<ChapterBean> chapterBeans, int cPosition) {
        if (chapterBeans == null || chapterBeans.size() == 0) {

            return;
        }
        this.cPosition = cPosition;
        show();
        mAdapter.setNewData(chapterBeans);
        recycler_view.stopScroll();
        LinearLayoutManager manager = (LinearLayoutManager) recycler_view.getLayoutManager();
        if (cPosition - 2 > -1) {
            manager.scrollToPositionWithOffset(cPosition - 2, 0);

        } else {
            manager.scrollToPositionWithOffset(0, 0);

        }
    }

    @Override
    protected int setContentView() {
        return R.layout.dialog_manager_list;
    }
}
