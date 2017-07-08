package com.app.ryanbansal.mymusic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class Webpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webpage);

        WebView webView = (WebView) findViewById(R.id.webpage);
        webView.loadUrl(getIntent().getExtras().getString("url"));
    }
}
