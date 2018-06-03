package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.view.View;

import com.old.time.R;
import com.old.time.constants.Code;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.EasyPhotos;

import java.util.ArrayList;

public class PuzzleMActivity extends BaseActivity {

    public static void startPuzzleMActivity(Activity mContext) {
        Intent intent = new Intent(mContext, PuzzleMActivity.class);
        ActivityUtils.startActivity(mContext, intent);

    }

    @Override
    protected void initView() {
        findViewById(R.id.constraint_layout_parent).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.constraint_layout_parent:
                TakePicActivity.startCameraActivity(mContext, Code.REQUEST_CODE_30);

                break;
        }
    }

    /**
     * 选择的图片集
     */
    private ArrayList<String> selectedPhotoList = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {

            return;
        }
        switch (requestCode) {
            case Code.REQUEST_CODE_30:
                ArrayList<String> resultPhotos = data.getStringArrayListExtra(EasyPhotos.RESULT_PHOTOS);
                if (resultPhotos.size() == 1) {
                    resultPhotos.add(resultPhotos.get(0));

                }
                selectedPhotoList.clear();
                selectedPhotoList.addAll(resultPhotos);
                EasyPhotos.startPuzzleWithPaths(mContext, selectedPhotoList, Environment.getExternalStorageDirectory().getAbsolutePath(), "AlbumBuilder", Code.REQUEST_CODE_40, false);

                break;
            case Code.REQUEST_CODE_40:
                selectedPhotoList.clear();
                selectedPhotoList.add(data.getStringExtra(EasyPhotos.RESULT_PATHS));
                PhotoPagerActivity.startPhotoPagerActivity(mContext, selectedPhotoList, 0);

                break;
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_puzzle_m;
    }
}
