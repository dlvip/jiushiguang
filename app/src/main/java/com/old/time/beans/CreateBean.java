package com.old.time.beans;

/**
 * Created by NING on 2018/9/29.
 */

public class CreateBean {

    public static CreateBean getInstance(String title, Class aClass) {

        return new CreateBean(title, aClass);
    }

    public CreateBean(String title, Class aClass) {
        this.title = title;
        this.aClass = aClass;

    }

    private String title;

    private Class aClass;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }
}
