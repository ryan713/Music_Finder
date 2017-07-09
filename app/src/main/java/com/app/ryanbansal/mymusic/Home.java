package com.app.ryanbansal.mymusic;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.app.ryanbansal.mymusic.ActivityDetectionIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ResultCallback<Status> {

    private final int MY_PERMISSIONS_REQUEST_CODE = 200;

    public static final int TOP_ALBUMS = 1;

    public static final int TOP_ARTISTS = 2;

    public static final int TOP_TRACKS = 3;

    public static final int PARTICULAR = 4;

    public static final int FROM_DATABASE = 5;

    private GoogleApiClient googleApiClient;

    private LocationRequest locationRequest;

    public TextView statusText;

    public TextView locationText;

    public ActivityRecognitionBroadcastReceiver broadcastreceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        NotificationUtils.remindUser(this);

        locationText = (TextView) findViewById(R.id.location);

        statusText = (TextView) findViewById(R.id.status);

        broadcastreceiver = new ActivityRecognitionBroadcastReceiver();

        buildGoogleApiClient();

        Button button1 = (Button) findViewById(R.id.albums);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Requested.class);
                intent.putExtra("useThis", TOP_ALBUMS);
                startActivity(intent);
            }
        });

        Button button2 = (Button) findViewById(R.id.artist);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Requested.class);
                intent.putExtra("useThis", TOP_ARTISTS);
                startActivity(intent);
            }
        });

        Button button3 = (Button) findViewById(R.id.tracks);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Requested.class);
                intent.putExtra("useThis", TOP_TRACKS);
                startActivity(intent);
            }
        });

        Button button4 = (Button) findViewById(R.id.yourmusic);

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Requested.class);
                intent.putExtra("useThis", FROM_DATABASE);
                startActivity(intent);
            }
        });
    }

    public void Go(View view) {

        EditText editText = (EditText) findViewById(R.id.search);

        String input = editText.getText().toString().trim();

        if (!input.isEmpty()) {
            Intent intent = new Intent(Home.this, Requested.class);
            intent.putExtra("useThis", PARTICULAR);
            intent.putExtra("input", input);
            startActivity(intent);
        } else if (input.isEmpty()) {
            Toast.makeText(this, "Write some shit!", Toast.LENGTH_SHORT).show();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(ActivityRecognition.API)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000); //Updates location every second.


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_CODE);
        } else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }

        requestActivityUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        locationText.setText("Location services failed to connect.");
    }

    @Override
    public void onLocationChanged(Location location) {

        Geocoder geocoder;
        List<Address> addresses = new ArrayList<>();
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0)
                + ", " + addresses.get(0).getLocality()
                + ", " + addresses.get(0).getAdminArea()
                + "- " + addresses.get(0).getPostalCode()
                + ", " + addresses.get(0).getCountryName();

        locationText.setText(address);
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastreceiver, new IntentFilter(Constants.BROADCAST_ACTION));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastreceiver);
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CODE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onResult(@NonNull Status status) {
        if (!status.isSuccess())
            statusText.setText("Activity recoginition services failed.");
    }

    public void requestActivityUpdates() {
        if (!googleApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected),
                    Toast.LENGTH_SHORT).show();
        }
        else {
            ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(googleApiClient,
                    Constants.DETECTION_INTERVAL_IN_MILLISECONDS,
                    getActivityDetectionPendingIntent()).setResultCallback(this);
        }
    }

    private PendingIntent getActivityDetectionPendingIntent() {
        Intent intent = new Intent(this, ActivityDetectionIntentService.class );

        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // requestActivityUpdates() and removeActivityUpdates().
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Returns a human readable String corresponding to a detected activity type.
     */
    public String getActivityString(int detectedActivityType) {
        Resources resources = this.getResources();
        switch(detectedActivityType) {
            case DetectedActivity.IN_VEHICLE:
                return resources.getString(R.string.in_vehicle);
            case DetectedActivity.ON_BICYCLE:
                return resources.getString(R.string.on_bicycle);
            case DetectedActivity.ON_FOOT:
                return resources.getString(R.string.on_foot);
            case DetectedActivity.RUNNING:
                return resources.getString(R.string.running);
            case DetectedActivity.STILL:
                return resources.getString(R.string.still);
            case DetectedActivity.TILTING:
                return resources.getString(R.string.tilting);
            case DetectedActivity.UNKNOWN:
                return resources.getString(R.string.unknown);
            case DetectedActivity.WALKING:
                return resources.getString(R.string.walking);
            default:
                return resources.getString(R.string.unidentifiable_activity, detectedActivityType);
        }
    }

    /**
     * Receiver for intents sent by DetectedActivitiesIntentService via a sendBroadcast().
     * Receives a list of one or more DetectedActivity objects associated with the current state of
     * the device.
     */
    public class ActivityRecognitionBroadcastReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<DetectedActivity> updatedActivities =
                    intent.getParcelableArrayListExtra(Constants.ACTIVITY_EXTRA);

            String strStatus = "";
            for(DetectedActivity thisActivity: updatedActivities){
                strStatus +=  getActivityString(thisActivity.getType()) + ": " + thisActivity.getConfidence() + "%\n";
            }
            statusText.setText(strStatus);
        }
    }

}
