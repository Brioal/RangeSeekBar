package com.brioal.rangeseek.entity;

/**
 * Range Select Entity
 * Created by Brioal on 2016/9/14.
 */
public class RangeEntity {
    private String mTitle;
    private Object mValue;

    public RangeEntity(String title, int value) {
        mTitle = title;
        mValue = value;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Object getValue() {
        return mValue;
    }

    public void setValue(int value) {
        mValue = value;
    }
}
