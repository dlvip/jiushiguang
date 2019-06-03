package com.old.time.readLib;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.dialogs.BaseDialog;
import com.old.time.utils.UIHelper;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class SettingDialog extends BaseDialog implements View.OnClickListener {

    private TextView tv_dark;
    private SeekBar sb_brightness;
    private TextView tv_bright;
    private TextView tv_xitong;
    private TextView tv_subtract;
    private TextView tv_size;
    private TextView tv_add;
    private TextView tv_qihei;
    private TextView tv_default;
    private CircleViewImage iv_bg_default;
    private CircleViewImage iv_bg1;
    private CircleViewImage iv_bg2;
    private CircleViewImage iv_bg3;
    private CircleViewImage iv_bg4;
    private TextView tv_size_default;
    private TextView tv_fzxinghei;
    private TextView tv_fzkatong;
    private TextView tv_bysong;


    private Config config;
    private Boolean isSystem;
    private SettingListener mSettingListener;
    private int FONT_SIZE_MIN;
    private int FONT_SIZE_MAX;
    private int currentFontSize;

    public SettingDialog(@NonNull Activity context, SettingListener settingListener) {
        super(context, R.style.transparentFrameWindowStyle);
        this.mSettingListener = settingListener;

    }

    @Override
    protected void initDialogView() {
        tv_dark = findViewbyId(R.id.tv_dark);
        sb_brightness = findViewbyId(R.id.sb_brightness);
        tv_bright = findViewbyId(R.id.tv_bright);
        tv_xitong = findViewbyId(R.id.tv_xitong);
        tv_subtract = findViewbyId(R.id.tv_subtract);
        tv_size = findViewbyId(R.id.tv_size);
        tv_add = findViewbyId(R.id.tv_add);
        tv_qihei = findViewbyId(R.id.tv_qihei);
        tv_default = findViewbyId(R.id.tv_default);
        iv_bg_default = findViewbyId(R.id.iv_bg_default);
        iv_bg1 = findViewbyId(R.id.iv_bg_1);
        iv_bg2 = findViewbyId(R.id.iv_bg_2);
        iv_bg3 = findViewbyId(R.id.iv_bg_3);
        iv_bg4 = findViewbyId(R.id.iv_bg_4);
        tv_size_default = findViewbyId(R.id.tv_size_default);
        tv_fzxinghei = findViewbyId(R.id.tv_fzxinghei);
        tv_fzkatong = findViewbyId(R.id.tv_fzkatong);
        tv_bysong = findViewbyId(R.id.tv_bysong);

        FONT_SIZE_MIN = (int) getContext().getResources().getDimension(R.dimen.reading_min_text_size);
        FONT_SIZE_MAX = (int) getContext().getResources().getDimension(R.dimen.reading_max_text_size);

        config = Config.getInstance();

        //初始化亮度
        isSystem = config.isSystemLight();
        setTextViewSelect(tv_xitong, isSystem);
        setBrightness(config.getLight());

        //初始化字体大小
        currentFontSize = (int) config.getFontSize();
        tv_size.setText(String.valueOf(currentFontSize));

        //初始化字体
        tv_default.setTypeface(config.getTypeface(Config.FONTTYPE_DEFAULT));
        tv_qihei.setTypeface(config.getTypeface(Config.FONTTYPE_QIHEI));
//        tv_fzxinghei.setTypeface(config.getTypeface(Config.FONTTYPE_FZXINGHEI));
        tv_fzkatong.setTypeface(config.getTypeface(Config.FONTTYPE_FZKATONG));
        tv_bysong.setTypeface(config.getTypeface(Config.FONTTYPE_BYSONG));
//        tv_xinshou.setTypeface(config.getTypeface(Config.FONTTYPE_XINSHOU));
//        tv_wawa.setTypeface(config.getTypeface(Config.FONTTYPE_WAWA));
        selectTypeface(config.getTypefacePath());

        selectBg(config.getBookBgType());

        sb_brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress > 10) {
                    changeBright(false, progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        setOnclick(new View[]{tv_dark, tv_bright, tv_xitong//
                , tv_subtract, tv_add, tv_size_default, tv_qihei//
                , tv_fzxinghei, tv_fzkatong, tv_bysong, tv_default//
                , iv_bg_default, iv_bg1, iv_bg2, iv_bg3, iv_bg4});
    }

    @Override
    protected int setContentView() {
        return R.layout.dialog_setting;
    }

    //选择背景
    private void selectBg(int type) {
        switch (type) {
            case Config.BOOK_BG_DEFAULT:
                iv_bg_default.setBorderWidth(UIHelper.dip2px(2));
                iv_bg1.setBorderWidth(UIHelper.dip2px(0));
                iv_bg2.setBorderWidth(UIHelper.dip2px(0));
                iv_bg3.setBorderWidth(UIHelper.dip2px(0));
                iv_bg4.setBorderWidth(UIHelper.dip2px(0));

                break;
            case Config.BOOK_BG_1:
                iv_bg_default.setBorderWidth(UIHelper.dip2px(0));
                iv_bg1.setBorderWidth(UIHelper.dip2px(2));
                iv_bg2.setBorderWidth(UIHelper.dip2px(0));
                iv_bg3.setBorderWidth(UIHelper.dip2px(0));
                iv_bg4.setBorderWidth(UIHelper.dip2px(0));

                break;
            case Config.BOOK_BG_2:
                iv_bg_default.setBorderWidth(UIHelper.dip2px(0));
                iv_bg1.setBorderWidth(UIHelper.dip2px(0));
                iv_bg2.setBorderWidth(UIHelper.dip2px(2));
                iv_bg3.setBorderWidth(UIHelper.dip2px(0));
                iv_bg4.setBorderWidth(UIHelper.dip2px(0));

                break;
            case Config.BOOK_BG_3:
                iv_bg_default.setBorderWidth(UIHelper.dip2px(0));
                iv_bg1.setBorderWidth(UIHelper.dip2px(0));
                iv_bg2.setBorderWidth(UIHelper.dip2px(0));
                iv_bg3.setBorderWidth(UIHelper.dip2px(2));
                iv_bg4.setBorderWidth(UIHelper.dip2px(0));

                break;
            case Config.BOOK_BG_4:
                iv_bg_default.setBorderWidth(UIHelper.dip2px(0));
                iv_bg1.setBorderWidth(UIHelper.dip2px(0));
                iv_bg2.setBorderWidth(UIHelper.dip2px(0));
                iv_bg3.setBorderWidth(UIHelper.dip2px(0));
                iv_bg4.setBorderWidth(UIHelper.dip2px(2));

                break;
        }
    }

    //设置字体
    private void setBookBg(int type) {
        config.setBookBg(type);
        if (mSettingListener != null) {
            mSettingListener.changeBookBg(type);

        }
    }

    //选择字体
    private void selectTypeface(String typeface) {
        switch (typeface) {
            case Config.FONTTYPE_DEFAULT:
                setTextViewSelect(tv_default, true);
                setTextViewSelect(tv_qihei, false);
                setTextViewSelect(tv_fzxinghei, false);
                setTextViewSelect(tv_fzkatong, false);
                setTextViewSelect(tv_bysong, false);
//            setTextViewSelect(tv_xinshou, false);
//            setTextViewSelect(tv_wawa, false);
                break;
            case Config.FONTTYPE_QIHEI:
                setTextViewSelect(tv_default, false);
                setTextViewSelect(tv_qihei, true);
                setTextViewSelect(tv_fzxinghei, false);
                setTextViewSelect(tv_fzkatong, false);
                setTextViewSelect(tv_bysong, false);
//            setTextViewSelect(tv_xinshou, false);
//            setTextViewSelect(tv_wawa, false);
                break;
            case Config.FONTTYPE_FZXINGHEI:
                setTextViewSelect(tv_default, false);
                setTextViewSelect(tv_qihei, false);
                setTextViewSelect(tv_fzxinghei, true);
                setTextViewSelect(tv_fzkatong, false);
                setTextViewSelect(tv_bysong, false);
//            setTextViewSelect(tv_xinshou, true);
//            setTextViewSelect(tv_wawa, false);
                break;
            case Config.FONTTYPE_FZKATONG:
                setTextViewSelect(tv_default, false);
                setTextViewSelect(tv_qihei, false);
                setTextViewSelect(tv_fzxinghei, false);
                setTextViewSelect(tv_fzkatong, true);
                setTextViewSelect(tv_bysong, false);
//            setTextViewSelect(tv_xinshou, false);
//            setTextViewSelect(tv_wawa, true);
                break;
            case Config.FONTTYPE_BYSONG:
                setTextViewSelect(tv_default, false);
                setTextViewSelect(tv_qihei, false);
                setTextViewSelect(tv_fzxinghei, false);
                setTextViewSelect(tv_fzkatong, false);
                setTextViewSelect(tv_bysong, true);
//            setTextViewSelect(tv_xinshou, false);
//            setTextViewSelect(tv_wawa, true);
                break;
        }
    }

    //设置字体
    private void setTypeface(String typeface) {
        config.setTypeface(typeface);
        Typeface tface = config.getTypeface(typeface);
        if (mSettingListener != null) {
            mSettingListener.changeTypeFace(tface);
        }
    }

    //设置亮度
    private void setBrightness(float brightness) {
        sb_brightness.setProgress((int) (brightness * 100));
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

    private void setOnclick(View[] views) {
        for (View view : views) {
            if (view != null) view.setOnClickListener(this);

        }
    }

    //变大书本字体
    private void addFontSize() {
        if (currentFontSize < FONT_SIZE_MAX) {
            currentFontSize += 1;
            tv_size.setText(String.valueOf(currentFontSize + ""));
            config.setFontSize(currentFontSize);
            if (mSettingListener != null) {
                mSettingListener.changeFontSize(currentFontSize);

            }
        }
    }

    private void defaultFontSize() {
        currentFontSize = (int) getContext().getResources().getDimension(R.dimen.reading_default_text_size);
        tv_size.setText(String.valueOf(currentFontSize + ""));
        config.setFontSize(currentFontSize);
        if (mSettingListener != null) {
            mSettingListener.changeFontSize(currentFontSize);

        }
    }

    //变小书本字体
    private void subtractFontSize() {
        if (currentFontSize > FONT_SIZE_MIN) {
            currentFontSize -= 1;
            tv_size.setText(String.valueOf(currentFontSize + ""));
            config.setFontSize(currentFontSize);
            if (mSettingListener != null) {
                mSettingListener.changeFontSize(currentFontSize);

            }
        }
    }

    //改变亮度
    private void changeBright(Boolean isSystem, int brightness) {
        float light = (float) (brightness / 100.0);
        setTextViewSelect(tv_xitong, isSystem);
        config.setSystemLight(isSystem);
        config.setLight(light);
        if (mSettingListener != null) {
            mSettingListener.changeSystemBright(isSystem, light);
        }
    }

    void setSettingListener(SettingListener settingListener) {
        this.mSettingListener = settingListener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_dark:
                break;
            case R.id.tv_bright:
                break;
            case R.id.tv_xitong:
                isSystem = !isSystem;
                changeBright(isSystem, sb_brightness.getProgress());
                break;
            case R.id.tv_subtract:
                subtractFontSize();
                break;
            case R.id.tv_add:
                addFontSize();
                break;
            case R.id.tv_size_default:
                defaultFontSize();
                break;
            case R.id.tv_qihei:
                selectTypeface(Config.FONTTYPE_QIHEI);
                setTypeface(Config.FONTTYPE_QIHEI);
                break;
            case R.id.tv_fzxinghei:
                selectTypeface(Config.FONTTYPE_FZXINGHEI);
                setTypeface(Config.FONTTYPE_FZXINGHEI);
                break;
            case R.id.tv_fzkatong:
                selectTypeface(Config.FONTTYPE_FZKATONG);
                setTypeface(Config.FONTTYPE_FZKATONG);
                break;
            case R.id.tv_bysong:
                selectTypeface(Config.FONTTYPE_BYSONG);
                setTypeface(Config.FONTTYPE_BYSONG);
                break;
//            case R.id.tv_xinshou:
//                selectTypeface(Config.FONTTYPE_XINSHOU);
//                setTypeface(Config.FONTTYPE_XINSHOU);
//                break;
//            case R.id.tv_wawa:
//                selectTypeface(Config.FONTTYPE_WAWA);
//                setTypeface(Config.FONTTYPE_WAWA);
//                break;
            case R.id.tv_default:
                selectTypeface(Config.FONTTYPE_DEFAULT);
                setTypeface(Config.FONTTYPE_DEFAULT);
                break;
            case R.id.iv_bg_default:
                setBookBg(Config.BOOK_BG_DEFAULT);
                selectBg(Config.BOOK_BG_DEFAULT);
                break;
            case R.id.iv_bg_1:
                setBookBg(Config.BOOK_BG_1);
                selectBg(Config.BOOK_BG_1);
                break;
            case R.id.iv_bg_2:
                setBookBg(Config.BOOK_BG_2);
                selectBg(Config.BOOK_BG_2);
                break;
            case R.id.iv_bg_3:
                setBookBg(Config.BOOK_BG_3);
                selectBg(Config.BOOK_BG_3);
                break;
            case R.id.iv_bg_4:
                setBookBg(Config.BOOK_BG_4);
                selectBg(Config.BOOK_BG_4);
                break;
        }
    }

    public interface SettingListener {

        void changeSystemBright(Boolean isSystem, float brightness);

        void changeFontSize(int fontSize);

        void changeTypeFace(Typeface typeface);

        void changeBookBg(int type);
    }
}