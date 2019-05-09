package com.old.time.dialogs;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.old.time.R;
import com.old.time.beans.BookEntity;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.pops.BasePopWindow;
import com.old.time.utils.MyLinearLayoutManager;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.ScreenTools;
import com.old.time.utils.UIHelper;

import java.util.List;

import static io.rong.common.fwlog.FwLog.W;

public class DialogMallCart extends BasePopWindow {

    private OnItemDateUpdateListener onItemDateUpdateListener;

    public DialogMallCart(@NonNull Activity context, OnItemDateUpdateListener onItemDateUpdateListener) {
        super(context);
        this.onItemDateUpdateListener = onItemDateUpdateListener;
    }

    private BaseQuickAdapter<BookEntity, BaseViewHolder> mAdapter;
    private RecyclerView recycler_view;
    private TextView tv_book_num;

    private int[] nums = new int[]{1, 2, 5, 10};
    private int indext = 0;
    private int num = 1;

    @Override
    protected void initView() {
        View linear_layout_parent = parentView.findViewById(R.id.linear_layout_parent);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linear_layout_parent.getLayoutParams();
        params.height = ScreenTools.instance(context).getScreenHeight() - UIHelper.dip2px(60);
        linear_layout_parent.setLayoutParams(params);

        setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_radiu10_tran_60));
        tv_book_num = findViewById(R.id.tv_book_num);
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new MyLinearLayoutManager(context));
        recycler_view.addItemDecoration(new RecyclerItemDecoration(context));
        mAdapter = new BaseQuickAdapter<BookEntity, BaseViewHolder>(R.layout.adapter_cart_dialog) {
            @Override
            protected void convert(BaseViewHolder helper, BookEntity item) {
                ImageView img_book_pic = helper.getView(R.id.img_book_pic);
                GlideUtils.getInstance().setImageView(mContext, item.getImages_large(), img_book_pic);
                helper.setText(R.id.tv_book_name, item.getTitle())//
                        .setText(R.id.tv_book_price, item.getPriceStr())//
                        .setText(R.id.tv_book_count, String.valueOf(item.getCount()))//
                        .addOnClickListener(R.id.img_count_reduce)//
                        .addOnClickListener(R.id.img_count_plus);


            }
        };
        recycler_view.setAdapter(mAdapter);
        recycler_view.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                BookEntity bookEntity = mAdapter.getItem(position);
                if (bookEntity == null) {

                    return;
                }
                switch (view.getId()) {
                    case R.id.img_count_reduce:
                        bookEntity.setCount(bookEntity.getCount() - num);

                        break;
                    case R.id.img_count_plus:
                        bookEntity.setCount(bookEntity.getCount() + num);

                        break;
                }
                if (bookEntity.getCount() < 1) {
                    mAdapter.remove(position);

                } else {
                    mAdapter.setData(position, bookEntity);

                }
                if (onItemDateUpdateListener != null) {
                    onItemDateUpdateListener.onItemListener(bookEntity);

                }
            }
        });
        findViewById(R.id.img_count_reduce).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indext--;
                if (indext < 0) {
                    indext = 3;

                }
                num = nums[indext];
                tv_book_num.setText(String.valueOf(num));

            }
        });
        findViewById(R.id.img_count_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indext++;
                if (indext > 3) {
                    indext = 0;

                }
                num = nums[indext];
                tv_book_num.setText(String.valueOf(num));

            }
        });
        linear_layout_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });
    }

    /**
     * 显示
     *
     * @param bookEntities
     */
    public void showMallCartDialog(View view, List<BookEntity> bookEntities) {
        if (bookEntities == null || bookEntities.size() == 0) {

            return;
        }
        mAdapter.setNewData(bookEntities);

        //获取自身的长宽高
        parentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupHeight = parentView.getMeasuredHeight();

        int[] location = new int[2];
        view.getLocationOnScreen(location);
        showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] - popupHeight);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_mall_cart;
    }

    public interface OnItemDateUpdateListener {
        void onItemListener(BookEntity bookEntity);

    }
}
