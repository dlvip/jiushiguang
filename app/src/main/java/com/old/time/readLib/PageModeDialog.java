package com.old.time.readLib;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.dialogs.BaseDialog;

/**
 * Created by wcliang on 2019/6/3.
 */

public class PageModeDialog extends BaseDialog implements View.OnClickListener{

    public PageModeDialog(@NonNull Activity context, PageModeListener pageModeListener) {
        super(context, R.style.transparentFrameWindowStyle);
        this.pageModeListener = pageModeListener;

    }

    TextView tv_simulation;
    TextView tv_cover;
    TextView tv_slide;
    TextView tv_none;

    private Config config;
    private PageModeListener pageModeListener;

    @Override
    protected void initDialogView() {
        tv_simulation = findViewbyId(R.id.tv_simulation);
        tv_cover = findViewbyId(R.id.tv_cover);
        tv_slide = findViewbyId(R.id.tv_slide);
        tv_none = findViewbyId(R.id.tv_none);

        config = Config.getInstance();
        selectPageMode(config.getPageMode());

        tv_simulation.setOnClickListener(this);
        tv_cover.setOnClickListener(this);
        tv_slide.setOnClickListener(this);
        tv_none.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_simulation:
                selectPageMode(Config.PAGE_MODE_SIMULATION);
                setPageMode(Config.PAGE_MODE_SIMULATION);

                break;
            case R.id.tv_cover:
                selectPageMode(Config.PAGE_MODE_COVER);
                setPageMode(Config.PAGE_MODE_COVER);

                break;
            case R.id.tv_slide:
                selectPageMode(Config.PAGE_MODE_SLIDE);
                setPageMode(Config.PAGE_MODE_SLIDE);

                break;
            case R.id.tv_none:
                selectPageMode(Config.PAGE_MODE_NONE);
                setPageMode(Config.PAGE_MODE_NONE);

                break;
        }
    }

    //设置翻页
    public void setPageMode(int pageMode) {
        config.setPageMode(pageMode);
        if (pageModeListener != null) {
            pageModeListener.changePageMode(pageMode);

        }
    }
    //选择怕翻页
    private void selectPageMode(int pageMode) {
        if (pageMode == Config.PAGE_MODE_SIMULATION) {
            setTextViewSelect(tv_simulation, true);
            setTextViewSelect(tv_cover, false);
            setTextViewSelect(tv_slide, false);
            setTextViewSelect(tv_none, false);

        } else if (pageMode == Config.PAGE_MODE_COVER) {
            setTextViewSelect(tv_simulation, false);
            setTextViewSelect(tv_cover, true);
            setTextViewSelect(tv_slide, false);
            setTextViewSelect(tv_none, false);

        } else if (pageMode == Config.PAGE_MODE_SLIDE) {
            setTextViewSelect(tv_simulation, false);
            setTextViewSelect(tv_cover, false);
            setTextViewSelect(tv_slide, true);
            setTextViewSelect(tv_none, false);

        } else if (pageMode == Config.PAGE_MODE_NONE) {
            setTextViewSelect(tv_simulation, false);
            setTextViewSelect(tv_cover, false);
            setTextViewSelect(tv_slide, false);
            setTextViewSelect(tv_none, true);

        }
    }

    //设置按钮选择的背景
    private void setTextViewSelect(TextView textView, Boolean isSelect) {
        if (isSelect) {
            textView.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.button_select_bg));
            textView.setTextColor(getContext().getResources().getColor(R.color.read_dialog_button_select));

        } else {
            textView.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.button_bg));
            textView.setTextColor(getContext().getResources().getColor(R.color.color_fff));

        }
    }

    @Override
    protected int setContentView() {
        return R.layout.dialog_pagemode;
    }

    public interface PageModeListener {
        void changePageMode(int pageMode);

    }
}
