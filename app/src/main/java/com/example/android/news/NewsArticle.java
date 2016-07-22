package com.example.android.news;

/**
 * Created by Jason on 02/07/2016.
 */
public class NewsArticle {

    String mHeadline = "";
    String mTrailText = "";
    String mShortUrl = "";
    String mImageResourceId = "";
    private static final String NO_IMAGE_PROVIDED = null;

    /**
     * Constructor
     *
     * @param headline        of news article
     * @param trailText       of news article
     * @param imageResourceId id of image as an integer
     */
    public NewsArticle(String headline, String trailText, String shortUrl, String imageResourceId) {
        this.mHeadline = headline;
        this.mTrailText = trailText;
        this.mShortUrl = shortUrl;
        this.mImageResourceId = imageResourceId;

    }

    /**
     * get Title of news article
     *
     * @return string
     */
    public String getHeadline() {
        return mHeadline;
    }

    /**
     * get TrailText of news article
     *
     * @return string
     */
    public String getTrailText() {
        return mTrailText;
    }

    /**
     * get Web Url for news article
     *
     * @return String
     */
    public String getShortUrl() {
        return mShortUrl;
    }

    /**
     * get Image of news article
     *
     * @return String
     */
    public String getImageResourceId() {
        return mImageResourceId;
    }

    public boolean hasImage() { return mImageResourceId != NO_IMAGE_PROVIDED; }

}

