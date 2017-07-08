package com.app.ryanbansal.mymusic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by RyanBansal on 6/17/17.
 */

public class ExtractfromAPI {

    private ExtractfromAPI() { }


    private static String makeHttpRequest(URL url) {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        } catch (ProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

//    public static Bitmap circularBitmap(Bitmap bitmap) {
//        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//        BitmapShader shader = new BitmapShader (bitmap,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//        Paint paint = new Paint();
//        paint.setShader(shader);
//        paint.setAntiAlias(true);
//        Canvas c = new Canvas(circleBitmap);
//        c.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2, bitmap.getWidth()/2, paint);
//        return circleBitmap;
//    }

    public static ArrayList<Music> getMusic(URL finalUrl) {

        ArrayList<Music> musics = new ArrayList<>();

        String JSON = null;

        if ( finalUrl != null ) {
            JSON = makeHttpRequest(finalUrl);
        }

        try {
            JSONObject root = new JSONObject(JSON);

            JSONObject topRoot;

            JSONArray top;

            switch (Requested.request) {

                case Home.TOP_ALBUMS:
                    topRoot = root.getJSONObject("albums");
                    top = topRoot.getJSONArray(("album"));
                    break;
                case Home.TOP_ARTISTS :
                    topRoot = root.getJSONObject("topartists");
                    top = topRoot.getJSONArray("artist");
                    break;
                case Home.TOP_TRACKS:
                    topRoot = root.getJSONObject("tracks");
                    top = topRoot.getJSONArray("track");
                    break;
                default :
                    return null;
            }
                JSONObject element;
                String name,url,artist=null,image;

                for (int i=0;i<top.length();i++) {
                    element = top.getJSONObject(i);
                    name = element.getString("name");
                    url = element.getString("url");
                    image = element.getJSONArray("image").getJSONObject(2).getString("#text");
                    if (Requested.request != Home.TOP_ARTISTS) {
                        artist = "By " + element.getJSONObject("artist").getString("name");
                    }
                    musics.add(new Music(name, url, artist, image));
                }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            return musics;
        }
    }

//    static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//
//        protected Bitmap doInBackground(String... urls) {
//            String urldisplay = urls[0];
//            Bitmap mIcon11 = null;
//            try {
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                mIcon11 = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//            return mIcon11;
//        }
//    }
}
