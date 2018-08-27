package com.old.time.beans;

import java.io.Serializable;

/**
 * Created by wcl on 2018/8/14.
 */

public class MusicBean implements Serializable {

    private String id;

    private String title;

    private String albumId;

    private String musicUrl;

    private String musicTitle;

    private String musicPic;

    private long musicTime;

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

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public String getMusicTitle() {
        return musicTitle;
    }

    public void setMusicTitle(String musicTitle) {
        this.musicTitle = musicTitle;
    }

    public String getMusicPic() {
        return musicPic;
    }

    public void setMusicPic(String musicPic) {
        this.musicPic = musicPic;
    }

    public long getMusicTime() {
        return musicTime;
    }

    public void setMusicTime(long musicTime) {
        this.musicTime = musicTime;
    }
}
