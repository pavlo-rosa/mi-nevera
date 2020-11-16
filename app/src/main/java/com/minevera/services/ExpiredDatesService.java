package com.minevera.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.backend.groupHomeApi.model.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.minevera.Controller;
import com.minevera.asynctasks.GH2AsyncTask;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import static com.google.android.gms.internal.zzir.runOnUiThread;

public class ExpiredDatesService extends Service {

    private final String TAG = "ExpiredDatesService";
    private int delay =  30000;   // delay for 5 sec.
    private int period = 30000;  // repeat every sec.
    private Timer timer = new Timer();
    private GH2AsyncTask asyncTask;


    public ExpiredDatesService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       // throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
//        Log.i(TAG, "Service Started");
        //      Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        doSomethingRepeatedly();
        return  START_NOT_STICKY;
    }

    private void doSomethingRepeatedly() {
        try {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable(){
                        public void run() {
                            asyncTask = new GH2AsyncTask();
                            try {
                                asyncTask.execute(getBaseContext()).get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }, delay, period);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer != null){
            timer.cancel();
        }
//        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
    }

}
