package com.old.time.adapters;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.downloads.DownLoadManager;
import com.old.time.downloads.TaskInfo;

import java.util.List;

/**
 * Created by NING on 2018/5/29.
 */

public class DownLoadAdapter extends BaseQuickAdapter<TaskInfo, BaseViewHolder> {

    public DownLoadAdapter(List<TaskInfo> data) {
        super(R.layout.adapter_download, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, TaskInfo item) {
        helper.setText(R.id.file_name, item.getFileName()).setText(R.id.file_size, item.getProgress() + "%");

        ProgressBar mProgressBar = helper.getView(R.id.progressbar);
        mProgressBar.setProgress(item.getProgress());

        CheckBox mCheckBox = helper.getView(R.id.checkbox);
        mCheckBox.setChecked(item.isOnDownloading());

        mCheckBox.setOnCheckedChangeListener(new CheckedChangeListener(helper.getLayoutPosition()));

    }

    private class CheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

        private int position = 0;

        public CheckedChangeListener(int position) {
            this.position = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {// 继续下载
                getItem(position).setOnDownloading(true);

            } else { //停止下载
                getItem(position).setOnDownloading(false);

            }
        }
    }
}
