package com.app.ryanbansal.mymusic;

import android.graphics.Bitmap;

/**
 * Created by RyanBansal on 6/17/17.
 */

public class Music {

    private String mName;

    private String mUrl;

    private String mImageUrl;

    private String artist;

    public Music(String mName, String mUrl, String artist, String mImageUrl) {
        this.mName = mName;
        this.mUrl = mUrl;
        this.artist=artist;
        this.mImageUrl=mImageUrl;
    }

    public String getmName() {
        return mName;
    }

    public String getmUrl() {
        return mUrl;
    }

    public String getArtist() { return artist; }

    public String getmImageUrl() {return mImageUrl; }
}
