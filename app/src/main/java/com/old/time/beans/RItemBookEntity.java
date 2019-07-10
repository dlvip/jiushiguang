package com.old.time.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wcl on 2019/6/17.
 */

public class RItemBookEntity implements Serializable {

    private String title;
    private List<BookEntity> bookEntities;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public List<BookEntity> getBookEntities() {
        return bookEntities;
    }

    public void setBookEntities(List<BookEntity> bookEntities) {
        this.bookEntities = bookEntities;
    }
}
