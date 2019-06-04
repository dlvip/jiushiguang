package com.old.time.beans;

import android.text.TextUtils;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

public class BookEntity extends DataSupport implements Serializable {

    private String id;

    private String levelNum;
    private String subtitle;
    private String author;
    private String pubdate;
    private String origin_title;
    private String binding;
    private String pages;
    private String images_medium;
    private String images_large;
    private String publisher;
    private String isbn10;
    private String isbn13;
    private String title;
    private String summary;
    private String price;
    private String url;

    private String filePath;
    private long begin;
    private String charset;

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getCharset() {
        return charset;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    //选购数量
    private int count = 1;
    //选择状态
    private boolean isSelect = true;

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public String getId() {
        if (TextUtils.isEmpty(id)) {

            return "0";
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLevelNum() {

        return levelNum;
    }

    public void setLevelNum(String levelNum) {
        this.levelNum = levelNum;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getOrigin_title() {
        return origin_title;
    }

    public void setOrigin_title(String origin_title) {
        this.origin_title = origin_title;
    }

    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getImages_medium() {
        return images_medium;
    }

    public void setImages_medium(String images_medium) {
        this.images_medium = images_medium;
    }

    public String getImages_large() {
        return images_large;
    }

    public void setImages_large(String images_large) {
        this.images_large = images_large;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPrice() {
        if (TextUtils.isEmpty(price)) {
            price = "0";

        }

        return price;
    }

    public double getDPrice() {
        double d = Double.parseDouble(getPrice()) * 0.3 + 2;

        return (double) (Math.round(d * 100) / 100);
    }

    public String getPriceStr() {


        return "￥ " + getDPrice();
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
