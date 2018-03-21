package com.old.time.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;

import com.old.time.activitys.ClipImageActivity;
import com.old.time.permission.PermissionUtil;

import java.io.File;

public class PictureUtil {


    /**
     * 照相
     *
     * @param context     上下文
     * @param requestCode 请求码
     * @return 照片地址的uri
     */
    public static Uri takePhoto(Activity context, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用android自带的照相机
        ContentValues values = new ContentValues();
        Uri photoUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        context.startActivityForResult(intent, requestCode);
        return photoUri;
    }

    /**
     * 自定义照相路径
     *
     * @param context
     * @param uri
     * @param requestCode
     */
    public static void takePhoto(Activity context, Uri uri, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用android自带的照相机
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 从相册选取一张照片
     *
     * @param context     上下文
     * @param requestCode 请求码
     */
    public static void pickPhoto(Activity context, int requestCode) {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 裁切图片
     *
     * @param context     上下文
     * @param srcPath     原始图片所在path
     * @param dstUri      裁切过后要保存到的uri，如果srcuri和desuri一致，将会被覆盖
     * @param requestCode 请求码
     */
    public static void cropPhoto(@NonNull Activity context, @NonNull String srcPath, @NonNull Uri dstUri, @NonNull int requestCode) {
        if (dstUri == null) {
            Toast.makeText(context, "图片不存在", Toast.LENGTH_SHORT).show();

            return;
        }
        if (TextUtils.isEmpty(srcPath)) {
            Toast.makeText(context, "缺少图片地址", Toast.LENGTH_SHORT).show();
            return;
        }
        cropPhotoRetrunBitmap3(context, srcPath, dstUri, new int[]{1, 1}, requestCode);

    }


    /**
     * 裁切返回bitmap  身份证件比例
     *
     * @param context     上下文
     * @param srcPath     源path
     * @param requestCode 请求码
     */
    public static void cropPhotoRetrunBitmap3(Activity context, String srcPath, Uri imageUri, int[] ints, int requestCode) {
        if (TextUtils.isEmpty(srcPath) || imageUri == null) {
            Toast.makeText(context, "图片不存在", Toast.LENGTH_SHORT).show();

            return;
        }
        ClipImageActivity.prepare()
                .aspectX(ints[0]).aspectY(ints[1])
                .inputPath(srcPath).outputPath(imageUri.getPath())
                .startForResult(context, requestCode);
    }

    /**
     * 裁切返回bitmap  身份证件比例
     *
     * @param context     上下文
     * @param srcPath     源path
     * @param requestCode 请求码
     */
    public static void cropPhotoRetrunBitmap3(Fragment context, String srcPath, Uri imageUri, int[] ints, int requestCode) {
        if (TextUtils.isEmpty(srcPath) || imageUri == null) {
            Toast.makeText(context.getContext(), "图片不存在", Toast.LENGTH_SHORT).show();

            return;
        }
        ClipImageActivity.prepare()
                .aspectX(ints[0]).aspectY(ints[1])
                .inputPath(srcPath).outputPath(imageUri.getPath())
                .startForResult(context, requestCode);

    }

    /**
     * 插入bitmap到相册
     *
     * @param context 上下文
     * @param bitmap  bitmap
     */
    public static void insertBitmapToGallery(Activity context, Bitmap bitmap) {
        MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "", "");
    }

    /**
     * 通知图库刷新
     *
     * @param context 上下文
     * @param path    路径
     */
    public static void notifyGallery(Context context, String path) {
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        notifyGallery(context, contentUri);
    }

    /**
     * 通知图库刷新
     *
     * @param context 上下文
     * @param uri     注意uri必须为file类型 Uri.fromFile();
     */
    public static void notifyGallery(Context context, Uri uri) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(uri);
        context.sendBroadcast(mediaScanIntent);
    }
}
