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

public class MainActivity extends AppCompatActivity {

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

    }

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
