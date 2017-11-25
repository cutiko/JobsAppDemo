package cl.cutiko.jobsapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FireDataService extends IntentService implements ValueEventListener {

    private static final String ACTION_FOO = "cl.cutiko.jobsapp.action.ACTION_FOO";
    private DatabaseReference root;

    public FireDataService() {
        super("FireDataService");
    }

    public static void starFireData(Context context) {
        Intent intent = new Intent(context, FireDataService.class);
        intent.setAction(ACTION_FOO);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                handleFireData();
            }
        }
    }

    private void handleFireData() {
        Log.d("CRONJOB", "FireDataService");
        root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userValue = root.child("users").child("one");
        userValue.addListenerForSingleValueEvent(this);
    }


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
            String name = dataSnapshot.getValue(String.class);
            Log.d("CRONJOB", name);
            root.child("products").orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //TODO compare the products prices here
                    NewMessageNotification.notify(FireDataService.this, "something", 1);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
