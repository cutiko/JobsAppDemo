package cl.cutiko.jobsapp;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by cutiko on 25-11-17.
 */

public class FirebaseJob extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {
        Log.d("CRONJOB", "working");
        FireDataService.starFireData(this);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }

}
