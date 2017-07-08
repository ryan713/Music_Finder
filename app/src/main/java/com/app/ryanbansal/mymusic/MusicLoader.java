package com.app.ryanbansal.mymusic;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.webkit.URLUtil;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by RyanBansal on 6/17/17.
 */

public class MusicLoader extends AsyncTaskLoader<ArrayList<Music>> {

    URL mFinalUrl;

    public MusicLoader(Context context, URL finalUrl) {
        super(context);
        mFinalUrl=finalUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Music> loadInBackground() {
        if (!URLUtil.isValidUrl(String.valueOf(mFinalUrl))) {
            return null;
        }
        return ExtractfromAPI.getMusic(mFinalUrl);
    }
}
