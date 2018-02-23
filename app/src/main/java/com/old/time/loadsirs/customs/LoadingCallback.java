package com.old.time.loadsirs.customs;

import com.old.time.R;
import com.old.time.loadsirs.callback.Callback;

/**
 * Created by NING on 2017/12/27.
 */

public class LoadingCallback extends Callback {

    @Override
    protected int onCreateView() {
        return R.layout.loading_empty_view;
    }
}
