package com.app.ryanbansal.mymusic;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by RyanBansal on 7/1/17.
 */

public class MyMusicViewHolder {

    public ImageView imageView;

    public TextView textView1;

    public TextView textView2;

    public MyMusicViewHolder(View view) {

        imageView = (ImageView) view.findViewById(R.id.image);

        textView1 = (TextView) view.findViewById(R.id.details);

        textView2 = (TextView) view.findViewById(R.id.artistname);
    }
}
