package com.example.android.news;

import android.os.Parcel;
import android.os.Parcelable;

public class NewsArticle implements Parcelable {

    String mHeadline = "";
    String mTrailText = "";
    String mShortUrl = "";
    String mImageResourceId = "";
    String mLastModified = "";
    private static final String NO_IMAGE_PROVIDED = null;

    public NewsArticle(String headline, String trailText, String shortUrl, String imageResourceId, String lastModified) {
        this.mHeadline = headline;
        this.mTrailText = trailText;
        this.mShortUrl = shortUrl;
        this.mImageResourceId = imageResourceId;
        this.mLastModified = lastModified;

    }

    private NewsArticle(Parcel in) {
        mHeadline = in.readString();
        mTrailText = in.readString();
        mShortUrl = in.readString();
        mImageResourceId = in.readString();
        mLastModified = in.readString();
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

    public boolean hasImage() { return !mImageResourceId.equals(NO_IMAGE_PROVIDED); }

    public String getLastModified() {
        return mLastModified;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mHeadline);
        out.writeString(mTrailText);
        out.writeString(mShortUrl);
        out.writeString(mImageResourceId);
        out.writeString(mLastModified);
    }
    public static final Parcelable.Creator<NewsArticle> CREATOR = new Parcelable.Creator<NewsArticle>() {
        public NewsArticle createFromParcel(Parcel in) {
            return new NewsArticle(in);
        }

        public NewsArticle[] newArray(int size) {
            return new NewsArticle[size];
        }
    };
}

