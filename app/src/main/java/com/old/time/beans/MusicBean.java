package com.old.time.beans;

import com.old.time.music.client.model.IMusicInfo;

/**
 * Created by wcl on 2018/8/14.
 */

public class MusicBean implements IMusicInfo {

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

    @Override
    public String getMediaId() {
        return id;
    }

    @Override
    public String getSource() {
        return musicUrl;
    }

    @Override
    public String getArtUrl() {
        return musicPic;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getArtist() {
        return "";
    }

    @Override
    public String getAlbum() {
        return "";
    }

    @Override
    public String getAlbumArtUrl() {
        return musicPic;
    }

    @Override
    public String getGenre() {
        return "";
    }

    @Override
    public String freeType() {
        return null;
    }

    @Override
    public long getDuration() {
        return musicTime;
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
