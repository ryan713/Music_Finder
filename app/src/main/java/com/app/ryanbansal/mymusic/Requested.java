package com.app.ryanbansal.mymusic;

import android.Manifest;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.app.ryanbansal.mymusic.MusicContract.MusicEntry;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Requested extends AppCompatActivity {

    private final int MY_PERMISSIONS_REQUEST_CODE = 200;

    private static boolean STATUS_GRANTED = false;

    private final String BASE_URL = "http://ws.audioscrobbler.com/2.0/?api_key=039abc1ec23c58daf3140761ee7045eb&limit=50&format=json";

    private final String METHOD = "&method=";

    private final String TAG = "&tag=all";

    private final String GET_TOP_ALBUMS = "tag.gettopalbums";

    private final String GET_TOP_ARTISTS = "tag.gettopartists";

    private final String GET_TOP_TRACKS = "tag.gettoptracks";

    private final String PAGE = "&page=";

    private String finalStringUrl;

    public static int request;

    private static int mPageCount = 1;

    private ProgressBar progressBar;

    private ListView listView;

    private TextView textView;

    private URL mUrl;

    MusicAdapter mAdapter;

    private LoaderManager loaderManager = getLoaderManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested);

        progressBar = (ProgressBar) findViewById(R.id.progress);

        progressBar.setVisibility(View.INVISIBLE);

        listView = (ListView) findViewById(R.id.musicList);

        textView = (TextView) findViewById(R.id.emptyView);

        listView.setEmptyView(textView);

        request = getIntent().getExtras().getInt("useThis");

        if (request == Home.FROM_DATABASE) {

            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this,
                            new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                            MY_PERMISSIONS_REQUEST_CODE);

            }
            else if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                updateUIwithCursor();
            }

        } else {

            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {

                mAdapter = new MusicAdapter(this, new ArrayList<Music>());
                listView.setAdapter(mAdapter);

                finalStringUrl = stringUrlBuilder(BASE_URL);
                progressBar.setVisibility(View.VISIBLE);
                loaderManager.initLoader(1, null, networkRequestListener);

                Button loadMore = new Button(this);
                loadMore.setText("Load More");
                listView.addFooterView(loadMore);

                loadMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finalStringUrl = stringUrlBuilder(BASE_URL);
                        loaderManager.initLoader(mPageCount, null, networkRequestListener);
                    }
                });
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                textView.setText("No Internet Connectivity!");
            }
        }
    }

    private String stringUrlBuilder(String baseUrl) {
        StringBuilder finalUrl = new StringBuilder(baseUrl);

        finalUrl.append(METHOD);

        switch (request) {

            case Home.TOP_ALBUMS:
                finalUrl.append(GET_TOP_ALBUMS);
                break;
            case Home.TOP_ARTISTS:
                finalUrl.append(GET_TOP_ARTISTS);
                break;
            case Home.TOP_TRACKS:
                finalUrl.append(GET_TOP_TRACKS);
                break;
            default:
                return null;
        }
        finalUrl.append(TAG);

        finalUrl.append(PAGE);
        finalUrl.append(mPageCount);
        mPageCount++;

        return finalUrl.toString();
    }

    LoaderManager.LoaderCallbacks<ArrayList<Music>> networkRequestListener = new LoaderManager.LoaderCallbacks<ArrayList<Music>>() {

        @Override
        public Loader<ArrayList<Music>> onCreateLoader(int id, Bundle bundle) {

            mUrl = createUrl(finalStringUrl);
            if (URLUtil.isValidUrl(String.valueOf(mUrl))) {
                return new MusicLoader(Requested.this, mUrl);
            }
            progressBar.setVisibility(View.INVISIBLE);
            textView.setText("Invalid URL");
            return null;
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Music>> loader, ArrayList<Music> data) {
            if (!data.isEmpty()) {
                UpdateUI(data);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onLoaderReset(Loader loader) {
            UpdateUI(null);
        }

    };

    public void UpdateUI(final ArrayList<Music> data) {
        Toast.makeText(this, "Here!", Toast.LENGTH_SHORT).show();
        mAdapter.addAll(data);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Requested.this, Webpage.class);
                intent.putExtra("url", data.get(position).getmUrl());
                startActivity(intent);
            }
        });
    }

    private static URL createUrl(String stringUrl) {
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        return url;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_requested, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                finish();
                startActivity(getIntent());
                break;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CODE:
               STATUS_GRANTED = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (STATUS_GRANTED)
                    updateUIwithCursor();
                else if (!STATUS_GRANTED)
                    ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE);
    }

}

    private void updateUIwithCursor() {
        Cursor data = getMusicFromDatabase();
        data.moveToFirst();
        CursorMusicAdapter cursorMusicAdapter = new CursorMusicAdapter(this, data);
        listView.setAdapter(cursorMusicAdapter);
        Toast.makeText(this, "Here", Toast.LENGTH_SHORT).show();
    }

    private Cursor getMusicFromDatabase() {
        String[] projection = {
                MusicEntry._ID,
                MusicEntry.ALBUM_NAME,
                MusicEntry.ARTIST_NAME,
                MusicEntry.ALBUM_ART
               };

        Uri contentUri = Uri.parse(MusicEntry.URI);
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        return cursor;
    }
}
