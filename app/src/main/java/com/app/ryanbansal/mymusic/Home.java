package com.app.ryanbansal.mymusic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import javax.xml.datatype.Duration;

public class Home extends AppCompatActivity {

    public static final int TOP_ALBUMS = 1;

    public static final int TOP_ARTISTS = 2;

    public static final int TOP_TRACKS = 3;

    public static final int PARTICULAR = 4;

    public static final int FROM_DATABASE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        NotificationUtils.remindUser(this);

        Button button1 = (Button) findViewById(R.id.albums);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this,Requested.class);
                intent.putExtra("useThis",TOP_ALBUMS);
                startActivity(intent);
            }
        });

        Button button2 = (Button) findViewById(R.id.artist);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this,Requested.class);
                intent.putExtra("useThis",TOP_ARTISTS);
                startActivity(intent);
            }
        });

        Button button3 = (Button) findViewById(R.id.tracks);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this,Requested.class);
                intent.putExtra("useThis",TOP_TRACKS);
                startActivity(intent);
            }
        });

        Button button4 = (Button) findViewById(R.id.yourmusic);

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this,Requested.class);
                intent.putExtra("useThis",FROM_DATABASE);
                startActivity(intent);
            }
        });
    }

    public void Go(View view) {

        EditText editText = (EditText) findViewById(R.id.search);

        String input = editText.getText().toString().trim();

        if (!input.isEmpty()) {
            Intent intent = new Intent(Home.this,Requested.class);
            intent.putExtra("useThis", PARTICULAR);
            intent.putExtra("input",input);
            startActivity(intent);
        }
        else if (input.isEmpty()) {
            Toast.makeText(this,"Write some shit!", Toast.LENGTH_SHORT).show();
        }
    }
}
