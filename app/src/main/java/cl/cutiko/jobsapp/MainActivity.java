package cl.cutiko.jobsapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import cl.cutiko.jobsapp.geo.GeoJob;

public class MainActivity extends AppCompatActivity /*implements OnFailureListener, OnSuccessListener<LocationSettingsResponse>*/ {

    private static final int GEO_RC = 45325;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            scheduleJob();
        } else {
            String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
            requestPermissions(permissions, GEO_RC);
        }

        //TODO handle user some how turn on the gps, or maybe not
        /*LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnFailureListener(this, this);
        task.addOnSuccessListener(this, this);*/
        //los this en el codigo de arriba son interfaces que hay que implementar


    }

    /*@Override
    public void onFailure(@NonNull Exception e) {
        int statusCode = ((ApiException) e).getStatusCode();
        final String tag = "NO_LOCATION";
        switch (statusCode) {
            case CommonStatusCodes.RESOLUTION_REQUIRED:
                try {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {
                    Log.d(tag, "NOT RESOLVABLE", sendEx.fillInStackTrace());
                    locationObtained(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.d(tag, "NOTHING TO DO", e.fillInStackTrace());
                locationObtained(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CHECK_SETTINGS == requestCode) {
            if (RESULT_OK == resultCode) {
                //echa a correr el job te arreglaron la cosa
            }
        }
    }

    @Override
    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
        EL USUARIO tiene gps
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (GEO_RC == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scheduleJob();
            } else {
                Toast.makeText(this, "NO ME DISTE PERMISO", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void scheduleJob(){
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        //This works but the problem is, this is literally fighting against battery saving modes,
        //so on the long runn it will work whenever the phone allow it
        Job cronJob = dispatcher.newJobBuilder()
                .setService(GeoJob.class)
                .setTag("geo-job")
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(0, 5))
                .build();

        dispatcher.mustSchedule(cronJob);
    }
}
