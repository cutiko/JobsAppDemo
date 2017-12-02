package cl.cutiko.jobsapp.geo;

import android.location.Location;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.List;

/**
 * Created by cutiko on 02-12-17.
 */

public class GeoJob extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {
        /*WARNING THERE IS NO SECURITY USER ACTUALLY TURNED ON THE GPS*/
        Log.d("GEOJOB", "start");
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1);
        mLocationRequest.setFastestInterval(1);
        //READ ABOUT THIS CONSTANTS https://developer.android.com/training/location/change-location-settings.html
        //ALSO HERE https://developers.google.com/android/reference/com/google/android/gms/location/LocationRequest.html#inherited-method-summary
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setNumUpdates(1);
        Log.d("LOCATION", "getting");
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                List<Location> locations = locationResult.getLocations();
                if (locations.size() > 0) {
                    Location location = locations.get(0);
                    Log.d("LOCATION", "result");
                    //TODO location obtained get firebase data
                    GeoNotification.notify(GeoJob.this, location.getLatitude(), location.getLongitude());
                    //you could start an intent service to do the firebase work
                } else {
                    GeoNotification.notify(GeoJob.this, 0, 0);
                }
            }
        };
        Log.d("LOCATION", "resulted");
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, locationCallback, null);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
