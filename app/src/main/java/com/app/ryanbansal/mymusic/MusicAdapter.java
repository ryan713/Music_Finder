package com.app.ryanbansal.mymusic;

import com.squareup.picasso.Transformation;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by RyanBansal on 6/17/17.
 */

 class MusicAdapter extends ArrayAdapter<Music> {

    public MusicAdapter(@NonNull Context context, @NonNull ArrayList<Music> musics) {
        super(context, 0, musics);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

     View currentView = convertView;
     MyMusicViewHolder viewHolder = null;

        if (currentView == null) {
            currentView = LayoutInflater.from(getContext()).inflate(R.layout.musicitem, parent, false);
            viewHolder = new MyMusicViewHolder(currentView);
            currentView.setTag(viewHolder);
        }

        else {
            viewHolder = (MyMusicViewHolder) currentView.getTag();
        }

        String imageStringUrl = null;
        imageStringUrl = getItem(position).getmImageUrl();

        if ( imageStringUrl == null )
            viewHolder.imageView.setImageResource(R.drawable.blankart);
        else
            Picasso.with(getContext()).load(imageStringUrl).transform(new CircleTransform()).into(viewHolder.imageView);

        viewHolder.textView1.setText(getItem(position).getmName());

        viewHolder.textView2.setText(getItem(position).getArtist());

        return currentView;
    }

    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}
