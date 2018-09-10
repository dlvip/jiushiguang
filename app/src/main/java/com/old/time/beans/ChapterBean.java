package com.old.time.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 序列化bean对象，使之能在activity和service中传递信息
 */
public class ChapterBean implements Parcelable {

    private long id;
    private long albumId;
    private long duration;
    private long size;
    private String title;
    private String artist;
    private String album;
    private String url;

    private String songId;
    private String songName;
    private String picUrl;
    private String audio;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public long getDuration() {

        return duration;
    }

    public String getDurationStr() {
        if (duration == 0) {

            return "00:00";
        }
        String durationStr;
        durationStr = duration / 60 > 9 ? "" + duration / 60 : "0" + duration / 60;
        durationStr += duration % 60 > 9 ? ":" + duration % 60 : ":0" + duration % 60;

        return durationStr;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public static Creator<ChapterBean> getCREATOR() {
        return CREATOR;
    }

    public ChapterBean() {

    }

    protected ChapterBean(Parcel in) {
        id = in.readLong();
        title = in.readString();
        artist = in.readString();
        album = in.readString();
        albumId = in.readLong();
        duration = in.readLong();
        size = in.readLong();
        url = in.readString();
        songId = in.readString();
        songName = in.readString();
        picUrl = in.readString();
        audio = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeString(album);
        dest.writeLong(albumId);
        dest.writeLong(duration);
        dest.writeLong(size);
        dest.writeString(url);
        dest.writeString(songId);
        dest.writeString(songName);
        dest.writeString(picUrl);
        dest.writeString(audio);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChapterBean> CREATOR = new Creator<ChapterBean>() {
        @Override
        public ChapterBean createFromParcel(Parcel in) {
            return new ChapterBean(in);
        }

        @Override
        public ChapterBean[] newArray(int size) {
            return new ChapterBean[size];
        }
    };

    @Override
    public String toString() {
        return "ChapterBean{" + "id=" + id + ", title='" + title + '\'' + ", artist='" + artist + '\'' + ", album='" + album + '\'' + ", albumId=" + albumId + ", duration=" + duration + ", size=" + size + ", url='" + url + '\'' + ", songId='" + songId + '\'' + ", songName='" + songName + '\'' + ", picUrl='" + picUrl + '\'' + ", audio='" + audio + '\'' + '}';
    }
}
