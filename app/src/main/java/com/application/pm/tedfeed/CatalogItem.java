package com.application.pm.tedfeed;

/* This class represents one item in the TED talks catalog */

import android.os.Parcel;
import android.os.Parcelable;

class CatalogItem implements Parcelable {

    private String mTitle;
    private String mImageUrl;
    private String mDescription;
    private String mVideoUrl;

    CatalogItem() {}

    // Parcel constructor
    private CatalogItem(Parcel in){
        String[] data= new String[4];
        in.readStringArray(data);

        mTitle = data[0];
        mImageUrl = data[1];
        mDescription = data[2];
        mVideoUrl = data[3];
    }

    String getTitle() {
        return mTitle;
    }

    void setTitle(String title) {
        mTitle = title;
    }

    String getImageUrl() {
        return mImageUrl;
    }

    void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    String getDescription() {
        return mDescription;
    }

    void setDescription(String description) {
        // description contains a faulty img tag at the end, will be chopped of
        mDescription = description.substring(0, description.indexOf('<'));
    }

    String getVideoUrl() {
        return mVideoUrl;
    }

    void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Write the object to parcel
    @Override
    public void writeToParcel(Parcel parcel, int flags) {

        parcel.writeStringArray(new String[] {
                mTitle,
                mImageUrl,
                mDescription,
                mVideoUrl});
    }

    public static final Creator<CatalogItem> CREATOR = new Creator<CatalogItem>() {

        @Override
        public CatalogItem createFromParcel(Parcel source) {
            return new CatalogItem(source);
        }

        @Override
        public CatalogItem[] newArray(int size) {
            return new CatalogItem[size];
        }
    };
}
