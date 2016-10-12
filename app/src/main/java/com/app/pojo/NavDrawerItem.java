package com.app.pojo;

import android.graphics.drawable.Drawable;

/**
 * Created by Ravi on 29/07/15.
 */
public class NavDrawerItem {
    private boolean showNotify;
    private String title;
    private int imageIcon;


    public NavDrawerItem() {

    }

    public NavDrawerItem(boolean showNotify, String title, int imageIcon) {
        this.showNotify = showNotify;
        this.title = title;
        this.imageIcon = imageIcon;
    }

    public boolean isShowNotify() {
        return showNotify;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public int getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(int imageIcon) {
        this.imageIcon = imageIcon;
    }
}
