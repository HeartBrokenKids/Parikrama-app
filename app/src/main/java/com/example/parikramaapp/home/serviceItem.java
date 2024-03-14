package com.example.parikramaapp.home;

public class serviceItem {
    private final String title;
    private final int iconRes;

    public serviceItem(String title, int iconRes) {
        this.title = title;
        this.iconRes = iconRes;
    }

    public String getTitle() {
        return title;
    }

    public int getIconRes() {
        return iconRes;
    }
}