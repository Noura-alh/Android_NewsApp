package com.example.android.newsapp;

/**
 * Created by Noura on 9/15/17.
 */

public class News {


    private String nTitle;

    private String nSection;

    private String nDate;

    private String nUrl;
    private String nAuthor;

    public News(String nTitle, String nSection, String nUrl, String nDate, String nAuthor) {
        this.nTitle = nTitle;
        this.nSection = nSection;
        this.nDate = nDate;
        this.nUrl = nUrl;
        this.nAuthor = nAuthor;
    }

    public String getnTitle() {
        return nTitle;
    }

    public String getnSection() {
        return nSection;
    }

    public String getnDate() {
        return nDate;
    }

    public String getnUrl() {
        return nUrl;
    }

    public String getnAuthor() {
        return nAuthor;
    }
}
