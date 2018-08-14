package com.old.time.beans;

import java.io.Serializable;

/**
 * Created by wcl on 2018/8/14.
 */

public class MusicBean implements Serializable {

    private String id;

    private String title;

    private String albumId;

    private String musiceUrl;

    private String musiceTitle;

    private String musicPic;

    private long musiceTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getMusiceUrl() {
        return musiceUrl;
    }

    public void setMusiceUrl(String musiceUrl) {
        this.musiceUrl = musiceUrl;
    }

    public String getMusiceTitle() {
        return musiceTitle;
    }

    public void setMusiceTitle(String musiceTitle) {
        this.musiceTitle = musiceTitle;
    }

    public String getMusicPic() {
        return musicPic;
    }

    public void setMusicPic(String musicPic) {
        this.musicPic = musicPic;
    }

    public long getMusiceTime() {
        return musiceTime;
    }

    public void setMusiceTime(long musiceTime) {
        this.musiceTime = musiceTime;
    }
}
