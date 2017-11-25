package cl.cutiko.jobsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //This is an example for Andrea
        /*String something = "wawewe wwQ";
        something.toLowerCase().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("products").orderByChild("sanitazed_name").equalTo(something);
        reference.child("products").orderByChild("sanitazed_name").startAt(something);*/

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        //This works but the problem is, this is literally fighting against battery saving modes,
        //so on the long runn it will work whenever the phone allow it
        Job cronJob = dispatcher.newJobBuilder()
                .setService(FirebaseJob.class)
                .setTag("cron-job")
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(0, 5))
                .setConstraints(
                        Constraint.ON_UNMETERED_NETWORK
                )
                .build();

        dispatcher.mustSchedule(cronJob);

    }
}
