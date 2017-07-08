package com.app.ryanbansal.mymusic;

import android.provider.BaseColumns;

/**
 * Created by RyanBansal on 7/1/17.
 */

public final class MusicContract {

    private MusicContract() {}

    public final class MusicEntry implements BaseColumns {

        public static final String URI = "content://media/external/audio/albums";

        public static final String _ID = BaseColumns._ID;

        public static final String ALBUM_NAME = "album";

        public static final String ARTIST_NAME = "artist";

        public static final String ALBUM_ART = "album_art";

    }
}
