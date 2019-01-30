package com.old.time.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.LetterBean;
import com.old.time.beans.PhoneBean;

import java.util.List;

/**
 * @ClassName: wcLiang
 * @Description:${TODO}
 * @author: 龙杯科技
 * @date: ${date} ${time}
 * <p>
 * ${tags}
 * @Copyright: ${year} www.longbei.com
 * 注意：本内容仅限于北京龙杯信息技术有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class LetterAdapter extends BaseQuickAdapter<PhoneBean, BaseViewHolder> {

    public LetterAdapter(@Nullable List<PhoneBean> data) {
        super(R.layout.item_text, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, PhoneBean item) {
        helper.setText(R.id.tv_item_text, item.getCodeKey());

    }
}
