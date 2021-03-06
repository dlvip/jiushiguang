package com.old.time.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Images.Media;

import com.old.time.models.AlbumModel;
import com.old.time.models.Photo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AlbumController {

    private ContentResolver resolver;
    private static final int IMAGE_SIZE = 0;
    public static final String RECENT_PHOTO = "最近照片";

    public AlbumController(Context context) {
        resolver = context.getContentResolver();

    }

    private String[] projections = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.MIME_TYPE, MediaStore.Images.Media.WIDTH, MediaStore.Images.Media.HEIGHT, MediaStore.Images.Media.SIZE};

    /**
     * 获取最近照片列表
     */
    public List<String> getCurrent() {
        List<String> photos = new ArrayList<>();
        Cursor cursor = resolver.query(Media.EXTERNAL_CONTENT_URI, projections, ImageColumns.SIZE + " > " + IMAGE_SIZE, null, ImageColumns.DATE_ADDED + "  DESC");

        if (cursor == null) {

            return photos;
        }

        while (cursor.moveToNext()) {
            int pathCol = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int nameCol = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
            int DateCol = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
            int mimeType = cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE);
            int sizeCol = cursor.getColumnIndex(MediaStore.Images.Media.SIZE);
            int WidthCol = cursor.getColumnIndex(MediaStore.Images.Media.WIDTH);
            int HeightCol = cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT);


            String picName = cursor.getString(nameCol);
            String picType = cursor.getString(mimeType);
            long picSize = cursor.getInt(sizeCol);
            long picTime = cursor.getLong(DateCol);

            int picWidth = cursor.getInt(WidthCol);
            int picHeight = cursor.getInt(HeightCol);
            String picUrl = cursor.getString(pathCol);
            Photo photo = new Photo(picName, picUrl, picTime, picWidth, picHeight, picSize, picType);
            photos.add(picUrl);
            DebugLog.e("Photo=", photo.toString());

        }

        if (!cursor.isClosed()) {
            cursor.close();
            cursor = null;

        }

        return photos;
    }


    /**
     * 获取所有相册列表
     * 主要给文件夹计数
     */
    public List<AlbumModel> getAlbums() {
        List<AlbumModel> albums = new ArrayList<>();
        Map<String, AlbumModel> map = new HashMap<>();
        Cursor cursor = resolver.query(Media.EXTERNAL_CONTENT_URI, new String[]{ImageColumns.DATA, ImageColumns.BUCKET_DISPLAY_NAME, ImageColumns.SIZE}, null, null, null);
        if (cursor == null || !cursor.moveToNext()) {

            return albums;
        }
        cursor.moveToLast();
        // "最近照片"相册
        AlbumModel current = new AlbumModel(RECENT_PHOTO, 0, cursor.getString(cursor.getColumnIndex(ImageColumns.DATA)));
        albums.add(current);
        do {
            if (cursor.getInt(cursor.getColumnIndex(ImageColumns.SIZE)) < IMAGE_SIZE) continue;
            current.increaseCount();//给最近照片+1
            String name = cursor.getString(cursor.getColumnIndex(ImageColumns.BUCKET_DISPLAY_NAME));//获取文件夹名字
            if (map.keySet().contains(name)) {
                map.get(name).increaseCount();

            } else {
                AlbumModel album = new AlbumModel(name, 1, cursor.getString(cursor.getColumnIndex(ImageColumns.DATA)));
                map.put(name, album);
                albums.add(album);

            }
        } while (cursor.moveToPrevious());


        if (!cursor.isClosed()) {
            cursor.close();
            cursor = null;

        }

        return albums;
    }

    /**
     * 获取对应相册下的照片
     */
    public List<String> getAlbum(String name) {
        List<String> photos = new ArrayList<>();
        Cursor cursor = resolver.query(Media.EXTERNAL_CONTENT_URI, projections, "bucket_display_name = ? AND _size > ? ", new String[]{name, IMAGE_SIZE + ""}, ImageColumns.DATE_ADDED + " DESC");
        if (cursor == null) {

            return photos;
        }
        while (cursor.moveToNext()) {
            int pathCol = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int nameCol = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
            int DateCol = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
            int mimeType = cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE);
            int sizeCol = cursor.getColumnIndex(MediaStore.Images.Media.SIZE);
            int WidthCol = cursor.getColumnIndex(MediaStore.Images.Media.WIDTH);
            int HeightCol = cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT);


            String picName = cursor.getString(nameCol);
            String picType = cursor.getString(mimeType);
            long picSize = cursor.getInt(sizeCol);
            long picTime = cursor.getLong(DateCol);

            int picWidth = cursor.getInt(WidthCol);
            int picHeight = cursor.getInt(HeightCol);
            String picUrl = cursor.getString(pathCol);
            Photo photo = new Photo(picName, picUrl, picTime, picWidth, picHeight, picSize, picType);
            photos.add(picUrl);
            DebugLog.e("Photo=", photo.toString());

        }
        if (!cursor.isClosed()) {
            cursor.close();
            cursor = null;

        }

        return photos;
    }


}
