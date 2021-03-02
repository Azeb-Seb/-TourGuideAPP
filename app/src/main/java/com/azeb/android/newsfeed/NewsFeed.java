package com.azeb.android.newsfeed;

import java.util.ArrayList;

public class NewsFeed {


    private String mSectionName;
    private String mDate;
    private String mTitle;
    private ArrayList<String> mAuthor;
    private String mUrl;

    public NewsFeed(String sectionName, String date, String title, ArrayList<String> author, String url){
        mSectionName = sectionName;
        mDate = date;
        mTitle = title;
        mAuthor = author;
        mUrl = url;
    }

    public String getmUrl() {
        return mUrl;
    }

    public String getmSectionName() {
        return mSectionName;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmTitle() {
        return mTitle;
    }

    public ArrayList<String> getmAuthor() {
        return mAuthor;
    }


}
