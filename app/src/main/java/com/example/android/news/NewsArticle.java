package com.example.android.news;

/**
 * Created by Jason on 02/07/2016.
 */
public class NewsArticle {

    String mTitle = "";
    String mAuthor = "";
    String mShortUrl = "";
    String mImageResourceId = "";

    /**
     * Constructor
     * @param mTitle of news article
     * @param mAuthor of news article
     * @param mImageResourceId id of image as an integer
     */
    public NewsArticle(String mTitle, String mAuthor, String mShortUrl, String mImageResourceId) {
        this.mTitle = mTitle;
        this.mAuthor = mAuthor;
        this.mShortUrl = mShortUrl;
        this.mImageResourceId = mImageResourceId;
    }

    /**
     * get Title of news article
     * @return string
     */
    public String getmHeadline() {
        return mTitle;
    }

    /**
     * get author of news article
     * @return string
     */
    public String getmTrailText() {
        return mAuthor;
    }

    /**
     * get web url for news article
     * @return String
     */
    public String getmShortUrl() { return mShortUrl; }

    /**
     * get image of news article
     * @return String
     */
    public String getmImageResourceId() {
        return mImageResourceId;
    }

}

