package com.old.time.beans;

public class LetterBean {

    public static LetterBean getInstance(String sortKey, int position) {
        return new LetterBean(sortKey, position);
    }

    public LetterBean(String sortKey, int position) {
        this.sortKey = sortKey;
        this.position = position;

    }

    private String sortKey;//首字母

    private int position;//出现的位置

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    @Override
    public String toString() {
        return "LetterBean{" + "sortKey='" + sortKey + '\'' + ", position=" + position + '}';
    }
}
