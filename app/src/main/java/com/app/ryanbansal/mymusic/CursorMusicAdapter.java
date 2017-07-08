package com.app.ryanbansal.mymusic;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.ryanbansal.mymusic.MusicContract.MusicEntry;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by RyanBansal on 7/1/17.
 */

public class CursorMusicAdapter extends CursorAdapter {

    public CursorMusicAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View currentView = LayoutInflater.from(context).inflate(R.layout.musicitem, parent, false);
        MyMusicViewHolder viewHolder = new MyMusicViewHolder(currentView);
        currentView.setTag(viewHolder);
        return currentView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        MyMusicViewHolder viewHolder = (MyMusicViewHolder) view.getTag();

        viewHolder.textView1.setText(cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.ALBUM_NAME)));

        viewHolder.textView2.setText("By " + cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.ARTIST_NAME)));

        String stringUri = cursor.getString(cursor.getColumnIndex(MusicEntry.ALBUM_ART));

        if (stringUri == null)
            viewHolder.imageView.setImageResource(R.drawable.blankart);
        else
            viewHolder.imageView.setImageURI(Uri.parse("file:///" + stringUri));
    }
}
